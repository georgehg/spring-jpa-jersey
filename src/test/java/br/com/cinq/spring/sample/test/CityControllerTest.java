package br.com.cinq.spring.sample.test;

import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
	public void searchCitiesByUnknownCountry() throws Exception {

		//REST API assert
		this.mockMvc.perform(get("/rest/cities?country={country}", "Marte").contextPath("/rest"))
				.andExpect(status().isNoContent());
		
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
	
	
	@Test
	public void postCitiesJson() throws Exception {
		
		String cities = "[{\"city\":\"São Paulo\", \"country\":\"Brazil\"},"
						+ "{\"city\":\"Belo Horizonte\", \"country\":\"Brazil\"},"
						+ "{\"city\":\"Mar del Plata\", \"country\":\"Argentina\"},"
						+ "{\"city\":\"Vancouver\", \"country\":\"Canadá\"}]";
		
		//REST API assert
		this.mockMvc.perform(post("/rest/cities").contextPath("/rest")
					.contentType(contentType)
					.content(cities))
					.andExpect(status().isCreated());
		
		this.mockMvc.perform(get("/rest/cities?country={country}", "Canadá").contextPath("/rest"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(contentType))
		.andExpect(jsonPath("[0].name", is("Vancouver")))
		.andExpect(jsonPath("[0].country.name", is("Canadá")));
		
	}
}
