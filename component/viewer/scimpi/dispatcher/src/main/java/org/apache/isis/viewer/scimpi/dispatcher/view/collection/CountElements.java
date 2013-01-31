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

package org.apache.isis.viewer.scimpi.dispatcher.view.collection;

import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.metamodel.facets.collections.modify.CollectionFacet;
import org.apache.isis.core.metamodel.spec.feature.ObjectAssociation;
import org.apache.isis.viewer.scimpi.dispatcher.processor.TemplateProcessor;
import org.apache.isis.viewer.scimpi.dispatcher.view.AbstractObjectProcessor;

public class CountElements extends AbstractObjectProcessor {

    @Override
    protected void process(final TemplateProcessor templateProcessor, final ObjectAdapter collection) {
        final CollectionFacet facet = collection.getSpecification().getFacet(CollectionFacet.class);
        final int size = facet.size(collection);
        if (size == 0) {
            templateProcessor.appendHtml(templateProcessor.getOptionalProperty("none", "0"));
        } else if (size == 1) {
            templateProcessor.appendHtml(templateProcessor.getOptionalProperty("one", "1"));
        } else {
            final String text = templateProcessor.getOptionalProperty("many", "" + size);
            templateProcessor.appendHtml(String.format(text, size));
        }
    }

    @Override
    protected String checkFieldType(final ObjectAssociation objectField) {
        return objectField.isOneToManyAssociation() ? null : "must be a collection";
    }

    @Override
    public String getName() {
        return "count";
    }

}