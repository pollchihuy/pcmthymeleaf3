package com.juaracoding.pcmthymeleaf3.utils;


import cn.apiclub.captcha.Captcha;
import com.juaracoding.pcmthymeleaf3.config.OtherConfig;
import com.juaracoding.pcmthymeleaf3.dto.validation.LoginDTO;
import com.juaracoding.pcmthymeleaf3.security.BcryptImpl;
import org.springframework.validation.BindingResult;

import java.util.regex.Pattern;

public class GlobalFunction {

    public static void getCaptchaLogin(LoginDTO loginDTO){
        Captcha captcha = CaptchaUtils.createCaptcha(275,70);
        String answer = captcha.getAnswer();
        if(OtherConfig.getEnableAutomationTesting().equals("y")){
            loginDTO.setHiddenCaptcha(answer);
        }else {
            loginDTO.setHiddenCaptcha(BcryptImpl.hash(answer));
        }
        loginDTO.setCaptcha("");
        loginDTO.setRealCaptcha(CaptchaUtils.encodeCaptcha(captcha));
    }

    public static void matchingPattern(String value,String regex,
                                       String field,String message,
                                       String modelAttribut,
                                       BindingResult result){
        Boolean isValid = Pattern.compile(regex).matcher(value).find();
        if(!isValid){
            result.rejectValue(field,"error."+modelAttribut,message);
        }
    }
}
