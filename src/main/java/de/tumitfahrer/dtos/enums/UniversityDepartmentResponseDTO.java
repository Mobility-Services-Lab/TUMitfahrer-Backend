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

package de.tumitfahrer.dtos.enums;

import de.tumitfahrer.dtos.http.ResponseDTO;

import java.util.Collection;

public class UniversityDepartmentResponseDTO extends ResponseDTO {

    private Collection<UniversityDepartmentDTO> departments;

    public Collection<UniversityDepartmentDTO> getDepartments() {
        return departments;
    }

    public void setDepartments(Collection<UniversityDepartmentDTO> departments) {
        this.departments = departments;
    }
}
