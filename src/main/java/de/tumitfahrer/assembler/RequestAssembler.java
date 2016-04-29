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

package de.tumitfahrer.assembler;

import de.tumitfahrer.dtos.request.response.RideRequestEntityDTO;
import de.tumitfahrer.dtos.request.response.RideRequestResponseDTO;
import de.tumitfahrer.dtos.request.response.RideRequestsResponseDTO;
import de.tumitfahrer.entities.Request;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RequestAssembler extends AbstractAssembler {

    public RideRequestResponseDTO toResponse(Request request) {
        RideRequestResponseDTO rideRequestResponseDTO = new RideRequestResponseDTO();
        rideRequestResponseDTO.setRequest(mapper.map(request, RideRequestEntityDTO.class));
        return rideRequestResponseDTO;
    }

    public List<RideRequestEntityDTO> toDto(List<Request> requests) {
        List<RideRequestEntityDTO> rideRequestsDTO = new ArrayList<>();
        for (Request request : requests) {
            RideRequestEntityDTO requestDTO = mapper.map(request, RideRequestEntityDTO.class);
            rideRequestsDTO.add(requestDTO);
        }
        return rideRequestsDTO;
    }

    public RideRequestsResponseDTO toResponseFromDto(List<RideRequestEntityDTO> requestsDTO) {
        RideRequestsResponseDTO rideRequestsResponseDTO = new RideRequestsResponseDTO();
        rideRequestsResponseDTO.setRequests(requestsDTO);
        return rideRequestsResponseDTO;
    }

    public RideRequestsResponseDTO toResponse(List<Request> requests) {
        return toResponseFromDto(toDto(requests));
    }

    public Request toEntity(de.tumitfahrer.dtos.request.request.RideRequestDTO rideRequestDTO) {
        return mapper.map(rideRequestDTO, Request.class);
    }
}
