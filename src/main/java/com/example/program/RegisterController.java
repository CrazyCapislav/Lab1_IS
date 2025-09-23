/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.program;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Rahim
 */

@RestController
public class RegisterController {

     @Autowired
    private UserRepo userRepo;

    @PostMapping("/auth/register")
    public String register(
        @RequestParam String username,
        @RequestParam String password) {
        
        User user = new User(username, password);

        userRepo.save(user);
        return "Зарегистирован";
    }

}
