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

package org.apache.isis.core.progmodel.facets.collections.sortedby;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;

import org.apache.isis.applib.annotation.Render;
import org.apache.isis.applib.annotation.SortedBy;
import org.apache.isis.applib.annotation.Render.Type;
import org.apache.isis.core.metamodel.facetapi.Facet;
import org.apache.isis.core.metamodel.facetapi.MetaModelValidatorRefiner;
import org.apache.isis.core.metamodel.facets.FacetFactory.ProcessMethodContext;
import org.apache.isis.core.metamodel.facets.FacetedMethod;
import org.apache.isis.core.metamodel.facets.collections.sortedby.SortedByFacet;
import org.apache.isis.core.metamodel.facets.members.resolve.RenderFacet;
import org.apache.isis.core.progmodel.facets.AbstractFacetFactoryTest;

public class SortedByAnnotationFacetFactoryTest extends AbstractFacetFactoryTest {

    private SortedByAnnotationFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new SortedByAnnotationFacetFactory();
    }

    @Override
    protected void tearDown() throws Exception {
        facetFactory = null;
        super.tearDown();
    }


    public void testAnnotationPickedUpOnSortedSet() {
        class Order {
        
        }
        class OrderComparator implements Comparator<Order> {
            @Override
            public int compare(Order o1, Order o2) {
                return 0;
            }};
        class Customer {
            @SortedBy(OrderComparator.class)
            public SortedSet<Order> getOrders() {
                return null;
            }
        }
        facetedMethod = FacetedMethod.createForCollection(Customer.class, "orders");
        facetFactory.process(new ProcessMethodContext(Customer.class, facetedMethod.getMethod(), methodRemover, facetedMethod));

        final Facet facet = facetedMethod.getFacet(SortedByFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof SortedByFacetAnnotation);
        SortedByFacet sortedByFacet = (SortedByFacet) facet;
        assertEquals(OrderComparator.class, sortedByFacet.value());

        assertNoMethodsRemoved();
    }

    
    /**
     * This is picked up because {@link SortedByAnnotationFacetFactory} is also a
     * {@link MetaModelValidatorRefiner}.
     */
    public void testAnnotationAddedEvenIfClassIfNotComparator() {
        class Order {
        
        }
        class NotAComparator  {
            };
        class Customer {
            @SortedBy(NotAComparator.class)
            public SortedSet<Order> getOrders() {
                return null;
            }
        }
        facetedMethod = FacetedMethod.createForCollection(Customer.class, "orders");
        facetFactory.process(new ProcessMethodContext(Customer.class, facetedMethod.getMethod(), methodRemover, facetedMethod));

        final Facet facet = facetedMethod.getFacet(SortedByFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof SortedByFacetAnnotation);
        SortedByFacet sortedByFacet = (SortedByFacet) facet;
        assertEquals(NotAComparator.class, sortedByFacet.value());

        assertNoMethodsRemoved();
    }

}
