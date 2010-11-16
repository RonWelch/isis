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


package org.apache.isis.core.runtime.runner.options;

import java.util.List;

import org.apache.isis.core.commons.lang.Maybe;
import org.apache.isis.core.runtime.options.standard.OptionHandlerViewer;
import org.apache.isis.core.runtime.runner.IsisRunner;
import org.apache.isis.runtime.system.DeploymentType;

public final class OptionValidatorForViewers implements
        OptionValidator {
    private final OptionHandlerViewer optionHandlerViewer;

    public OptionValidatorForViewers(
            OptionHandlerViewer optionHandlerViewer) {
        this.optionHandlerViewer = optionHandlerViewer;
    }

    @Override
    public Maybe<String> validate(DeploymentType deploymentType) {
        List<String> viewerNames = optionHandlerViewer.getViewerNames();

        boolean fail = !deploymentType.canSpecifyViewers(viewerNames);
        String failMsg = String
                .format(
                        "Error: cannot specify %s viewer%s for deployment type %s\n",
                        Strings.plural(viewerNames, "more than one",
                                "any"), Strings.plural(
                                viewerNames, "", "s"),
                        deploymentType
                                .nameLowerCase());
        return Maybe
                .setIf(
                        fail,
                        failMsg);
    }
}