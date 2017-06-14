package br.com.cinq.spring.sample.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.com.cinq.spring.sample.entity.Country;

public interface CountryRepository extends CrudRepository<Country, Integer> {
	
	Optional<Country> findByName(String name);

	List<Country> findByNameContaining(String name);

}