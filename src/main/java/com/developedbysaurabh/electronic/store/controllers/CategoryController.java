package com.developedbysaurabh.electronic.store.controllers;

import com.developedbysaurabh.electronic.store.dtos.*;
import com.developedbysaurabh.electronic.store.services.CategoryService;
import com.developedbysaurabh.electronic.store.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private CategoryService categoryService;
    private FileService fileService;
    @Autowired
    public CategoryController(CategoryService categoryService, FileService fileService) {
        this.categoryService = categoryService;
        this.fileService = fileService;
    }

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${categories.image.path}")
    private String imageUploadPath;


    //create
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
        CategoryDto categoryDto1 = categoryService.create(categoryDto);
        return new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable String categoryId,
            @RequestBody CategoryDto categoryDto
    ){
        CategoryDto updatedCategory = categoryService.update(categoryDto, categoryId);
        return new ResponseEntity<>(updatedCategory,HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId){
        categoryService.delete(categoryId);

        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .message("Category Deleted Successfully !")
                .success(true)
                .status(HttpStatus.OK)
                .build();

        return new ResponseEntity<>(apiResponseMessage,HttpStatus.OK);
    }

    //get all
    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAll(
            @RequestParam(value = "pageNumber", defaultValue = "0",required = false ) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        return new ResponseEntity<>(categoryService.getAll(pageNumber,pageSize,sortBy,sortDir), HttpStatus.OK);
    }

    //get single category
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable String categoryId){
        return new ResponseEntity<>(categoryService.get(categoryId),HttpStatus.OK);
    }

    //upload category image
    @PostMapping("/categoryCoverImage/{categoryId}")
    public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("image") MultipartFile image, @PathVariable("categoryId") String categoryId) throws IOException {

        String imageName = fileService.uploadFile(image, imageUploadPath);

        CategoryDto categoryDto = categoryService.get(categoryId);
        categoryDto.setCoverImage(imageName);
        categoryService.update(categoryDto,categoryId);

        ImageResponse imageResponse = ImageResponse.builder()
                .imageName(imageName)
                .message("Category Image Uploaded Successfully")
                .success(true)
                .status(HttpStatus.CREATED)
                .build();

        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }

    //serve Category image
    @GetMapping("/categoryCoverImage/{categoryId}")
    public void serveCategoryImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {

        CategoryDto categoryDto = categoryService.get(categoryId);
        logger.info("Category Cover image name : {}",categoryDto.getCoverImage());
        InputStream resource = fileService.getResource(imageUploadPath, categoryDto.getCoverImage());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());

    }
}
