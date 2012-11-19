/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */
package de.due.ec.httpclient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import de.due.ec.constants.AcmUrl;
import de.due.ec.dto.Article;
import de.due.ec.dto.Author;
import de.due.ex.util.Progress;

/**
 * @author dennis
 * 
 */
public class AcmDatabaseClient extends WebClient implements IScienceDB {

	private Integer resultCount;
	private Integer iterations;

	public List<Article> getArticlesByKeywords(String searchterm,
			String keywords, Integer limit) {
		List<Article> articles = new LinkedList<Article>();

		String url = String.format(AcmUrl.searchUrl, searchterm, keywords);
		TagNode searchResultNode = getTagNodeFromUrl(url);

		resultCount = extractResultCount(searchResultNode);
		iterations = resultCount / 20;

		if (iterations > limit) {
			iterations = limit;
		}

		for (Integer it = 1; it <= iterations; it++) {
			Progress.updateProgress(it, iterations);
			for (Integer i = 2; i < 22; i++) {
				TagNode title = selectSingleElement(searchResultNode,
						"//table/tbody/tr[" + i
								+ "]/td[2]/table/tbody/tr[1]/td/a");
				String articleUrl = title.getAttributeByName("href");

				articles.add(extractIndividualArticle(articleUrl, true));
			}
		}

		return articles;
	}

	/**
	 * @param articleUrl
	 */
	private Article extractIndividualArticle(String articleUrl, Boolean inDeep) {
		Article article = new Article();

		// Flat Layout allows to evaluate all data without additional Ajax
		// request.
		articleUrl = "http://dl.acm.org/" + articleUrl + "&preflayout=flat";
		System.out.println(articleUrl);

		// Individual Article
		TagNode articleDetailsNode = getTagNodeFromUrl(articleUrl);

		TagNode title = selectSingleElement(articleDetailsNode,
				"//*[@id=\"divmain\"]/div/h1/strong");

		article.setArticleID(extractID(articleUrl));
		article.setTitle(title.getText().toString());
		article.setAuthors(extractAuthors(articleDetailsNode));
		article.setDoi(extractDoi(articleDetailsNode));
		article.setIsbn(extractIsbn(articleDetailsNode));
		article.setPublished(extractPublished(articleDetailsNode));
		if (inDeep) {
			System.out.println(article.getTitle());
			article.setReferences(getReferences(articleDetailsNode));
			// article.setCitedBy(getCitedByArticles(articleDetailsNode));
		} else {
			System.out.println("----" + article.getTitle());
		}

		return article;
	}

	private String extractPublished(TagNode articleDetailsNode) {
		TagNode publishedNode = selectSingleElement(articleDetailsNode,
				"//*[@id=\"divmain\"]/table[1]/tbody/tr/td[2]/table/tbody/tr[1]/td[2]");
		Pattern pattern = Pattern.compile("\\d{4}");
		Matcher matcher = pattern.matcher(publishedNode.getText().toString());
		String published = "";
		while (matcher.find()) {
			published = matcher.group();
		}

		return published;
	}

	private List<Article> getReferences(TagNode articleDetailsNode) {
		List<Article> referencedArticles = new LinkedList<Article>();

		Boolean hasReference = true;
		Integer counter = 1;
		while (hasReference) {
			TagNode referenceNode = selectSingleElement(
					articleDetailsNode,
					String.format(
							"//*[@id=\"fback\"]/div[5]/table/tbody/tr[%s]/td[3]/div/a[1]",
							counter));
			if (referenceNode != null) {
				String referenceUrl = referenceNode.getAttributeByName("href");
				referencedArticles.add(extractIndividualArticle(referenceUrl,
						false));
			} else {
				referenceNode = selectSingleElement(
						articleDetailsNode,
						String.format(
								"//*[@id=\"fback\"]/div[5]/table/tbody/tr[%s]/td[2]/div",
								counter));
				if (referenceNode == null) {
					hasReference = false;
				}
			}
			counter++;
		}
		return referencedArticles;
	}

	// private List<Article> getCitedByArticles(TagNode articleDetailsNode) {
	// List<Article> citedByArticles = new LinkedList<Article>();
	// List<String> tempArticleList = new LinkedList<String>();
	//
	// TagNode citeCount = selectSingleElement(articleDetailsNode,
	// "//*[@id=\"divmain\"]/table/tbody/tr/td[2]/table/tbody/tr[3]/td");
	//
	// String[] splited = citeCount.getText().toString().split(":");
	// Integer count = Integer.parseInt(splited[3].replace("\r\n ", "")
	// .replace(" ", ""));
	//
	// if (count > 0) {
	// for (Integer i = 1; i <= count; i++) {
	// TagNode citedByTag = selectSingleElement(
	// articleDetailsNode,
	// String.format(
	// "//*[@id=\"fback\"]/div[6]/table/tbody/tr[%s]/td[2]/div/a",
	// i));
	// String citedByUrl = citedByTag.getAttributeByName("href");
	// if (tempArticleList.contains(citedByUrl)) {
	// continue;
	// }
	//
	// tempArticleList.add(citedByUrl);
	//
	// citedByArticles
	// .add(extractIndividualArticle(citedByUrl, false));
	// }
	// }
	//
	// return citedByArticles;
	// }

	private String extractIsbn(TagNode articleDetailsNode) {
		TagNode isbnNode = selectSingleElement(
				articleDetailsNode,
				"//*[@id=\"divmain\"]/table/tbody/tr/td[1]/table[3]/tbody/tr/td/table/tbody/tr[4]/td/text()[2]");
		if (isbnNode != null) {
			return isbnNode.getText().toString();
		}
		return null;
	}

	private URL extractDoi(TagNode articleDetailsNode) {
		TagNode doiNode = selectSingleElement(
				articleDetailsNode,
				"//*[@id=\"divmain\"]/table/tbody/tr/td[1]/table[3]/tbody/tr/td/table/tbody/tr[4]/td/span[4]/a");
		if (doiNode != null) {
			try {
				return new URL(doiNode.getAttributeByName("href"));
			} catch (MalformedURLException e) {
				// TODO Logger
			}
		}
		return null;
	}

	/**
	 * @param articleDetailsNode
	 */
	private List<Author> extractAuthors(TagNode articleDetailsNode) {
		List<Author> authors = new LinkedList<Author>();
		Boolean authorsAvailabile = true;
		Integer authorCount = 1;
		while (authorsAvailabile) {
			TagNode authorNode = selectSingleElement(articleDetailsNode,
					"//*[@id=\"divmain\"]/table/tbody/tr/td[1]/table[2]/tbody/tr["
							+ authorCount + "]/td[2]/a");

			if (authorNode != null) {
				TagNode affiliationNode = selectSingleElement(
						articleDetailsNode,
						"//*[@id=\"divmain\"]/table/tbody/tr/td[1]/table[2]/tbody/tr["
								+ authorCount + "]/td[3]/a/small");
				if (affiliationNode == null) {
					affiliationNode = selectSingleElement(articleDetailsNode,
							"//*[@id=\"divmain\"]/table/tbody/tr/td[1]/table[2]/tbody/tr["
									+ authorCount + "]/td[3]/small");
				}

				authorCount++;
				Author author = new Author();

				String authorName = authorNode.getText().toString();
				String affiliation = null;
				if (affiliationNode != null) {
					affiliation = affiliationNode.getText().toString();
				}

				String authorUrl = authorNode.getAttributeByName("href");
				Double authorID = extractID(authorUrl);

				author.setId(authorID);
				author.setName(authorName);
				author.setUniversity(affiliation);

				authors.add(author);

			} else {
				authorsAvailabile = false;
			}
		}

		return authors;

	}

	/**
	 * @param url
	 * @return
	 */
	private Double extractID(String url) {
		Integer pos1 = url.indexOf("=") + 1;
		Integer pos2 = url.indexOf("&");

		if (url.contains("results.cfm?query=Name")) {
			return 0.0;
		}

		String id = url.subSequence(pos1, pos2).toString();

		return Double.parseDouble(id);
	}

	/**
	 * @param node
	 * @throws XPatherException
	 */
	private Integer extractResultCount(TagNode node) {
		TagNode currentNode = selectSingleElement(node, "//tr/td/p[2]/b");
		return Integer.parseInt(currentNode.getText().toString()
				.replace(",", ""));
	}

	private TagNode selectSingleElement(TagNode node, String xPath) {
		try {
			Object[] result = node.evaluateXPath(xPath);
			if (result.length > 0) {
				return (TagNode) result[0];
			}
		} catch (XPatherException e) {
			// TODO Logger
		}
		return null;
	}

}
