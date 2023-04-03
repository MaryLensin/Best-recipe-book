package com.project.recipebook.services;

import com.project.recipebook.models.Category;
import com.project.recipebook.models.Image;
import com.project.recipebook.models.Recipe;
import com.project.recipebook.repositories.CategoryRepository;
import com.project.recipebook.repositories.RecipeRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    @PostConstruct
    public void init(){
        addCategoryIfNew((Category.builder().title("Breakfast").build()));
        addCategoryIfNew((Category.builder().title("Lunch").build()));
        addCategoryIfNew((Category.builder().title("Dinner").build()));
        addCategoryIfNew((Category.builder().title("Healthy food").build()));
        addCategoryIfNew((Category.builder().title("Low calories").build()));
    }
    public void addCategoryIfNew(Category newCategory){
        if(categoryRepository.findByTitle(newCategory.getTitle())==null){
            categoryRepository.save(newCategory);
        }

    }

    public List<Category> getAllCategories(){
        List<Category> categories= categoryRepository.findAll();
        return categories;
    }

    public List<Recipe> getRecipes(String title) {
        if (title != null) {
            return recipeRepository.findByTitle(title);
        }
        return recipeRepository.findAll();
    }


    public void saveRecipe(Recipe recipe, List<MultipartFile> files) throws IOException {
        if(!files.isEmpty())
        for (int i = 0; i < files.size(); i++) {
            if (files.get(i).getSize() != 0) {
                Image image;
                image = toImageEntity(files.get(i));
                if (i == 0) {
                    image.setPreviewImage(true);
                }
                recipe.addImageToProduct(image);
            }
        }
        log.info("Saving new Recipe. Title: {}; Author: {}", recipe.getTitle(), recipe.getAuthor());
        Recipe recipeFromDb = recipeRepository.save(recipe);
        recipeFromDb.setPreviewImageId(recipeFromDb.getImages().get(0).getId());
        recipeRepository.save(recipe);
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = Image.builder()
                .name(file.getName())
                .originalFilename(file.getOriginalFilename())
                .contentType(file.getContentType())
                .size(file.getSize())
                .bytes(file.getBytes())
                .build();
        return image;
    }

    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id).orElse(null);
    }
}
