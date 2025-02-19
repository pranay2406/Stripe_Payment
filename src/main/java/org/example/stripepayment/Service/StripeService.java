package org.example.stripepayment.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Review;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.example.stripepayment.dto.ProductRequest;
import org.example.stripepayment.dto.StripeResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    @Value("${stripe.secret.key}")
     private String secretKey;
    //stripe -API
    // -> productName, amount,quantity, currency
    //-> return sessionId and url
    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }
    public StripeResponse getStripe(ProductRequest productRequest) throws StripeException {
        Stripe.apiKey=secretKey;

        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(productRequest.getName() == null ? "Product Payment" : productRequest.getName())
                        .build();

        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency(productRequest.getCurrency()==null?"eur" : productRequest.getCurrency())
                        .setUnitAmount(productRequest.getAmount() * 100)
                        .setProductData(productData)
                        .build();

        SessionCreateParams.LineItem lineItem =
                SessionCreateParams.LineItem.builder()
                        .setQuantity(productRequest.getQuantity() == null || productRequest.getQuantity() <= 0 ? 1 : productRequest.getQuantity())
                        .setPriceData(priceData)
                        .build();

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://localhost:8080/api/payment/success")
                        .setCancelUrl("http://localhost:8080/api/payment/cancel")
                        .addLineItem(lineItem)
                        .build();

        Session session = Session.create(params);
        String url= session.getUrl();
        System.out.println("URL-------------------"+url);
        try {
                session = Session.create(params);
               return StripeResponse.builder()
                       .status("SUCCESS")
                       .message("Payment session created successfully")
                       .sessionId(session.getId())
                       .sessionUrl(url)
                       .build();
           } catch (StripeException e) {
            return StripeResponse.builder()
                    .status("Failed")
                    .message("Payment session Failed")
                    .build();
           }

    }
}






