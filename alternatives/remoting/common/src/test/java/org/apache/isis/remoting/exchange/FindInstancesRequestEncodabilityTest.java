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


package org.apache.isis.remoting.exchange;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.apache.isis.commons.matchers.NofMatchers.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.apache.isis.metamodel.encoding.EncodabilityContractTest;
import org.apache.isis.metamodel.encoding.Encodable;
import org.apache.isis.remoting.data.common.ReferenceData;
import org.apache.isis.remoting.data.query.PersistenceQueryData;
import org.apache.isis.remoting.exchange.AuthorizationRequestUsability;
import org.apache.isis.remoting.exchange.FindInstancesRequest;

public class FindInstancesRequestEncodabilityTest extends EncodabilityContractTest {

	private PersistenceQueryData mockPersistenceQueryData;


	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		mockPersistenceQueryData = context.mock(PersistenceQueryData.class);
	}


	protected Encodable createEncodable() {
		return new FindInstancesRequest(mockAuthSession, mockPersistenceQueryData);
	}
	
	
	@Override
	@Ignore
	@Test
	public void shouldRoundTrip() throws IOException {
		super.shouldRoundTrip();
	}

	@Override
	protected void assertRoundtripped(
			Object decodedEncodable,
			Object originalEncodable) {
		AuthorizationRequestUsability decoded = (AuthorizationRequestUsability) decodedEncodable;
		AuthorizationRequestUsability original = (AuthorizationRequestUsability) originalEncodable;
		
		// TODO: to complete, may need to setup mock expectations
		assertThat(decoded.getId(), is(equalTo(original.getId())));
	}

}