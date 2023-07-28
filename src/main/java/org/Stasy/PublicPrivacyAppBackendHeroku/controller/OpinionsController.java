package org.Stasy.PublicPrivacyAppBackendHeroku.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.Stasy.PublicPrivacyAppBackendHeroku.JWT.JwtDecoder;
import org.Stasy.PublicPrivacyAppBackendHeroku.JWT.JwtGenerator;
import org.Stasy.PublicPrivacyAppBackendHeroku.entity.Opinion;
import org.Stasy.PublicPrivacyAppBackendHeroku.entity.User;
import org.Stasy.PublicPrivacyAppBackendHeroku.exception.UserNotFoundException;
import org.Stasy.PublicPrivacyAppBackendHeroku.repository.OpinionsRepository;
import org.Stasy.PublicPrivacyAppBackendHeroku.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/forum")
public class OpinionsController {

    private final JwtGenerator jwtGenerator;
    private final JwtDecoder jwtDecoder;
    private final UserServiceImpl userServiceImpl;
    private final OpinionsRepository opinionsRepository;

    @Autowired
    //constructor
    public OpinionsController(JwtGenerator jwtGenerator, JwtDecoder jwtDecoder, UserServiceImpl userServiceImpl, OpinionsRepository opinionsRepository) {
        this.jwtGenerator = jwtGenerator;
        this.jwtDecoder = jwtDecoder;
        this.userServiceImpl = userServiceImpl;
        this.opinionsRepository = opinionsRepository;
    }

    //simply testing
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    //view all opinions
    @GetMapping("/opinions")
    public ResponseEntity<?> readOpinions(@CookieValue(name = "loginToken", required = true) String loginToken, HttpServletResponse response) throws UserNotFoundException {
        System.out.println("no turning back");
        boolean is200=false;
        boolean is403=false;
        boolean is500=false;
        try {
            String email = jwtDecoder.decodeUserEmailFromLoginToken(loginToken);
            User user = userServiceImpl.findUserByEmail(email);

            if(!Objects.equals(user,null) && !Objects.equals(email,null)){
                is200=true;
            }
        } catch (UserNotFoundException e) {
            is403=true;
        } catch (Exception e) {
            is200=false;
            e.printStackTrace();
            //return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }finally {
            if(is200==true){
                response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE,OPTIONS");
                response.setHeader("Access-Control-Allow-Headers", "origin, content-type, accept");
                response.addHeader("Access-Control-Expose-Headers", "loginToken");
                response.addHeader("Access-Control-Max-Age", "3600");

                return new ResponseEntity<List<Opinion>>(opinionsRepository.findAll(), HttpStatus.OK);
            }else if(is403){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The Collaborator doesn't exist in the Database. Please contact us.");
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
            }
        }
    }

    //read an opinion by ID
    //public ResponseEntity<Opinion> readOpinion(@PathVariable Long id){
    @GetMapping("/opinions/{id}")
    public ResponseEntity<?> readOpinion(@CookieValue(name = "loginToken", required = true) String loginToken, @PathVariable Integer id, HttpServletResponse response) throws UserNotFoundException {
        Opinion opinion = opinionsRepository.findOpinionById(id);//this is the step where data loads. in put method)this cannot be directly reflected upon deleting. I can still fetch ID.
        boolean is200 = false;

        boolean is500 = false;
        boolean is204 = false;//deleted

        try {
            if (Objects.equals(opinion, null)) {
                is204 = true;
            }
        } catch (NullPointerException e) {
            System.out.println("A NullPointerException occurred: " + e.getMessage());
        }
        try {
            //verify if the data is really there
            if (!Objects.equals(id, null)) {

                String email = jwtDecoder.decodeUserEmailFromLoginToken(loginToken);
                System.out.println("email in readOpinions:" + email);


                is200 = true;
               // return new ResponseEntity<Opinion>(opinionsRepository.findOpinionById(id), HttpStatus.OK);
            } else {
                is204 = true;
                return new ResponseEntity<String>("This opinion was deleted.", HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            is500 = true;
            System.out.println("Oops, something went wrong. at getmapping opinions line 55");
            e.printStackTrace();
        } finally {
            if (is204) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Sorry,the content is deleted.");  ///Debug
            } else if (is500) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); //500
            }  else if (is200) {
                if (!Objects.equals(id, null)) {
                    response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE,OPTIONS");
                    response.setHeader("Access-Control-Allow-Headers", "origin, content-type, accept");
                    response.addHeader("Access-Control-Expose-Headers", "loginToken");

                    response.addHeader("Access-Control-Max-Age", "3600");
                }
                return new ResponseEntity<Opinion>(opinionsRepository.save(opinion), HttpStatus.OK);
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    //C==> create an opinion
    @PostMapping(value = "/opinions")
    public ResponseEntity<?> createOpinions(@RequestBody Opinion opinion, HttpServletResponse response) {
        boolean is201=false;
        boolean is205=false;
        User user=null;
        try {
            String collaboratorName=opinion.getCollaboratorName();
            user=userServiceImpl.findUserByUsername(collaboratorName);
            Opinion savedOpinion = opinionsRepository.save(opinion);
            System.out.println(savedOpinion);
            is201=true;
            System.out.println("Java 3, edit an opinion from PostMapping/opinions");
            System.out.println("Opinion Data before processing :" + savedOpinion.toString());//this is where things might go wrong

        } catch (UserNotFoundException e) {
            is201=false;
            is205=true;
            return new ResponseEntity<String>(HttpStatus.RESET_CONTENT);//205==>when to new, and when to not new?
        }finally {
            if(is201){
                response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE,OPTIONS");
                response.setHeader("Access-Control-Allow-Headers", "origin, content-type, accept");
                response.addHeader("Access-Control-Expose-Headers", "loginToken");
                response.addHeader("Access-Control-Expose-Headers", "addedListToken");
                response.addHeader("Access-Control-Max-Age", "3600");

                //add cookie now
                String dashboardToken = jwtGenerator.generateDashboardToken(user);
                Cookie Cookie=new Cookie("dashboardToken",dashboardToken);
                System.out.println("dashboardToken:"+dashboardToken);
                Cookie.setHttpOnly(true);
                Cookie.setMaxAge(3600); //an hour
                response.setContentType("application/json");
                response.addCookie(Cookie);

                return new ResponseEntity<String>(dashboardToken, HttpStatus.CREATED); //201
            }else if(is205){
                return new ResponseEntity<String>(HttpStatus.RESET_CONTENT);//205
            }else{
                return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);//500
            }
        }
    }

    //Update==>edit

    @PutMapping("/opinions")
    public ResponseEntity<?> updateOpinion(@RequestBody Opinion opinion, @CookieValue(name = "dashboardToken", required = true) String dashboardToken, HttpServletResponse response) throws UserNotFoundException
    {

        boolean is200=false;
        boolean is205=false;
        boolean is403=true;
        boolean is500=false;
        String username1=null;

        System.out.println("opinion.toString()");
        System.out.println(opinion.toString());

        try{
            username1 = jwtDecoder.decodeUserInfoFromDashboardToken(dashboardToken);
            Opinion existingOpinion = opinionsRepository.findOpinionById(opinion.getId());
            String username2 = existingOpinion.getCollaboratorName();//是用Opinion的ID找

            if(!userServiceImpl.findUserByUsername(username1).isEnabled() || !userServiceImpl.findUserByUsername(username2).isEnabled()){
                is403=false;
            }
            if (username1.equals(username2)) {
                is403=false;
                //return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only the initial author is able to modify the content.");
            }
            if(Objects.equals(opinion, null)){
                is205=true;
                //return ResponseEntity.status(HttpStatus.RESET_CONTENT).body("Please reset the content");
            }

            is200=true;
            opinionsRepository.save(opinion);

            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE,OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "origin, content-type, accept");
            response.addHeader("Access-Control-Expose-Headers", "loginToken");
            response.addHeader("Access-Control-Max-Age", "3600");

            System.out.println(opinion.toString());
           // return new ResponseEntity<List<Opinion>>(opinionsRepository.findOpinionByCollaboratorName(username1),HttpStatus.OK);
        }catch (Exception e){
            is500=true;
            e.printStackTrace();
            e.getMessage();
        }finally {
            if(is403){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only the initial author is able to modify the content.");
            }else if(is205){
                return ResponseEntity.status(HttpStatus.RESET_CONTENT).body("Please reset the content");
            }else if(is500){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }else if(is200){
                return new ResponseEntity<List<Opinion>>(opinionsRepository.findOpinionByCollaboratorName(username1),HttpStatus.OK);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    //delete
    @Transactional
    @DeleteMapping("/opinions/{id}")
    public ResponseEntity<?> deleteOpinion(@PathVariable Integer id, @CookieValue(name = "dashboardToken", required = true) String dashboardToken, HttpServletResponse response) throws UserNotFoundException{
        boolean is204=false;
        boolean is403=false;
        try {
            //verify the user identity first
            String username1 = jwtDecoder.decodeUserInfoFromDashboardToken(dashboardToken);//return name
            System.out.println("username1 in deleteOpinions:" + username1);

            Opinion opinion2 = opinionsRepository.findOpinionById(id);
            String username2 = opinion2.getCollaboratorName();

            if (!Objects.equals(username1, username2)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The Collaborator does not correspond with the author of the article in the Database. Please contact us.");
            }

            opinionsRepository.deleteById(id);
            is204=true;


        } catch (Exception e) {
            System.out.println("Oops, something went wrong. at putmapping opinions line 179");
            e.printStackTrace();
        }finally {
            if(is204){
                response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE,OPTIONS");
                response.setHeader("Access-Control-Allow-Headers", "origin, content-type, accept");
                response.addHeader("Access-Control-Expose-Headers", "loginToken");
                response.addHeader("Access-Control-Max-Age", "3600");

                System.out.println("Response Data after putmapping processing :" + response);

                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

            }else if(is403){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}