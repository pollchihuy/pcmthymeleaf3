package com.juaracoding.pcmthymeleaf3.controller;

import com.juaracoding.pcmthymeleaf3.httpclient.MenuService;
import com.juaracoding.pcmthymeleaf3.utils.GlobalFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping
    public String defaultPage(Model model, WebRequest request){

        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, request);
        if(jwt.equals("redirect:/774$_3")){
            return jwt;
        }
        try{
            response = menuService.findAll(jwt);
            Map<String,Object> map = (Map<String, Object>) response.getBody();
            Map<String,Object> mapData = (Map<String, Object>) map.get("data");
            GlobalFunction.setPagingElement(model,mapData,"group-menu",new HashMap<>());
            GlobalFunction.setGlobalAttribute(model,request,"GROUP MENU");
        }catch (Exception e){
            return "redirect:/3314&5";
        }
        return "/main";
    }
}
