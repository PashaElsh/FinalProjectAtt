package com.example.finalproject.controllers;

import com.example.finalproject.enumm.Status;
import com.example.finalproject.models.Cart;
import com.example.finalproject.models.Order;
import com.example.finalproject.models.Person;
import com.example.finalproject.models.Product;
import com.example.finalproject.repositories.CartRepository;
import com.example.finalproject.repositories.OrderRepository;
import com.example.finalproject.repositories.ProductRepository;
import com.example.finalproject.security.PersonDetails;
import com.example.finalproject.services.PersonService;
import com.example.finalproject.services.ProductService;
import com.example.finalproject.util.PersonValidator;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class MainController {

    private final ProductRepository productRepository;

    private final PersonValidator personValidator;
    private final PersonService personService;
    private final ProductService productService;

    private final CartRepository cartRepository;

    private final OrderRepository orderRepository;

    public MainController(ProductRepository productRepository, PersonValidator personValidator, PersonService personService, ProductService productService, CartRepository cartRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.personValidator = personValidator;
        this.personService = personService;
        this.productService = productService;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/person_account")
    public String index(Model model){
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
        model.addAttribute("products", productService.getAllProduct());
        return "/user/index";
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
        return "redirect:/person_account";
    }

//    @GetMapping("/person_account/product")
//    public String getAllProduct(Model model){ // обращаемся к сервису и вызываем метод геттАллПродукт который вернет нам лист всех существующих продуктов (каталог всех товаров), далее все кладем в модель по атрибуту продуктс и возвращаем шаблон
//        model.addAttribute("products", productService.getAllProduct());
//        return "user/index";
//    }

    @GetMapping("/person_account/product/info/{id}")// Обработали нажатие на ссылку на конкретный товар, получили ID, далее в модель положили атрибут по ключу продукт, обратились к сервису и гетПродуктАйди вернул нам продукт по конкретному ID
    public String infoProduct(@PathVariable("id") int id, Model model){
        model.addAttribute("product", productService.getProductId(id));
        return "/user/infoProduct";
    }

    @PostMapping("/person_account/product/search")
    public String productSearch(@RequestParam("search") String search, @RequestParam("ot") String ot, @RequestParam("do") String Do, @RequestParam(value = "price", required = false, defaultValue = "") String price, @RequestParam(value = "contract", required = false, defaultValue = "")String contract, Model model){
        model.addAttribute("products", productService.getAllProduct());
        if(!ot.isEmpty() & !Do.isEmpty()){
            if(!price.isEmpty()){
                if(price.equals("sorted_by_ascending_price")) {
                    if (!contract.isEmpty()) {
                        if (contract.equals("furniture")) {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), 1));
                        } else if (contract.equals("appliances")) {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), 3));
                        } else if (contract.equals("clothes")) {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), 2));
                        }
                    } else {
                        model.addAttribute("search_product", productRepository.findByTitleOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do)));
                    }
                } else if(price.equals("sorted_by_descending_price")){
                    if(!contract.isEmpty()){
                        System.out.println(contract);
                        if(contract.equals("furniture")){
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), 1));
                        }else if (contract.equals("appliances")) {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), 3));
                        } else if (contract.equals("clothes")) {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), 2));
                        }
                    }  else {
                        model.addAttribute("search_product", productRepository.findByTitleOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do)));
                    }
                }
            } else {
                model.addAttribute("search_product", productRepository.findByTitleAndPriceGreaterThanEqualAndPriceLessThanEqual(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do)));
            }
        } else {
            model.addAttribute("search_product", productRepository.findByTitleContainingIgnoreCase(search));
        }

        model.addAttribute("value_search", search);
        model.addAttribute("value_price_ot", ot);
        model.addAttribute("value_price_do", Do);
        return "/product/product";

    }

    @GetMapping("/cart/add{id}")
    public String addProductInCart(@PathVariable("id") int id, Model model){
        // Получаем продукт по ID
        Product product = productService.getProductId(id);
        // Извлкаем объект айтиф пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        // Извлекаем id пользователя из объекта
        int id_person = personDetails.getPerson().getId();

        Cart cart = new Cart(id_person, product.getId()); // создали новую карзину
        cartRepository.save(cart);
        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String cart(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        // Извлекаем id пользователя из объекта
        int id_person = personDetails.getPerson().getId();
        List<Cart> cartList = cartRepository.findByPersonId(id_person);
        List<Product> productList = new ArrayList<>();

        // Получаем продукты из корзины по id товара
        for (Cart cart : cartList){
            productList.add(productService.getProductId(cart.getProductId()));
        }

        // Вычисление итоговой цены
        float price = 0;
        for (Product product : productList){
            price += product.getPrice();
        }

        model.addAttribute("price", price);
        model.addAttribute("cart_product", productList);
        return "/user/cart";
    }

    @GetMapping("/cart/delete/{id}")
    public String deleteProductFromCart(Model model, @PathVariable("id") int id){// Получаем id продукта который хотим удалить
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();// извлекаем объект сессии аутиф пользователя
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        // Извлекаем id пользователя из объекта
        int id_person = personDetails.getPerson().getId();
        List<Cart> cartList = cartRepository.findByPersonId(id_person);// корзина по id пользователя и получаем корзину всех товаров
        List<Product> productList = new ArrayList<>();// создаем пустой лист товаров

        // Получаем продукты из корзины по id товара
        for (Cart cart : cartList){
            productList.add(productService.getProductId(cart.getProductId()));
        }

        // Вычисление итоговой цены
        float price = 0;
        for (Product product : productList){
            price += product.getPrice();
        }
        cartRepository.deleteCartByProductId(id);
        return "redirect:/cart";
    }

    @GetMapping("/order/create")
    public String order(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();// Получаем объект аутиф пользователя
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        // Извлекаем id пользователя из объекта
        int id_person = personDetails.getPerson().getId();
        List<Cart> cartList = cartRepository.findByPersonId(id_person);// По id пользователя получаем корзину товаров
        List<Product> productList = new ArrayList<>();

        // Получаем продукты из корзины по id товара
        for (Cart cart : cartList){
            productList.add(productService.getProductId(cart.getProductId()));
        }

        // Вычисление итоговой цены
        float price = 0;
        for (Product product : productList){
            price += product.getPrice();
        }

        String uuid = UUID.randomUUID().toString();// это уникальная строка, которая генирирует номер заказа
        for (Product product : productList){
            Order newOrder = new Order(uuid, product, personDetails.getPerson(), 1, product.getPrice(), Status.Оформлен);
            orderRepository.save(newOrder);
            cartRepository.deleteCartByProductId(product.getId());
        }
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orderUser(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        List<Order> orderList = orderRepository.findByPerson(personDetails.getPerson()); // Позволит получить лист всех заказов по объекту персон,
        model.addAttribute("orders", orderList);
        return "/user/orders";
    }
}
