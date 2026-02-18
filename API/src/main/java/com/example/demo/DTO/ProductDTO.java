package com.example.demo.DTO;

import com.mongodb.lang.NonNull;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProductDTO {
    private String id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NonNull
    @Positive(message = "Price must be greater than 0")
    private Double price;
    private String userId;

    public ProductDTO() {
    } 

    public ProductDTO(String id, String name, String description, Double price, String userId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.userId = userId;
    }

}
