package com.example.finalproject.repositories;

import com.example.finalproject.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByLogin(String login); //Метод ищет пользователя по логину и возвращает объект типа Optional
}
