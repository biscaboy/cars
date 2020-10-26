package com.udacity.pricing;

import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

// @TODO add tests for error handling
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PricingServiceApplicationTests {

	@LocalServerPort
	int port;

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("Create a price (via POST).")
	public void testCreatePrice() throws Exception {

		String currency = "USD";
		double price = 12000.00;
		int vehicleId = 1;

		MockHttpServletRequestBuilder post = post("/prices")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content("{\"currency\":\"" + currency +
						"\", \"price\":\"" + price +
						"\", \"vehicle_id\":\"" + vehicleId +
						"\"}");

		String content = mockMvc.perform(post)
						.andExpect(status().isCreated())
						.andReturn()
						.getResponse()
						.getContentAsString();

		JSONObject obj = new JSONObject(content);
		Assertions.assertAll("Create Price",
			() -> Assertions.assertNotNull(obj.get("price_id")),
			() -> Assertions.assertEquals(currency, obj.get("currency")),
			() -> Assertions.assertEquals(price, obj.get("price")),
			() -> Assertions.assertEquals(vehicleId, obj.get("vehicle_id"))
		);
	}

	@Test
	@DisplayName("Delete a price.")
	public void testDeletePrice() throws Exception {
		MockHttpServletRequestBuilder createPost = post("/prices")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content("{\"currency\":\"USD\", \"price\":\"12000\", \"vehicle_id\":\"2\"}");

		MvcResult createResult = mockMvc.perform(createPost)
				.andExpect(status().isCreated())
				.andReturn();

		String content = createResult.getResponse().getContentAsString();

		JSONObject obj = new JSONObject(content);
		int priceId = (int) obj.get("price_id");

		mockMvc.perform(delete("/prices/" + priceId))
				.andExpect(status().isNoContent());

		mockMvc.perform(get("/prices/" + priceId))
				.andExpect(status().isNotFound());

	}

	static class UpdatePriceArgumentsProvider implements ArgumentsProvider {

		public UpdatePriceArgumentsProvider() {}

		@Override
		public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
			return Stream.of(
					Arguments.of("currency", "EUR", "10000.00", "USD", "300"),
					Arguments.of("vehicle_id", "310", "10000.00", "USD", "301"),
					Arguments.of("price", "22222.22", "10000.00", "USD", "302")
			);
		}
	}

	@ParameterizedTest
	@ArgumentsSource(UpdatePriceArgumentsProvider.class)
	@DisplayName("Update a price (via PATCH).")
	public void testUpdatePrice(String parameter, String newValue, String price, String currency, String vehicleId) throws Exception {

		MockHttpServletRequestBuilder createPost = post("/prices")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content("{\"currency\":\"" + currency + "\", \"price\":\"" + price +"\", \"vehicle_id\":\"" + vehicleId + "\"}");

		MvcResult createResult = mockMvc.perform(createPost)
				.andExpect(status().isCreated())
				.andReturn();

		String content = createResult.getResponse().getContentAsString();

		JSONObject obj = new JSONObject(content);
		int priceId = (int) obj.get("price_id");

		MockHttpServletRequestBuilder updatePost = patch("/prices/" + priceId)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content("{\"" + parameter + "\":\"" + newValue + "\"}");

		content = mockMvc.perform(updatePost)
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		obj = new JSONObject(content);
		Assertions.assertEquals(newValue, obj.get(parameter).toString());
	}

	@Test
	@DisplayName("Find a price using Vehicle ID")
	public void testFindPrice() throws Exception{

		int vehicleIdStart = 20;
		int vehicleIdToFind = 25;
		int vehicleIdEnd = 30;

		// insert new prices
		for (int i = vehicleIdStart; i < vehicleIdEnd; i++) {
			int price = ThreadLocalRandom.current().nextInt(15000, 100000);
			mockMvc.perform(post("/prices/")
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content("{\"currency\":\"USD\", \"price\":\"" + price + "\", \"vehicle_id\":\"" + i + "\"}" ))
					.andExpect(status().isCreated());
		}

		MockHttpServletRequestBuilder query = get("/prices/search/findByVehicleId?vehicle_id=" + vehicleIdToFind);

		String content = mockMvc.perform(query)
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		JSONObject obj = new JSONObject(content);
		int returnedVehicleId = (int) obj.get("vehicle_id");

		Assertions.assertEquals(vehicleIdToFind, returnedVehicleId);

	}

	@Test
	@DisplayName("Validate sending an invalid currency.")
	public void testCurrencyValidation() throws Exception {

		String currency = "ABC";  // bad currency code
		double price = 1200.00;
		int vehicleId = 4;

		MockHttpServletRequestBuilder post = post("/prices")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content("{\"currency\":\"" + currency +
						"\", \"price\":\"" + price +
						"\", \"vehicle_id\":\"" + vehicleId +
						"\"}");

		mockMvc.perform(post)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("errors.[0].code").value("currency.code.invalid"));
	}

	@Test
	@DisplayName("Validate sending a null request.")
	public void testNullRequestValidation() throws Exception {

		MockHttpServletRequestBuilder post = post("/prices")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content("{}");

		mockMvc.perform(post)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("errors.[0].code").value("currency.code.required"))
				.andExpect(jsonPath("errors.[1].code").value("price.required"))
				.andExpect(jsonPath("errors.[2].code").value("vehicle_id.required"));
	}

	@Test
	@DisplayName("Validate saving an existing vehicle id.")
	public void testExistingVehicleValidation() throws Exception {

		String currency = "EUR";  // bad currency code
		double price = 34000.00;
		int vehicleId = 5;

		MockHttpServletRequestBuilder post = post("/prices")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content("{\"currency\":\"" + currency +
						"\", \"price\":\"" + price +
						"\", \"vehicle_id\":\"" + vehicleId +
						"\"}");
		// Save the price
		mockMvc.perform(post)
				.andExpect(status().isCreated());
		// Try to save it again
		mockMvc.perform(post)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("errors.[0].code").value("vehicle_id.not.unique"));
	}

}
