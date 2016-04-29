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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractResponseAssembler<Entity, DTO extends Serializable, ResponseDtoClass, ResponsesDtoClass> extends AbstractAssembler {

    protected Class<Entity> entityClass;
    protected Class<DTO> dtoClass;
    protected Class<ResponseDtoClass> responseDtoClass;
    protected Class<ResponsesDtoClass> responsesDtoClassClass;

    AbstractResponseAssembler(Class<Entity> entityClass, Class<DTO> dtoClass, Class<ResponseDtoClass> responseDtoClass, Class<ResponsesDtoClass> responsesDtoClassClass) {
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
        this.responseDtoClass = responseDtoClass;
        this.responsesDtoClassClass = responsesDtoClassClass;
    }

    public DTO toDto(Entity entity) {
        return mapper.map(entity, dtoClass);
    }

    public Entity toEntity(DTO dto) {
        return mapper.map(dto, entityClass);
    }

    public List<DTO> toDto(List<Entity> entityList) {
        List<DTO> dtoList = new ArrayList<>();
        for (Entity entity : entityList) {
            dtoList.add(toDto(entity));
        }
        return dtoList;
    }

    public ResponseDtoClass toResponse(Entity entity) {
        return toResponse(toDto(entity));
    }

    public abstract ResponseDtoClass toResponse(DTO dto);

    public abstract ResponsesDtoClass toResponseFromDto(List<DTO> dtoList);

    public ResponsesDtoClass toResponse(List<Entity> entityList) {
        return toResponseFromDto(toDto(entityList));
    }
}
