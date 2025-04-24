package com.example.movie_service.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(
        "Movie"
)
@Data
public class Movie {
    @Id
    private String id;
    private String movieName;
    private String movieDuration;
    private String movieDescription;
    private String userName;
}
