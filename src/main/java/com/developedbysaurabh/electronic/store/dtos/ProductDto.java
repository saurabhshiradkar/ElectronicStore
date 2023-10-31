package com.developedbysaurabh.electronic.store.dtos;

import lombok.*;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

    private String productId;
    @NotBlank(message = "Title Is Required !")
    private String title;
    private String description;
    @DecimalMin(value = "0.0", message = "Price must be greater than or equal to 0.0")
    private Double price;
    @DecimalMin(value = "0.0", message = "Price must be greater than or equal to 0.0")
    private Double discountedPrice = price;
    @Min(value = 1, message = "Quantity must be 1 or greater")
    private int quantity;
    private Date addedDate;
    private boolean live;
    private boolean stock;
    private String productImageName;
    private CategoryDto category;
}
