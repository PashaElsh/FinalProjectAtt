package com.example.finalproject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

@Entity //Пометили что это модель а не обычный JAVA класс
public class Product {// Модель для взаимодействия с товарами

    @Id //любая модель должна иметь Первичный ключ
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title", nullable = false, columnDefinition = "text", unique = true)
    @NotEmpty(message = "Наименование товара не может быть пустым")
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "text")
    @NotEmpty(message = "Описание товара должно быть написано")
    private String description;

    @Column(name = "price", nullable = false)
    @Min(value = 1, message = "Цена не может быть отрицательной или нулевой")
    private float price;

    @Column(name = "warehouse", nullable = false)
    @NotEmpty(message = "Склад по нахождению товара не может быть пустым")
    private String warehouse;

    @Column(name = "seller", nullable = false)
    @NotEmpty(message = "Информация о продавце не может быть пустой")
    private String seller;
}
