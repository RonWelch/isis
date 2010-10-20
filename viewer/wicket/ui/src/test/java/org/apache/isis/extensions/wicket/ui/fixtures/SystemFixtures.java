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


package org.apache.isis.extensions.wicket.ui.fixtures;

import org.apache.isis.extensions.wicket.model.mementos.ObjectAdapterMemento;
import org.apache.isis.extensions.wicket.ui.components.widgets.cssmenu.CssMenuLinkFactory;
import org.apache.isis.metamodel.adapter.oid.Oid;
import org.apache.isis.metamodel.adapter.oid.stringable.OidStringifier;
import org.apache.isis.metamodel.spec.feature.ObjectAction;
import org.apache.wicket.markup.html.link.Link;
import org.jmock.Expectations;
import org.jmock.Mockery;

public final class SystemFixtures {

	private Mockery context;
	public SystemFixtures(Mockery context) {
		this.context = context;
	}

	public void enstringOid(final OidStringifier mockOidStringifier, final Oid mockOid, final String returns) {
		context.checking(new Expectations() {
			{
				allowing(mockOidStringifier).enString(mockOid);
				will(returnValue(returns));
			}
		});
	}

	public <T> void newLink(final CssMenuLinkFactory mockLinkBuilder,
			final String linkId, final ObjectAdapterMemento adapterMemento, final ObjectAction noAction, final Link<T> returns) {
		context.checking(new Expectations() {
			{
				allowing(mockLinkBuilder).newLink(adapterMemento, noAction, linkId);
				will(returnValue(returns));
			}
		});
	}

}