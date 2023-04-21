package com.example.finalproject.services;

import com.example.finalproject.models.Category;
import com.example.finalproject.models.Product;
import com.example.finalproject.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Данный метод позволяет получить список всех товаров
    public List<Product> getAllProduct(){
        return productRepository.findAll();
    }

    // Данный метод позволяет получить товар по ID
    public Product getProductId(int id){
        Optional<Product> optionalProduct = productRepository.findById(id); // Получаем список товаров по ID
        return optionalProduct.orElse(null);
    }

    // Даный метод позволяет сохранить товар
    @Transactional
    public void saveProduct(Product product, Category category){
        product.setCategory(category);
        productRepository.save(product);
    }

    // Данный метод позволяет обновить данные о товаре
    @Transactional
    public void updateProduct(int id, Product product){
        product.setId(id);
        productRepository.save(product);
    }

    // Данный метод позволяет удалить  товар по ID
    @Transactional
    public void deleteProduct(int id){
        productRepository.deleteById(id);
    }
}
