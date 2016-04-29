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

package de.tumitfahrer.dtos.ride.response;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import de.tumitfahrer.dtos.http.ResponseDTO;

import java.util.List;

@ApiModel
public class RidesResponseDTO extends ResponseDTO {

    private List<RideDTO> rides;

    @ApiModelProperty(position = 1, value = "List of all rides.")
    public List<RideDTO> getRides() {
        return rides;
    }

    public void setRides(List<RideDTO> rides) {
        this.rides = rides;
    }
}
