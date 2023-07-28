package org.Stasy.PublicPrivacyAppBackendHeroku.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.Stasy.PublicPrivacyAppBackendHeroku.EmailValidation.Entity.ConfirmationToken2;
import org.Stasy.PublicPrivacyAppBackendHeroku.EmailValidation.Repository.ConfirmationTokenRepository2;
import org.Stasy.PublicPrivacyAppBackendHeroku.EmailValidation.Repository.UserRepository2;
import org.Stasy.PublicPrivacyAppBackendHeroku.EmailValidation.Service.EmailService2;
import org.Stasy.PublicPrivacyAppBackendHeroku.EmailValidation.Service.UserService2;
import org.Stasy.PublicPrivacyAppBackendHeroku.JWT.JwtDecoder;
import org.Stasy.PublicPrivacyAppBackendHeroku.JWT.JwtGenerator;
import org.Stasy.PublicPrivacyAppBackendHeroku.JWT.JwtGeneratorInterface;
import org.Stasy.PublicPrivacyAppBackendHeroku.entity.model.ResetPasswordRequest;
import org.Stasy.PublicPrivacyAppBackendHeroku.exception.UserAlreadyExistsException;
import org.Stasy.PublicPrivacyAppBackendHeroku.exception.UserNotFoundException;
import org.Stasy.PublicPrivacyAppBackendHeroku.entity.User;
import org.Stasy.PublicPrivacyAppBackendHeroku.repository.OpinionsRepository;
import org.Stasy.PublicPrivacyAppBackendHeroku.service.UserServiceInterface;
import org.Stasy.PublicPrivacyAppBackendHeroku.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.io.UnsupportedEncodingException;

import java.util.Objects;

import org.springframework.web.bind.annotation.CookieValue;

import static org.Stasy.PublicPrivacyAppBackendHeroku.service.passwordVerifier.verifyPassword;

@RestController
@RequestMapping(value="/collaborator")
public class UserController {

    private final JwtGenerator jwtGenerator;
    private final UserServiceImpl userServiceImpl;
    private final UserServiceInterface userServiceInterface;
    private final JwtGeneratorInterface jwtGeneratorInterface;
    private final JwtDecoder jwtDecoder;

    private final OpinionsRepository opinionsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    ConfirmationTokenRepository2 confirmationTokenRepository2;
    @Autowired
    EmailService2 emailService2;
    @Autowired
    UserService2 userService2;
    @Autowired
    UserRepository2 userRepository2;

    //constructor
    @Autowired
    public UserController(JwtGenerator jwtGenerator, UserServiceImpl userServiceImpl, UserServiceInterface userServiceInterface, JwtGeneratorInterface jwtGeneratorInterface, JwtDecoder jwtDecoder, OpinionsRepository opinionsRepository) {
        this.jwtGenerator = jwtGenerator;
        this.userServiceImpl = userServiceImpl;
        this.userServiceInterface = userServiceInterface;
        this.jwtGeneratorInterface = jwtGeneratorInterface;
        this.jwtDecoder=jwtDecoder;
        this.opinionsRepository=opinionsRepository;
    }

    //all methods
    @PutMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@CookieValue(name = "loginToken", required = true) String loginToken,@RequestBody ResetPasswordRequest payload,HttpServletResponse response) throws UserNotFoundException {


        String email = payload.getEmail();
        String newPassword = payload.getNewPassword();
        String newHashedPassword=passwordEncoder.encode(newPassword);
        User user_db=userServiceImpl.findUserByEmail(email);
        if (user_db.isEnabled()==false){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();//403
        }
        System.out.println(Objects.equals(newHashedPassword,user_db.getPassword()));

        user_db.setPassword(newHashedPassword);
        userServiceInterface.save(user_db);//This is super important!

        user_db.getPassword();
        //now the new password is hashed.

        System.out.println(user_db.getPassword());

        //make new cookie
        try {
            loginToken = jwtGenerator.generateLoginToken(user_db);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.RESET_CONTENT).build();//204
        }
        Cookie loginCookie = new Cookie("loginToken", loginToken);//because in JwtGeneratorImpl line 34, we call "jwtToken"
        System.out.println("Hi, this loginToken is printing from UserController:" + loginToken);

        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE,OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "origin, content-type, accept");
        // response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        //response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Expose-Headers", "loginToken");
        response.addHeader("Access-Control-Max-Age", "3600");

        return ResponseEntity.ok("Password has been reset successfully");
    }

    @Transactional
    @DeleteMapping("/deleteAccount")
    public ResponseEntity<?> deleteAccount(@CookieValue(name = "loginToken", required = true) String loginToken,@CookieValue(name = "dashboardToken", required = true) String dashboardToken,@RequestParam("username") String username, HttpServletResponse response)
    {
        User user= null;


        try {
            user=userServiceImpl.findUserByUsername(username);

            if (user.isEnabled()==false){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();//403
            }
            userServiceImpl.deleteByUsername(username);

            User user1=userServiceImpl.findUserByUsername(username);


        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }finally {

            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE,OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "origin, content-type, accept");
            //response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
            //response.addHeader("Access-Control-Allow-Credentials", "true");
            response.addHeader("Access-Control-Expose-Headers", "loginToken");
            response.addHeader("Access-Control-Max-Age", "3600");

            user=null;
//                loginToken = jwtGenerator.generateLoginToken(user);
//
//                Cookie loginCookie = new Cookie("loginToken", loginToken);//because in JwtGeneratorImpl line 34, we call "jwtToken"
//                System.out.println("Hi, this loginToken is printing from UserController:" + loginToken);

            return ResponseEntity.status(HttpStatus.OK).build();//200

        }
    }



    @PostMapping("/login") //when login, we are typing email and password
    public ResponseEntity<?> loginUser(@RequestBody User user, HttpServletResponse response) throws UserNotFoundException
    {
        boolean isExist = false;
        boolean isActivated=false;
        String loginToken=null;
        try {

            String email = user.getEmail();//this cannot be directly reflected upon deleting. I can still fetch ID.
            String enteredPassword = user.getPassword();//這應該是從request拿出來的吧
            User user_db = userServiceImpl.findUserByEmail(email); // Retrieve the hashed password from the backend based on the user's email

            if (user_db.isEnabled() == false) {
                isActivated = false;
//
            } else {
                isActivated = true;
            }
            String hashedPassword = user_db.getPassword();

            try {
                if (Objects.equals((userServiceImpl.findUserByEmail(user.getEmail())), null)) {
                    isExist = false;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                isExist = false;
            }

            try {
                if (verifyPassword(enteredPassword, user_db.getPassword())) {
                    isExist = true;

                    response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE,OPTIONS");
                    response.setHeader("Access-Control-Allow-Headers", "origin, content-type, accept");
                    //response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
                    //response.addHeader("Access-Control-Allow-Credentials", "true");
                    response.addHeader("Access-Control-Expose-Headers", "loginToken");
                    response.addHeader("Access-Control-Max-Age", "3600");

                    //是這裡直接設定jwtToken的

                    try {
                        loginToken = jwtGenerator.generateLoginToken(user_db);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Cookie loginCookie = new Cookie("loginToken", loginToken);//because in JwtGeneratorImpl line 34, we call "jwtToken"
                    System.out.println("Hi, this loginToken is printing from UserController:" + loginToken);

                    loginCookie.setHttpOnly(true);
                    loginCookie.setMaxAge(3600); //an hour
                    response.setContentType("application/json");
                    response.addCookie(loginCookie);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//
        }finally {
            if (!isExist || !isActivated) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().body(loginToken);
    }
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user, HttpServletResponse response) throws UserNotFoundException, UserAlreadyExistsException
    {
        boolean isSaved=false;

        if(!Objects.equals(userServiceInterface.findUserByUsername(user.getUsername()),null)){
            return new ResponseEntity<>("User Already Exists", HttpStatus.CONFLICT);//409
        }
        if(!Objects.equals(userServiceInterface.findUserByEmail(user.getEmail()),null)){
            return new ResponseEntity<>("User Already Exists", HttpStatus.CONFLICT);//409
        }
        if(Objects.equals(user.getUsername(),null) || Objects.equals(user.getEmail(),null) || Objects.equals(user.getPassword(),null)){
            return new ResponseEntity<>("The fields are required to be filled", HttpStatus.CONFLICT);
        }

        try{
                String password=user.getPassword();
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                System.out.println(user.getPassword());

                userServiceInterface.save(user);
                isSaved=true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);//500
        }finally {
            if(isSaved){
                response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE,OPTIONS");
                response.setHeader("Access-Control-Allow-Headers", "origin, content-type, accept");

                 ///send verification token
                ////add the confirmationToken part
                ConfirmationToken2 confirmationToken = new ConfirmationToken2(user);

                confirmationTokenRepository2.save(confirmationToken);

                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(user.getEmail());
                mailMessage.setSubject("Complete Registration!");
                mailMessage.setText("To confirm your account, please click here : "
                        +"http://localhost:3000/collaborator/confirm-account?token="+confirmationToken.getConfirmationToken());
                mailMessage.setFrom("noreply@baeldung.com");
                emailService2.sendEmail(mailMessage);

                System.out.println("Confirmation Token: " + confirmationToken.getConfirmationToken());

                return ResponseEntity.status(HttpStatus.CREATED).build();//201
            }
            else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); //500
            }
        }
    }

    ///active the account
    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> confirmUserAccount(@RequestParam("token")String confirmationToken) {
        //this is the part of confirm email
        ConfirmationToken2 token = confirmationTokenRepository2.findByConfirmationToken(confirmationToken);
        if(token != null)
        {
            User user = userRepository2.findByEmailIgnoreCase(token.getUserEntity().getEmail());
            user.setEnabled(true);
            userRepository2.save(user);

            String message = "Email verified successfully! Head to homepage: " + "https://th-hsieh.github.io";
            return ResponseEntity.ok(message);
        }
        return ResponseEntity.badRequest().body("Error: Couldn't verify email. Please try again with the address.");
    }

    @GetMapping("/dashboard")
    public ResponseEntity <?> getUserInfo(@CookieValue(name = "loginToken", required = true) String loginToken, HttpServletResponse response) throws UserNotFoundException
    {
        boolean isEntered = false;
        boolean isActivated=false;
        if (Objects.equals(jwtDecoder.decodeUserEmailFromLoginToken(loginToken), null)) {//true
            isEntered = false;
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);//403
        }
        if (Objects.equals(loginToken, null)) {
            isEntered = false;
            return new ResponseEntity<>("Login Token is required.", HttpStatus.RESET_CONTENT);//205
        }
        if (Objects.equals(userServiceImpl.findUserByEmail(jwtDecoder.decodeUserEmailFromLoginToken(loginToken)), null))
        {
            isEntered = false;
            return new ResponseEntity<>("The Input information from login Token might be incorrect.", HttpStatus.RESET_CONTENT);//205
        }

        try {
            isEntered = true;
            String email = jwtDecoder.decodeUserEmailFromLoginToken(loginToken);
            User user = userServiceImpl.findUserByEmail(email);
            if(user.isEnabled()==false){
                isActivated=false;
            }else{
                isActivated=true;
            }
            System.out.println("User at /dashboard:" + user.toString());//WTF,竟然是username=email?

        } finally {
                if (isEntered && isActivated) {

                    String email = jwtDecoder.decodeUserEmailFromLoginToken(loginToken);
                    User user = userServiceImpl.findUserByEmail(email);
                    System.out.println("User at /dashboard:" + user.toString());//WTF,竟然是username=email?

                    response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE,OPTIONS");
                    response.setHeader("Access-Control-Allow-Headers", "origin, content-type, accept");
                    //response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
                    //response.addHeader("Access-Control-Allow-Credentials", "true");
                    response.addHeader("Access-Control-Expose-Headers", "dashboardToken");
                    response.addHeader("Access-Control-Max-Age", "3600");

                    String dashboardToken = jwtGenerator.generateDashboardToken(user);
                    Cookie dashboardCookie = new Cookie("dashboardToken", dashboardToken);
                    System.out.println("dashboardToken:" + dashboardToken);
                    dashboardCookie.setHttpOnly(true);
                    dashboardCookie.setMaxAge(3600); //an hour
                    response.setContentType("application/json");
                    response.addCookie(dashboardCookie);
                    return ResponseEntity.ok().body(dashboardToken);
                } else {
                    return ResponseEntity.status(HttpStatus.RESET_CONTENT).build(); //205
                }
        }
    }
}
