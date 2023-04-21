package com.example.finalproject.config;


import com.example.finalproject.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig  {
//    private final AuthenticationProvider authenticationProvider;

//    public SecurityConfig(AuthenticationProvider authenticationProvider) {
//        this.authenticationProvider = authenticationProvider;
//    }
    private final PersonDetailsService personDetailsService;

    @Bean
    public PasswordEncoder getPasswordEncode(){ // Метод при котором наши пароли не шифруются, используется когда тестируешь
        return new BCryptPasswordEncoder();// Хешируем пароль + соль)
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        .csrf().disable() //отключаем защиту от межсайтовой подделки запросов
        // Конфигурируем работу Spring Security
        http
                .authorizeHttpRequests() // указываем что страницы должны быть защищены аутентификацией
                // указываем что не аутинтиф пользователи могут зайти на страницу аутентиф и на объект ошибки
                // с помощью permitAll указываем что не аутентиф пользователи могут заходить на перечисленные страницы
//                .requestMatchers("/authentication", "/error", "/registration").permitAll()
                // указываем что для всех остальных страниц необходимо вызывать метод authentitication(), который открывает форму аутентификации
//                .anyRequest().authenticated()
                .requestMatchers("/admin").hasRole("ADMIN")//указываем на то что страница /admin доступна с ролью ADMIN
                .requestMatchers("/authentication", "/registration", "/error", "/resources/**","static/**", "/css/**", "/js/**", "/img/**", "/product", "/product/info/{id}").permitAll()// страница доступна всем пользователям
                .anyRequest().hasAnyRole("USER", "ADMIN")
                .and() // указываем что дальше настраивается аутен и соединяем ее с настройкой доступа
                .formLogin().loginPage("/authentication") // указываем какой url запрос будет отправляться при заходе на защищенные страницы
                .loginProcessingUrl("/process_login") // указываем на какой адрес будут отправляться
                                                        // данные с формы. Нам уже не нужно будет создавать метод в контроллере и обрабатывать  данные с формы. Мы задали url, который используется по умолчанию для обработки формы
                                                        //        аутентификации по средствам Spring Security. Spring Security будет ждать объект с формы  аутентификации и затем    сверять логин и пароль с данными в БД
                .defaultSuccessUrl("/person_account", true) // Указываем на какой url
                                                            //необходимо направить пользователя после успешной аутентификации. Вторым аргументом
                                                            //указывается true чтобы перенаправление шло в любом случае послу успешной аутентификации
                .failureUrl("/authentication?error")// Указываем куда необходимо
                                                                    //«перенаправить пользователя при проваленной аутентификации. В запрос будет передан объект,  error, который будет проверятся на форме и при наличии данного объекта в запросе
                                                                    //выводится сообщение "Неправильный логин или пароль"
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/authentication");
        return http.build();
    }
    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
        authenticationManagerBuilder.userDetailsService(personDetailsService)
                .passwordEncoder(getPasswordEncode()); // Хеширование паролей
    }
}
