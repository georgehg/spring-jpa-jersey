package br.com.cinq.spring.sample.test.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.cinq.spring.sample.Application;
import br.com.cinq.spring.sample.entity.City;
import br.com.cinq.spring.sample.repository.CityRepository;
import br.com.cinq.spring.sample.repository.CountryRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("unit")
public class CityRepositoryPositiveTest {

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private CountryRepository countryRepository;

	@Test
	public void testSaveCity() {

		assertThat(cityRepository).isNotNull();

		City city = City.of("São Paulo", countryRepository.findByName("Brazil").get());
		cityRepository.save(city);

		assertThat(cityRepository.findOne(city.getId())).isEqualTo(city);

	}

	@Test
	public void testDeleteCity() {

		assertThat(cityRepository).isNotNull();

		City city = City.of("Washington", countryRepository.findByName("United States").get());
		cityRepository.save(city);
		int cityId = city.getId();

		assertThat(cityRepository.findOne(cityId)).isEqualTo(city);

		cityRepository.delete(cityId);

		assertThat(cityRepository.findOne(cityId)).isNull();

	}

}
