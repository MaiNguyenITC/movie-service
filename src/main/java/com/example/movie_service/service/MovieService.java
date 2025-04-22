package com.example.movie_service.service;

import com.example.movie_service.dto.MovieDTO;
import com.example.movie_service.model.Movie;

import java.util.List;

public interface MovieService {
    Movie createMovie(MovieDTO movieDTO);
    Movie updateMovie(String movieId, MovieDTO movieDTO);
    void deleteMovie(String movieId);
    Movie getMovie(String movieId);
    List<Movie> getMovies();
}
