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

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

/**
 * @author dennis
 * 
 */
public abstract class WebClient {

	private DefaultHttpClient httpclient = new DefaultHttpClient();
	private HtmlCleaner cleaner = new HtmlCleaner();

	/**
	 * @param url
	 */
	protected TagNode getTagNodeFromUrl(String url) {
		HttpGet httpGet = new HttpGet(url);
		TagNode page = null;
		try {
			Thread.sleep(100);

			HttpResponse response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream pageStream = entity.getContent();
			page = cleaner.clean(pageStream);

			EntityUtils.consume(entity);
		} catch (ClientProtocolException e) {

		} catch (IOException e) {

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return page;
	}

}
