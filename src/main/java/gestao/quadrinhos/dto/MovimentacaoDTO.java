package gestao.quadrinhos.dto;

import java.util.Date;
import java.util.UUID;

import gestao.quadrinhos.entities.Movimentacao;
import gestao.quadrinhos.entities.StatusMovimentacao;

public class MovimentacaoDTO {
    
    private UUID id;
    private QuadrinhoDTO quadrinho;
    private StatusMovimentacao tipo;
    private Integer quantidade;
    private Date dataMovimentacao;
    private String descricao;

    public MovimentacaoDTO() {}

    public MovimentacaoDTO(Movimentacao movimentacao) {
        this.id = movimentacao.getId();
        this.quadrinho = new QuadrinhoDTO(movimentacao.getQuadrinho());
        this.tipo = movimentacao.getTipo();
        this.quantidade = movimentacao.getQuantidade();
        this.dataMovimentacao = movimentacao.getDataMovimentacao();
        this.descricao = movimentacao.getDescricao();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public QuadrinhoDTO getQuadrinho() { return quadrinho; }
    public void setQuadrinho(QuadrinhoDTO quadrinho) { this.quadrinho = quadrinho; }
    
    public StatusMovimentacao getTipo() { return tipo; }
    public void setTipo(StatusMovimentacao tipo) { this.tipo = tipo; }
    
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    
    public Date getDataMovimentacao() { return dataMovimentacao; }
    public void setDataMovimentacao(Date dataMovimentacao) { this.dataMovimentacao = dataMovimentacao; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}