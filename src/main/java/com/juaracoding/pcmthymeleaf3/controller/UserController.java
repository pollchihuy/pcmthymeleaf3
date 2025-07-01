package com.juaracoding.pcmthymeleaf3.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.juaracoding.pcmthymeleaf3.dto.response.ResUserDTO;
import com.juaracoding.pcmthymeleaf3.dto.validation.ValUserDTO;
import com.juaracoding.pcmthymeleaf3.httpclient.UserService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;
    private Map<String,Object> filterColumn = new HashMap<String,Object>();

    public UserController() {
        filterColumn.put("username","Username");
        filterColumn.put("email","Email");
        filterColumn.put("no-hp","No Handphone");
        filterColumn.put("nama-lengkap","Nama");
        filterColumn.put("alamat","Alamat");
        filterColumn.put("akses","Akses");
        filterColumn.put("umur","Umur");
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
            response = userService.findAll(jwt);
            Map<String,Object> map = (Map<String, Object>) response.getBody();
            Map<String,Object> mapData = (Map<String, Object>) map.get("data");
            GlobalFunction.setPagingElement(model,mapData,"user",filterColumn);
            GlobalFunction.setGlobalAttribute(model,request,"USER");
        }catch (Exception e){
            return "redirect:/3314&5";
        }
        if(err.equals("500")){
            err = "Internal Server Error (500)";
        }
        model.addAttribute("errorInternalServer",err);
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
            response = userService.findByParam(jwt,sort,sortBy,page,size,column,value);
            Map<String,Object> map = (Map<String, Object>) response.getBody();
            Map<String,Object> mapData = (Map<String, Object>) map.get("data");
            GlobalFunction.setPagingElement(model,mapData,"user",filterColumn);
            GlobalFunction.setGlobalAttribute(model,request,"USER");
        }catch (Exception e){
            return "redirect:/3314&5";
        }
        return "main";
    }

    @GetMapping("excel")
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
            response = userService.downloadExcel(jwt,column,value);
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

    @GetMapping("pdf")
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
            response = userService.downloadPdf(jwt,column,value);
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
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, request);
        if(jwt.equals("redirect:/774$_3")){
            return jwt;
        }
//        model.addAttribute("data",new ResUserDTO());
        return "user/add";
    }

//    @PostMapping("")
//    public String save(
//            @ModelAttribute("data") @Valid ValUserDTO valUserDTO,
//            @RequestParam MultipartFile file,
//            BindingResult bindingResult,
//            Model model,
//            WebRequest request){
//
//        if(bindingResult.hasErrors()){
//            model.addAttribute("data",valUserDTO);
//            return "user/add";
//        }
//
//        ResponseEntity<Object> response = null;
//        String jwt = GlobalFunction.tokenCheck(model, request);
//        if(jwt.equals("redirect:/774$_3")){
//            return jwt;
//        }
//        try{
////            response = userService.save(jwt,valUserDTO);
//            response = userService.save(jwt,valUserDTO,file);
//        }catch (Exception e){
//            System.out.println("error "+e.getMessage());
//            model.addAttribute("data",valUserDTO);
//            return "user/add";
//        }
//        return "form-success";
//    }


    private Boolean userFormValidation(Model model,String username,String password,
                                    String email,String namaLengkap,String alamat,
                                    String tanggalLahir,String idAkses,String noHp){
        Boolean isValid = false;
        /**jika ada 1 atau lebih field yang error
         karena validasi maka
         counter tidak menjadi 0 lagi dan itu menandakan
         request tidak dapat diteruskan ke rest api*/
        int intCounter = 0;

        intCounter = setErrorMessage(model,"usernameMessage",
                username,"Format Huruf kecil ,numeric dan titik saja min 8 max 16 karakter, ex : paulch.1234",
                "^([a-z0-9\\.]{8,16})$")?intCounter:1;
        intCounter = setErrorMessage(model,"passwordMessage",
                password,"Format minimal 1 angka, 1 huruf kecil, 1 huruf besar, 1 spesial karakter (_ \"Underscore\", - \"Hyphen\", # \"Hash\", atau $ \"Dollar\" atau @ \"At\") setelah 4 kondisi min 9 max 16 alfanumerik, contoh : aB4$12345",
                "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@_#\\-$])[\\w].{8,15}$")?intCounter:1;
        intCounter = setErrorMessage(model,
                "emailMessage",email,"Format tidak valid ex : user_name123@sub.domain.com",
                "^(?=.{1,256})(?=.{1,64}@.{1,255}$)(?:(?![.])[a-zA-Z0-9._%+-]+(?:(?<!\\\\)[.][a-zA-Z0-9-]+)*?)@[a-zA-Z0-9.-]+(?:\\.[a-zA-Z]{2,50})+$")?intCounter:intCounter++;
        intCounter = setErrorMessage(model,
                "noHpMessage",noHp,"Format No HP Tidak Valid , min 9 max 13 setelah angka 8, contoh : (0/62/+62)81111111",
                "^(62|\\+62|0)8[0-9]{9,13}$")?intCounter:1;
        intCounter = setErrorMessage(model,"namaLengkapMessage",
                namaLengkap ,"Hanya Alfabet dan spasi Minimal 4 Maksimal 70","^[a-zA-Z\\s]{4,70}$")?intCounter:1;
        intCounter = setErrorMessage(model,"alamatMessage",
                alamat,"Format Alamat Tidak Valid min 20 maks 255, contoh : Jln. Kenari 2B jakbar 11480",
                "^[\\w\\s\\.\\,]{20,255}$")?intCounter:1;
        intCounter = setErrorMessage(model,"tanggalLahirMessage",
                tanggalLahir,"Format Tanggal Lahir Tidak Sesuai Standar",
                "^(19|20)\\d{2}-(0[1-9]|1[1,2])-(0[1-9]|[12][0-9]|3[01])$")?intCounter:1;
        intCounter = setErrorMessage(model,
                "idAksesMessage",idAkses,"Akses Wajib Dipilih",
                "^[\\d]{1,5}$")?intCounter:1;
        if(intCounter==0){
            isValid = true;
        }
        return isValid;
    }

    private Boolean setErrorMessage(Model model,String fieldError, String value,
                                    String message,String regex){
        Boolean isValid = Pattern.compile(regex).matcher(value).find();
        if(!isValid){
            model.addAttribute(fieldError,message);//untuk mengisi pesan error dari field yang bermasalah
        }
        return isValid;
    }
    /** set nilai yang sudah di entry sebelumnya jika terjadi error */
    private void setValueToFormIfError(Model model,String username,String password,
                                       String email,String namaLengkap,String alamat,
                                       String tanggalLahir,String idAkses){
        model.addAttribute("username",username);
        model.addAttribute("password",password);
        model.addAttribute("email",email);
        model.addAttribute("namaLengkap",namaLengkap);
        model.addAttribute("alamat",alamat);
        model.addAttribute("tanggalLahir",tanggalLahir);
        model.addAttribute("idAkses",idAkses);
    }


    @PostMapping("")
    public String save(Model model,
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "namaLengkap") String namaLengkap,
            @RequestParam(value = "alamat") String alamat,
            @RequestParam(value = "tanggalLahir") String tanggalLahir,
            @RequestParam(value = "idAkses") String idAkses,
            @RequestParam(value = "noHp") String noHp,
            @RequestParam(value = "file") MultipartFile file,
            WebRequest request){

        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, request);
        if(jwt.equals("redirect:/774$_3")){
            return jwt;
        }
        if(!userFormValidation(model,username,password,email,namaLengkap,alamat,tanggalLahir,idAkses,noHp)){
            setValueToFormIfError(model,username,password,email,namaLengkap,alamat,tanggalLahir,idAkses);
            return "user/add";
        }
        try{

            response = userService.save(jwt,
                    username,password,email,namaLengkap,alamat,tanggalLahir,idAkses,noHp,
                    file);
        }catch (Exception e){
            System.out.println("error "+e.getMessage());
            setValueToFormIfError(model,username,password,email,namaLengkap,alamat,tanggalLahir,idAkses);
            return "user/add";
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
            response = userService.findById(jwt,id);
            map = (Map<String, Object>) response.getBody();
            mapData = (Map<String, Object>) map.get("data");

        }catch (Exception e){
            model.addAttribute("id",mapData.get("id"));
            return "user/edit";
        }
        model.addAttribute("data",new ObjectMapper().convertValue(mapData,ResUserDTO.class));
        model.addAttribute("id",id);
        return "user/edit";
    }

    @PostMapping("{id}")
    public String update(
            @ModelAttribute("data") @Valid ValUserDTO valUserDTO,
            BindingResult bindingResult,
            Model model,
            @PathVariable Long id,
            WebRequest request){

        if(bindingResult.hasErrors()){
            model.addAttribute("data",valUserDTO);
            model.addAttribute("id",id);
            return "user/edit";
        }

        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, request);
        if(jwt.equals("redirect:/774$_3")){
            return jwt;
        }

        try{
            response = userService.update(jwt,valUserDTO,id);
        }catch (Exception e){
            model.addAttribute("data",valUserDTO);
            model.addAttribute("id",id);
            return "user/edit";
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
//            int z = 1/0;
            response = userService.delete(jwt,id);
        }catch (Exception e){
            return "error-400";
        }
        return "status-200";
    }

    @PostMapping("files/upload/{username}")
    public String uploadImage(
            Model model,
            @PathVariable String username,
            @RequestParam MultipartFile file, WebRequest request) {
        ResponseEntity<Object> response = null;
        String jwt = GlobalFunction.tokenCheck(model, request);
        if (jwt.equals("redirect:/774$_3")) {
            return jwt;
        }
        try {
            response = userService.uploadImage(jwt,username, file);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "redirect:/home";
        }
        Map<String, Object> data = (Map<String, Object>) response.getBody();
        String urlImg = data.get("url-img").toString();
//        model.addAttribute("pesan","Data Berhasil Diubah");
        request.setAttribute("URL_IMG", urlImg, 1);
        GlobalFunction.setGlobalAttribute(model,request,"HOME");
        model.addAttribute("URL_IMG",urlImg);
        return "auth/home";
    }
}