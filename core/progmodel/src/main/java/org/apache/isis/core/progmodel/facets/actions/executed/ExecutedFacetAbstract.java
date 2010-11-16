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


package org.apache.isis.core.progmodel.facets.actions.executed;

import org.apache.isis.core.metamodel.facets.Facet;
import org.apache.isis.core.metamodel.facets.FacetHolder;
import org.apache.isis.core.metamodel.facets.SingleValueFacetAbstract;
import org.apache.isis.core.metamodel.facets.actions.executed.ExecutedFacet;
import org.apache.isis.core.metamodel.spec.Target;


public abstract class ExecutedFacetAbstract extends SingleValueFacetAbstract implements ExecutedFacet {

    public static Class<? extends Facet> type() {
        return ExecutedFacet.class;
    }

    private final Where value;

    public ExecutedFacetAbstract(final Where value, final FacetHolder holder) {
        super(type(), holder);
        this.value = value;
    }

    public Where value() {
        return value;
    }

    public Target getTarget() {
        if (value == Where.LOCALLY) {
            return Target.LOCAL;
        }
        if (value == Where.REMOTELY) {
            return Target.REMOTE;
        }
        return Target.DEFAULT;
    }

    @Override
    protected String toStringValues() {
        return "where=" + value.getFriendlyName();
    }
}