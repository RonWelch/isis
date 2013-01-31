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

package org.apache.isis.viewer.scimpi.dispatcher.view.message;

import org.apache.isis.viewer.scimpi.dispatcher.context.RequestState;
import org.apache.isis.viewer.scimpi.dispatcher.processor.TemplateProcessor;
import org.apache.isis.viewer.scimpi.dispatcher.view.AbstractElementProcessor;

public class Errors extends AbstractElementProcessor {

    @Override
    public void process(final TemplateProcessor templateProcessor, RequestState state) {
        final String cls = templateProcessor.getOptionalProperty(CLASS);
        final StringBuffer buffer = new StringBuffer();
        write(templateProcessor, cls, buffer);
        if (buffer.length() > 0) {
            templateProcessor.appendHtml("<div class=\"error\">");
            templateProcessor.appendHtml(buffer.toString());
            templateProcessor.appendHtml("</div>");
        }
    }

    public static void write(final TemplateProcessor templateProcessor, String cls, final StringBuffer buffer) {
        if (cls == null) {
            cls = "error";
        }
        final String message = (String) templateProcessor.getContext().getVariable("_error-message");
        if (message != null) {
            buffer.append(message);
        }
        final String details = (String) templateProcessor.getContext().getVariable("_error-details");
        if (details != null) {
            buffer.append(details);
        }

        /*
         * final MessageBroker messageBroker = IsisContext.getMessageBroker();
         * final List<String> warnings = messageBroker.getWarnings(); for (final
         * String warning : warnings) { buffer.append("<div class=\"" + cls +
         * "\">" + warning + "</div>"); }
         */
    }

    @Override
    public String getName() {
        return "errors";
    }

}