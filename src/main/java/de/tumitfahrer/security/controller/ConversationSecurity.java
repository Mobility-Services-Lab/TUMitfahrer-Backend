package de.tumitfahrer.security.controller;


import de.tumitfahrer.entities.Passenger;
import de.tumitfahrer.entities.Ride;
import de.tumitfahrer.services.PassengerService;
import de.tumitfahrer.services.RideService;
import de.tumitfahrer.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ConversationSecurity extends AbstractSecurity {

    @Autowired
    private PassengerService passengerService;
    @Autowired
    private RideService rideService;

    public void getConversations() {
        if (!deletedRide()) {
            requestContext.abortWith(ResponseUtils.notFound("Cannot send message. Ride deleted!"));
        }
        if (isAdmin() || userHasAccessOnRideId()) {
            return;
        }
        requestContext.abortWith(ResponseUtils.unauthorized());
    }

    public void getConversation() {
        if (!deletedRide()) {
            requestContext.abortWith(ResponseUtils.notFound("Cannot send message. Ride deleted!"));
        }
        if (isAdmin() || userHasAccessOnRideId()) {
            return;
        }
        requestContext.abortWith(ResponseUtils.unauthorized());
    }

    private boolean deletedRide() {
        Integer rideId;
        try {
            rideId = Integer.parseInt(pathParams.getFirst("rideId"));
        } catch (ClassCastException e) {
            return false;
        }

        Ride ride = rideService.load(rideId);
        return ride != null;
    }

    private boolean userHasAccessOnRideId() {
        Integer rideId;
        try {
            rideId = Integer.parseInt(pathParams.getFirst("rideId"));
        } catch (ClassCastException e) {
            return false;
        }

        Passenger passenger = passengerService.loadByRideAndUserId(rideId, currentUser.getId());

        return passenger != null;


    }
}
