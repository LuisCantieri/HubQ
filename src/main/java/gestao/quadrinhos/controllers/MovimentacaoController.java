package gestao.quadrinhos.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import gestao.quadrinhos.dto.MovimentacaoDTO;
import gestao.quadrinhos.entities.StatusMovimentacao;
import gestao.quadrinhos.services.MovimentacaoService;

@RestController
@RequestMapping("/api/movimentacoes")

public class MovimentacaoController {

    @Autowired
    private MovimentacaoService movimentacaoService;

    @GetMapping
    public ResponseEntity<List<MovimentacaoDTO>> getAllMovimentacoes() {
        try {
            List<MovimentacaoDTO> movimentacoes = movimentacaoService.getAllMovimentacoes();
            return ResponseEntity.ok(movimentacoes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<MovimentacaoDTO>> getMovimentacoesByTipo(
            @PathVariable StatusMovimentacao tipo) {
        try {
            List<MovimentacaoDTO> movimentacoes = movimentacaoService.getMovimentacoesByTipo(tipo);
            return ResponseEntity.ok(movimentacoes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<MovimentacaoDTO>> getMovimentacoesByPeriodo(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {

        try {
            if (inicio == null) {
                inicio = LocalDateTime.now().minusDays(30);
            }
            if (fim == null) {
                fim = LocalDateTime.now();
            }

            List<MovimentacaoDTO> movimentacoes = movimentacaoService.getMovimentacoesByPeriodo(inicio, fim);
            return ResponseEntity.ok(movimentacoes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/inventario/{inventarioId}")
    public ResponseEntity<List<MovimentacaoDTO>> getMovimentacoesByInventario(
            @PathVariable("inventarioId") Long inventarioId) {
        try {
            List<MovimentacaoDTO> movimentacoes = movimentacaoService.getMovimentacoesByInventario(inventarioId);
            return ResponseEntity.ok(movimentacoes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
