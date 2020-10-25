package com.udacity.pricing;

import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PricingServiceApplicationTests {

	@LocalServerPort
	int port;

	@Autowired
	private MockMvc mockMvc;

//	@Test
//	@DisplayName("The pricing microservice is running.")
//	public void testIsRunning() throws Exception {
//
//		mockMvc.perform(get("/"))
//				.andExpect(status().isOk())
//				.andExpect(content().contentType("application/hal+json;charset=UTF-8"))
//				.andExpect(jsonPath("_links.prices.href").value("http://localhost/prices"))
//				.andExpect(jsonPath("_links.profile.href").value("http://localhost/profile"));
//	}

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
						"\", \"vehicleId\":\"" + vehicleId +
						"\"}");

		String content = mockMvc.perform(post)
						.andExpect(status().isCreated())
						.andReturn()
						.getResponse()
						.getContentAsString();

		JSONObject obj = new JSONObject(content);
		Assertions.assertAll("Create Price",
			() -> Assertions.assertNotNull(obj.get("priceId")),
			() -> Assertions.assertEquals(currency, obj.get("currency")),
			() -> Assertions.assertEquals(price, obj.get("price")),
			() -> Assertions.assertEquals(vehicleId, obj.get("vehicleId"))
		);
	}

	@Test
	@DisplayName("Delete a price.")
	public void testDeletePrice() throws Exception {
		MockHttpServletRequestBuilder createPost = post("/prices")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content("{\"currency\":\"USD\", \"price\":\"12000\", \"vehicleId\":\"2\"}");

		MvcResult createResult = mockMvc.perform(createPost)
				.andExpect(status().isCreated())
				.andReturn();

		String content = createResult.getResponse().getContentAsString();

		JSONObject obj = new JSONObject(content);
		int priceId = (int) obj.get("priceId");

		mockMvc.perform(delete("/prices/" + priceId))
				.andExpect(status().isNoContent());

		mockMvc.perform(get("/prices/" + priceId))
				.andExpect(status().isNotFound());

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
					.content("{\"currency\":\"USD\", \"price\":\"" + price + "\", \"vehicleId\":\"" + i + "\"}" ))
					.andExpect(status().isCreated());
		}

		MockHttpServletRequestBuilder query = get("/prices/search/findByVehicleId?vehicleId=" + vehicleIdToFind);

		String content = mockMvc.perform(query)
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		JSONObject obj = new JSONObject(content);
		int returnedVehicleId = (int) obj.get("vehicleId");

		Assertions.assertEquals(vehicleIdToFind, returnedVehicleId);

	}

	@Test
	@DisplayName("Send an invalid currency.")
	public void testCurrencyValidation() throws Exception {

		String currency = "ABC";  // bad currency code
		double price = 1200.00;
		int vehicleId = 4;

		MockHttpServletRequestBuilder post = post("/prices")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content("{\"currency\":\"" + currency +
						"\", \"price\":\"" + price +
						"\", \"vehicleId\":\"" + vehicleId +
						"\"}");

		mockMvc.perform(post)
				.andExpect(status().isInternalServerError());
//				.andExpect(jsonPath("Currency code must be valid."),);

	}
}
