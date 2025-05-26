package com.juaracoding.pcmthymeleaf3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("coba")
public class TestController {

    // localhost:8080/coba
    @GetMapping("")
    public String cobaAja(){
        return "/testing/coba-aja";
    }

    @GetMapping("/tembak")
    public String tembakAja(Model m){

        List<String> listData = new ArrayList<String>();
        listData.add("A");
        listData.add("B");
        listData.add("C");
        listData.add("D");

        m.addAttribute("cobaText","HUAHUHAUHAUHUA");
        m.addAttribute("linkServer","/coba");
        m.addAttribute("x",listData);
        return "/testing/tembak";
    }
}