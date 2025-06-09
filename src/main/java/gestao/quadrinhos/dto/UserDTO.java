// src/main/java/gestao/quadrinhos/dto/UserDTO.java
package gestao.quadrinhos.dto;

import java.util.Date;
import gestao.quadrinhos.entities.StatusRole;
import gestao.quadrinhos.entities.User;

public class UserDTO {

    private Long id;
    private String email;
    private String nome;
    private StatusRole role;
    private Date createdAt;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nome = user.getNome();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public StatusRole getRole() {
        return role;
    }

    public void setRole(StatusRole role) {
        this.role = role;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
