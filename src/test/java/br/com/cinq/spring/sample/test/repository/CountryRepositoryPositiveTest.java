package br.com.cinq.spring.sample.test.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.cinq.spring.sample.Application;
import br.com.cinq.spring.sample.entity.Country;
import br.com.cinq.spring.sample.repository.CountryRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("unit")
public class CountryRepositoryPositiveTest {

	@Autowired
	private CountryRepository countryRepository;

	@Test
	public void testSaveCountry() {

		assertThat(countryRepository).isNotNull();

		Country country = Country.of("Argentina");
		countryRepository.save(country);

		assertThat(countryRepository.findOne(country.getId())).isEqualTo(country);

	}

	@Test
	public void testDeleteCountry() {

		assertThat(countryRepository).isNotNull();

		Country country = Country.of("Germany");
		countryRepository.save(country);
		int countryId = country.getId();

		assertThat(countryRepository.findOne(countryId)).isEqualTo(country);

		countryRepository.delete(countryId);

		assertThat(countryRepository.findOne(countryId)).isNull();

	}

}