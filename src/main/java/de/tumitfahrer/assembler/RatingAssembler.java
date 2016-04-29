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

import de.tumitfahrer.dtos.rating.request.RatingRequestDTO;
import de.tumitfahrer.dtos.rating.response.RatingDTO;
import de.tumitfahrer.dtos.rating.response.RatingResponseDTO;
import de.tumitfahrer.dtos.rating.response.RatingsResponseDTO;
import de.tumitfahrer.entities.Rating;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RatingAssembler extends AbstractAssembler {

    public Rating toEntity(RatingRequestDTO ratingRequestDTO) {
        return mapper.map(ratingRequestDTO, Rating.class);
    }

    public RatingDTO toDto(Rating rating) {
        return mapper.map(rating, RatingDTO.class);
    }

    public List<RatingDTO> toDto(List<Rating> ratings) {
        List<RatingDTO> ratingsDTO = new ArrayList<>();
        for (Rating rating : ratings) {
            ratingsDTO.add(toDto(rating));
        }
        return ratingsDTO;
    }

    public RatingsResponseDTO toResponseFromDto(List<RatingDTO> ratingsDTO) {
        RatingsResponseDTO ratingsResponseDTO = new RatingsResponseDTO();
        ratingsResponseDTO.setRatings(ratingsDTO);
        return ratingsResponseDTO;
    }

    public RatingsResponseDTO toResponse(List<Rating> ratings) {
        return toResponseFromDto(toDto(ratings));
    }

    public RatingResponseDTO toResponse(RatingDTO ratingDTO) {
        RatingResponseDTO ratingResponseDTO = new RatingResponseDTO();
        ratingResponseDTO.setRating(ratingDTO);
        return ratingResponseDTO;
    }

    public RatingResponseDTO toResponse(Rating rating) {
        return toResponse(toDto(rating));
    }
}
