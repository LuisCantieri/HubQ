// src/main/java/gestao/quadrinhos/dto/LoginResponseDTO.java
package gestao.quadrinhos.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Retorna token e informações do usuário autenticado.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponseDTO {

    private String token;
    private UserDTO user;

    public LoginResponseDTO() {
    }

    /** Construtor apenas com token */
    public LoginResponseDTO(String token) {
        this.token = token;
    }

    /** Construtor com token e dados do usuário */
    public LoginResponseDTO(String token, UserDTO user) {
        this.token = token;
        this.user = user;
    }

    // Getters e Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
