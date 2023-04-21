package com.example.finalproject.config;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Config implements WebMvcConfigurer {// Конфиг для загрузки файлов фото
    @Value("${upload.path}")
    private String uploadPath; // Внедряем путь где будут храниться наши фотографии

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) { // Зарегистрировали новый ресурс, где указали что если нам гдето встречается этот путь "/img/**", то необходимо брать файл из этого пути "file://" + uploadPath + "/"
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file://" + uploadPath + "/");
    }
}
