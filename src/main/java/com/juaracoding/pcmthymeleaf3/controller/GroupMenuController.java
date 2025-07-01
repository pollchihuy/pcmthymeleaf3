package com.juaracoding.pcmthymeleaf3.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.juaracoding.pcmthymeleaf3.dto.response.ResGroupMenuDTO;
import com.juaracoding.pcmthymeleaf3.dto.validation.ValGroupMenuDTO;
import com.juaracoding.pcmthymeleaf3.httpclient.GroupMenuService;
import com.juaracoding.pcmthymeleaf3.utils.GlobalFunction;
import feign.Response;
import jakarta.validation.Valid;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.io.InputStream;
import java.util.*;

@Controller
@RequestMapping("group-menu")
public class GroupMenuController {

    @Autowired
    private GroupMenuService groupMenuService;
    private Map<String,Object> filterColumn = new HashMap<String,Object>();

    public GroupMenuController() {
        filterColumn.put("nama","Nama");
        filterColumn.put("deskripsi","Deskripsi");
    }

    @GetMapping
    public String defaultPage(Model model, WebRequest request){

        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, request);
        if(jwt.equals("redirect:/774$_3")){
            return jwt;
        }
        try{
            response = groupMenuService.findAll(jwt);
            Map<String,Object> map = (Map<String, Object>) response.getBody();
            Map<String,Object> mapData = (Map<String, Object>) map.get("data");
            GlobalFunction.setPagingElement(model,mapData,"group-menu",filterColumn);
            GlobalFunction.setGlobalAttribute(model,request,"GROUP MENU");
        }catch (Exception e){
            return "redirect:/3314&5";
        }
        return "main";
    }

    @GetMapping("/{err}")
    public String defaultPageError(Model model,
                                   @PathVariable String err,
                                   WebRequest request){

        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, request);
        if(jwt.equals("redirect:/774$_3")){
            return jwt;
        }
        try{
            response = groupMenuService.findAll(jwt);
            Map<String,Object> map = (Map<String, Object>) response.getBody();
            Map<String,Object> mapData = (Map<String, Object>) map.get("data");
            GlobalFunction.setPagingElement(model,mapData,"group-menu",filterColumn);
            GlobalFunction.setGlobalAttribute(model,request,"GROUP MENU");
        }catch (Exception e){
            return GlobalFunction.errorHandleMainPage(e.getMessage(),"group-menu");
        }
        switch (err){
            case "400":err="DATA TIDAK DITEMUKAN (400)";break;
            case "error-pdf":err="GAGAL MENGUNDUH FILE PDF (500)";break;
            case "error-xlsx":err="GAGAL MENGUNDUH FILE EXCEL (500)";break;
            default:err="INTERNAL SERVER ERROR";break;
        }
        model.addAttribute("globalError",err);
        return "main";
    }

    @GetMapping("{sort}/{sortBy}/{page}")
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
            response = groupMenuService.findByParam(jwt,sort,sortBy,page,size,column,value);
            System.out.println("Error Nih !!");
            Map<String,Object> map = (Map<String, Object>) response.getBody();
            Map<String,Object> mapData = (Map<String, Object>) map.get("data");
            GlobalFunction.setPagingElement(model,mapData,"group-menu",filterColumn);
            GlobalFunction.setGlobalAttribute(model,request,"GROUP MENU");
        }catch (Exception e){
            return GlobalFunction.errorHandleMainPage(e.getMessage(),"group-menu");
        }
        return "main";
    }

    @GetMapping("excel")
    public Object downloadExcel(
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
            return "redirect:/774$_3";
        }
        try{
            response = groupMenuService.downloadExcel(jwt,column,value);
            fileName = response.headers().get("Content-Disposition").toString();
            InputStream inputStream = response.body().asInputStream();
            resource = new ByteArrayResource(IOUtils.toByteArray(inputStream));
        }catch (Exception e){
            return GlobalFunction.errorHandleFile(e.getMessage(),"group-menu","xlsx");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Disposition",fileName.substring(0,fileName.length()-1));

        return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")).
                body(resource);
    }

    @GetMapping("pdf")
    public Object downloadPdf(
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
            return "redirect:/774$_3";
        }
        try{
            response = groupMenuService.downloadPdf(jwt,column,value);
            fileName = response.headers().get("Content-Disposition").toString();
            InputStream inputStream = response.body().asInputStream();
            resource = new ByteArrayResource(IOUtils.toByteArray(inputStream));
        }catch (Exception e){
            return GlobalFunction.errorHandleFile(e.getMessage(),"group-menu","pdf");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Disposition",fileName.substring(0,fileName.length()-1));

        return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/pdf")).
                body(resource);
    }

    @GetMapping("a")
    public String openModalsAdd(
            Model model,
            WebRequest request){
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, request);
        if(jwt.equals("redirect:/774$_3")){
            return jwt;
        }
        model.addAttribute("data",new ResGroupMenuDTO());
        model.addAttribute("formAdd","cobaAddGroupMenuDoank");
        return "group-menu/add";
    }

    @PostMapping("")
    public String save(
            @ModelAttribute("data") @Valid ValGroupMenuDTO valGroupMenuDTO,
            BindingResult bindingResult,
            Model model,
            WebRequest request){
        if(bindingResult.hasErrors()){
            model.addAttribute("data",valGroupMenuDTO);
            return "group-menu/add";
        }

        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, request);
        if(jwt.equals("redirect:/774$_3")){
            return "error-401";
        }

        try{
            response = groupMenuService.save(jwt,valGroupMenuDTO);
        }catch (Exception e){
//            model.addAttribute("data",valGroupMenuDTO);
            return GlobalFunction.errorHandleModals(e.getMessage());
//            return "group-menu/add";
        }
        return "status-200";
    }

    @GetMapping("e/{id}")
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
            response = groupMenuService.findById(jwt,id);
            map = (Map<String, Object>) response.getBody();
            mapData = (Map<String, Object>) map.get("data");

        }catch (Exception e){
            return "group-menu/edit";
        }
        model.addAttribute("data",new ObjectMapper().convertValue(mapData,ResGroupMenuDTO.class));
        model.addAttribute("id",mapData.get("id"));

        return "group-menu/edit";
    }

    @PostMapping("{id}")
    public String update(
            @ModelAttribute("data") @Valid ValGroupMenuDTO valGroupMenuDTO,
            BindingResult bindingResult,
            Model model,
            @PathVariable Long id,
            WebRequest request){

        if(bindingResult.hasErrors()){
            model.addAttribute("data",valGroupMenuDTO);
            model.addAttribute("id",id);
            return "group-menu/edit";
        }

        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, request);
        if(jwt.equals("redirect:/774$_3")){
            return "error-401";
        }
        try{
            response = groupMenuService.update(jwt,valGroupMenuDTO,id);
        }catch (Exception e){
            return GlobalFunction.errorHandleModals(e.getMessage());
        }
        return "status-200";
    }

    @PostMapping("d/{id}")
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
            response = groupMenuService.delete(jwt,id);
        }catch (Exception e){
            return "error-400";
        }
        return "status-200";
    }

    @GetMapping("{idComp}/{descComp}/{sort}/{sortBy}/{page}")
    public String dataTable(Model model,
                            @PathVariable(value = "sort") String sort,
                            @PathVariable(value = "sortBy") String sortBy,//name
                            @PathVariable(value = "page") Integer page,
                            @RequestParam(value = "size") Integer size,
                            @RequestParam(value = "column") String column,
                            @RequestParam(value = "value") String value,
                            @PathVariable(value = "idComp") String idComp,
                            @PathVariable(value = "descComp") String descComp,
                            WebRequest request){
        ResponseEntity<Object> response = null;
        page = page!=0?(page-1):page;
        String jwt = GlobalFunction.tokenCheck(model, request);
        if(jwt.equals("redirect:/774$_3")){
            return jwt;
        }
        Map<String,Object> map = null;
        Map<String,Object> mapData = null;
        try{
            response = groupMenuService.findByParam(jwt,sort,sortBy,page,0,size,column,value);
            map = (Map<String, Object>) response.getBody();
            mapData = (Map<String, Object>) map.get("data");
        }catch (Exception e){
            model.addAttribute("idComp", idComp);
            model.addAttribute("descComp",descComp);
            return "global/data-table-form";
        }

        GlobalFunction.setPagingElement(model,mapData,"group-menu",filterColumn);
        GlobalFunction.setGlobalAttribute(model,request,"GROUP MENU");
        model.addAttribute("idComp", idComp);
        model.addAttribute("descComp",descComp);
        return "global/data-table-form";
    }
}