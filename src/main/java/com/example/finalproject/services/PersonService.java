package com.example.finalproject.services;

import com.example.finalproject.models.Person;
import com.example.finalproject.repositories.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder; //позволяет хешировать наши пароли

    @Autowired
    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Person findByLogin(Person person){ //поиск пользователя по логину
        Optional<Person> person_db = personRepository.findByLogin(person.getLogin());// Получим пользователя по логину
        return person_db.orElse(null); // Если найден то вернем person_db. если нет то null
    }

    @Transactional
    public void register (Person person) { // сохранение пользователя который зарегистрировался в БД
        person.setPassword(passwordEncoder.encode(person.getPassword()));// пользователь который должен быть сохранен в базу, устанавливаем новый пароль, обращаемся к passwordEncoder, и вызывем метод encode. encode позволит из обычного пароля с помощью функции БиКрипт образовать пароль в Хеш, далее обращаемся к пользователю(person), и методу гетПас
        person.setRole("ROLE_USER");
        personRepository.save(person);
    }
}
