package com.juaracoding.pcmthymeleaf3.controller;


import com.juaracoding.pcmthymeleaf3.dto.validation.LoginDTO;
import com.juaracoding.pcmthymeleaf3.dto.validation.RegisDTO;
import com.juaracoding.pcmthymeleaf3.utils.GlobalFunction;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.Random;

@Controller
@RequestMapping("/")
public class DefaultController {

    @GetMapping
    public String indexAwal(Model model, @PathVariable String flag) {

        LoginDTO loginDTO = new LoginDTO();
        GlobalFunction.getCaptchaLogin(loginDTO);
        loginDTO.setUsername("paul.123");
        loginDTO.setCaptcha(loginDTO.getHiddenCaptcha());
        model.addAttribute("usr", loginDTO);

        return "auth/login";
    }

    @GetMapping("/{flag}")
    public String index(Model model, @PathVariable String flag) {

        LoginDTO loginDTO = new LoginDTO();
        GlobalFunction.getCaptchaLogin(loginDTO);
        loginDTO.setUsername("paul.123");
        loginDTO.setCaptcha(loginDTO.getHiddenCaptcha());
        model.addAttribute("usr", loginDTO);
        String notif = "notif";
        String message = "";
        switch (flag){
            case "xyz$413":message="REGISTRASI BERHASIL, SILAHKAN LOGIN !!";break;
            case "774$_3":message="SESI HABIS, SILAHKAN LOGIN KEMBALI";break;
            case "43$x_y":message="AKSES DITOLAK, SILAHKAN LOGIN KEMBALI";break;
            case "3314&5":message="SERVER SEDANG MENGALAMI GANGGUAN";break;
            default:notif="none";break;
        }
        model.addAttribute(notif, message);
        return "auth/login";
    }

//    @GetMapping("/x")
//    public String regisSuccess(Model model) {
//
//        LoginDTO loginDTO = new LoginDTO();
//        GlobalFunction.getCaptchaLogin(loginDTO);
//        model.addAttribute("usr", loginDTO);
//        return "/login";
//    }

    @GetMapping("/regis")
    public String regis(Model model) {

        model.addAttribute("usr", new RegisDTO());
        return "auth/regis";
    }

    @GetMapping("/home")
    public String home(Model model, WebRequest webRequest) {

        Object username = webRequest.getAttribute("USR_NAME",1);
        Object menuNavBar= webRequest.getAttribute("MENU_NAVBAR",1);
        Object urlImg= webRequest.getAttribute("URL_IMG",1);

        model.addAttribute("USR_NAME",username);
        model.addAttribute("MENU_NAVBAR",menuNavBar);
        model.addAttribute("URL_IMG",urlImg);

        return "auth/home";
    }
    @GetMapping("/logout")
    public String destroySession(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:/"+new Random().nextInt(100);
    }
}