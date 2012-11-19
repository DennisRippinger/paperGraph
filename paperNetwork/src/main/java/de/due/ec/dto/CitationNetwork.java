package de.due.ec.dto;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class CitationNetwork {

	@ElementList
	private List<Article> articles;

	public CitationNetwork(List<Article> articles) {
		this.articles = articles;
	}

	/**
	 * @return the articles
	 */
	public final List<Article> getArticles() {
		return articles;
	}

	/**
	 * @param articles
	 *            the articles to set
	 */
	public final void setArticles(List<Article> articles) {
		this.articles = articles;
	}
}
