package br.com.cinq.spring.sample.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.com.cinq.spring.sample.entity.City;
import br.com.cinq.spring.sample.entity.Country;

public interface CityRepository extends CrudRepository<City, Integer> {

	List<City> findByCountry(Country country);
	
	List<City> findByCountry(List<Country> country);

}
