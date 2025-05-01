package com.ecommerce.manideep.sb.ecom.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId ;
    @NotBlank
    @Size(min = 5 ,message = "Category must contain atleast 5 charecters")
    private String categoryName ;

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
    private List<Product> products ;
}
