/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.example.program;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


/**
 *
 * @author Rahim
 */
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User>findByUsername(String username);
}
