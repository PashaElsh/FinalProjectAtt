package com.example.finalproject.util;

import com.example.finalproject.models.Person;
import com.example.finalproject.services.PersonService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    private final PersonService personService; // создаем поле класса

    public PersonValidator(PersonService personService) {
        this.personService = personService;
    }

    // В данном методе указываем для какой модели предназначен данный валидатор
    @Override
    public boolean supports(Class<?> clazz) {//указывает с какой моделью мы будем работать
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {   //прописывается сама валидация
        Person person = (Person) target; //даункастим до объекта Person
        if (personService.findByLogin(person) !=null){
            errors.rejectValue("login", "", "Данный логин уже зарегистрирован в системе");
        }
    }
}
