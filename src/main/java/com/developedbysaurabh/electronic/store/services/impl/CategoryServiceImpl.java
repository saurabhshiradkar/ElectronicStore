package com.developedbysaurabh.electronic.store.services.impl;

import com.developedbysaurabh.electronic.store.dtos.CategoryDto;
import com.developedbysaurabh.electronic.store.dtos.PageableResponse;
import com.developedbysaurabh.electronic.store.dtos.ProductDto;
import com.developedbysaurabh.electronic.store.entities.Category;
import com.developedbysaurabh.electronic.store.entities.Product;
import com.developedbysaurabh.electronic.store.exceptions.ResourceNotFoundException;
import com.developedbysaurabh.electronic.store.helper.Helper;
import com.developedbysaurabh.electronic.store.repositories.CategoryRepository;
import com.developedbysaurabh.electronic.store.services.CategoryService;
import com.developedbysaurabh.electronic.store.services.ProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    private ProductService productService;
    private ModelMapper mapper;

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Value("${categories.image.path}")
    private String imageUploadPath;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ProductService productService, ModelMapper mapper) {
        this.categoryRepository = categoryRepository;
        this.productService = productService;
        this.mapper = mapper;
    }




    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        //generate unique id in string format
        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);

        Category category = mapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepository.save(category);

        return mapper.map(savedCategory,CategoryDto.class);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, String categoryId) {

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category Not Found With given ID"));

        //update category details
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());

        Category updatedCategory = categoryRepository.save(category);

        return mapper.map(updatedCategory,CategoryDto.class);
    }

    @Override
    public void delete(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category Not Found With given ID"));

        List<Product> products = category.getProducts();

        //WHEN DELETING A CATEGORY DONT DELETE PRODUCTS UNDER THE CATEGORY INSTEAD UPDATE CATEGORY ID TO NULL
        for (Product product : products){
            product.setCategory(null);
            ProductDto updatedProduct = mapper.map(product, ProductDto.class);
            productService.update(updatedProduct,updatedProduct.getProductId());
        }

        //delete Category Cover image
        String fullImagePath = imageUploadPath + category.getCoverImage();

        try
        {
            Path path = Paths.get(fullImagePath);
            Files.delete(path);
        } catch (NoSuchFileException e) {
            logger.info("Category Cover Image Not Found in folder.");
            e.printStackTrace();
        }catch (InvalidPathException e){
            logger.info("Category Cover Image Not Found in folder ! USER DELETED");
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            categoryRepository.delete(category);
        }
    }

    @Override
    public PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);

        Page<Category> page = categoryRepository.findAll(pageable);

        PageableResponse<CategoryDto> pageableResponse = Helper.getPageableResponse(page, CategoryDto.class);

        return pageableResponse;
    }

    @Override
    public CategoryDto get(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category Not Found With given ID"));
        return mapper.map(category,CategoryDto.class);
    }
}
