package com.audit.authentication.controller;

import com.audit.authentication.model.AuthResponse;
import com.audit.authentication.model.User;
import com.audit.authentication.model.UserRecord;
import com.audit.authentication.service.AuthService;
import com.audit.authentication.service.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.sql.SQLOutput;

@RestController
@CrossOrigin
public class AuthenticationController {

    @Autowired
    AuthService authService;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        System.out.println(user);
        UserDetails userRecord = authService.loadUserByUsername(user.getUserName());
        System.out.println("UserRecord " + userRecord);
        if(userRecord == null) {
            return new ResponseEntity<>("No User Found", HttpStatus.BAD_GATEWAY);
        }
        if (userRecord.getPassword().equalsIgnoreCase(user.getUserPassword())) {
            System.out.println("Inside ");
            String token = jwtUtil.generateToken(userRecord);
            UserRecord newUserRecord = new UserRecord(userRecord.getUsername(),
                    userRecord.getPassword(), token);
            authService.saveUser(newUserRecord);
            HttpHeaders responseHeaders = new HttpHeaders();
            return ResponseEntity.ok().headers(responseHeaders)
                    .body(newUserRecord);
        }
        return new ResponseEntity<>("", HttpStatus.BAD_GATEWAY);
    }

    @GetMapping(value = "/validate")
    public ResponseEntity<?> getValidity(@RequestHeader("Authorization") String token) {
        System.out.println(token);
        token = token.substring(7);
        AuthResponse res = new AuthResponse();
        ResponseEntity<?> response = null;
        try {
            if (jwtUtil.validateToken(token)) {

                res.setUid(jwtUtil.extractUsername(token));
                res.setValid(true);

            }
        } catch (Exception e) {
            res.setValid(false);
            System.out.println(e.getMessage());
            if (e.getMessage().contains("the token is expired and not valid anymore")) {
                response = new ResponseEntity<String>("the token is expired and not valid anymore",
                        HttpStatus.FORBIDDEN);
            }
            if (e.getMessage().contains("Authentication Failed. Username or Password not valid")) {
                response = new ResponseEntity<String>("Authentication Failed. Username or Password not valid",
                        HttpStatus.FORBIDDEN);
            }
            response = new ResponseEntity<>(res, HttpStatus.FORBIDDEN);
            return response;
        }
        response = new ResponseEntity<AuthResponse>(res, HttpStatus.OK);
        return response;

    }
    
    @GetMapping("/health")
	public ResponseEntity<?> getHealthCheck() {
		return ResponseEntity.ok().body("ok");
	}
}
