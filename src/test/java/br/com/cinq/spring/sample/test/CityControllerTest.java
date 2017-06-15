package br.com.cinq.spring.sample.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import br.com.cinq.spring.sample.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class CityControllerTest {
	
	@Rule
	public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets/rest/v1");
	
	private MockMvc mockMvc;
	
	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			  MediaType.APPLICATION_JSON.getSubtype(),
			  Charset.forName("utf8"));
	
	@Autowired
    private WebApplicationContext context;

	private RestDocumentationResultHandler document;

	@Before
	public void setup() throws Exception {
		
		// Prepare Spring Restdocs with default directory and preprocessors
		this.document = document("{class-name}/{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
						.apply(documentationConfiguration(this.restDocumentation))
						.alwaysDo(MockMvcResultHandlers.print())
						.build();		
	}
	
	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {

		this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
				.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().orElse(null);

		assertNotNull("the JSON message converter must not be null", this.mappingJackson2HttpMessageConverter);
		
	}
	
	protected String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}
	
	@Test
	public void contextLoads() throws Exception {}
	
	
	@Test
	public void getCitiesList() throws Exception {

		//REST API assert
		this.mockMvc.perform(get("/rest/cities").contextPath("/rest"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				
				// Request specific restdocs configuration
				.andDo(this.document.document(responseFields(
												fieldWithPath("[0].id").description("City ID")
												,fieldWithPath("[0].name").description("City Name")
												,fieldWithPath("[0].country.id").description("City´s Country Name")
												,fieldWithPath("[0].country.name").description("City´s Country Name"))));
	}
	
	
	@Test
	public void searchCitiesByCountry() throws Exception {

		//REST API assert
		this.mockMvc.perform(get("/rest/cities?country={country}", "Uni").contextPath("/rest"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("[0].country.name", is("United States")));
		
	}
	
	
	@Test
	public void searchCitiesByCountryList() throws Exception {

		//REST API assert
		this.mockMvc.perform(get("/rest/cities?country={country}", "ra").contextPath("/rest"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("[0].country.name", is("Brazil")))
				.andExpect(jsonPath("[5].country.name", is("France")));
	
	}
	
}
