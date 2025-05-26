package com.juaracoding.pcmthymeleaf3.controller;


import com.juaracoding.pcmthymeleaf3.config.OtherConfig;
import com.juaracoding.pcmthymeleaf3.dto.validation.LoginDTO;
import com.juaracoding.pcmthymeleaf3.httpclient.AuthService;
import com.juaracoding.pcmthymeleaf3.security.BcryptImpl;
import com.juaracoding.pcmthymeleaf3.utils.GlobalFunction;
import jakarta.validation.Valid;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

@Controller
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthService authService;

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
        //ganti password
        // masukkan email
        // masukkan otp
        // confirm new password 

        if(result.hasErrors() || !isValid){
            if(!isValid){
                model.addAttribute("captchaMessage", "Invalid Captcha");
            }
            GlobalFunction.getCaptchaLogin(loginDTO);
            return "/auth/login";
        }

        return "auth/home";
    }


}
