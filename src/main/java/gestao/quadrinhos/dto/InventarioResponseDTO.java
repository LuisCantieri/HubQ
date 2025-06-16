package gestao.quadrinhos.dto;

import java.math.BigDecimal;
import java.util.Date;

public class InventarioResponseDTO {

    private Long id;
    private Integer quantidade;
    private QuadrinhoDTO quadrinho;
    private BigDecimal valorTotal;
    private Date createdAt;
    private Date updatedAt;

    public InventarioResponseDTO(Long id,
                                 Integer quantidade,
                                 QuadrinhoDTO quadrinho,
                                 BigDecimal valorTotal,
                                 Date createdAt,
                                 Date updatedAt) {
        this.id = id;
        this.quantidade = quantidade;
        this.quadrinho = quadrinho;
        this.valorTotal = valorTotal;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public QuadrinhoDTO getQuadrinho() {
        return quadrinho;
    }

    public void setQuadrinho(QuadrinhoDTO quadrinho) {
        this.quadrinho = quadrinho;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Inclui o campo quadrinhoId diretamente no JSON,
     * permitindo ao front-end usar item.quadrinhoId sem undefined.
     */
    public Long getQuadrinhoId() {
        return quadrinho != null ? quadrinho.getId() : null;
    }
}