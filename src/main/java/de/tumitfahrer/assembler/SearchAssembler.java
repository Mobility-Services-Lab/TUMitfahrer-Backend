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

import de.tumitfahrer.dtos.search.SearchDTO;
import de.tumitfahrer.dtos.search.SearchRequestDTO;
import de.tumitfahrer.entities.Search;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchAssembler extends AbstractAssembler {

    public Search toEntity(SearchRequestDTO searchRequestDTO) {
        return mapper.map(searchRequestDTO, Search.class);
    }

    public List<SearchDTO> toDto(List<Search> searches) {
        List<SearchDTO> searchesDTO = new ArrayList<>();
        for (Search search : searches) {
            SearchDTO sDTO = mapper.map(search, SearchDTO.class);
            searchesDTO.add(sDTO);
        }
        return searchesDTO;
    }
}