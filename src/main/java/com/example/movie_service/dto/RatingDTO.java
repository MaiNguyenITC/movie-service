package com.example.movie_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Nếu dùng Lombok
@NoArgsConstructor
@AllArgsConstructor
public class RatingDTO {
    private String id;
    private int ratingStar;
    private String ratingContent;
}
