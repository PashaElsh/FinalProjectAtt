package com.example.finalproject.controllers;

import com.example.finalproject.models.Category;
import com.example.finalproject.models.Image;
import com.example.finalproject.models.Product;
import com.example.finalproject.repositories.CategoryRepository;
import com.example.finalproject.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class AdminController {

    private final ProductService productService;

    @Value("${upload.path}")
    private String uploadPath;

    private final CategoryRepository categoryRepository;

    public AdminController(ProductService productService, CategoryRepository categoryRepository) {
        this.productService = productService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("admin/product/add")
    public String addProduct(Model model){
        model.addAttribute("product", new Product()); //Объект продукта кладем в модель
        model.addAttribute("category", categoryRepository.findAll());
        return "product/addProduct";
    }

    @PostMapping("/admin/product/add")
    public String addProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @RequestParam("file_one")MultipartFile file_one, @RequestParam("file_two")MultipartFile file_two, @RequestParam("file_three")MultipartFile file_three, @RequestParam("file_four")MultipartFile file_four, @RequestParam("file_five")MultipartFile file_five, @RequestParam("category") int category, Model model) throws IOException {
        Category category_db = categoryRepository.findById(category).orElseThrow();
        System.out.println(category_db.getName());
        if (bindingResult.hasErrors()){
            model.addAttribute("category", categoryRepository.findAll());
            return "product/addProduct";
        }

        if (file_one !=null){ // превращаем ссылку на файл в объeкт файла
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString(); // Генерируем уникальное наименование файла
            String resultFileName = uuidFile + "." + file_one.getOriginalFilename(); // Отправили файл по нужному пути
            file_one.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image(); // Создаем объект фотографии
            image.setProduct(product); // К фотографии привязываем продукт
            image.setFileName(resultFileName); // Создаем наименование файлу
            product.addImageToProduct(image); // Добовляет фотографию в лист продуктов
        }

        if (file_two !=null){  // превращаем ссылку на файл в объeкт файла
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString(); // Генерируем уникальное наименование файла
            String resultFileName = uuidFile + "." + file_two.getOriginalFilename();
            file_two.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image(); // Создаем объект фотографии
            image.setProduct(product); // К фотографии привязываем продукт
            image.setFileName(resultFileName); // Создаем наименование файлу
            product.addImageToProduct(image); // Добовляет фотографию в лист продуктов
        }

        if (file_three !=null){ // превращаем ссылку на файл в объeкт файла
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString(); // Генерируем уникальное наименование файла
            String resultFileName = uuidFile + "." + file_three.getOriginalFilename();
            file_three.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image(); // Создаем объект фотографии
            image.setProduct(product); // К фотографии привязываем продукт
            image.setFileName(resultFileName); // Создаем наименование файлу
            product.addImageToProduct(image); // Добовляет фотографию в лист продуктов
        }

        if (file_four !=null){ // превращаем ссылку на файл в объeкт файла
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString(); // Генерируем уникальное наименование файла
            String resultFileName = uuidFile + "." + file_four.getOriginalFilename();
            file_four.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image(); // Создаем объект фотографии
            image.setProduct(product); // К фотографии привязываем продукт
            image.setFileName(resultFileName); // Создаем наименование файлу
            product.addImageToProduct(image); // Добовляет фотографию в лист продуктов
        }

        if (file_five !=null){ // превращаем ссылку на файл в объeкт файла
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString(); // Генерируем уникальное наименование файла
            String resultFileName = uuidFile + "." + file_five.getOriginalFilename();
            file_five.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image(); // Создаем объект фотографии
            image.setProduct(product); // К фотографии привязываем продукт
            image.setFileName(resultFileName); // Создаем наименование файлу
            product.addImageToProduct(image); // Добовляет фотографию в лист продуктов
        }

        productService.saveProduct(product, category_db);

        return "redirect:/admin";
    }

    @GetMapping("admin") // Метод отображения страницы Admin
    public String admin(Model model){
        model.addAttribute("products", productService.getAllProduct()); //Мы в модель положем Лист всех продуктов
        return "admin";
    }

    @GetMapping("admin/product/delete/{id}") // Удаление товара
    public String deleteProduct(@PathVariable("id") int id){
        productService.deleteProduct(id);
        return "redirect:/admin";
    }

    @GetMapping("admin/product/edit/{id}")
    public String editProduct(Model model, @PathVariable("id") int id){
        model.addAttribute("product", productService.getProductId(id));// Найдем продукт по Id, положем его в модель, модель привяжем к форме и благодаря этому заполним все поля значениями данного объекта
        model.addAttribute("category", categoryRepository.findAll());
        return "product/editProduct";
    }

    @PostMapping("admin/product/edit/{id}")
    public String editProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @PathVariable("id") int id, Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("category", categoryRepository.findAll());
            return "product/editProduct";
        }
        productService.updateProduct(id, product);
        return "redirect:/admin";
    }
}
