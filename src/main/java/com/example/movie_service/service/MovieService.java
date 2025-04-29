package com.example.movie_service.service;

import com.example.movie_service.dto.MovieDTO;
import com.example.movie_service.dto.MovieResponse;
import com.example.movie_service.model.Movie;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MovieService {
    Movie createMovie(MovieDTO movieDTO);
    Movie updateMovie(String movieId, MovieDTO movieDTO);
    void deleteMovie(String movieId);
    Movie getMovie(String movieId);
    List<Movie> getMovies();
    MovieResponse getMovieInformation(String movieId);
    Page<Movie> pagingMovie(int page, int size);
}
