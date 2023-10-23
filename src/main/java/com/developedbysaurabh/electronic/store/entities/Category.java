package com.developedbysaurabh.electronic.store.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
