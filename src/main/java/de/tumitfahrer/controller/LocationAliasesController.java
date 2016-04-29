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

package de.tumitfahrer.controller;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import de.tumitfahrer.assembler.LocationAliasAssember;
import de.tumitfahrer.assembler.LocationEntryAssember;
import de.tumitfahrer.dtos.locationalias.LocationAliasResponseDTO;
import de.tumitfahrer.dtos.locationentry.LocationEntryResponseDTO;
import de.tumitfahrer.entities.LocationAlias;
import de.tumitfahrer.entities.LocationEntry;
import de.tumitfahrer.security.annotations.CacheMaxAge;
import de.tumitfahrer.services.LocationAliasService;
import de.tumitfahrer.util.ResponseUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Path("/locations")
@Api(value = "Location Aliases Controller", description = "Maintains a lists of custom locations with aliases.")
public class LocationAliasesController extends AbstractController {

    @Autowired
    private LocationAliasService locationEntryService;

    @Autowired
    private LocationEntryAssember locationEntryAssember;

    @Autowired
    private LocationAliasAssember locationAliasAssember;

    @GET
    @CacheMaxAge(time = 6, unit = TimeUnit.HOURS)
    @ApiOperation("Returns a list of all location entries with all known aliases")
    public Response getAllEntries() {
        final Collection<LocationEntry> entries = locationEntryService.getAllEntries();
        final LocationEntryResponseDTO locationEntryResponseDTO = locationEntryAssember.toResponse(entries);
        return ResponseUtils.ok(locationEntryResponseDTO);
    }

    @GET
    @Path("/search")
    @ApiOperation("Allows to search for entries based on a search query")
    public Response searchForAliases(@QueryParam("q") @NotEmpty final String query) {
        final Collection<LocationAlias> entries = locationEntryService.searchForQuery(query);
        final LocationAliasResponseDTO locationAliasResponseDTO = locationAliasAssember.toResponse(entries);
        return ResponseUtils.ok(locationAliasResponseDTO);
    }

}
