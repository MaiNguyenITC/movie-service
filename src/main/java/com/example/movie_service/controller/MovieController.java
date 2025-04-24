package com.example.movie_service.controller;

import com.example.movie_service.dto.MovieDTO;
import com.example.movie_service.dto.MovieResponse;
import com.example.movie_service.model.Movie;
import com.example.movie_service.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/movie")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping()
    public ResponseEntity<Movie> createMovie(@RequestBody MovieDTO movieDTO){
        Movie movie = movieService.createMovie(movieDTO);
        return ResponseEntity.ok(movie);
    }

    @PutMapping("/{movieId}")
    public ResponseEntity<Movie> updateMovie(@PathVariable String movieId, @RequestBody MovieDTO movieDTO){
        Movie movie = movieService.updateMovie(movieId, movieDTO);
        return ResponseEntity.ok(movie);
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<Movie> getMovie(@PathVariable String movieId){
        Movie movie = movieService.getMovie(movieId);
        return ResponseEntity.ok(movie);
    }

    @GetMapping()
    public ResponseEntity<List<Movie>> getAllMovie(){
        List<Movie> movies = movieService.getMovies();
        return ResponseEntity.ok(movies);
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<Void> deleteMovie(@PathVariable String movieId){
        movieService.deleteMovie(movieId);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/infor/{movieId}")
    public ResponseEntity<MovieResponse> getMovieInformation(@PathVariable String movieId){
        MovieResponse movie = movieService.getMovieInformation(movieId);
        return ResponseEntity.ok(movie);
    }
}
