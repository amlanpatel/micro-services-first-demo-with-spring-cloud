package com.practice.moviecatalogservice.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.practice.moviecatalogservice.models.CatalogItem;
import com.practice.moviecatalogservice.models.Movie;
import com.practice.moviecatalogservice.models.UserRating;

@RestController
@RequestMapping("/catalog")
public class MoviecatalogResource {

	@Autowired
	private RestTemplate restTemplate;

	/*
	 * @Autowired private WebClient.Builder webClientBuilder;
	 */

	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable String userId) {
		UserRating userRating = restTemplate.getForObject("http://RATING-DATA-SERVICE/ratingsdata/users/ " + userId,
				UserRating.class);

		return userRating.getRatings().stream().map(rating -> {// Calling Movie Info Service
			Movie movie = restTemplate.getForObject("http://MOVIE-INFO-SERVICE/movies/" + rating.getMovieId(),
					Movie.class);

			/*
			 * Movie movie = webClientBuilder.build() .get()
			 * .uri("http://localhost:8082/movies/" + rating.getMovieId()) .retrieve()
			 * .bodyToMono(Movie.class) .block();
			 */

			return new CatalogItem(movie.getName(), movie.getDesc(), rating.getRating());
		}).collect(Collectors.toList());
	}
}
