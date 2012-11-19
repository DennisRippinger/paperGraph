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

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * @author dennis
 * 
 */
@Root
public class Author {

	@Element(required=false)
	private Double id;

	@Element(required=false)
	private String name;

	@Element(required=false)
	private String university;

	/**
	 * @return the id
	 */
	public final Double getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @return the university
	 */
	public final String getUniversity() {
		return university;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public final void setId(Double id) {
		this.id = id;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public final void setName(String name) {
		this.name = name;
	}

	/**
	 * @param university
	 *            the university to set
	 */
	public final void setUniversity(String university) {
		this.university = university;
	}

}
