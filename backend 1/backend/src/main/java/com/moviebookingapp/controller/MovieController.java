package com.moviebookingapp.controller;


import com.moviebookingapp.model.Movie;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.moviebookingapp.service.MovieService;

import java.util.List;

@RestController
@CrossOrigin
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/v1.0/moviebooking/movie")
public class MovieController {

	@Autowired
	private MovieService movieService;

	@PreAuthorize("hasRole('User') || hasRole('Admin')")
	@GetMapping("/all")
	public ResponseEntity<List<Movie>> getAllMovie() {
		return new ResponseEntity<List<Movie>>(movieService.getAllMovies(), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('Admin')")
	@PutMapping("/update/{id}")
	public ResponseEntity<Movie> updateMovie(@PathVariable String id, @RequestBody Movie movie) {
		return new ResponseEntity<Movie>(movieService.updateMovie(id,movie), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('Admin')")
	@PostMapping("/add")
	public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
		return new ResponseEntity<Movie>(movieService.addMovie(movie), HttpStatus.OK);
	}
//
	@PreAuthorize("hasRole('Admin')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Movie> deleteMovie(@PathVariable String id) {
		return new ResponseEntity<Movie>(movieService.deleteMovie(id), HttpStatus.OK);
	}
	
	
	@PreAuthorize("hasRole('User') || hasRole('Admin')")
	@GetMapping("/search/{id}")
	public ResponseEntity<Movie> searchMovieBYId(@PathVariable String id) {
		return new ResponseEntity<Movie>(movieService.searchMovieById(id), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('User') || hasRole('Admin')")
	@GetMapping("/searchMovieName/{name}")
	public ResponseEntity<List<Movie>> searchMovieByName(@PathVariable String name) {
		return new ResponseEntity<List<Movie>>(movieService.searchMovieByName(name), HttpStatus.OK);
	}

}

