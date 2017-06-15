package br.com.cinq.spring.sample.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.cinq.spring.sample.dto.LoadCity;
import br.com.cinq.spring.sample.entity.City;
import br.com.cinq.spring.sample.entity.Country;
import br.com.cinq.spring.sample.repository.CityRepository;
import br.com.cinq.spring.sample.repository.CountryRepository;

@RestController
@RequestMapping("/cities")
public class CityController {

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private CountryRepository countryRepository;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCitiesList(@RequestParam(value = "country", required = false) String country) {

		return Optional.ofNullable(country)
				.map((param) -> cityRepository.findByCountryIn(countryRepository.findLikeName(param)))
				.map((list) -> list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok().body(list))
				.orElse(ResponseEntity.ok().body(cityRepository.findAll()));

	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> loadCitiesJson(@RequestBody List<LoadCity> input) {
		
		input.stream().forEach(loadCity);		
		return ResponseEntity.created(URI.create("/rest/cities")).build();
		
	}
	
	public Consumer<LoadCity> loadCity = (cityDto) -> {
		
		Country country;
		Optional<Country> optCountry = countryRepository.findByName(cityDto.getCountry());
		
		if (optCountry.isPresent()) {
			country = optCountry.get();
		} else {
			country = countryRepository.save(Country.of(cityDto.getCountry()));
		}
		
		cityRepository.save(City.of(cityDto.getCity(),country));		
				
	};
	
	


}
