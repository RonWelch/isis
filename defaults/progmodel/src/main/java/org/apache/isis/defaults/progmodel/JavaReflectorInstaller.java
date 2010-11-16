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


package org.apache.isis.defaults.progmodel;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.isis.core.commons.factory.InstanceFactory;
import org.apache.isis.core.metamodel.config.ConfigurationConstants;
import org.apache.isis.core.metamodel.config.IsisConfiguration;
import org.apache.isis.core.metamodel.facetdecorator.FacetDecorator;
import org.apache.isis.core.metamodel.facets.FacetFactory;
import org.apache.isis.core.metamodel.specloader.FacetDecoratorInstaller;
import org.apache.isis.core.metamodel.specloader.ObjectReflector;
import org.apache.isis.core.metamodel.specloader.ObjectReflectorInstaller;
import org.apache.isis.core.metamodel.specloader.ReflectorConstants;
import org.apache.isis.core.metamodel.specloader.classsubstitutor.ClassSubstitutor;
import org.apache.isis.core.metamodel.specloader.classsubstitutor.ClassSubstitutorComposite;
import org.apache.isis.core.metamodel.specloader.collectiontyperegistry.CollectionTypeRegistry;
import org.apache.isis.core.metamodel.specloader.collectiontyperegistry.CollectionTypeRegistryDefault;
import org.apache.isis.core.metamodel.specloader.progmodelfacets.ProgrammingModelFacets;
import org.apache.isis.core.metamodel.specloader.traverser.SpecificationTraverser;
import org.apache.isis.core.metamodel.specloader.validator.MetaModelValidator;
import org.apache.isis.core.runtime.installers.InstallerAbstract;
import org.apache.isis.core.runtime.installers.InstallerLookup;
import org.apache.isis.core.runtime.installers.InstallerLookupAware;

import com.google.common.collect.Lists;


public class JavaReflectorInstaller extends InstallerAbstract implements ObjectReflectorInstaller, InstallerLookupAware {

    private static final Logger LOG = Logger.getLogger(JavaReflectorInstaller.class);

    public static final String PROPERTY_BASE = ConfigurationConstants.ROOT;

    /**
     * Defaulted in the constructor.
     */
    private final LinkedHashSet<FacetDecoratorInstaller> decoratorInstallers;

    private InstallerLookup installerLookup;

    // /////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////

    public JavaReflectorInstaller() {
    	this("java");
    }

    public JavaReflectorInstaller(String name) {
    	super(ObjectReflectorInstaller.TYPE, name);
    	decoratorInstallers = new LinkedHashSet<FacetDecoratorInstaller>();
    }

    
    // /////////////////////////////////////////////////////
    // createReflector, doCreateReflector
    // /////////////////////////////////////////////////////

    /**
     * Should call {@link #addFacetDecoratorInstaller(ReflectorDecoratorInstaller)} prior to calling this.
     */
    public JavaReflector createReflector() {
        final ClassSubstitutor classSubstitutor = createClassSubstitutor(getConfiguration());
        final CollectionTypeRegistry collectionTypeRegistry = createCollectionTypeRegistry(getConfiguration());
        final SpecificationTraverser specificationTraverser = createSpecificationTraverser(getConfiguration());
        final ProgrammingModelFacets programmingModelFacets = createProgrammingModelFacets(getConfiguration());
        final Set<FacetDecorator> facetDecorators = createFacetDecorators(getConfiguration());
        final MetaModelValidator metaModelValidator = createMetaModelValidator(getConfiguration());

		final JavaReflector reflector = doCreateReflector(getConfiguration(), classSubstitutor, collectionTypeRegistry,
                specificationTraverser, programmingModelFacets, facetDecorators, metaModelValidator);

        return reflector;
    }

    /**
     * Hook method to allow subclasses to specify a different implementation of {@link ClassSubstitutor}.
     * 
     * <p>
     * By default, looks up implementation from provided {@link IsisConfiguration} using
     * {@link ReflectorConstants#CLASS_SUBSTITUTOR_CLASS_NAME_LIST}. If not specified, then defaults to
     * {@value ReflectorConstants#CLASS_SUBSTITUTOR_CLASS_NAME_DEFAULT}.
     * 
     * <p>
     * 
     */
    protected ClassSubstitutor createClassSubstitutor(IsisConfiguration configuration) {
        final String[] configuredClassNames = configuration
        	.getList(ReflectorConstants.CLASS_SUBSTITUTOR_CLASS_NAME_LIST);
        if(configuredClassNames == null || configuredClassNames.length == 0) {
			return InstanceFactory.createInstance(ReflectorConstants.CLASS_SUBSTITUTOR_CLASS_NAME_DEFAULT, ClassSubstitutor.class);
        }
        List<ClassSubstitutor> substitutors = Lists.newArrayList();
        for (String className : configuredClassNames) {
        	ClassSubstitutor substitutor = InstanceFactory.createInstance(className, ClassSubstitutor.class);
			substitutors.add(substitutor);
		}
        return substitutors.size() == 1? substitutors.get(0): new ClassSubstitutorComposite(substitutors);
    }

    /**
     * Hook method to allow subclasses to specify a different implementation of {@link SpecificationTraverser}.
     * 
     * <p>
     * By default, looks up implementation from provided {@link IsisConfiguration} using
     * {@link ReflectorConstants#SPECIFICATION_TRAVERSER_CLASS_NAME}. If not specified, then defaults to
     * {@value ReflectorConstants#SPECIFICATION_TRAVERSER_CLASS_NAME_DEFAULT}.
     */
    protected SpecificationTraverser createSpecificationTraverser(IsisConfiguration configuration) {
        final String specificationTraverserClassName = configuration.getString(ReflectorConstants.SPECIFICATION_TRAVERSER_CLASS_NAME,
                ReflectorConstants.SPECIFICATION_TRAVERSER_CLASS_NAME_DEFAULT);
        SpecificationTraverser specificationTraverser = InstanceFactory.createInstance(specificationTraverserClassName,
                SpecificationTraverser.class);
        return specificationTraverser;
    }


    /**
     * Hook method to allow subclasses to specify a different implementations (that is, sets of
     * {@link ProgrammingModelFacets}.
     * 
     * <p>
     * By default, looks up implementation from provided {@link IsisConfiguration} using
     * {@link ReflectorConstants#PROGRAMMING_MODEL_FACETS_CLASS_NAME}. If not specified, then defaults to
     * {@value ReflectorConstants#PROGRAMMING_MODEL_FACETS_CLASS_NAME_DEFAULT}.
     * 
     * <p>
     * The list of facets can be adjusted using
     * {@link ReflectorConstants#FACET_FACTORY_INCLUDE_CLASS_NAME_LIST} to specify additional
     * {@link FacetFactory factories} to include, and
     * {@link ReflectorConstants#FACET_FACTORY_EXCLUDE_CLASS_NAME_LIST} to exclude.
     */
    protected ProgrammingModelFacets createProgrammingModelFacets(final IsisConfiguration configuration) {
        ProgrammingModelFacets programmingModelFacets = lookupAndCreateProgrammingModelFacets(configuration);

        includeFacetFactories(configuration, programmingModelFacets);

        excludeFacetFactories(configuration, programmingModelFacets);

        return programmingModelFacets;
    }

    private ProgrammingModelFacets lookupAndCreateProgrammingModelFacets(final IsisConfiguration configuration) {
        final String progModelFacetsClassName = configuration.getString(ReflectorConstants.PROGRAMMING_MODEL_FACETS_CLASS_NAME,
                ReflectorConstants.PROGRAMMING_MODEL_FACETS_CLASS_NAME_DEFAULT);
        ProgrammingModelFacets programmingModelFacets = InstanceFactory.createInstance(progModelFacetsClassName,
                ProgrammingModelFacets.class);
        return programmingModelFacets;
    }

    /**
     * Factored out of {@link #createProgrammingModelFacets(IsisConfiguration)} so that subclasses that
     * choose to override can still support customization of their {@link ProgrammingModelFacets} in a similar
     * way.
     */
    protected void includeFacetFactories(
            final IsisConfiguration configuration,
            ProgrammingModelFacets programmingModelFacets) {
        final String[] facetFactoriesIncludeClassNames = configuration
                .getList(ReflectorConstants.FACET_FACTORY_INCLUDE_CLASS_NAME_LIST);
        if (facetFactoriesIncludeClassNames != null) {
            for (String facetFactoryClassName : facetFactoriesIncludeClassNames) {
                Class<? extends FacetFactory> facetFactory = InstanceFactory.loadClass(facetFactoryClassName, FacetFactory.class);
                programmingModelFacets.addFactory(facetFactory);
            }
        }
    }

    /**
     * Factored out of {@link #createProgrammingModelFacets(IsisConfiguration)} so that subclasses that
     * choose to override can still support customization of their {@link ProgrammingModelFacets} in a similar
     * way.
     */
    protected void excludeFacetFactories(
            final IsisConfiguration configuration,
            ProgrammingModelFacets programmingModelFacets) {
        final String[] facetFactoriesExcludeClassNames = configuration
                .getList(ReflectorConstants.FACET_FACTORY_EXCLUDE_CLASS_NAME_LIST);
        for (String facetFactoryClassName : facetFactoriesExcludeClassNames) {
            Class<? extends FacetFactory> facetFactory = InstanceFactory.loadClass(facetFactoryClassName, FacetFactory.class);
            programmingModelFacets.removeFactory(facetFactory);
        }
    }

    /**
     * Hook method to allow subclasses to specify a different sets of {@link FacetDecorator}s.
     * 
     * <p>
     * By default, returns the {@link FacetDecorator}s that are specified in the
     * {@link IsisConfiguration} (using {@link ReflectorConstants#FACET_DECORATOR_CLASS_NAMES}) along
     * with any {@link FacetDecorator}s explicitly registered using
     * {@link #addFacetDecoratorInstaller(FacetDecoratorInstaller)}. created using the
     * {@link FacetDecoratorInstaller}s.
     */
    protected Set<FacetDecorator> createFacetDecorators(IsisConfiguration configuration) {
        addFacetDecoratorInstallers(configuration);
        return createFacetDecorators(decoratorInstallers);
    }

    private void addFacetDecoratorInstallers(final IsisConfiguration configuration) {
        final String[] decoratorNames = configuration.getList(ReflectorConstants.FACET_DECORATOR_CLASS_NAMES);
        for (String decoratorName : decoratorNames) {
            if (LOG.isInfoEnabled()) {
                LOG.info("adding reflector facet decorator from configuration " + decoratorName);
            }
            addFacetDecoratorInstaller(lookupFacetDecorator(decoratorName));
        }
    }

    private FacetDecoratorInstaller lookupFacetDecorator(final String decoratorClassName) {
        return (FacetDecoratorInstaller) installerLookup.getInstaller(FacetDecoratorInstaller.class, decoratorClassName);
    }

    private Set<FacetDecorator> createFacetDecorators(final Set<FacetDecoratorInstaller> decoratorInstallers) {
        LinkedHashSet<FacetDecorator> decorators = new LinkedHashSet<FacetDecorator>();
        if (decoratorInstallers.size() == 0) {
            if (LOG.isInfoEnabled()) {
                LOG.info("No facet decorators installers added");
            }
        }
        for (final FacetDecoratorInstaller installer : decoratorInstallers) {
            decorators.addAll(installer.createDecorators());
        }
        return Collections.unmodifiableSet(decorators);
    }

    /**
     * Hook method to allow subclasses to specify a different implementation of {@link MetaModelValidator}.
     * 
     * <p>
     * By default, looks up implementation from provided {@link IsisConfiguration} using
     * {@link ReflectorConstants#META_MODEL_VALIDATOR_CLASS_NAME}. If not specified, then defaults to
     * {@value ReflectorConstants#META_MODEL_VALIDATOR_CLASS_NAME_DEFAULT}.
     */
    protected MetaModelValidator createMetaModelValidator(IsisConfiguration configuration) {
        final String metaModelValidatorClassName = configuration.getString(ReflectorConstants.META_MODEL_VALIDATOR_CLASS_NAME,
                ReflectorConstants.META_MODEL_VALIDATOR_CLASS_NAME_DEFAULT);
        MetaModelValidator metaModelValidator = InstanceFactory.createInstance(metaModelValidatorClassName,
                MetaModelValidator.class);
        return metaModelValidator;
    }


    /**
     * Creates the {@link CollectionTypeRegistry}, hardcoded to be the {@link CollectionTypeRegistryDefault}.
     * 
     * <p>
     * Note: the intention is to remove this interface and instead to use a mechanism similar to the
     * <tt>@Value</tt> annotation to specify which types represent collections. For now, have factored out
     * this method similar to be similar to the creation methods of other subcomponents such as the
     * {@link #createClassSubstitutor(IsisConfiguration) ClassSubstitutor}. Note however that this
     * method is <tt>final</tt> so that it cannot be overridden.
     */
    protected final CollectionTypeRegistry createCollectionTypeRegistry(final IsisConfiguration configuration) {
        return new CollectionTypeRegistryDefault();
    }

    /**
     * Hook method to allow for other implementations (still based on {@link JavaReflector}).
     */
    protected JavaReflector doCreateReflector(
            final IsisConfiguration configuration,
            final ClassSubstitutor classSubstitutor,
            final CollectionTypeRegistry collectionTypeRegistry,
            final SpecificationTraverser specificationTraverser,
            final ProgrammingModelFacets programmingModelFacets, 
            final Set<FacetDecorator> facetDecorators, 
            final MetaModelValidator metaModelValidator) {
        return new JavaReflector(configuration, classSubstitutor, collectionTypeRegistry, specificationTraverser, programmingModelFacets, facetDecorators, metaModelValidator);
    }

    // /////////////////////////////////////////////////////
    // Optionally Injected: InstallerLookup
    // /////////////////////////////////////////////////////

    /**
     * Injected by virtue of being {@link InstallerLookupAware}.
     */
    public void setInstallerLookup(InstallerLookup installerLookup) {
        this.installerLookup = installerLookup;
    }

    // /////////////////////////////////////////////////////
    // Optionally Injected: DecoratorInstallers
    // /////////////////////////////////////////////////////

    /**
     * Adds in {@link FacetDecoratorInstaller}; if <tt>null</tt> or if already added then request will be
     * silently ignored.
     */
    public void addFacetDecoratorInstaller(final FacetDecoratorInstaller decoratorInstaller) {
        if (decoratorInstaller == null) {
            return;
        }
        decoratorInstallers.add(decoratorInstaller);
    }


    
    // /////////////////////////////////////////////////////
    // Guice
    // /////////////////////////////////////////////////////

    
    @Override
    public List<Class<?>> getTypes() {
    	return listOf(ObjectReflector.class);
    }
}
