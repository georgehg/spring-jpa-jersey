package br.com.cinq.spring.sample.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.cinq.spring.sample.entity.City;
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

		if (country == null) {

			return new ResponseEntity<List<City>>(cityRepository.findAll(), HttpStatus.OK);

		} else {

			return new ResponseEntity<List<City>>(
					cityRepository.findByCountryIn(countryRepository.findLikeName(country)), HttpStatus.OK);
		}

	}

}
