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

package de.tumitfahrer.dtos.activity;

import de.tumitfahrer.dtos.request.response.RideRequestEntityDTO;
import de.tumitfahrer.dtos.ride.response.RideDTO;
import de.tumitfahrer.dtos.search.SearchDTO;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class ActivityDTO {

    private List<RideDTO> rides;
    private List<RideRequestEntityDTO> requests;
    private List<SearchDTO> rideSearches;

    @XmlElement(name = "requests")
    public List<RideRequestEntityDTO> getRequests() {
        return requests;
    }

    @XmlElement(name = "requests")
    public void setRequests(List<RideRequestEntityDTO> requests) {
        this.requests = requests;
    }

    @XmlElement(name = "rides")
    public List<RideDTO> getRides() {
        return rides;
    }

    @XmlElement(name = "rides")
    public void setRides(List<RideDTO> rides) {
        this.rides = rides;
    }

    @XmlElement(name = "ride_searches")
    public List<SearchDTO> getRideSearches() {
        return this.rideSearches;
    }

    @XmlElement(name = "ride_searches")
    public void setRideSearches(List<SearchDTO> rideSearches) {
        this.rideSearches = rideSearches;
    }
}
