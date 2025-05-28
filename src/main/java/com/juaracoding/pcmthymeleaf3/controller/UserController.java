package com.juaracoding.pcmthymeleaf3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;


@Controller
@RequestMapping("user")
public class UserController {
    @GetMapping
    public String defaultPage(Model model, WebRequest request){
        String username = request.getAttribute("USR_NAME",1).toString();
        String menuNavBar= request.getAttribute("MENU_NAVBAR",1).toString();

        model.addAttribute("USR_NAME",username);
        model.addAttribute("MENU_NAVBAR",menuNavBar);
        model.addAttribute("PAGE_NAME","USER");
        return "/main";
    }
}
