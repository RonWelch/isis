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



package org.apache.isis.core.runtime.options.standard;

import org.apache.isis.runtime.system.DeploymentType;

import static org.apache.isis.core.runtime.runner.Constants.TYPE_CLIENT;
import static org.apache.isis.core.runtime.runner.Constants.TYPE_EXPLORATION;
import static org.apache.isis.core.runtime.runner.Constants.TYPE_PROTOTYPE;
import static org.apache.isis.core.runtime.runner.Constants.TYPE_SERVER;
import static org.apache.isis.core.runtime.runner.Constants.TYPE_SERVER_EXPLORATION;
import static org.apache.isis.core.runtime.runner.Constants.TYPE_SERVER_PROTOTYPE;
import static org.apache.isis.core.runtime.runner.Constants.TYPE_SINGLE_USER;


public class OptionHandlerDeploymentTypeIsis extends OptionHandlerDeploymentType {
  
    public OptionHandlerDeploymentTypeIsis() {
        super(DeploymentType.PROTOTYPE, TYPE_EXPLORATION + "; " + TYPE_PROTOTYPE + " (default); " + TYPE_SINGLE_USER + "; " + TYPE_CLIENT + "; "
                + TYPE_SERVER_EXPLORATION + "; " + TYPE_SERVER_PROTOTYPE + "; " + TYPE_SERVER);
    }


}