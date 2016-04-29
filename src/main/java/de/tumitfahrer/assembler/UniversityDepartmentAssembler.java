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

import de.tumitfahrer.dtos.enums.UniversityDepartmentDTO;
import de.tumitfahrer.dtos.enums.UniversityDepartmentResponseDTO;
import de.tumitfahrer.enums.UniversityDepartment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UniversityDepartmentAssembler extends AbstractAssembler {

    public Collection<UniversityDepartmentDTO> toDto(UniversityDepartment[] departments) {
        Collection<UniversityDepartmentDTO> dtos = new ArrayList();

        for (UniversityDepartment dep : departments) {

            // dont expose the unknown state as part of the available departments
            if (dep == UniversityDepartment.UNKNOWN) {
                continue;
            }

            dtos.add(toDto(dep));
        }

        return dtos;
    }

    public UniversityDepartmentDTO toDto(UniversityDepartment department) {
        UniversityDepartmentDTO dto = new UniversityDepartmentDTO();
        dto.setName(department.name());
        dto.setFriendlyName(department.getFriendlyName());
        return dto;
    }

    public UniversityDepartmentResponseDTO toResponse(UniversityDepartment[] departments) {
        UniversityDepartmentResponseDTO response = new UniversityDepartmentResponseDTO();
        response.setDepartments(toDto(departments));
        return response;
    }
}
