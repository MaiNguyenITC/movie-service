package com.example.movie_service.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MovieResponse {
    private String id;
    private String movieName;
    private String movieDuration;
    private String movieDescription;
    private String userId;
    private String displayName;
    private List<RatingDTO> ratings = new ArrayList<>();

}
