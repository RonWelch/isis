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

package org.apache.isis.viewer.wicket.ui.components.collection;

import java.util.List;


import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;

import org.apache.isis.core.metamodel.spec.feature.OneToManyAssociation;
import org.apache.isis.viewer.wicket.model.links.LinkAndLabel;
import org.apache.isis.viewer.wicket.model.models.EntityCollectionModel;
import org.apache.isis.viewer.wicket.model.models.EntityModel;
import org.apache.isis.viewer.wicket.ui.ComponentType;
import org.apache.isis.viewer.wicket.ui.components.additionallinks.EntityActionUtil;
import org.apache.isis.viewer.wicket.ui.components.scalars.ScalarPanelAbstract;
import org.apache.isis.viewer.wicket.ui.panels.PanelAbstract;

/**
 * Panel for rendering entity collection; analogous to (any concrete subclass
 * of) {@link ScalarPanelAbstract}.
 */
public class CollectionPanel extends PanelAbstract<EntityCollectionModel> {

    private static final long serialVersionUID = 1L;

    private static final String ID_COLLECTION = "collection";
    private static final String ID_FEEDBACK = "feedback";

    private static EntityCollectionModel createEntityCollectionModel(EntityModel entityModel, OneToManyAssociation otma) {
        EntityCollectionModel collectionModel = EntityCollectionModel.createParented(entityModel, otma);
        List<LinkAndLabel> entityActions = EntityActionUtil.entityActions(entityModel, otma);

        collectionModel.addEntityActions(entityActions);
        return collectionModel;
    }

    public CollectionPanel(final String id, final EntityModel entityModel, OneToManyAssociation otma) {
        this(id, createEntityCollectionModel(entityModel, otma));
    }

    CollectionPanel(String id, EntityCollectionModel collectionModel) {
        super(id, collectionModel);

        buildGui();
    }

    
    private void buildGui() {
        final WebMarkupContainer markupContainer = new WebMarkupContainer(ID_COLLECTION);

        final Component collectionContents = getComponentFactoryRegistry().addOrReplaceComponent(markupContainer, ComponentType.COLLECTION_CONTENTS, getModel());
        
        addOrReplace(new ComponentFeedbackPanel(ID_FEEDBACK, collectionContents));
        addOrReplace(markupContainer);
    }
    
}
