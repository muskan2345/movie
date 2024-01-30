package com.moviebookingapp.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.moviebookingapp.excpetion.CustomMessageException;

import com.moviebookingapp.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moviebookingapp.repository.MovieRepo;
import com.moviebookingapp.service.MovieService;

@Service
public class MovieServiceImpl implements MovieService {


	@Autowired
	MovieRepo movieRepo;

	@Override
	public Movie updateMovie(String id, Movie movie) {
		Optional<Movie> mov = movieRepo.findById(id);
		if(mov.isPresent()) {
			mov.get().setMovieName(movie.getMovieName());
			mov.get().setTheatreName(movie.getTheatreName());
			mov.get().setSeatAvailable(movie.getSeatAvailable());
			return movieRepo.save(mov.get());
		}
		throw new CustomMessageException("Movie not found for this id.");
	}

	@Override
	public Movie addMovie(Movie movie) {
		movie.setSeatAvailable(100);
		return movieRepo.save(movie);
	}

	@Override
	public List<Movie> getAllMovies() {
		List<Movie> allmovie = new ArrayList<>();
		movieRepo.findAll().forEach(e-> allmovie.add(e) );
		return allmovie;
	}


	@Override
	public Movie deleteMovie(String id) throws CustomMessageException {
		// TODO Auto-generated method stub
		Optional<Movie> mov = movieRepo.findById(id);
		if(mov.isPresent()) {
			movieRepo.deleteById(id);
			return mov.get();
		}
		throw new CustomMessageException("Movie not found for this id.");
	}


	@Override
	public Movie searchMovieById(String id) {
		return movieRepo.findById(id).get();
	}

	@Override
	public List<Movie> searchMovieByName(String name) {
		
		return movieRepo.findAllByMovieNameIgnoreCaseContaining(name);
	}


}
