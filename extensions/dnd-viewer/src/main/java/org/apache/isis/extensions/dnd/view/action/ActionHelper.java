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

import org.apache.isis.metamodel.adapter.ObjectAdapter;
import org.apache.isis.metamodel.consent.Consent;
import org.apache.isis.metamodel.spec.ObjectSpecification;
import org.apache.isis.metamodel.spec.feature.ObjectAction;
import org.apache.isis.metamodel.spec.feature.ObjectActionParameter;
import org.apache.isis.metamodel.spec.feature.OneToOneActionParameter;
import org.apache.isis.metamodel.spec.feature.ParseableEntryActionParameter;


public class ActionHelper {

    public static ActionHelper createInstance(final ObjectAdapter target, final ObjectAction action) {
        final int numberParameters = action.getParameterCount();
        final ObjectAdapter[] parameters = new ObjectAdapter[numberParameters];
        final ObjectActionParameter[] parameterSpecs = action.getParameters();
        ObjectAdapter[] defaultValues;
        ObjectAdapter[][] options;

        // action choices most be old or new syntax - cannot be mixed

        defaultValues = new ObjectAdapter[parameterSpecs.length];
        options = new ObjectAdapter[parameterSpecs.length][];

        for (int i = 0; i < parameterSpecs.length; i++) {
            defaultValues[i] = parameterSpecs[i].getDefault(target);
            options[i] = parameterSpecs[i].getChoices(target);
        }

        if (!hasValues(defaultValues) && !hasValues(options)) {
            // fall back to old method

            defaultValues = action.getDefaults(target);
            options = action.getChoices(target);
        }

        for (int i = 0; i < parameterSpecs.length; i++) {
            if (defaultValues[i] != null) {
                parameters[i] = defaultValues[i];
            } else {
                parameters[i] = null; // PersistorUtil.createValueInstance(noap.getSpecification());
            }
        }

        /*
         * int[] maxLength = action.getParameterMaxLengths(); int[] typicalLength =
         * action.getParameterTypicalLengths(); int[] noLines = action.getParameterNoLines(); boolean[]
         * canWrap = action.canParametersWrap();
         */
        return new ActionHelper(target, action, parameters, defaultValues, options);
    }
    private final ObjectAction action;
    private final ObjectAdapter[] parameters;
    private final ObjectAdapter target;
    private final ObjectAdapter[][] options;

    private ActionHelper(
            final ObjectAdapter target,
            final ObjectAction action,
            final ObjectAdapter[] parameters,
            final ObjectAdapter[] defaultValues,
            final ObjectAdapter[][] options) {
        this.target = target;
        this.action = action;
        this.parameters = parameters;
        this.options = options;
    }

    public ParameterContent[] createParameters() {
        final ParameterContent[] parameterContents = new ParameterContent[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            final ObjectActionParameter[] parameters2 = action.getParameters();
            final ObjectAdapter adapter = parameters[i];
            final ObjectSpecification specification = parameters2[i].getSpecification();
            if (specification.isParseable()) {
                final ParseableEntryActionParameter parseableEntryActionParameter = (ParseableEntryActionParameter) parameters2[i];
                parameterContents[i] = new TextParseableParameterImpl(parseableEntryActionParameter, adapter, options[i], i,
                        this);
            } else {
                parameterContents[i] = new ObjectParameterImpl((OneToOneActionParameter) parameters2[i], adapter, options[i],
                        i, this);
            }
        }

        return parameterContents;
    }

    public Consent disabled() {
        // REVIEW this is no good as it lumps all the parameters together; I need to know which parameter is
        // disabled!
        return action.isProposedArgumentSetValid(target, parameters);
    }

    public String getName() {
        return action.getName();
    }

    public String getDescription() {
        return action.getDescription();
    }

    public String getHelp() {
        return action.getHelp();
    }

    public ObjectAdapter getParameter(final int index) {
        return parameters[index];
    }

    public ObjectAdapter getTarget() {
        return action.realTarget(target);
    }

    public ObjectAdapter invoke() {
        return action.execute(target, parameters);
    }

    public void setParameter(final int index, final ObjectAdapter parameter) {
        this.parameters[index] = parameter;
    }

    public String title() {
        return getTarget().titleString();
    }

    public String getIconName() {
        return getTarget().getIconName();
    }

    private static boolean hasValues(ObjectAdapter[] values) {
        if (values != null) {
            for (ObjectAdapter adapter : values) {
                if (adapter != null) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean hasValues(ObjectAdapter[][] values) {
        if (values != null) {
            for (ObjectAdapter[] adapters : values) {
                if (hasValues(adapters)) {
                    return true;
                }
            }
        }
        return false;
    }
}