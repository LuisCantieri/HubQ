// src/main/java/gestao/quadrinhos/dto/MovimentacaoDTO.java
package gestao.quadrinhos.dto;

import java.util.Date;
import java.util.UUID;

import gestao.quadrinhos.entities.Movimentacao;
import gestao.quadrinhos.entities.StatusMovimentacao;

public class MovimentacaoDTO {
    
    private UUID id;
    private QuadrinhoDTO quadrinho;
    private String tipo;
    private Integer quantidade;
    private Date dataMovimentacao;
    private String descricao;

    public MovimentacaoDTO() {}

    public MovimentacaoDTO(Movimentacao movimentacao) {
        this.id               = movimentacao.getId();
        this.quantidade       = movimentacao.getQuantidade();
        this.dataMovimentacao = movimentacao.getDataMovimentacao();
        this.descricao        = movimentacao.getDescricao();

        // protege contra quadrinho null
        if (movimentacao.getQuadrinho() != null) {
            this.quadrinho = new QuadrinhoDTO(movimentacao.getQuadrinho());
        } else {
            this.quadrinho = null;
        }

        // converte o enum em texto (entrada, saida, ajuste, transferencia)
        StatusMovimentacao status = movimentacao.getTipo();
        this.tipo = (status != null) ? status.getTipo() : null;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public QuadrinhoDTO getQuadrinho() {
        return quadrinho;
    }

    public void setQuadrinho(QuadrinhoDTO quadrinho) {
        this.quadrinho = quadrinho;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Date getDataMovimentacao() {
        return dataMovimentacao;
    }

    public void setDataMovimentacao(Date dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
