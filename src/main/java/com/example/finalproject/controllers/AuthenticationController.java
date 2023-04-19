package com.example.finalproject.controllers;

import com.example.finalproject.models.Product;
import com.example.finalproject.repositories.CategoryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {

    @GetMapping("/authentication")//Метод аутификации пользователей
    public String login(){
        return "authentication"; // возможность регистрации пользователя
    }
}
