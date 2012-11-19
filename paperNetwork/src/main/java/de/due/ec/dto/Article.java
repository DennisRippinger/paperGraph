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
package de.due.ec.dto;

import java.net.URL;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * @author dennis
 * 
 */
@Root
public class Article {

	@Element(required = false)
	private Double articleID;
	@ElementList(required = false)
	private List<Author> authors;
	@ElementList(required = false)
	private List<Article> citedBy;
	@Element(required = false)
	private URL doi;
	@Element(required = false)
	private String isbn;
	@Element(required = false)
	private String published;
	@ElementList(required = false)
	private List<Article> references;
	@Element(required = false)
	private String title;

	/**
	 * @return the articleID
	 */
	public Double getArticleID() {
		return articleID;
	}

	/**
	 * @return the authors
	 */
	public final List<Author> getAuthors() {
		return authors;
	}

	/**
	 * @return the citedBy
	 */
	public List<Article> getCitedBy() {
		return citedBy;
	}

	/**
	 * @return the doi
	 */
	public final URL getDoi() {
		return doi;
	}

	/**
	 * @return the isbn
	 */
	public final String getIsbn() {
		return isbn;
	}

	/**
	 * @return the published
	 */
	public final String getPublished() {
		return published;
	}

	/**
	 * @return the references
	 */
	public final List<Article> getReferences() {
		return references;
	}

	/**
	 * @return the title
	 */
	public final String getTitle() {
		return title;
	}

	/**
	 * @param articleID
	 *            the articleID to set
	 */
	public void setArticleID(Double articleID) {
		this.articleID = articleID;
	}

	/**
	 * @param authors
	 *            the authors to set
	 */
	public final void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	/**
	 * @param citedBy
	 *            the citedBy to set
	 */
	public void setCitedBy(List<Article> citedBy) {
		this.citedBy = citedBy;
	}

	/**
	 * @param doi
	 *            the doi to set
	 */
	public final void setDoi(URL doi) {
		this.doi = doi;
	}

	/**
	 * @param isbn
	 *            the isbn to set
	 */
	public final void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	/**
	 * @param published
	 *            the published to set
	 */
	public final void setPublished(String published) {
		this.published = published;
	}

	/**
	 * @param references
	 *            the references to set
	 */
	public final void setReferences(List<Article> references) {
		this.references = references;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public final void setTitle(String title) {
		this.title = title;
	}

}
