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


package org.apache.isis.extensions.dnd.view.action;

import org.apache.isis.commons.debug.DebugString;
import org.apache.isis.commons.exceptions.IsisException;
import org.apache.isis.metamodel.adapter.ObjectAdapter;
import org.apache.isis.metamodel.consent.Consent;
import org.apache.isis.metamodel.consent.Veto;
import org.apache.isis.metamodel.spec.ObjectSpecification;
import org.apache.isis.extensions.dnd.view.content.AbstractObjectContent;


/**
 * Links an action on an object to a view.
 */
public class ObjectActionContent extends AbstractObjectContent implements ActionContent {
    private final ActionHelper actionHelper;
    private final ParameterContent[] parameters;

    public ObjectActionContent(final ActionHelper invocation) {
        this.actionHelper = invocation;
        parameters = invocation.createParameters();
    }

    @Override
    public Consent canClear() {
        return Veto.DEFAULT;
    }

    @Override
    public Consent canSet(final ObjectAdapter dragSource) {
        return Veto.DEFAULT;
    }

    @Override
    public void clear() {
        throw new IsisException("Invalid call");
    }

    public void debugDetails(final DebugString debug) {
        debug.appendln("action", getActionName());
        debug.appendln("target", getAdapter());
        String parameterSet = "";
        for (int i = 0; i < parameters.length; i++) {
            parameterSet += parameters[i];
        }
        debug.appendln("parameters", parameterSet);
    }

    public Consent disabled() {
        return actionHelper.disabled();
    }

    public ObjectAdapter execute() {
        return actionHelper.invoke();
    }

    public String getActionName() {
        return actionHelper.getName();
    }

    @Override
    public String getIconName() {
        return actionHelper.getIconName();
    }

    public ObjectAdapter getAdapter() {
        return actionHelper.getTarget();
    }

    public int getNoParameters() {
        return parameters.length;
    }

    @Override
    public ObjectAdapter getObject() {
        return actionHelper.getTarget();
    }

    public ParameterContent getParameterContent(final int index) {
        return parameters[index];
    }

    public ObjectAdapter getParameterObject(final int index) {
        return actionHelper.getParameter(index);
    }

    public ObjectSpecification getSpecification() {
        return getObject().getSpecification();
    }

    /**
     * Can't persist actions
     */
    @Override
    public boolean isPersistable() {
        return false;
    }

    @Override
    public boolean isObject() {
        return true;
    }

    public boolean isTransient() {
        return true;
    }

    @Override
    public void setObject(final ObjectAdapter object) {
        throw new IsisException("Invalid call");
    }

    public String title() {
        return actionHelper.title();
    }

    @Override
    public String windowTitle() {
        return getActionName();
    }

    public String getId() {
        return actionHelper.getName();
    }

    public String getDescription() {
        return actionHelper.getDescription();
    }

    public String getHelp() {
        return actionHelper.getHelp();
    }

    public ObjectAdapter[] getOptions() {
        return null;
    }

    public boolean isOptionEnabled() {
        return false;
    }

}