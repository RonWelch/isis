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


package org.apache.isis.runtime.fixturesinstaller.config;

import org.apache.log4j.Logger;
import org.apache.isis.commons.exceptions.IsisException;
import org.apache.isis.commons.factory.InstanceFactory;
import org.apache.isis.metamodel.config.ConfigurationConstants;
import org.apache.isis.runtime.fixture.ObjectLoaderFixture;
import org.apache.isis.runtime.fixturesinstaller.FixturesInstallerAbstract;
import org.apache.isis.runtime.fixturesinstaller.FixturesInstallerDelegate;


public class FixturesFromConfiguration extends FixturesInstallerAbstract {
	
	private static final Logger LOG = Logger.getLogger(FixturesFromConfiguration.class);
    private static final String NAKEDOBJECTS_FIXTURES = ConfigurationConstants.ROOT + "fixtures";
    private static final String NAKEDOBJECTS_FIXTURES_PREFIX = ConfigurationConstants.ROOT + "fixtures.prefix";
    private static final String EXPLORATION_OBJECTS = ConfigurationConstants.ROOT + "exploration-objects";
    
    public FixturesFromConfiguration() {
    	super("configuration");
    }
    
    protected void addFixturesTo(FixturesInstallerDelegate delegate) {
        String fixturePrefix = getConfiguration().getString(NAKEDOBJECTS_FIXTURES_PREFIX);
        fixturePrefix = fixturePrefix == null ? "" : fixturePrefix.trim();
        if (fixturePrefix.length() > 0 && !fixturePrefix.endsWith(ConfigurationConstants.DELIMITER)) {
            fixturePrefix = fixturePrefix + ConfigurationConstants.DELIMITER;
        }

		try {
	        final String[] fixtureList = getConfiguration().getList(NAKEDOBJECTS_FIXTURES);
	        boolean fixtureLoaded = false;
            for (int i = 0; i < fixtureList.length; i++) {
                String fixtureFullyQualifiedName = fixturePrefix + fixtureList[i];
                LOG.info("  adding fixture " + fixtureFullyQualifiedName);
				final Object fixture = InstanceFactory.createInstance(fixtureFullyQualifiedName);
				fixtureLoaded = true;
				delegate.addFixture(fixture);
            }
            if (getConfiguration().getBoolean(EXPLORATION_OBJECTS)){
                delegate.addFixture(new ObjectLoaderFixture());
            }
	        if (!fixtureLoaded) {
	        	LOG.warn("No fixtures loaded from configuration");
	        }
		} catch (IllegalArgumentException e) {
            throw new IsisException(e);
		} catch (SecurityException e) {
			throw new IsisException(e);
		}
    }

}