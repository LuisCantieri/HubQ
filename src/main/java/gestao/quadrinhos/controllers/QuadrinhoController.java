package gestao.quadrinhos.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gestao.quadrinhos.entities.Quadrinho;
import gestao.quadrinhos.services.QuadrinhoService;

@RestController
@RequestMapping("/api/quadrinho")

public class QuadrinhoController {

    private final QuadrinhoService service;

    public QuadrinhoController(QuadrinhoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Quadrinho> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Quadrinho buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public Quadrinho criar(@RequestBody Quadrinho quadrinho) {
        return service.salvar(quadrinho);
    }

    @PutMapping("/{id}")
    public Quadrinho atualizar(@PathVariable Long id, @RequestBody Quadrinho quadrinho) {
        quadrinho.setId(id);
        return service.salvar(quadrinho);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            service.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar quadrinho: " + e.getMessage());
        }
    }

}
