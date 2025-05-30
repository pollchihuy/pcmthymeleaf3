package com.juaracoding.pcmthymeleaf3.controller;

import com.juaracoding.pcmthymeleaf3.httpclient.UserService;
import com.juaracoding.pcmthymeleaf3.utils.GlobalFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;
    private Map<String,Object> filterColumn = new HashMap<String,Object>();

    public UserController() {
        filterColumn.put("namaLengkap","Nama Lengkap");
        filterColumn.put("email","Email");
        filterColumn.put("alamat","Alamat");
        filterColumn.put("no-hp","No Handphone");
        filterColumn.put("username","Username");
        filterColumn.put("umur","Umur");
        filterColumn.put("akses","Akses");
    }
    @GetMapping
    public String defaultPage(Model model, WebRequest request){

        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, request);
        if(jwt.equals("redirect:/774$_3")){
            return jwt;
        }
        try{
            response = userService.findAll(jwt);
            Map<String,Object> map = (Map<String, Object>) response.getBody();
            Map<String,Object> mapData = (Map<String, Object>) map.get("data");
            GlobalFunction.setPagingElement(model,mapData,"user",filterColumn);
            GlobalFunction.setGlobalAttribute(model,request,"USER");
        }catch (Exception e){
            return "redirect:/3314&5";
        }
        return "/main";
    }

    @GetMapping("/{sort}/{sortBy}/{page}")
    public String findByParam(
            Model model,
            @PathVariable String sort,
            @PathVariable String sortBy,
            @PathVariable Integer page,
            @RequestParam Integer size,
            @RequestParam String column,
            @RequestParam String value,
            WebRequest request){
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, request);
        page = page!=0?(page-1):page;
        if(jwt.equals("redirect:/774$_3")){
            return jwt;
        }
        try{
            response = userService.findByParam(jwt,sort,sortBy,page,size,column,value);
            Map<String,Object> map = (Map<String, Object>) response.getBody();
            Map<String,Object> mapData = (Map<String, Object>) map.get("data");
            GlobalFunction.setPagingElement(model,mapData,"user",filterColumn);
            GlobalFunction.setGlobalAttribute(model,request,"User");
        }catch (Exception e){
            return "redirect:/3314&5";
        }
        return "/main";
    }
}
