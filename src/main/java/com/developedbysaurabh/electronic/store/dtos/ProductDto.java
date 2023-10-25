package com.developedbysaurabh.electronic.store.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

    @NotBlank(message = "Product Id Is Required !")
    private String productId;
    @NotBlank(message = "Title Is Required !")
    private String title;
    private String description;
    @NotBlank(message = "Price Is Required !")
    private Double price;
    private Double discountedPrice = price;
    @NotBlank(message = "quantity Is Required !")
    private int quantity;
    private Date addedDate;
    private boolean live;
    private boolean stock;
    private String productImageName;
    private CategoryDto category;
}
