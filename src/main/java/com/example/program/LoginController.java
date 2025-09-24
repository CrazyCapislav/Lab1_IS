package com.example.program;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/auth/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        Optional<User> userOpt = userRepo.findByUsername(username);
        if (userOpt.isEmpty()) return "redirect-error";
        
        User user = userOpt.get();
        if (!passwordEncoder.matches(password, user.getPassword())) return "redirect-error";
        
        session.setAttribute("loggedInUser", username);
        return "redirect:/data";
    }
    
    @PostMapping("/api/auth/login")
    @ResponseBody
    public String apiLogin(@RequestParam String username, @RequestParam String password) {
        Optional<User> userOpt = userRepo.findByUsername(username);
        if (userOpt.isEmpty()) return "{\"error\": \"Invalid credentials\"}";
        
        User user = userOpt.get();
        if (!passwordEncoder.matches(password, user.getPassword())) return "{\"error\": \"Invalid credentials\"}";
        
        String token = jwtUtil.generateToken(username);
        return "{\"token\": \"" + token + "\", \"username\": \"" + username + "\"}";
    }

    @PostMapping("/auth/register")
    public String register(@RequestParam String username, @RequestParam String password, Model model) {
        if (userRepo.findByUsername(username).isPresent()) {
            model.addAttribute("error", "UserExist");
            return "index";
        }
        
        User user = new User(username, passwordEncoder.encode(password));
        userRepo.save(user);
        model.addAttribute("message", "Registered");
        return "redirect-wait";
    }

    @DeleteMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) return "redirect:/";
        
        if (userRepo.existsById(id)) {
            userRepo.deleteById(id);
            session.setAttribute("loggedInUser", null);
            return "redirect:/?message=User deleted";
        } else {
            return "redirect:/?message=User not found";
        }
    }
}
