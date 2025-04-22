package com.example.movie_service;

import com.example.movie_service.dto.MovieDTO;
import com.example.movie_service.exception.BadRequestException;
import com.example.movie_service.exception.NotFoundException;
import com.example.movie_service.model.Movie;
import com.example.movie_service.repository.MovieRepository;
import com.example.movie_service.serviceImpl.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MovieServiceTest {
    @InjectMocks
    private MovieService movieService;
    @Mock
    private MovieRepository movieRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateMovie(){
        MovieDTO movieDTO = new MovieDTO();

        movieDTO.setMovieDescription("test movie description");
        movieDTO.setMovieName("test movie name");
        movieDTO.setMovieDuration("test movie duration");

        Movie movie = new Movie();
        movie.setMovieDescription("test movie description");
        movie.setMovieName("test movie name");
        movie.setMovieDuration("test movie duration");
        movie.setId("test id");

        Mockito.when(movieRepository.save(Mockito.any(Movie.class))).thenReturn(movie);

        Movie savedMovie = movieService.createMovie(movieDTO);

        assertNotNull(savedMovie);
        assertEquals(movieDTO.getMovieName(), savedMovie.getMovieName());
        assertEquals(movieDTO.getMovieDescription(), savedMovie.getMovieDescription());
        assertEquals(movieDTO.getMovieDuration(), savedMovie.getMovieDuration());

        Mockito.verify(movieRepository, Mockito.times(1)).save(Mockito.any(Movie.class));
    }

    @Test
    void testUpdateMovie(){
        MovieDTO movieDTO = new MovieDTO();
        String movieId = "testId";
        movieDTO.setMovieDescription("update movie description");
        movieDTO.setMovieName("update movie name");
        movieDTO.setMovieDuration("update movie duration");

        Movie movie = new Movie();
        movie.setMovieDescription("test movie description");
        movie.setMovieName("test movie name");
        movie.setMovieDuration("test movie duration");
        movie.setId(movieId);

        Mockito.when(movieRepository.save(Mockito.any(Movie.class))).thenReturn(movie);
        Mockito.when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        Movie updatedMovie = movieService.updateMovie(movieId, movieDTO);

        assertNotNull(updatedMovie);
        assertEquals(movieDTO.getMovieName(), updatedMovie.getMovieName());
        assertEquals(movieDTO.getMovieDescription(), updatedMovie.getMovieDescription());
        assertEquals(movieDTO.getMovieDuration(), updatedMovie.getMovieDuration());

        Mockito.verify(movieRepository, Mockito.times(1)).save(Mockito.any(Movie.class));
    }

    @Test
    void testDeleteMovie(){
        String movieId = "testId";

        Movie movie = new Movie();
        movie.setMovieDescription("test movie description");
        movie.setMovieName("test movie name");
        movie.setMovieDuration("test movie duration");
        movie.setId(movieId);


        Mockito.doNothing().when(movieRepository).deleteById(movieId);

        movieService.deleteMovie(movieId);


        Mockito.verify(movieRepository, Mockito.times(1)).deleteById(movieId);
    }

    @Test
    void testGetMovie(){

        String movieId = "testId";
        Movie movie = new Movie();
        movie.setMovieDescription("test movie description");
        movie.setMovieName("test movie name");
        movie.setMovieDuration("test movie duration");
        movie.setId(movieId);

        Mockito.when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        Movie getMovie = movieService.getMovie(movieId);

        assertNotNull(getMovie);
        assertEquals(movie.getMovieName(), getMovie.getMovieName());
        assertEquals(movie.getMovieDescription(), getMovie.getMovieDescription());
        assertEquals(movie.getMovieDuration(), getMovie.getMovieDuration());

        Mockito.verify(movieRepository, Mockito.times(1)).findById(movieId);
    }

    @Test
    void testGetAllMovie(){

        List<Movie> movies = new ArrayList<>();
        String movieId1 = "testId1";
        Movie movie1 = new Movie();
        movie1.setMovieDescription("test movie description");
        movie1.setMovieName("test movie name");
        movie1.setMovieDuration("test movie duration");
        movie1.setId(movieId1);

        String movieId2 = "testId2";
        Movie movie2 = new Movie();
        movie2.setMovieDescription("test movie description");
        movie2.setMovieName("test movie name");
        movie2.setMovieDuration("test movie duration");
        movie2.setId(movieId2);

        movies.add(movie1);
        movies.add(movie2);

        Mockito.when(movieRepository.findAll()).thenReturn(movies);

        List<Movie> getMovies = movieService.getMovies();

        assertEquals(getMovies.size(), movies.size());
        assertEquals(getMovies.get(0).getId(), movies.get(0).getId());
        assertEquals(getMovies.get(1).getId(), movies.get(1).getId());

        Mockito.verify(movieRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testCreateMovie_MovieIsExisted(){
        MovieDTO movieDTO = new MovieDTO();

        movieDTO.setMovieDescription("test movie description");
        movieDTO.setMovieName("test movie name");
        movieDTO.setMovieDuration("test movie duration");

        Movie existedMovie = new Movie();
        existedMovie.setMovieDescription("test movie description");
        existedMovie.setMovieName("test movie name");
        existedMovie.setMovieDuration("test movie duration");
        existedMovie.setId("test id");

        Mockito.when(movieRepository.findBymovieName("test movie name")).thenReturn(existedMovie);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> {
            movieService.createMovie(movieDTO);
        });

        assertTrue(ex.getMessage().contains("Movie is already exist with name: " + movieDTO.getMovieName()));

    }

    @Test
    void testUpdateMovie_MovieNotFound(){
        MovieDTO movieDTO = new MovieDTO();

        movieDTO.setMovieDescription("test movie description");
        movieDTO.setMovieName("test movie name");
        movieDTO.setMovieDuration("test movie duration");

        String movieId = "test id";

        Movie existedMovie = new Movie();
        existedMovie.setMovieDescription("test movie description");
        existedMovie.setMovieName("test movie name");
        existedMovie.setMovieDuration("test movie duration");
        existedMovie.setId(movieId);

        Mockito.when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            movieService.updateMovie(movieId, movieDTO);
        });

        assertTrue(ex.getMessage().contains("Movie is not found with id: " + movieId));

    }

    @Test
    void testUpdateMovie_MovieIsExisted(){
        MovieDTO movieDTO = new MovieDTO();

        movieDTO.setMovieDescription("test movie description");
        movieDTO.setMovieName("test movie name 2");
        movieDTO.setMovieDuration("test movie duration");

        String movieId = "test id";

        Movie movie = new Movie();
        movie.setMovieDescription("test movie description");
        movie.setMovieName("test movie name");
        movie.setMovieDuration("test movie duration");
        movie.setId(movieId);

        Movie existedMovie = new Movie();
        existedMovie.setMovieDescription("test movie description");
        existedMovie.setMovieName("test movie name 2");
        existedMovie.setMovieDuration("test movie duration");
        existedMovie.setId(movieId);

        Mockito.when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        Mockito.when(movieRepository.findBymovieName("test movie name 2")).thenReturn(existedMovie);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> {
            movieService.updateMovie(movieId, movieDTO);
        });

        assertTrue(ex.getMessage().contains("Movie is already exist with name: " + movieDTO.getMovieName()));
    }

    @Test
    void testGetMovie_MovieNotFound(){

        String movieId = "test id";

        Movie existedMovie = new Movie();
        existedMovie.setMovieDescription("test movie description");
        existedMovie.setMovieName("test movie name");
        existedMovie.setMovieDuration("test movie duration");
        existedMovie.setId(movieId);

        Mockito.when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            movieService.getMovie(movieId);
        });

        assertTrue(ex.getMessage().contains("Movie is not found with id: " + movieId));

    }
}
