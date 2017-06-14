package br.com.cinq.spring.sample.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = SampleApplication.class)
//@WebIntegrationTest(randomPort = true)
//@IntegrationTest("server.port=9000")
//@ActiveProfiles("unit")
public class EndpointTest {
    Logger logger = LoggerFactory.getLogger(EndpointTest.class);

    private final String localhost = "http://localhost:";

    @Value("${local.server.port}")
    private int port;

  //  private RestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void testGetCities() throws InterruptedException {
        String country = "France";

//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(this.localhost + this.port + "/rest/cities/")
//                .queryParam("country", country);

//        HttpEntity<?> entity = new HttpEntity<>(headers);

//        ResponseEntity<City[]> response = this.restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET,
//                entity, City[].class);

//        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

//        Thread.sleep(2000L);

//        City[] cities = response.getBody();

//        Assert.assertEquals(2, cities.length);

    }
}
