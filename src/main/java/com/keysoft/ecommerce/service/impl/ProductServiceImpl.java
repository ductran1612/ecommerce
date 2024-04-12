package com.keysoft.ecommerce.service.impl;

import com.keysoft.ecommerce.dto.ProductDTO;
import com.keysoft.ecommerce.dto.ProductImageDTO;
import com.keysoft.ecommerce.model.Category;
import com.keysoft.ecommerce.model.Product;
import com.keysoft.ecommerce.model.ProductImage;
import com.keysoft.ecommerce.repository.CategoryRepository;
import com.keysoft.ecommerce.repository.ProductImageRepository;
import com.keysoft.ecommerce.repository.ProductRepository;
import com.keysoft.ecommerce.service.ProductService;
import com.keysoft.ecommerce.specification.ProductSpecification;
import com.keysoft.ecommerce.util.CodeHelper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private ProductSpecification productSpecification;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Page<ProductDTO> getAllProducts(ProductDTO productDTO) {
        log.info("service: get all products");
        Page<Product> page = productRepository.findAll(productSpecification.filter(productDTO), PageRequest.of(productDTO.getPage(), productDTO.getSize()));
        List<ProductDTO> results = new ArrayList<>();

        for(Product item : page.getContent()){
            ProductDTO dto = modelMapper.map(item, ProductDTO.class);
            results.add(dto);
        }
        return new PageImpl<>(results, PageRequest.of(productDTO.getPage(), productDTO.getSize()), page.getTotalElements());
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public boolean save(ProductDTO productDTO) {
        log.info("service: save product");
        Product product;

        boolean isCheck = checkNameUsed(productDTO);
        if(isCheck){
            throw  new IllegalStateException("Sản phẩm đã tồn tại");
        }

        if (productDTO.getId() != null) {
            product = productRepository.findById(productDTO.getId()).orElse(null);
            if (product == null) {
                throw new IllegalStateException("Không tìm thấy sản phẩm!");
            }
            product.setName(productDTO.getName());
            product.setPrice(productDTO.getPrice());
            product.setImportPrice(productDTO.getImportPrice());
            product.getCategories().removeAll(product.getCategories());
        } else {
            productDTO.setEnable(true);
            productDTO.setQuantity(0);
            product = modelMapper.map(productDTO, Product.class);
            product.setCode(CodeHelper.spawnCodeFromName(productDTO.getName()));
        }

        List<Category> categories = new ArrayList<>();
        for (Long categoryId : productDTO.getProductCategories()) {
            Category selectedCategory = categoryRepository.findById(categoryId).orElse(null);
            if (selectedCategory == null) {
                throw new IllegalStateException("Không tìm thấy danh mục sản phẩm!");
            }
            categories.add(selectedCategory);
        }
        product.setCategories(categories);

        List<ProductImage> productImages = new ArrayList<>();
        List<Long> imageIds = new ArrayList<>();
        for (ProductImageDTO imageDTO : productDTO.getImages()) {
//            kiểm tra xem list ảnh hiện tại có chứa ảnh được gửi từ client sau khi sửa không
            if (imageDTO.getId() == null) {
                String[] parts = imageDTO.getName().split(",");
                if (parts.length == 2) {
                    byte[] imageBytes = Base64.getDecoder().decode(parts[1]);
                    String fileName = UUID.randomUUID().toString() + ".jpg";
                    ProductImage image = new ProductImage();
                    image.setName(fileName);
                    image.setProduct(product);
                    productImages.add(image);
                    FileOutputStream fos;
                    try {
                        Path imagePath = Paths.get("E:\\Workspace\\frontend\\reactjs\\frontend_nextjs\\public", fileName);
                        // Tạo một FileOutputStream để ghi dữ liệu vào tệp
                        fos = new FileOutputStream(imagePath.toFile());
                        fos.write(imageBytes);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                imageIds.add(imageDTO.getId());
            }
        }

        if(productDTO.getId() != null){
            if(product.getImages() != null){
                for(ProductImage img : product.getImages()){
                    if(!imageIds.contains(img.getId())){
                        productImageRepository.deleteById(img.getId());
                        Path imagePathToDelete = Paths.get("E:\\Workspace\\frontend\\reactjs\\frontend_nextjs\\public", img.getName());
                        try {
                            Files.deleteIfExists(imagePathToDelete);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        product.setImages(productImages);
        return productRepository.save(product).getId() != null;
    }

    @Override
    public ProductDTO get(String id) {
        ProductDTO productDTO;
        try{
            productDTO = modelMapper.map(productRepository.findById(Long.valueOf(id)).orElse(null), ProductDTO.class);
        }catch (NumberFormatException e) {
            throw new IllegalArgumentException("Id không hợp lệ: " + id);
        }
        return productDTO;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public boolean delete(String id) {
        log.info("service: delete product id: {}", id);
        Product product;
        try {
            product = productRepository.findById(Long.valueOf(id)).orElse(null);
        }catch (NumberFormatException e) {
            throw new IllegalArgumentException("Id không hợp lệ: " + id);
        }

        if(product == null){
            return false;
        }
        product.setEnable(false);
        productRepository.save(product);
        return !productRepository.findById(Long.valueOf(id)).orElse(new Product()).getEnable();

    }

    @Override
    public List<ProductDTO> searchByKeyword(String keyword) {
        if (StringUtils.hasText(keyword)) {
            List<Product> results = productRepository.findAll(productSpecification.filterByName(keyword));

            List<ProductDTO> resultsDTO = new ArrayList<>();

            for (Product entity : results) {
                resultsDTO.add(modelMapper.map(entity, ProductDTO.class));
            }

            return resultsDTO;
        }

        return Collections.emptyList();
    }

    public boolean checkNameUsed(ProductDTO criteria) {
        log.info("SERVICE PROCESS: CHECK PRODUCT NAME USED, CRITERIA: {}", criteria);

        Product product = productRepository.findProductByName(criteria.getName()).orElse(null);

        if (product == null)
            return false;

        if (criteria.getId() == null) {
            return true;
        }

        return (!Objects.equals(product.getId(), criteria.getId()));
    }
}
