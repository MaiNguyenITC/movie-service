package com.example.movie_service.service;

import com.example.movie_service.dto.MovieDTO;
import com.example.movie_service.dto.MovieResponse;
import com.example.movie_service.dto.RatingDTO;
import com.example.movie_service.dto.UserDTO;
import com.example.movie_service.exception.BadRequestException;
import com.example.movie_service.exception.NotFoundException;
import com.example.movie_service.model.Movie;
import com.example.movie_service.repository.MovieRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpHeaders;
import java.util.List;
import java.util.function.Consumer;

@Service
public class MovieServiceImpl implements com.example.movie_service.service.MovieService {
    @Autowired
    private MovieRepository movieRepository;

    private final StreamBridge streamBridge;

    public MovieServiceImpl(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    private static final String User_Service_URL = "http://localhost:6063/api/auth/";
    private static final String Rating_Service_URL = "http://localhost:6062/api/rating/";

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

        streamBridge.send("createMovie-out-0", "Create Movie");

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


    @Override
    public MovieResponse getMovieInformation(String movieId) {
        RestTemplate restTemplate = new RestTemplate();
        Movie movie = movieRepository.findById(movieId).orElseThrow(
                () -> new NotFoundException("Movie is not found with id: " + movieId)
        );


        MovieResponse movieResponse = new MovieResponse();
        movieResponse.setId(movieId);
        movieResponse.setMovieName(movie.getMovieName());
        movieResponse.setMovieDuration(movie.getMovieDuration());
        movieResponse.setMovieDescription(movie.getMovieDescription());

        UserDTO userDTO = restTemplate.getForObject(User_Service_URL + "username/"
                + SecurityContextHolder.getContext().getAuthentication().getName(), UserDTO.class);

        movieResponse.setDisplayName(userDTO.getDisplayName() != null ? userDTO.getDisplayName() : "");
        movieResponse.setUserId(userDTO.getId() != null ? userDTO.getId() : "");


        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.set("X-User-Name", SecurityContextHolder.getContext().getAuthentication().getName());
        headers.set("X-User-Roles", SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());

        HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<RatingDTO>> response = restTemplate.exchange(
                Rating_Service_URL + "movie/" + movieId,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<RatingDTO>>() {}
        );

        movieResponse.setRatings(response.getBody());

        return movieResponse;
    }


    //This function need to update in call Rating service api
    @Bean
    public Consumer<String> createRating() {
        return movieString -> {
            System.out.println("📥 Nhận message từ RabbitMQ - create rating: " + movieString);

            Movie movie = movieRepository.findById(movieString).orElseThrow(
                    () -> new NotFoundException("Movie is not found with id: " + movieString)
            );

            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set("X-User-Name", SecurityContextHolder.getContext().getAuthentication().getName());
            headers.set("X-User-Roles", SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());

            HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<List<RatingDTO>> response = restTemplate.exchange(
                    Rating_Service_URL + "movie/" + movieString,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<List<RatingDTO>>() {}
            );


            int sum = 0;
            for (RatingDTO ratingDTO : response.getBody()){
                sum += ratingDTO.getRatingStar();
            }
            movie.setAverageStar(sum/response.getBody().size());
            movieRepository.save(movie);
        };
    }


}
