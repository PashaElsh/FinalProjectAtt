package com.example.finalproject.controllers;

import com.example.finalproject.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public String getAllProduct(Model model){ // обращаемся к сервису и вызываем метод геттАллПродукт который вернет нам лист всех существующих продуктов (каталог всех товаров), далее все кладем в модель по атрибуту продуктс и возвращаем шаблон
        model.addAttribute("products", productService.getAllProduct());
        return "product/product";
    }

    @GetMapping("/info/{id}")// Обработали нажатие на ссылку на конкретный товар, получили ID, далее в модель положили атрибут по ключу продукт, обратились к сервису и гетПродуктАйди вернул нам продукт по конкретному ID
    public String infoProduct(@PathVariable("id") int id, Model model){
        model.addAttribute("product", productService.getProductId(id));
        return "/product/infoProduct";
    }
}
