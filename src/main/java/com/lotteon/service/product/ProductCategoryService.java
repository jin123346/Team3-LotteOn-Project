package com.lotteon.service.product;


import com.lotteon.dto.product.CreateCategoryRequestDTO;
import com.lotteon.dto.product.ProductCategoryDTO;
import com.lotteon.entity.product.ProductCategory;
import com.lotteon.repository.Impl.ProductCategoryRepositoryImpl;
import com.lotteon.repository.product.ProductCategoryRepository;
import com.lotteon.service.CacheableService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;
    private final CacheableService cacheableService;

    private final ModelMapper modelMapper;
    private final ProductCategoryRepositoryImpl productCategoryRepositoryImpl;
    private final CacheManager cacheManager;

    @Cacheable(value = "categories", key = "'categoryList'")
    public List<ProductCategoryDTO> populateCategories() {
        List<ProductCategory> categories = getCategoryHierarchy();
        List<ProductCategoryDTO> categoryDTOs = new ArrayList<>();
        categories.forEach(category -> categoryDTOs.add(convertToDto(category)));
        return categoryDTOs;
    }

    private ProductCategoryDTO convertToDto(ProductCategory category) {
        ProductCategoryDTO categoryDTO = modelMapper.map(category, ProductCategoryDTO.class);
        if (category.getChildren() != null) {
            List<ProductCategoryDTO> childrenDTOs = new ArrayList<>();
            for (ProductCategory child : category.getChildren()) {
                childrenDTOs.add(convertToDto(child));
            }
            categoryDTO.setChildren(childrenDTOs);
        }
        return categoryDTO;
    }
    public ProductCategoryDTO getCategoryName(long categoryId) {

        ProductCategory productCategory =  productCategoryRepository.findById(categoryId).orElseThrow(EntityNotFoundException::new);
        return modelMapper.map(productCategory, ProductCategoryDTO.class);

    }

    // 3차 카테고리로부터 2차, 1차 카테고리를 가져오는 메서드
    @Transactional(readOnly = true)
    public List<ProductCategoryDTO> getAllParentCategoryDTOs(Long categoryId) {
        List<ProductCategoryDTO> parentCategoryDTOs = new ArrayList<>();
        ProductCategory category = productCategoryRepository.findById(categoryId).orElse(null);

        while (category != null ) {

            ProductCategoryDTO dto =ProductCategoryDTO.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .level(category.getLevel())
                    .subcategory(category.getSubcategory())
                    .build();

            parentCategoryDTOs.add(dto);
            category = category.getParent();
        }

        return parentCategoryDTOs;
    }

//    public List<ProductCategoryDTO> populateCategories() {
//        log.info("Fetching category data from the database...");
//        List<ProductCategory> categories = getCategoryHierarchy();
//        List<ProductCategoryDTO> categoryDTOs = new ArrayList<>();
//        categories.forEach(category -> categoryDTOs.add(convertToDto(category)));
//        return categoryDTOs;
//    }
//
//    private ProductCategoryDTO convertToDto(ProductCategory category) {
//        ProductCategoryDTO categoryDTO = modelMapper.map(category, ProductCategoryDTO.class);
//        // 재귀적으로 children도 매핑
//        if (category.getChildren() != null) {
//            List<ProductCategoryDTO> childrenDTOs = new ArrayList<>();
//            for (ProductCategory child : category.getChildren()) {
//                childrenDTOs.add(convertToDto(child));
//            }
//            categoryDTO.setChildren(childrenDTOs);
//        }
//        return categoryDTO;
//    }

    public ProductCategory insertCategory(CreateCategoryRequestDTO createCategoryRequestDTO  ) {
        ProductCategory parentCategory = null;
        if(createCategoryRequestDTO.getParentId() != null && createCategoryRequestDTO.getLevel() > 1) {
            parentCategory = productCategoryRepository.findById(createCategoryRequestDTO.getParentId())
                    .orElseThrow(()->new EntityNotFoundException("parent category not found"));

        }



        // Create new category
        ProductCategory newCategory = ProductCategory.builder()
                .name(createCategoryRequestDTO.getName())
                .parent(parentCategory)  // Set parent category if applicable
                .level(createCategoryRequestDTO.getLevel())
                .subcategory(createCategoryRequestDTO.getSubcategory())
                .disp_yn(createCategoryRequestDTO.getDispYn())
                .note(createCategoryRequestDTO.getNote())
                .build();

        // Save the new category
        return productCategoryRepository.save(newCategory);
    }

    public void updateCategory(){}
    public void deleteCategory(){}


//    level로 카테고리 가져오기
    public List<ProductCategoryDTO> getCategoriesByLevel(int level){

        List<ProductCategory> categories = productCategoryRepository.findByLevel(level);
        return categories.stream()
                .map(category -> modelMapper.map(category, ProductCategoryDTO.class))
                .collect(Collectors.toList());  // Use collect for Java 8+
    }
// parentId값을 기준으로 카테고리 가져오기
    public List<ProductCategoryDTO> getCategoriesByParentId(long parentId){

        List<ProductCategory> categories =  productCategoryRepository.findByParentId(parentId);

        if(categories.isEmpty()){
            log.warn("No categories found for parent id {}", parentId);
        }
        return categories.stream()
                .map(category -> modelMapper.map(category, ProductCategoryDTO.class))
                .toList();
    }


    //메인 카테고리 가져오기
    public ProductCategoryDTO getCategoryById(long id){
        Optional<ProductCategory> opt  = productCategoryRepository.findById(id);
        if (opt.isPresent()) {
            return modelMapper.map(opt.get(), ProductCategoryDTO.class);
        }
        return null;

    }

    public List<ProductCategoryDTO> selectCategory(long id){

       List<ProductCategory> categories =  productCategoryRepository.SelectParentCategories(id);

       List<ProductCategoryDTO> categoriesDTO = categories.stream().map(category -> modelMapper.map(category, ProductCategoryDTO.class)).toList();

        return categoriesDTO;
    }




    public List<ProductCategory> getCategoryHierarchy() {
        return buildCategoryTree(null); // 최상위 카테고리부터 시작 (parentId가 NULL인 것)
    }


    private List<ProductCategory> buildCategoryTree(Long parentId) {
        List<ProductCategory> categories = productCategoryRepository.findByParentId(parentId);
        for (ProductCategory category : categories) {
            category.setChildren(buildCategoryTree(category.getId())); // 재귀적으로 하위 카테고리를 가져옴
        }


        return categories;
    }

    @Cacheable(value = "categories", key = "'categoryList'")
    public List<ProductCategoryDTO> getCategoriesWithCacheCheck() {
        log.info("Cache miss - Fetching categories from the database");
        List<ProductCategoryDTO> categories = populateCategories();
        return categories;
//        Cache cache = cacheManager.getCache("categories");
//        List<ProductCategoryDTO> cachedCategories = cache != null ? cache.get(0, List.class) : null;
//
//        if (cachedCategories == null) {
//            log.info("Cache miss - Fetching categories from the database");
//            return populateCategories(); // 캐시 갱신
//        } else {
//            log.info("Cache hit - Returning cached categories");
//            return cachedCategories;
//        }
    }
}
