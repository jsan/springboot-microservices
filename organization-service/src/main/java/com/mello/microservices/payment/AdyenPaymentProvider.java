package com.mello.microservices.payment;

import com.adyen.Client;
import com.adyen.enums.Environment;
import com.adyen.model.RequestOptions;
import com.adyen.model.checkout.Amount;
import com.adyen.model.checkout.*;
import com.adyen.service.checkout.PaymentsApi;

import com.adyen.service.exception.ApiException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AdyenPaymentProvider
{
    public String sendPaymentRequest() throws IOException, ApiException
    {
        // TODO: Get card data from event
        //       Set return status from api response (Authorized, Unauthorized ...)
        try {
            // Setup the client and service.
            Client client = new Client("AQEphmfxKojLYhdCw0m/n3Q5qf3Ve4pDHpxEXWT2eh72dbBETF7sTVr5Z/sQwV1bDb7kfNy1WIxIIkxgBw==-8Pnle/sFbNdUKZZjLFOskX/LOx3ntx8LbyJNXF5dhBU=-%6RuXHN4&N:sxvZN", Environment.TEST);
            PaymentsApi paymentsApi = new PaymentsApi(client);

            PaymentRequest paymentRequest = new PaymentRequest();
            paymentRequest.setMerchantAccount("SansoneECOM");
            CardDetails cardDetails = new CardDetails();
            cardDetails.encryptedCardNumber("test_4111111111111111")
                    .encryptedSecurityCode("test_737")
                    .encryptedExpiryMonth("test_03")
                    .encryptedExpiryYear("test_2030");
            paymentRequest.setPaymentMethod(new CheckoutPaymentMethod(cardDetails));
            Amount amount = new Amount().currency("EUR").value(1000L);
            paymentRequest.setAmount(amount);
            paymentRequest.setReference("My first Adyen test payment with an API library/SDK");
            paymentRequest.setReturnUrl("https://localhost:3000/checkout?shopperOrder=12xy..");

            // Add your idempotency key.
            RequestOptions requestOptions = new RequestOptions();
            // requestOptions.setIdempotencyKey("YOUR_IDEMPOTENCY_KEY");

            // Make a request to the /payments endpoint.
            PaymentResponse paymentResponse = paymentsApi.payments(paymentRequest, requestOptions);
            System.out.println("Pago!");
            return "Completed";

        } catch (ApiException e) {
            System.out.println("Error Payment Api: " + e);
            return "Cancelled";
        }

    }

}
