package org.example.stripepayment.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    private String name;
    private Long amount;
    private Long quantity;
    private String currency;

}
