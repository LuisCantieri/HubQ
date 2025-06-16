// src/main/java/gestao/quadrinhos/controllers/LoginController.java
package gestao.quadrinhos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gestao.quadrinhos.dto.AuthenticationDTO;
import gestao.quadrinhos.dto.RegisterDTO;
import gestao.quadrinhos.services.LoginService;
import jakarta.validation.Valid;

@CrossOrigin(origins = "https://hub-q.vercel.app")
@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid AuthenticationDTO authenticationDTO) {
        return loginService.login(authenticationDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid RegisterDTO registerDTO) {
        return loginService.register(registerDTO);
    }
}
