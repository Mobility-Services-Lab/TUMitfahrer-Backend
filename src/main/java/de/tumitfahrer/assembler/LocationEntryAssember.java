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

import de.tumitfahrer.dtos.locationentry.LocationEntryDTO;
import de.tumitfahrer.dtos.locationentry.LocationEntryResponseDTO;
import de.tumitfahrer.entities.LocationAlias;
import de.tumitfahrer.entities.LocationEntry;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class LocationEntryAssember extends AbstractAssembler {

    public Collection<LocationEntryDTO> toDto(final Collection<LocationEntry> locationEntries) {
        final Collection<LocationEntryDTO> dtos = new ArrayDeque<>();
        locationEntries.forEach(le -> dtos.add(toDto(le)));
        return dtos;
    }

    public LocationEntryDTO toDto(final LocationEntry locationEntry) {
        final LocationEntryDTO dto = mapper.map(locationEntry, LocationEntryDTO.class);

        dto.setLatitude(locationEntry.getGeoLocation().getLatitude());
        dto.setLongitude(locationEntry.getGeoLocation().getLongitude());

        final Collection<String> aliases = locationEntry.getLocationAliases()
                .stream()
                .map(LocationAlias::getName)
                .collect(Collectors.toCollection(ArrayList::new));
        dto.setAliases(aliases);

        return dto;
    }

    public LocationEntryResponseDTO toResponse(final Collection<LocationEntry> locationEntries) {
        final Collection<LocationEntryDTO> locationEntryDTOs = toDto(locationEntries);
        return toResponseFromDto(locationEntryDTOs);
    }

    public LocationEntryResponseDTO toResponseFromDto(final Collection<LocationEntryDTO> locationEntryDTOs) {
        final LocationEntryResponseDTO dto = new LocationEntryResponseDTO();
        dto.setLocationEntries(locationEntryDTOs);
        return dto;
    }
}
