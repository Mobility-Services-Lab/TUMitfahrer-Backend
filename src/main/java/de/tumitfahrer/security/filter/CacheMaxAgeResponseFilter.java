/*
 * Copyright 2016 TUM Technische Universität München
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.tumitfahrer.security.filter;

import de.tumitfahrer.security.annotations.CacheMaxAge;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;

@Provider
@CacheMaxAge
@Priority(Priorities.HEADER_DECORATOR)
public class CacheMaxAgeResponseFilter implements ContainerResponseFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(final ContainerRequestContext containerRequestContext, final ContainerResponseContext containerResponseContext) throws IOException {

        final Method method = resourceInfo.getResourceMethod();

        if (method != null) {

            // check if a cache control header is already present
            final Collection<Object> cacheControls = containerResponseContext.getHeaders().get(HttpHeaders.CACHE_CONTROL);
            if (cacheControls != null && cacheControls.size() > 0) {
                // abort, we don't want to overwrite any existing headers
                return;
            }

            // add Cache-Control: max-age header
            final CacheMaxAge cacheAnnotation = method.getAnnotation(CacheMaxAge.class);
            final String headerValue = "max-age= " + cacheAnnotation.unit().toSeconds(cacheAnnotation.time());
            containerResponseContext.getHeaders().putSingle(HttpHeaders.CACHE_CONTROL, headerValue);
        }
    }
}
