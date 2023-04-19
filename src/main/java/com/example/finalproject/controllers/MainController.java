package com.example.finalproject.controllers;

import com.example.finalproject.models.Person;
import com.example.finalproject.security.PersonDetails;
import com.example.finalproject.services.PersonService;
import com.example.finalproject.util.PersonValidator;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {

    private final PersonValidator personValidator;
    private final PersonService personService;

    public MainController(PersonValidator personValidator, PersonService personService) {
        this.personValidator = personValidator;
        this.personService = personService;
    }

    @GetMapping("/index")
    public String index(){
        // Получаем объект аутентификации -> с помощью
        //SpringContextHolder обращаемся к контексту и на нем вызываем
        // метод аутентификации. Из сессии текущего пользователя
        //получаем объект, который был положен в данную сессию после  аутентификации пользователя

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();// Объект пользователя из объекта аутификации

        String role = personDetails.getPerson().getRole(); // Извлекаем персона и извлекаем пароль
        if (role.equals("ROLE_ADMIN")){
            return "redirect:/admin";
        }

//        System.out.println(personDetails.getPerson());
//        System.out.println("ID пользователя: " + personDetails.getPerson().getId());
//        System.out.println("Логин пользователя: " + personDetails.getPerson().getLogin());
//        System.out.println("Пароль пользователя: " + personDetails.getPerson().getPassword());
//        System.out.println(personDetails);
        return "index";
    }

    @GetMapping("/registration") // метод который позволит открыть форму для регистрации новых пользователей
    public String registration(@ModelAttribute("person") Person person){
        return "registration";
    }

    // В рамках данного метода мы обрабатываем форму регистрации, принимаем обьект с модели, и создаем объект ошибки
    @PostMapping("/registration")
    public String resultRegistration (@ModelAttribute("person") @Valid Person person, BindingResult bindingResult){
        personValidator.validate(person, bindingResult); //validate()-Позваолит нам проверить данные
        if (bindingResult.hasErrors()){
            return "registration";
        }
        personService.register(person);
        return "redirect:/index";
    }
}
