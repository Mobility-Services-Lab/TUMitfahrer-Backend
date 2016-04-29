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

import de.tumitfahrer.dtos.ride.request.UserRideDTO;
import de.tumitfahrer.dtos.ride.request.UserRideRequestDTO;
import de.tumitfahrer.dtos.ride.response.*;
import de.tumitfahrer.dtos.user.response.RideOwnerDTO;
import de.tumitfahrer.entities.Ride;
import de.tumitfahrer.services.ConversationService;
import de.tumitfahrer.services.PassengerService;
import de.tumitfahrer.services.RequestService;
import de.tumitfahrer.services.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RideAssembler extends AbstractAssembler {

    @Autowired
    private RequestService requestService;
    @Autowired
    private PassengerService passengerService;
    @Autowired
    private RideService rideService;
    @Autowired
    private ConversationService conversationService;

    @Autowired
    private RequestAssembler requestAssembler;
    @Autowired
    private UserAssembler userAssembler;

    public List<RideDTO> toDto(List<Ride> rides) {
        List<RideDTO> ridesDTO = new ArrayList<>();
        RideDTO rideDTO;

        for (Ride ride : rides) {
            rideDTO = toDto(ride);
            ridesDTO.add(rideDTO);
        }

        return ridesDTO;
    }

    public Ride toEntity(UserRideRequestDTO userRideRequestDTO) {
        UserRideDTO userRideDTO = userRideRequestDTO.getRide();
        Ride ride = mapper.map(userRideDTO, Ride.class);
        return ride;
    }

    public RideDTO toDto(Ride ride) {
        RideDTO rideDTO = mapper.map(ride, RideDTO.class);
        rideDTO.setFreeSeatsCurrent(rideService.getCurrentFreeSeats(ride));

        rideDTO.setUserId(ride.getUser().getId());
        rideDTO.setUser(mapper.map(ride.getUser(), RideOwnerDTO.class));
        rideDTO.setRequests(requestAssembler.toDto(requestService.getRequestsByRideId(ride.getId())));
        rideDTO.setPassengers(userAssembler.toDto(passengerService.getByRideId(ride.getId()).getEntity()));

        rideDTO.setIsRideRequest(rideService.isRideRequest(ride.getId()));

        // set empty lists, features not implemented yet and take long time to get lists
        //rideDTO.setConversations(conversationAssembler.toDto(conversationService.getByRideId(ride.getId()).getEntity()));
        //rideDTO.setRatings(ratingAssembler.toDto(ratingService.getByRideId(ride.getId()).getEntity()));
        List emptyList = new ArrayList<>();
        rideDTO.setRatings(emptyList);
        rideDTO.setConversationId(conversationService.getByRideId(ride.getId()).getEntity().getId());
        return rideDTO;
    }

    public RideResponseDTO toResponse(RideDTO rideDTO) {
        RideResponseDTO rideResponseDTO = new RideResponseDTO();
        rideResponseDTO.setRide(rideDTO);
        return rideResponseDTO;
    }

    public RideDeletedResponseDTO toDeletedResponse(Ride ride) {
        RideDeletedResponseDTO rideDeletedResponseDTO = new RideDeletedResponseDTO();
        rideDeletedResponseDTO.setRide(mapper.map(ride, RideDeletedDTO.class));
        return rideDeletedResponseDTO;
    }

    public RideResponseDTO toResponse(Ride ride) {
        return toResponse(toDto(ride));
    }

    public RidesResponseDTO toResponseFromDto(List<RideDTO> ridesDTO) {
        RidesResponseDTO ridesResponseDTO = new RidesResponseDTO();
        ridesResponseDTO.setRides(ridesDTO);
        return ridesResponseDTO;
    }

    public RidesResponseDTO toResponse(List<Ride> rides) {
        return toResponseFromDto(toDto(rides));
    }
}