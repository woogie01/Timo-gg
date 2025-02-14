package com.tools.seoultech.timoproject.rating;

import com.tools.seoultech.timoproject.rating.dto.RatingRequest;
import com.tools.seoultech.timoproject.rating.dto.RatingResponse;
import com.tools.seoultech.timoproject.rating.dto.RatingTotalResponse;

import java.util.List;

public interface RatingService {

        RatingResponse saveRating(RatingRequest rating);

        void deleteRating(Long id);

        RatingTotalResponse getRatings(Long id);

}
