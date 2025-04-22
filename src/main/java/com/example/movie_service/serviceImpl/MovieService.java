package com.example.movie_service.serviceImpl;

import com.example.movie_service.dto.MovieDTO;
import com.example.movie_service.exception.BadRequestException;
import com.example.movie_service.exception.NotFoundException;
import com.example.movie_service.model.Movie;
import com.example.movie_service.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService implements com.example.movie_service.service.MovieService {
    @Autowired
    private MovieRepository movieRepository;
    @Override
    public Movie createMovie(MovieDTO movieDTO) {
        Movie movie = movieRepository.findBymovieName(movieDTO.getMovieName());
        if(movie != null){
            throw new BadRequestException("Movie is already exist with name: " + movieDTO.getMovieName());
        }
        Movie createMovie = new Movie();
        createMovie.setMovieDuration(movieDTO.getMovieDuration());
        createMovie.setMovieName(movieDTO.getMovieName());
        createMovie.setMovieDescription(movieDTO.getMovieDescription());

        return movieRepository.save(createMovie);
    }

    @Override
    public Movie updateMovie(String movieId, MovieDTO movieDTO) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(
                () -> new NotFoundException("Movie is not found with id: " + movieId)
        );
        Movie existedMovie = movieRepository.findBymovieName(movieDTO.getMovieName());
        if(existedMovie != null && !movie.getMovieName().equals(movieDTO.getMovieName())){
            throw new BadRequestException("Movie is already exist with name: " + movieDTO.getMovieName());
        }

        movie.setMovieDuration(movieDTO.getMovieDuration());
        movie.setMovieName(movieDTO.getMovieName());
        movie.setMovieDescription(movieDTO.getMovieDescription());
        return movieRepository.save(movie);
    }

    @Override
    public void deleteMovie(String movieId) {
        movieRepository.deleteById(movieId);
    }

    @Override
    public Movie getMovie(String movieId) {
        return movieRepository.findById(movieId).orElseThrow(
                () -> new NotFoundException("Movie is not found with id: " + movieId)
        );
    }

    @Override
    public List<Movie> getMovies() {
        return movieRepository.findAll();
    }
}
