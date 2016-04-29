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
import de.tumitfahrer.services.TeamService;
import de.tumitfahrer.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/team")
@Api(value = "Team Controller", description = "Get the current and former teams.")
public class TeamController extends AbstractController {

    @Autowired
    TeamService teamService;

    @GET
    @ApiOperation("Returns the current team.")
    public Response getCurrentTeam() {
        return ResponseUtils.ok(teamService.getCurrentTeam());
    }

    @GET
    @Path("/{team}")
    @ApiOperation("Returns the current team.")
    public Response getTeam(@PathParam("team") String team) {
        return ResponseUtils.ok(teamService.getTeam(team));
    }
}