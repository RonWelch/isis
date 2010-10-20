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


package org.apache.isis.extensions.wicket.viewer.integration.nof;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.isis.extensions.wicket.viewer.integration.wicket.AuthenticatedWebSessionForIsis;
import org.apache.isis.metamodel.authentication.AuthenticationSession;
import org.apache.isis.metamodel.commons.exceptions.NotYetImplementedException;
import org.apache.isis.runtime.context.IsisContext;
import org.apache.isis.runtime.session.IsisSession;
import org.apache.isis.runtime.session.IsisSessionFactory;
import org.apache.isis.runtime.system.ContextCategory;
import org.apache.isis.runtime.system.internal.InitialisationSession;
import org.apache.wicket.Session;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;

/**
 * Implementation of Isis' {@link IsisContext}, associating a
 * {@link IsisSession} with a Wicket {@link Session}.
 * 
 * <p>
 * This implementation also takes multi-threading into account, so that the browser can submit multiple
 * requests on the same session simultaneously (eg to render an image of a pojo).
 */
public class IsisContextForWicket extends IsisContext {

	public static class WicketContextCategory extends ContextCategory {

		@Override
		public boolean canSpecifyViewers(List<String> viewers) {
			return false;
		}

		@Override
		public void initContext(IsisSessionFactory sessionFactory) {
			new IsisContextForWicket(
					ContextReplacePolicy.NOT_REPLACEABLE, SessionClosePolicy.EXPLICIT_CLOSE, sessionFactory);
		}
	}
	
	private static final class GetSessionIdFunction implements
			Function<Session, String> {
		@Override
		public String apply(Session from) {
			return from.getId();
		}
	}

	/**
	 * Only used while bootstrapping, corresponding to the {@link InitialisationSession}.
	 */
	private IsisSession bootstrapSession;
	/**
	 * Maps (our custom) {@link AuthenticatedWebSessionForIsis Wicket session}s to vanilla {@link IsisSession}s.
	 */
	private Map<AuthenticatedWebSessionForIsis, IsisSession> sessionMap = Maps.newHashMap();

	protected IsisContextForWicket(ContextReplacePolicy replacePolicy,
			SessionClosePolicy sessionClosePolicy,
			IsisSessionFactory sessionFactory) {
		super(replacePolicy, sessionClosePolicy, sessionFactory);
	}

	@Override
	public String[] allSessionIds() {
		Collection<String> transform = Collections2.transform(
				sessionMap.keySet(), new GetSessionIdFunction());
		return transform.toArray(new String[0]);
	}

	@Override
	protected void closeAllSessionsInstance() {
		throw new NotYetImplementedException();
	}

	@Override
	protected IsisSession getSessionInstance(String sessionId) {
		throw new NotYetImplementedException();
	}
	
	@Override
	public IsisSession getSessionInstance() {
		// special case handling if still bootstrapping
		if (bootstrapSession != null) {
			return bootstrapSession;
		}
		
		Session session = Session.get();
		IsisSession isisSession = sessionMap.get(session);
		return isisSession;
	}

	@Override
	public IsisSession openSessionInstance(AuthenticationSession session) {
		
		// special case handling if still bootstrapping
		if (session instanceof InitialisationSession) {
			bootstrapSession = getSessionFactory().openSession(session);
			bootstrapSession.open();
			return bootstrapSession;
		}

		// otherwise, regular processing
		return openSessionOrRegisterUsageOnExisting(session);
	}

	private synchronized IsisSession openSessionOrRegisterUsageOnExisting(
			AuthenticationSession authSession) {
		// we don't apply any session close policy here;
		// there could be multiple threads using a session.
		
		AuthenticatedWebSessionForIsis webSession = (AuthenticatedWebSessionForIsis) Session.get();
		webSession.registerUseByThread();
		
		IsisSession isisSession = sessionMap.get(webSession);
		if (isisSession == null) {
			isisSession = getSessionFactoryInstance().openSession(authSession);
			// put into map prior to opening, so that subsequent calls to
			// getSessionInstance() will find this new session.
			sessionMap.put(webSession, isisSession);
			isisSession.open();
		}

		return isisSession;
	}

	@Override
	public synchronized void closeSessionInstance() {
		// special case handling if still bootstrapping
		if (bootstrapSession != null) {

			bootstrapSession.close();
			bootstrapSession = null;
			return;
		}
		
		// otherwise, regular processing
		closeSessionOrDeregisterUsageOnExisting();
	}

	private synchronized void closeSessionOrDeregisterUsageOnExisting() {
		AuthenticatedWebSessionForIsis webSession = (AuthenticatedWebSessionForIsis) Session.get();
		boolean shouldClose = webSession.deregisterUseByThread();
		
		IsisSession isisSession = sessionMap.get(webSession);
		if (isisSession == null) {
			// nothing to be done
			return;
		}

		if (shouldClose) {
			isisSession.close();
			// remove after closing, so that any calls to getSessionInstance()
			// made while closing will still find this session
			sessionMap.remove(webSession);
		}
		
	}

	@Override
	public String debugTitle() {
		return "Wicket Context";
	}

}