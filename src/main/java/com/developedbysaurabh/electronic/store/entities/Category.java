package com.developedbysaurabh.electronic.store.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @Column(name = "id")
    private String categoryId;

    @Column(name = "category_title", length = 500, nullable = false)
    private String title;

    @Column(name = "category_description",length = 1000)
    private String description;

    @Column(name = "cover_image")
    private String coverImage;

    //category can contain many products
    @OneToMany(mappedBy = "category",fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();
}
