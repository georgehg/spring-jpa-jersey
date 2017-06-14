package br.com.cinq.spring.sample.test.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.cinq.spring.sample.SampleApplication;
import br.com.cinq.spring.sample.entity.City;
import br.com.cinq.spring.sample.entity.Country;
import br.com.cinq.spring.sample.repository.CityRepository;
import br.com.cinq.spring.sample.repository.CountryRepository;

/**
 * Eye candy: implements a sample in using JpaRespositories
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SampleApplication.class)
@ActiveProfiles("unit")
public class CityRepositoryTest {

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

	@Test
    public void testGelAllCities() {
		
    	assertThat(cityRepository).isNotNull();

    	int count = (int) cityRepository.count();
        assertThat(count).isGreaterThan(0);

        List<City> cities = new ArrayList<City>();
        cityRepository.findAll().forEach(cities::add);

        assertThat(cities.size()).isEqualTo(count);
	}
    
    @Test
    public void testQueryCity() {

    	assertThat(cityRepository).isNotNull();

        Country country = countryRepository.findOne(3);// Should be France
        List<City> cities = cityRepository.findByCountry(country);

        assertThat(cities.size()).isEqualTo(2);
    }
}
