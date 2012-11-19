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
package de.due.ec.client;

import it.uniroma1.dis.wiserver.gexf4j.core.EdgeType;
import it.uniroma1.dis.wiserver.gexf4j.core.Gexf;
import it.uniroma1.dis.wiserver.gexf4j.core.Graph;
import it.uniroma1.dis.wiserver.gexf4j.core.Node;
import it.uniroma1.dis.wiserver.gexf4j.core.data.Attribute;
import it.uniroma1.dis.wiserver.gexf4j.core.data.AttributeClass;
import it.uniroma1.dis.wiserver.gexf4j.core.data.AttributeList;
import it.uniroma1.dis.wiserver.gexf4j.core.data.AttributeType;
import it.uniroma1.dis.wiserver.gexf4j.core.impl.GexfImpl;
import it.uniroma1.dis.wiserver.gexf4j.core.impl.StaxGraphWriter;
import it.uniroma1.dis.wiserver.gexf4j.core.impl.data.AttributeListImpl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import de.due.ec.dto.Article;
import de.due.ec.dto.CitationNetwork;
import de.due.ec.httpclient.AcmDatabaseClient;
import de.due.ec.httpclient.IScienceDB;

public class Main {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		IScienceDB sienceDB = new AcmDatabaseClient();

		List<Article> articles = sienceDB.getArticlesByKeywords("", "Game+AI",
				20);

		Gexf gexf = new GexfImpl();
		Graph graph = gexf.getGraph();
		graph.setDefaultEdgeType(EdgeType.DIRECTED);

		AttributeList attrList = new AttributeListImpl(AttributeClass.NODE);
		graph.getAttributeLists().add(attrList);

		Attribute yearAttribute = attrList.createAttribute("0",
				AttributeType.INTEGER, "Date");

		for (Article article : articles) {
			Node articleNode = graph.createNode(article.getArticleID()
					.toString());
			articleNode.setLabel(article.getTitle()).getAttributeValues()
					.addValue(yearAttribute, article.getPublished());
			for (Article innerArticle : article.getReferences()) {
				Node innerNode = graph.createNode(innerArticle.getArticleID()
						.toString());
				innerNode.setLabel(innerArticle.getTitle())
						.getAttributeValues()
						.addValue(yearAttribute, innerArticle.getPublished());
				innerNode.connectTo(articleNode);
			}

		}

		StaxGraphWriter graphWriter = new StaxGraphWriter();
		graphWriter.writeToStream(gexf, new FileWriter(
				"target/citedByNetwork.gexf"), "UTF-8");

		Serializer serializer = new Persister();
		File result = new File("target/citationNetwork.xml");

		try {
			serializer.write(new CitationNetwork(articles), result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
