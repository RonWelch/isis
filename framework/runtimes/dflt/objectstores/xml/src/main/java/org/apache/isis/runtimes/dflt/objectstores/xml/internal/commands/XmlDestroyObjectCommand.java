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

package org.apache.isis.runtimes.dflt.objectstores.xml.internal.commands;

import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.runtimes.dflt.objectstores.xml.internal.data.DataManager;
import org.apache.isis.runtimes.dflt.runtime.persistence.objectstore.transaction.DestroyObjectCommand;
import org.apache.isis.runtimes.dflt.runtime.persistence.objectstore.transaction.PersistenceCommandContext;
import org.apache.isis.runtimes.dflt.runtime.persistence.oidgenerator.simple.SerialOid;
import org.apache.isis.runtimes.dflt.runtime.transaction.ObjectPersistenceException;
import org.apache.log4j.Logger;

public final class XmlDestroyObjectCommand extends AbstractXmlPersistenceCommand implements DestroyObjectCommand {
    private static final Logger LOG = Logger.getLogger(XmlDestroyObjectCommand.class);

    public XmlDestroyObjectCommand(final ObjectAdapter adapter, final DataManager dataManager) {
        super(adapter, dataManager);
    }

    @Override
    public void execute(final PersistenceCommandContext context) throws ObjectPersistenceException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("  destroy object " + onObject());
        }
        final SerialOid oid = (SerialOid) onObject().getOid();
        getDataManager().remove(oid);
        onObject().setOptimisticLock(null);
    }

    @Override
    public String toString() {
        return "DestroyObjectCommand [object=" + onObject() + "]";
    }
}