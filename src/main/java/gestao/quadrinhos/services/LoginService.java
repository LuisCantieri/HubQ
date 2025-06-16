// src/main/java/gestao/quadrinhos/services/LoginService.java
package gestao.quadrinhos.services;

import java.sql.Date;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gestao.quadrinhos.dto.AuthenticationDTO;
import gestao.quadrinhos.dto.LoginResponseDTO;
import gestao.quadrinhos.dto.RegisterDTO;
import gestao.quadrinhos.dto.UserDTO;
import gestao.quadrinhos.entities.User;
import gestao.quadrinhos.repositories.UserRepository;

@Service
public class LoginService implements UserDetailsService {

    private final ApplicationContext context;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public LoginService(ApplicationContext context,
                        UserRepository userRepository,
                        TokenService tokenService) {
        this.context = context;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + email));
    }

    @Transactional
    public ResponseEntity<Object> login(AuthenticationDTO data) {
        AuthenticationManager authManager = context.getBean(AuthenticationManager.class);
        var auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword())
        );
        User user = (User) auth.getPrincipal();
        String token = tokenService.generateToken(user);
        return ResponseEntity.ok(new LoginResponseDTO(token, new UserDTO(user)));
    }

    @Transactional
    public ResponseEntity<Object> register(RegisterDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email já está em uso");
        }
        String hashed = new BCryptPasswordEncoder().encode(dto.getPassword());
        User newUser = new User(dto.getEmail(), hashed, dto.getNome(), dto.getRole());
        newUser.setCreatedAt(new Date(System.currentTimeMillis()));
        User saved = userRepository.save(newUser);
        return ResponseEntity.ok(new UserDTO(saved));
    }
}
