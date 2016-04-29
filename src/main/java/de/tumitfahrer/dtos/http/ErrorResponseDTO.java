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

package de.tumitfahrer.dtos.http;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ApiModel
public class ErrorResponseDTO<T> extends ResponseDTO {

    @ApiModelProperty
    private List<String> errors = new ArrayList<>();

    public ErrorResponseDTO() {
        this.setStatus(HttpStatus.BAD_REQUEST);
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public void add(String error) {
        errors.add(error);
    }

    public void addConstraintViolations(Set<ConstraintViolation<T>> constraintViolationSet) {
        for (ConstraintViolation<?> violation : constraintViolationSet) {
            add(violation.getMessage());
        }
    }

    public void reset() {
        errors = new ArrayList<>();
    }
}
