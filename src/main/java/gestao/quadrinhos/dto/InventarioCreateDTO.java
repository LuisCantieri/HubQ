package gestao.quadrinhos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class InventarioCreateDTO {

    @NotNull(message = "ID do quadrinho é obrigatório")
    private Long quadrinhoId;

    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser positiva")
    private Integer quantidade;

    private Float precoUnitario;
    private String raridade;

    public InventarioCreateDTO() {}

    public InventarioCreateDTO(Long quadrinhoId, Integer quantidade, Float precoUnitario, String raridade) {
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

    public Float getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(Float precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public String getRaridade() {
        return raridade;
    }

    public void setRaridade(String raridade) {
        this.raridade = raridade;
    }
}
