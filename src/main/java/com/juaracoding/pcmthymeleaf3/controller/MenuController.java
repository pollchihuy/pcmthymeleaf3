package com.juaracoding.pcmthymeleaf3.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.juaracoding.pcmthymeleaf3.dto.response.ResMenuDTO;
import com.juaracoding.pcmthymeleaf3.dto.validation.ValMenuDTO;
import com.juaracoding.pcmthymeleaf3.httpclient.MenuService;
import com.juaracoding.pcmthymeleaf3.utils.GlobalFunction;
import feign.Response;
import jakarta.validation.Valid;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("menu")
public class MenuController {

    @Autowired
    private MenuService menuService;
    private Map<String,Object> filterColumn = new HashMap<String,Object>();

    public MenuController() {
        filterColumn.put("nama","Nama");
        filterColumn.put("deskripsi","Deskripsi");
        filterColumn.put("path","Path");
        filterColumn.put("group","Group");
    }

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
            GlobalFunction.setPagingElement(model,mapData,"menu",filterColumn);
            GlobalFunction.setGlobalAttribute(model,request,"MENU");
        }catch (Exception e){
            return "redirect:/3314&5";
        }
        return "/main";
    }

    @GetMapping("/err/{err}")
    public String defaultPageError(Model model,
                                   @PathVariable String err,
                                   WebRequest request){

        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, request);
        if(jwt.equals("redirect:/774$_3")){
            return jwt;
        }
        try{
            response = menuService.findAll(jwt);
            Map<String,Object> map = (Map<String, Object>) response.getBody();
            Map<String,Object> mapData = (Map<String, Object>) map.get("data");
            GlobalFunction.setPagingElement(model,mapData,"menu",filterColumn);
            GlobalFunction.setGlobalAttribute(model,request,"MENU");
        }catch (Exception e){
            return "redirect:/3314&5";
        }
        if(err.equals("500")){
            err = "Internal Server Error (500)";
        }
        model.addAttribute("errorInternalServer",err);
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
            response = menuService.findByParam(jwt,sort,sortBy,page,size,column,value);
            Map<String,Object> map = (Map<String, Object>) response.getBody();
            Map<String,Object> mapData = (Map<String, Object>) map.get("data");
            GlobalFunction.setPagingElement(model,mapData,"menu",filterColumn);
            GlobalFunction.setGlobalAttribute(model,request,"MENU");
        }catch (Exception e){
            return "redirect:/3314&5";
        }
        return "/main";
    }

    @GetMapping("/excel")
    public ResponseEntity<Object> downloadExcel(
            Model model,
            @RequestParam(value = "column") String column,
            @RequestParam(value = "value") String value,
            WebRequest request
    ){
        ByteArrayResource resource =null;
        Response response = null;
        String fileName = "";
        String jwt = GlobalFunction.tokenCheck(model, request);
        if(jwt.equals("redirect:/774$_3")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resource);
        }
        try{
            response = menuService.downloadExcel(jwt,column,value);
            fileName = response.headers().get("Content-Disposition").toString();
            InputStream inputStream = response.body().asInputStream();
            resource = new ByteArrayResource(IOUtils.toByteArray(inputStream));
        }catch (Exception e){
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Disposition",fileName.substring(0,fileName.length()-1));

        return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")).
                body(resource);
    }

    @GetMapping("/pdf")
    public ResponseEntity<Object> downloadPdf(
            Model model,
            @RequestParam(value = "column") String column,
            @RequestParam(value = "value") String value,
            WebRequest request
    ){
        ByteArrayResource resource =null;
        Response response = null;
        String fileName = "";
        String jwt = GlobalFunction.tokenCheck(model, request);
        if(jwt.equals("redirect:/774$_3")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resource);
        }
        try{
            response = menuService.downloadPdf(jwt,column,value);
            fileName = response.headers().get("Content-Disposition").toString();
            InputStream inputStream = response.body().asInputStream();
            resource = new ByteArrayResource(IOUtils.toByteArray(inputStream));
        }catch (Exception e){
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Disposition",fileName.substring(0,fileName.length()-1));

        return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/pdf")).
                body(resource);
    }

    @GetMapping("/a")
    public String openModalsAdd(
            Model model,
            WebRequest request){
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, request);
        if(jwt.equals("redirect:/774$_3")){
            return jwt;
        }
        model.addAttribute("data",new ResMenuDTO());
        return "/menu/add";
    }

    @PostMapping("")
    public String save(
            @ModelAttribute("data") @Valid ValMenuDTO valMenuDTO,
            BindingResult bindingResult,
            Model model,
            WebRequest request){

        if(bindingResult.hasErrors()){
            model.addAttribute("data",valMenuDTO);
            return "/menu/add";
        }

        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, request);
        if(jwt.equals("redirect:/774$_3")){
            return jwt;
        }

        try{
            response = menuService.save(jwt,valMenuDTO);
        }catch (Exception e){
            model.addAttribute("data",valMenuDTO);
            return "/menu/add";
        }
        return "form-success";
    }

    @GetMapping("/e/{id}")
    public String openModalsEdit(
            Model model,
            @PathVariable Long id,
            WebRequest request){
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, request);
        Map<String,Object> map = null;
        Map<String,Object> mapData = null;
        if(jwt.equals("redirect:/774$_3")){
            return jwt;
        }
        try{
            response = menuService.findById(jwt,id);
            map = (Map<String, Object>) response.getBody();
            mapData = (Map<String, Object>) map.get("data");

        }catch (Exception e){
            return "/menu/edit";
        }
        model.addAttribute("data",new ObjectMapper().convertValue(mapData,ResMenuDTO.class));
        return "/menu/edit";
    }

    @PostMapping("/{id}")
    public String update(
            @ModelAttribute("data") @Valid ValMenuDTO valMenuDTO,
            BindingResult bindingResult,
            Model model,
            @PathVariable Long id,
            WebRequest request){

        if(bindingResult.hasErrors()){
            valMenuDTO.setId(id);
            model.addAttribute("data",valMenuDTO);
            return "/menu/edit";
        }

        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, request);
        if(jwt.equals("redirect:/774$_3")){
            return jwt;
        }

        try{
            response = menuService.update(jwt,valMenuDTO,id);
        }catch (Exception e){
            System.out.println("error : "+e.getMessage());
            valMenuDTO.setId(id);
            model.addAttribute("data",valMenuDTO);
            return "/menu/edit";
        }
        return "form-success";
    }

    @PostMapping("/d/{id}")
    public String delete(
            Model model,
            @PathVariable Long id,
            WebRequest request){

        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, request);
        if(jwt.equals("redirect:/774$_3")){
            return jwt;
        }

        try{
//            int z = 1/0;
            response = menuService.delete(jwt,id);
        }catch (Exception e){
            return "/form-error";
        }
        return "/form-success";
    }
}