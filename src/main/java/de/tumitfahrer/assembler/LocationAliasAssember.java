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

import de.tumitfahrer.dtos.locationalias.LocationAliasDTO;
import de.tumitfahrer.dtos.locationalias.LocationAliasResponseDTO;
import de.tumitfahrer.entities.LocationAlias;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Collection;

@Service
public class LocationAliasAssember extends AbstractAssembler {

    public Collection<LocationAliasDTO> toDto(final Collection<LocationAlias> locationEntries) {
        final Collection<LocationAliasDTO> dtos = new ArrayDeque<>();
        locationEntries.forEach(le -> dtos.add(toDto(le)));
        return dtos;
    }

    public LocationAliasDTO toDto(final LocationAlias locationEntry) {
        final LocationAliasDTO dto = mapper.map(locationEntry, LocationAliasDTO.class);
        dto.setLatitiude(locationEntry.getLocationEntry().getGeoLocation().getLatitude());
        dto.setLongitude(locationEntry.getLocationEntry().getGeoLocation().getLongitude());
        return dto;
    }

    public LocationAliasResponseDTO toResponse(final Collection<LocationAlias> locationEntries) {
        final Collection<LocationAliasDTO> locationEntryDTOs = toDto(locationEntries);
        return toResponseFromDto(locationEntryDTOs);
    }

    public LocationAliasResponseDTO toResponseFromDto(final Collection<LocationAliasDTO> locationEntryDTOs) {
        final LocationAliasResponseDTO dto = new LocationAliasResponseDTO();
        dto.setLocationAliases(locationEntryDTOs);
        return dto;
    }
}
