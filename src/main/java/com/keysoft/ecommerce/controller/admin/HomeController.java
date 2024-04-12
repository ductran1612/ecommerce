package com.keysoft.ecommerce.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    @GetMapping( "/login")
    public String loginPage(){
        System.out.println(123);
        return "shop/login";
    }
}
