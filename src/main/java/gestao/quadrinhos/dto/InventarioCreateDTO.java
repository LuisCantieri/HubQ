// InventarioCreateDTO.java
package gestao.quadrinhos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class InventarioCreateDTO {

    @NotNull(message = "ID do quadrinho é obrigatório")
    private Long quadrinhoId;

    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser positiva")
    private Integer quantidade;

    // Agora BigDecimal em vez de Float
    private BigDecimal precoUnitario;
    private String raridade;

    public InventarioCreateDTO() {}

    public InventarioCreateDTO(Long quadrinhoId, Integer quantidade, BigDecimal precoUnitario, String raridade) {
        this.quadrinhoId = quadrinhoId;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.raridade = raridade;
    }

    public Long getQuadrinhoId() {
        return quadrinhoId;
    }

    public void setQuadrinhoId(Long quadrinhoId) {
        this.quadrinhoId = quadrinhoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public String getRaridade() {
        return raridade;
    }

    public void setRaridade(String raridade) {
        this.raridade = raridade;
    }
}
