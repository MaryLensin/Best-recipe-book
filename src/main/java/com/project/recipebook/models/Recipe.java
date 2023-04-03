package com.project.recipebook.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "recipes")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "author")
    private String author;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;
    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty")
    private Difficulty difficulty;
    @Column(name = "description")
    private String description;
    @Column(name = "rating")
    private double rating;
    @Column(name = "cookingTimeMinutes")
    private int cookingTimeMinutes;
    //    private List<String> ingredients;
//    private List<String> cookingSteps;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "recipe")
    private List<Image> images = new ArrayList<>();
    private Long previewImageId;
    private LocalDateTime dateAdded;

    @PrePersist
    private void init() {
        dateAdded = LocalDateTime.now();
    }

    public void addImageToProduct(Image image) {
        image.setRecipe(this);
        images.add(image);
    }
}
