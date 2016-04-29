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

import de.tumitfahrer.dtos.user.response.UserDTO;
import de.tumitfahrer.dtos.user.response.UserDeletedDTO;
import de.tumitfahrer.dtos.user.response.UserDeletedResponseDTO;
import de.tumitfahrer.dtos.user.response.UserResponseDTO;
import de.tumitfahrer.entities.User;
import de.tumitfahrer.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserAssembler extends AbstractAssembler {

    @Autowired
    private UserService userService;

    public List<UserDTO> toDto(List<User> users) {
        List<UserDTO> usersDTO = new ArrayList();

        for (User user : users) {
            usersDTO.add(toDto(user));
        }

        return usersDTO;
    }

    public UserDTO toDto(User user) {
        UserDTO userDTO = mapper.map(user, UserDTO.class);
        userDTO.setRidesAsDriver(userService.getRidesAsDriverCount(user.getId()));
        userDTO.setRidesAsPassenger(userService.getRidesAsPassengerCount(user.getId()));
        return userDTO;
    }

    public UserResponseDTO toResponse(UserDTO userDTO) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUser(userDTO);
        return userResponseDTO;
    }

    public UserResponseDTO toResponse(User user) {
        return toResponse(toDto(user));
    }

    public UserDeletedResponseDTO toDeletedResponse(User user) {
        UserDeletedResponseDTO userDeletedResponseDTO = new UserDeletedResponseDTO();
        userDeletedResponseDTO.setUser(mapper.map(user, UserDeletedDTO.class));
        return userDeletedResponseDTO;
    }
}