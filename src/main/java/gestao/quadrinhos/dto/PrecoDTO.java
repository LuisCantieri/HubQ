// src/main/java/gestao/quadrinhos/dto/PrecoDTO.java
package gestao.quadrinhos.dto;

import java.math.BigDecimal;

public class PrecoDTO {
    private BigDecimal novoPreco;
    public BigDecimal getNovoPreco() { return novoPreco; }
    public void setNovoPreco(BigDecimal novoPreco) { this.novoPreco = novoPreco; }
}
