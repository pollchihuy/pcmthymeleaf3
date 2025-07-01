package com.juaracoding.pcmthymeleaf3.utils;


import cn.apiclub.captcha.Captcha;
import com.juaracoding.pcmthymeleaf3.config.OtherConfig;
import com.juaracoding.pcmthymeleaf3.dto.validation.LoginDTO;
import com.juaracoding.pcmthymeleaf3.security.BcryptImpl;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class GlobalFunction {

    public static String errorHandleMainPage(String httpStatus, String form){
        String page = "";
        switch (httpStatus){
            case "400":page="redirect:/"+form+"/400";break;
            case "401":page="redirect:/774$_3";break;
            case "403":page="redirect:/43$x_y";break;
            default:page="redirect:/3314&5";break;
        }
        return page;
    }

    public static String errorHandleModals(String httpStatus){
        String page = "";
        switch (httpStatus){
            case "400":page="error-400";break;
            case "401":page="error-401";break;
            case "403":page="error-403";break;
            default:page="error-500";break;
        }
        return page;
    }
    public static String errorHandleFile(String httpStatus,String form,String fileType){
        String page = "";

        switch (httpStatus){
            case "500":page="redirect:/3314&5";break;
            case "401":page="redirect:/774$_3";break;
            case "403":page="redirect:/43$x_y";break;
            default:page="default";break;
        }
        if(page.equals("default")){
            page="redirect:/"+form+"/error-"+fileType;
        }
        return page;
    }

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

    public static String tokenCheck(Model model, WebRequest request){
        Object tokenJwt = request.getAttribute("JWT",1);
        if(tokenJwt == null){
            return "redirect:/774$_3";
        }
        return "Bearer "+tokenJwt;
    }

    public static String camelToStandard(String camel){
        StringBuilder sb = new StringBuilder();
        char c = camel.charAt(0);
        sb.append(Character.toLowerCase(c));
        for (int i = 1; i < camel.length(); i++) {
            char c1 = camel.charAt(i);
            if(Character.isUpperCase(c1)){
                sb.append(' ').append(Character.toLowerCase(c1));
            }
            else {
                sb.append(c1);
            }
        }
        return sb.toString();
    }

    public static void setPagingElement(Model model, Map<String,Object> mapData,String pathServer,Map<String,Object> filterColumn){
        List<Map<String,Object>> listContent = (List<Map<String, Object>>) mapData.get("content");
        Map<String,Object> listKolom = new LinkedHashMap<>();
        List<String> listHelper = new ArrayList<>();
        Map<String,Object> columnHeader = listContent.get(0);
        String keyVal = "";
        for(Map.Entry<String,Object> entry : columnHeader.entrySet()){
            keyVal = entry.getKey();
            listHelper.add(keyVal);
            listKolom.put(keyVal,GlobalFunction.camelToStandard(keyVal).toUpperCase());
        }
        /** Content Pagination */
        model.addAttribute("listKolom",listKolom);
        model.addAttribute("listContent",listContent);
        model.addAttribute("listHelper",listHelper);

        /** Element Pagination */
        int currentPage = (int) mapData.get("current-page");
        model.addAttribute("sort",mapData.get("sort"));
        model.addAttribute("sortBy",mapData.get("sort-by"));
        model.addAttribute("currentPage",currentPage==0?1:(currentPage+1));
        model.addAttribute("columnName",mapData.get("column-name"));
        model.addAttribute("value",mapData.get("value"));
        model.addAttribute("sizePerPage",mapData.get("size-per-page"));
        model.addAttribute("totalPage",mapData.get("total-pages"));
        model.addAttribute("totalData",mapData.get("total-data"));
        model.addAttribute("pathServer",pathServer);//REQUEST MAPPING
        model.addAttribute("SIZE_COMPONENT",ConstantValue.SIZE_COMPONENT);
        model.addAttribute("filterColumn",filterColumn);
    }

    public static void setGlobalAttribute(Model model,WebRequest request,String pageName){
        model.addAttribute("USR_NAME",request.getAttribute("USR_NAME",1).toString());
        model.addAttribute("MENU_NAVBAR",request.getAttribute("MENU_NAVBAR",1).toString());
        model.addAttribute("PAGE_NAME",pageName);
    }
}
