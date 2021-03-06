/*
 *
 * Copyright 2018 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package uk.ac.ebi.ampt2d.metadata.rest.assemblers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.mapping.LinkCollector;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.EmbeddedWrappers;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uk.ac.ebi.ampt2d.metadata.persistence.entities.BaseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GenericResourceAssembler<ENTITY extends BaseEntity, RESOURCE extends Resource<ENTITY>>
        extends ResourceAssemblerSupport<ENTITY, RESOURCE> {

    @Autowired
    private RepositoryEntityLinks repositoryEntityLinks;

    @Autowired
    private LinkCollector linkCollector;

    private static final EmbeddedWrappers WRAPPERS = new EmbeddedWrappers(false);

    public GenericResourceAssembler(Class<?> controllerClass, Class<RESOURCE> resourceType) {
        super(controllerClass, resourceType);
    }

    @SuppressWarnings("unchecked")
    public RESOURCE toResource(ENTITY entity) {
        return (RESOURCE) new Resource<ENTITY>(entity, linkCollector.getLinksFor(entity,
                Arrays.asList(repositoryEntityLinks.linkToSingleResource(entity.getClass(), entity.getId()))));
    }

    public Resources<?> toResources(Class<ENTITY> type, List<ENTITY> entities) {
        if ( !entities.iterator().hasNext() ) {
            List<Object> content = Arrays.asList(WRAPPERS.emptyCollectionOf(type));
            return new Resources<Object>(content, getDefaultSelfLink());
        }

        List<RESOURCE> resourceList = toResources(entities);

        return new Resources<RESOURCE>(resourceList, getDefaultSelfLink());
    }

    private Link getDefaultSelfLink() {
        return new Link(ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString());
    }

}
