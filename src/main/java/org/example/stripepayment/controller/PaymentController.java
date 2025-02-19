package org.example.stripepayment.controller;

import com.stripe.exception.StripeException;
import org.example.stripepayment.Service.StripeService;
import org.example.stripepayment.dto.ProductRequest;
import org.example.stripepayment.dto.StripeResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/plantiak")
public class PaymentController {

      private StripeService stripeService;

      public PaymentController(StripeService stripeService) {
          this.stripeService = stripeService;
      }

      @PostMapping("/command")
      public ResponseEntity<StripeResponse> checkoutPayment(@RequestBody ProductRequest productRequest) throws StripeException {
          StripeResponse stripeResponse = stripeService.getStripe(productRequest);
          return ResponseEntity.status(HttpStatus.OK).body(stripeResponse);
      }
}
