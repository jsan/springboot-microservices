package com.mello.microservices.organizationservice;

import com.adyen.Client;
import com.adyen.Config;
import com.adyen.httpclient.AdyenHttpClient;
import com.adyen.httpclient.HTTPClientException;
import com.adyen.model.checkout.*;
import com.adyen.service.checkout.PaymentsApi;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class OrganizationServiceApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void TestPaymentSuccess() throws Exception {
		Client client = createMockClientFromFile("mocks/checkout/paymentResponse.json");
		Amount amount = new Amount().currency("EUR").value(1000L);
		CardDetails cardDetails = new CardDetails();
		cardDetails.encryptedCardNumber("5136333333333335")
				.holderName("John Doe")
				.cvc("737")
				.encryptedExpiryMonth("08")
				.encryptedExpiryYear("2018");
		cardDetails.setType(CardDetails.TypeEnum.SCHEME);
		PaymentRequest paymentRequest = new PaymentRequest();
		paymentRequest.setAmount(amount);
		paymentRequest.setPaymentMethod(new CheckoutPaymentMethod(cardDetails));
		PaymentsApi checkout = new PaymentsApi(client);
		PaymentResponse paymentResponse = checkout.payments(paymentRequest);
		assertEquals("993617895204576J", paymentResponse.getPspReference());
		assertEquals(CheckoutRedirectAction.TypeEnum.REDIRECT, paymentResponse.getAction().getCheckoutRedirectAction().getType());
		assertEquals("https://checkoutshopper-test.adyen.com/checkoutshopper/threeDS/redirect?MD=M2R...", paymentResponse.getAction().getCheckoutRedirectAction().getUrl());
	}

	protected Client createMockClientFromFile(String fileName) {
		String response = getFileContents(fileName);

		return createMockClientFromResponse(response);
	}

	public String getFileContents(String fileName) {
		String result = "";

		ClassLoader classLoader = getClass().getClassLoader();
		try {
			byte[] buffer = new byte[1024];
			int length;
			InputStream fileStream = classLoader.getResourceAsStream(fileName);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			while ((length = fileStream.read(buffer)) != - 1) {
				outputStream.write(buffer, 0, length);
			}
			result = outputStream.toString(StandardCharsets.UTF_8.name());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	protected Client createMockClientFromResponse(String response) {
		AdyenHttpClient adyenHttpClient = mock(AdyenHttpClient.class);
		try {
			when(adyenHttpClient.request(anyString(), anyString(), any(Config.class), anyBoolean(), isNull())).thenReturn(response);
			when(adyenHttpClient.request(anyString(), any(), any(Config.class), anyBoolean(), isNull(), any())).thenReturn(response);
			when(adyenHttpClient.request(anyString(), any(), any(Config.class), anyBoolean(), isNull(), any(), any())).thenReturn(response);

		} catch (IOException | HTTPClientException e) {
			e.printStackTrace();
		}
		Client client = new Client();
		client.setHttpClient(adyenHttpClient);

		Config config = new Config();
		client.setConfig(config);

		return client;
	}
}
