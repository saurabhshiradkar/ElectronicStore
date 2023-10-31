package com.developedbysaurabh.electronic.store.dtos;

import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {


    private String categoryId;

    @Size(min = 3, message = "Title must be of minimum 3 characters !")
    private String title;

    @NotBlank(message = "Description is Required!")
    private String description;

    private String coverImage;

}
