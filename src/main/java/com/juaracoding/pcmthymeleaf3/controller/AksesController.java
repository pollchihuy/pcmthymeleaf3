package com.juaracoding.pcmthymeleaf3.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.juaracoding.pcmthymeleaf3.dto.rel.RelMenuDTO;
import com.juaracoding.pcmthymeleaf3.dto.response.ResAksesDTO;
import com.juaracoding.pcmthymeleaf3.dto.validation.SelectAksesDTO;
import com.juaracoding.pcmthymeleaf3.dto.validation.SelectMenuDTO;
import com.juaracoding.pcmthymeleaf3.dto.validation.ValAksesDTO;
import com.juaracoding.pcmthymeleaf3.httpclient.AksesService;
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
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("akses")
public class AksesController {

    @Autowired
    private AksesService aksesService;
    private Map<String,Object> filterColumn = new HashMap<String,Object>();

    @Autowired
    private MenuService menuService;

    public AksesController() {
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
            response = aksesService.findAll(jwt);
            Map<String,Object> map = (Map<String, Object>) response.getBody();
            Map<String,Object> mapData = (Map<String, Object>) map.get("data");
            GlobalFunction.setPagingElement(model,mapData,"akses",filterColumn);
            GlobalFunction.setGlobalAttribute(model,request,"AKSES");
        }catch (Exception e){
            return "redirect:/3314&5";
        }
        return "main";
    }

    @GetMapping("err/{err}")
    public String defaultPageError(Model model,
                                   @PathVariable String err,
                                   WebRequest request){

        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, request);
        if(jwt.equals("redirect:/774$_3")){
            return jwt;
        }
        try{
            response = aksesService.findAll(jwt);
            Map<String,Object> map = (Map<String, Object>) response.getBody();
            Map<String,Object> mapData = (Map<String, Object>) map.get("data");
            GlobalFunction.setPagingElement(model,mapData,"akses",filterColumn);
            GlobalFunction.setGlobalAttribute(model,request,"Akses");
        }catch (Exception e){
            return GlobalFunction.errorHandleMainPage(e.getMessage(),"akses");
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
            response = aksesService.findByParam(jwt,sort,sortBy,page,size,column,value);
            Map<String,Object> map = (Map<String, Object>) response.getBody();
            Map<String,Object> mapData = (Map<String, Object>) map.get("data");
            GlobalFunction.setPagingElement(model,mapData,"akses",filterColumn);
            GlobalFunction.setGlobalAttribute(model,request,"AKSES");
        }catch (Exception e){
            return GlobalFunction.errorHandleMainPage(e.getMessage(),"akses");
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
            response = aksesService.downloadExcel(jwt,column,value);
            fileName = response.headers().get("Content-Disposition").toString();
            InputStream inputStream = response.body().asInputStream();
            resource = new ByteArrayResource(IOUtils.toByteArray(inputStream));
        }catch (Exception e){
            return GlobalFunction.errorHandleFile(e.getMessage(),"akses","xlsx");
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
            response = aksesService.downloadPdf(jwt,column,value);
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

    @GetMapping("a")
    public String openModalsAdd(
            Model model,
            WebRequest request){
        String jwt = GlobalFunction.tokenCheck(model, request);

        List<Map<String,Object>> listDataMenu = null;
        if(jwt.equals("redirect:/774$_3")){
            return jwt;
        }
        try{
            listDataMenu = getListMenu(menuService,jwt);
        }catch (Exception e){

        }
        model.addAttribute("data",new ResAksesDTO());
        model.addAttribute("x",listDataMenu);
        return "akses/add";
    }

    @PostMapping("")
    public String save(
            @ModelAttribute("data") @Valid SelectAksesDTO selectAksesDTO,
            BindingResult bindingResult,
            Model model,
            WebRequest request){
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, request);
        if(jwt.equals("redirect:/774$_3")){
            return jwt;
        }

        if(bindingResult.hasErrors()){
            model.addAttribute("data",selectAksesDTO);
            model.addAttribute("x",getListMenu(menuService,jwt));
            return "akses/add";
        }
        ValAksesDTO valAksesDTO = mapToValAksesDTO(selectAksesDTO);
        try{
            response = aksesService.save(jwt,valAksesDTO);
        }catch (Exception e){
            return GlobalFunction.errorHandleModals(e.getMessage());
        }
        return "status-200";
    }

    @GetMapping("e/{id}")
    public String openModalsEdit(
            Model model,
            @PathVariable Long id,
            WebRequest request){

        String jwt = GlobalFunction.tokenCheck(model, request);
        if(jwt.equals("redirect:/774$_3")){
            return jwt;
        }
        Map<String,Object> mapProcessing = new HashMap<>();
        try{
            mapProcessing = getDataEdit(menuService,aksesService,jwt,id);
        }catch (Exception e){
            model.addAttribute("id",id);
            return "akses/edit";
        }

        model.addAttribute("data",mapProcessing.get("data"));
        model.addAttribute("id",id);
        model.addAttribute("menuSelected", mapProcessing.get("menuSelected"));
        model.addAttribute("listMenu", mapProcessing.get("listAllMenu"));

        return "akses/edit";
    }



    @PostMapping("{id}")
    public String update(
            @ModelAttribute("data") @Valid SelectAksesDTO selectAksesDTO,
            BindingResult bindingResult,
            Model model,
            @PathVariable Long id,
            WebRequest request){


        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, request);
        if(jwt.equals("redirect:/774$_3")){
            return "error-401";
        }

        if(bindingResult.hasErrors()){
            Map<String,Object> mapProcessing = getDataEdit(menuService,aksesService,jwt,id);
            model.addAttribute("data",selectAksesDTO);
            model.addAttribute("id",id);
            model.addAttribute("menuSelected", mapProcessing.get("menuSelected"));
            model.addAttribute("listMenu", mapProcessing.get("listAllMenu"));
            return "akses/edit";
        }

        try{
            response = aksesService.update(jwt,mapToValAksesDTO(selectAksesDTO),id);
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
            response = aksesService.delete(jwt,id);
        }catch (Exception e){
            return "error-400";
        }
        return "status-200";
    }

    private ValAksesDTO mapToValAksesDTO(SelectAksesDTO selectAksesDTO){
        ValAksesDTO valAksesDTO = new ValAksesDTO();
        valAksesDTO.setDeskripsi(selectAksesDTO.getDeskripsi());
        valAksesDTO.setNama(selectAksesDTO.getNama());
        List<String> lt = selectAksesDTO.getListMenu();
        List<RelMenuDTO> listRelMenuDTO = new ArrayList<>();
        for(int i=0;i<lt.size();i++){
            RelMenuDTO relMenuDTO = new RelMenuDTO();
            relMenuDTO.setId(Long.parseLong(lt.get(i)));
            listRelMenuDTO.add(relMenuDTO);
        }
        valAksesDTO.setListMenu(listRelMenuDTO);
        return valAksesDTO;
    }

    public List<Map<String,Object>> getListMenu(MenuService menuService,String jwt){
        ResponseEntity<Object> response = menuService.findAll(jwt);
        Map<String,Object> mapMenu =  (Map<String, Object>) response.getBody();
        Map<String,Object> mapDataMenu =(Map<String, Object>) mapMenu.get("data");
        return  (List<Map<String,Object>>) mapDataMenu.get("content");
    }

    public List<SelectMenuDTO> getAllMenu(List<Map<String,Object>> ltMenu){
        List<SelectMenuDTO> selectMenuDTOS = new ArrayList<>();
        SelectMenuDTO selectMenuDTO = null;
        for(Map<String,Object> menu:ltMenu){
            selectMenuDTO = new SelectMenuDTO();
            selectMenuDTO.setId(Long.parseLong(menu.get("id").toString()));
            selectMenuDTO.setNama(menu.get("nama").toString());
            selectMenuDTOS.add(selectMenuDTO);
        }
        return selectMenuDTOS;
    }

    /** seluruh processing mapping data pada saat proses Edit untuk relasi many to many ada di function ini */
    private Map<String,Object> getDataEdit(MenuService menuService,AksesService aksesService,String jwt,Long id){
        ResponseEntity<Object> response = null;
        Map<String,Object> map = null;
        Map<String,Object> mapData = null;
        List<Map<String,Object>> listAksesMenu = null;
        List<Map<String,Object>> listMenu = null;

        List<SelectMenuDTO> listAllMenu = null;
        Set<Long> menuSelected = null;
        List<SelectMenuDTO> selectedMenuDTO = new ArrayList<>();
        response = aksesService.findById(jwt,id);
        listMenu = getListMenu(menuService,jwt);
        map = (Map<String, Object>) response.getBody();
        mapData = (Map<String, Object>) map.get("data");
        listAksesMenu = (List<Map<String, Object>>) mapData.get("list-menu");

        listAllMenu = getAllMenu(listMenu);
        for (SelectMenuDTO menu : listAllMenu) {
            for(Map<String,Object> m:listAksesMenu){
                Long idMenu = menu.getId();
                Long idAksesMenu =Long.parseLong(m.get("id").toString());
                if(idMenu==idAksesMenu){
                    selectedMenuDTO.add(menu);
                    break;
                }
            }
        }
        menuSelected = selectedMenuDTO.stream().map(SelectMenuDTO::getId).collect(Collectors.toSet());
        Map<String, Object> mapReturn = new HashMap<>();
        mapReturn.put("menuSelected",menuSelected);
        mapReturn.put("data",new ObjectMapper().convertValue(mapData,ResAksesDTO.class));
        mapReturn.put("listAllMenu",listAllMenu);

        return mapReturn;
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
            response = aksesService.findByParam(jwt,sort,sortBy,page,0,size,column,value);
            map = (Map<String, Object>) response.getBody();
            mapData = (Map<String, Object>) map.get("data");
        }catch (Exception e){
            model.addAttribute("idComp", idComp);
            model.addAttribute("descComp",descComp);
            return "global/data-table-form";
        }

        GlobalFunction.setPagingElement(model,mapData,"akses",filterColumn);
        GlobalFunction.setGlobalAttribute(model,request,"AKSES");
        model.addAttribute("idComp", idComp);
        model.addAttribute("descComp",descComp);
        return "global/data-table-form";
    }
}