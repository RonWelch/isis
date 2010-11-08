/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */


package org.apache.isis.core.metamodel.config;

import org.apache.log4j.Logger;
import org.apache.isis.core.commons.resource.ResourceStreamSourceContextLoaderClassPath;
import org.apache.isis.core.commons.resource.ResourceStreamSourceFileSystem;

/**
 * Convenience implementation of {@link ConfigurationBuilder} that
 * loads configuration resource as per {@link ConfigurationBuilderFileSystem}
 * and otherwise from the {@link ResourceStreamSourceContextLoaderClassPath classpath}.
 * 
 * @see ResourceStreamSourceFileSystem
 */
public class ConfigurationBuilderDefault extends ConfigurationBuilderResourceStreams {

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger
			.getLogger(ConfigurationBuilderDefault.class);
	
	public ConfigurationBuilderDefault() {
		super(
				new ResourceStreamSourceFileSystem(ConfigurationConstants.DEFAULT_CONFIG_DIRECTORY),
				new ResourceStreamSourceFileSystem(ConfigurationConstants.WEBINF_CONFIG_DIRECTORY),
				new ResourceStreamSourceContextLoaderClassPath()
			);
	}



}