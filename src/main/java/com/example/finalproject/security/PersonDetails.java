package com.example.finalproject.security;

import com.example.finalproject.models.Person;
import  org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class PersonDetails implements UserDetails {
    private final Person person;

    public PersonDetails(Person person) {
        this.person = person;
    }

    public Person getPerson(){
        return this.person;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {// метод Позволит вернуть Роль текущим пользователям
        return Collections.singletonList(new SimpleGrantedAuthority(person.getRole()));
    }

    @Override
    public String getPassword() {
        return this.person.getPassword();
    }

    @Override
    public String getUsername() {
        return this.person.getLogin();
    }

    // Аккаунт действителен или нет
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Является ли аккаунт заблокированным или нет
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Пароль является действительным
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Активный аккаунт или его диактивировали
    @Override
    public boolean isEnabled() {
        return true;
    }
}
