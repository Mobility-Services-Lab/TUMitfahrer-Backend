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
import de.tumitfahrer.assembler.RideAssembler;
import de.tumitfahrer.assembler.SearchAssembler;
import de.tumitfahrer.dtos.ride.response.RideDTO;
import de.tumitfahrer.dtos.search.SearchRequestDTO;
import de.tumitfahrer.entities.Ride;
import de.tumitfahrer.entities.Search;
import de.tumitfahrer.entities.User;
import de.tumitfahrer.services.SearchService;
import de.tumitfahrer.services.UserService;
import de.tumitfahrer.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/search")
@Api(value = "Search Controller", description = "Search engine for rides.")
public class SearchesController extends AbstractController {

    @Autowired
    private SearchService searchService;
    @Autowired
    private UserService userService;
    @Autowired
    private SearchAssembler searchAssembler;
    @Autowired
    private RideAssembler rideAssembler;

    @POST
    @ApiOperation(value = "Returns all rides found by given criteria.", response = RideDTO.class, responseContainer = "List")
    public Response getSearchResult(@HeaderParam("Authorization") String authHeader, SearchRequestDTO searchRequestDTO) {
        Search search = searchAssembler.toEntity(searchRequestDTO);

        User user = null;
        if (authHeader != null) {
            user = userService.loadByApiKey(authHeader);
        }
        search.setUserId(user == null ? null : user.getId());

        List<Ride> rides = searchService.find(search);
        List<RideDTO> ridesDTO = rideAssembler.toDto(rides);
        return ResponseUtils.ok(ridesDTO);
    }
}