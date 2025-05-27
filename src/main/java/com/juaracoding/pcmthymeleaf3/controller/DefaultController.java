package com.juaracoding.pcmthymeleaf3.controller;


import com.juaracoding.pcmthymeleaf3.dto.validation.LoginDTO;
import com.juaracoding.pcmthymeleaf3.dto.validation.RegisDTO;
import com.juaracoding.pcmthymeleaf3.utils.GlobalFunction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DefaultController {

    @GetMapping("/{flag}")
    public String index(Model model, @PathVariable String flag) {

        LoginDTO loginDTO = new LoginDTO();
        GlobalFunction.getCaptchaLogin(loginDTO);
        model.addAttribute("usr", loginDTO);
        if(flag.equals("xyz$413")) {
            model.addAttribute("regisSuccess", "REGISTRASI BERHASIL, SILAHKAN LOGIN !!");
        }
        return "/auth/login";
    }

//    @GetMapping("/x")
//    public String regisSuccess(Model model) {
//
//        LoginDTO loginDTO = new LoginDTO();
//        GlobalFunction.getCaptchaLogin(loginDTO);
//        model.addAttribute("usr", loginDTO);
//        return "/auth/login";
//    }

    @GetMapping("/regis")
    public String regis(Model model) {

        model.addAttribute("usr", new RegisDTO());
        return "/auth/regis";
    }
}
