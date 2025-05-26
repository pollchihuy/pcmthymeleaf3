package com.juaracoding.pcmthymeleaf3.controller;


import com.juaracoding.pcmthymeleaf3.dto.validation.LoginDTO;
import com.juaracoding.pcmthymeleaf3.utils.GlobalFunction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {

    @GetMapping("/")
    public String index(Model model) {

        LoginDTO loginDTO = new LoginDTO();
        GlobalFunction.getCaptchaLogin(loginDTO);
        model.addAttribute("usr", loginDTO);
        return "/auth/login";
    }

    @GetMapping("/regis")
    public String regis() {
        return "/auth/registrasi";
    }
}
