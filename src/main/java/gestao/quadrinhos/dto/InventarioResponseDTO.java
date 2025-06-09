package gestao.quadrinhos.dto;

import java.util.Date;

public class InventarioResponseDTO {
    
    private Long id;
    private Long quadrinhoId;
    private String quadrinhoNome;
    private String quadrinhoCodigo;
    private String quadrinhoRaridade;
    private String quadrinhoUrlImagem;
    private Integer quantidade;
    private Float precoUnitario;
    private Double valorTotal;
    private Date createdAt;
    private Date updatedAt;

    public InventarioResponseDTO() {}

	public InventarioResponseDTO(Long id, Long quadrinhoId, String quadrinhoNome, String quadrinhoCodigo,
			String quadrinhoRaridade, String quadrinhoUrlImagem, Integer quantidade, Float precoUnitario,
			Double valorTotal, Date createdAt, Date updatedAt) {
		super();
		this.id = id;
		this.quadrinhoId = quadrinhoId;
		this.quadrinhoNome = quadrinhoNome;
		this.quadrinhoCodigo = quadrinhoCodigo;
		this.quadrinhoRaridade = quadrinhoRaridade;
		this.quadrinhoUrlImagem = quadrinhoUrlImagem;
		this.quantidade = quantidade;
		this.precoUnitario = precoUnitario;
		this.valorTotal = valorTotal;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getQuadrinhoId() {
		return quadrinhoId;
	}

	public void setQuadrinhoId(Long quadrinhoId) {
		this.quadrinhoId = quadrinhoId;
	}

	public String getQuadrinhoNome() {
		return quadrinhoNome;
	}

	public void setQuadrinhoNome(String quadrinhoNome) {
		this.quadrinhoNome = quadrinhoNome;
	}

	public String getQuadrinhoCodigo() {
		return quadrinhoCodigo;
	}

	public void setQuadrinhoCodigo(String quadrinhoCodigo) {
		this.quadrinhoCodigo = quadrinhoCodigo;
	}

	public String getQuadrinhoRaridade() {
		return quadrinhoRaridade;
	}

	public void setQuadrinhoRaridade(String quadrinhoRaridade) {
		this.quadrinhoRaridade = quadrinhoRaridade;
	}

	public String getQuadrinhoUrlImagem() {
		return quadrinhoUrlImagem;
	}

	public void setQuadrinhoUrlImagem(String quadrinhoUrlImagem) {
		this.quadrinhoUrlImagem = quadrinhoUrlImagem;
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

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
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

}