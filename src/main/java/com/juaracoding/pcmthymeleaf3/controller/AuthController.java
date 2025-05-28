package com.juaracoding.pcmthymeleaf3.controller;


import com.juaracoding.pcmthymeleaf3.config.OtherConfig;
import com.juaracoding.pcmthymeleaf3.dto.validation.LoginDTO;
import com.juaracoding.pcmthymeleaf3.dto.validation.RegisDTO;
import com.juaracoding.pcmthymeleaf3.dto.validation.VerifyRegisDTO;
import com.juaracoding.pcmthymeleaf3.httpclient.AuthService;
import com.juaracoding.pcmthymeleaf3.httpclient.RefreshTokenService;
import com.juaracoding.pcmthymeleaf3.security.BcryptImpl;
import com.juaracoding.pcmthymeleaf3.utils.GenerateStringMenu;
import com.juaracoding.pcmthymeleaf3.utils.GlobalFunction;
import jakarta.validation.Valid;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private AuthService authService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/regis")
    public String regis(Model model,
                        @Valid @ModelAttribute("usr") RegisDTO regisDTO ,
                        BindingResult result, WebRequest webRequest){

        if(result.hasErrors()){
            return "auth/regis";
        }

        ResponseEntity<Object> response = null;
        String email = "";
        String otp = "";
        try{
            response = authService.registration(regisDTO);
            Map<String,Object> map = (Map<String, Object>) response.getBody();
            Map<String,Object> mapData = (Map<String, Object>) map.get("data");
            email = (String) mapData.get("email");
            if(OtherConfig.getEnableAutomationTesting().equals("y")){
                otp = String.valueOf(mapData.get("otp"));
            }

        }catch (Exception e){
            System.out.println("Error : "+e.getMessage());
        }

        VerifyRegisDTO verifyRegisDTO = new VerifyRegisDTO();
        verifyRegisDTO.setEmail(email);
        verifyRegisDTO.setOtp(otp);
        model.addAttribute("x",verifyRegisDTO);
        model.addAttribute("token",otp);
        return "auth/verify-regis";

    }

    @PostMapping("/verify-regis")
    public String verifyRegis(Model model,
                        @Valid @ModelAttribute("x") VerifyRegisDTO verifyRegisDTO ,
                        BindingResult result, WebRequest webRequest){

        if(result.hasErrors()){
            return "auth/verify-regis";
        }

        ResponseEntity<Object> response = null;

        try{
            response = authService.verifyRegis(verifyRegisDTO);
        }catch (Exception e){
            return "auth/verify-regis";
        }

        return "redirect:/xyz$413";
    }

    @PostMapping("/login")
    public String login(Model model,
                        @Valid @ModelAttribute("usr") LoginDTO loginDTO ,
                        BindingResult result, WebRequest webRequest){

        String strAnswer = loginDTO.getHiddenCaptcha();
        String decodePassword = new String(Base64.decode(loginDTO.getPassword()));
        System.out.println("Password Decoded: " + decodePassword);
        GlobalFunction.matchingPattern(decodePassword,"^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@_#\\-$])[\\w].{8,15}$",
                "password","Format Password Tidak Valid","usr",result);

        Boolean isValid = false;
        if(OtherConfig.getEnableAutomationTesting().equals("y")){
            isValid = loginDTO.getCaptcha().equals(strAnswer);
        }else{
            isValid = BcryptImpl.verifyHash(loginDTO.getCaptcha(),strAnswer);
        }

        if(result.hasErrors() || !isValid){
            if(!isValid){
                model.addAttribute("captchaMessage", "Invalid Captcha");
            }
            GlobalFunction.getCaptchaLogin(loginDTO);
            return "/auth/login";
        }
        loginDTO.setPassword(decodePassword);
        loginDTO.setCaptcha("");
        loginDTO.setHiddenCaptcha("");
        loginDTO.setRealCaptcha("");

        ResponseEntity<Object> response = null;
        String tokenJwt = "";
        String menuNavBar = "";

        try {
            response = authService.login(loginDTO);
            Map<String,Object> map = (Map<String, Object>) response.getBody();
            Map<String,Object> mapData = (Map<String, Object>) map.get("data");
            tokenJwt = (String) mapData.get("token");
            List<Map<String,Object>> listMenu = (List<Map<String, Object>>) mapData.get("menu");
            menuNavBar = new GenerateStringMenu().stringMenu(listMenu);
//            System.out.println("Menu Nav Bar  : " + menuNavBar);
        }catch (Exception e){
            System.out.println("Error : "+e.getMessage());
            GlobalFunction.getCaptchaLogin(loginDTO);
            return "/auth/login";
        }
        /** input ke table session */
        webRequest.setAttribute("JWT",tokenJwt,1);
        webRequest.setAttribute("USR_NAME",loginDTO.getUsername(),1);
        webRequest.setAttribute("PASSWORD",loginDTO.getPassword(),1);
        webRequest.setAttribute("MENU_NAVBAR",menuNavBar,1);

        model.addAttribute("USR_NAME",loginDTO);
        model.addAttribute("MENU_NAVBAR",menuNavBar);
        return "auth/home";
    }

    @GetMapping("/coba")
    public String ambilRequest(Model model,WebRequest request){
        String username = request.getAttribute("USR_NAME",1).toString();
        String password = request.getAttribute("PASSWORD",1).toString();
        System.out.println("Username : "+username);
        System.out.println("Password : "+password);
        System.out.println("JWT : "+request.getAttribute("JWT",1));

        ResponseEntity<Object> response = null;
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername(username);
        loginDTO.setPassword(password);
        response = refreshTokenService.tokenExpired(loginDTO);
        Object obj = response.getBody();
        System.out.println("New Token : "+obj);
        Map<String,Object> map = (Map<String, Object>) obj;
        Map<String,Object> mapData = (Map<String, Object>) map.get("data");

        String jwtToken = mapData.get("token").toString();
        request.setAttribute("JWT",jwtToken,1);
        return "/auth/home";
    }
}