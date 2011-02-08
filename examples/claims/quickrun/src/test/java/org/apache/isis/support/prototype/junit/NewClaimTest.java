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

package org.apache.isis.support.prototype.junit;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.apache.isis.support.prototype.dom.claim.Claim;
import org.apache.isis.support.prototype.fixture.ClaimsFixture;
import org.apache.isis.viewer.junit.Fixture;
import org.apache.isis.viewer.junit.Fixtures;
import org.junit.Test;

@Fixtures({ @Fixture(ClaimsFixture.class) })
public class NewClaimTest extends AbstractTest {

    @Test
    public void whenCreateNewClaimDefaultsOk() throws Exception {
        Claim newClaim = claimRepository.newClaim(tomEmployee);
        assertThat(newClaim.getDescription(), is("enter a description here"));
        assertThat(newClaim.getStatus(), is("New"));
        assertThat(newClaim.getApprover(), is(tomEmployee.getApprover()));
        assertThat(newClaim.getItems().size(), is(0));
    }

}
