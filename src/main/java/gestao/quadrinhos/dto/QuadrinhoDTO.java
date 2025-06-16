
// src/main/java/gestao/quadrinhos/dto/QuadrinhoDTO.java
package gestao.quadrinhos.dto;

import java.math.BigDecimal;

import gestao.quadrinhos.entities.Quadrinho;
import gestao.quadrinhos.entities.User;

public class QuadrinhoDTO {

    private Long id;
    private String nome;
    private String codigo;
    private String raridade;
    private BigDecimal preco;
    private String urlImagem;
    private Long userId;

    public QuadrinhoDTO() {}

    public QuadrinhoDTO(Quadrinho quadrinho) {
        if (quadrinho == null) return;
        this.id        = quadrinho.getId();
        this.nome      = quadrinho.getNome();
        this.codigo    = quadrinho.getCodigo();
        this.raridade  = quadrinho.getRaridade();
        this.preco     = quadrinho.getPreco();
        this.urlImagem = quadrinho.getUrlImagem();
        this.userId    = quadrinho.getUser() != null ? quadrinho.getUser().getId() : null;
    }

    
    
    // Getters e Setters omitidos para brevidade

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getRaridade() {
		return raridade;
	}

	public void setRaridade(String raridade) {
		this.raridade = raridade;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public String getUrlImagem() {
		return urlImagem;
	}

	public void setUrlImagem(String urlImagem) {
		this.urlImagem = urlImagem;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/** Converte DTO em entidade (novo registro) e define o usuário */
    public Quadrinho toEntity(User user) {
        Quadrinho q = new Quadrinho();
        q.setNome(this.nome);
        q.setCodigo(this.codigo);
        q.setRaridade(this.raridade);
        q.setPreco(this.preco);
        q.setUrlImagem(this.urlImagem);
        q.setUser(user);
        return q;
    }

    /** Converte DTO em entidade e define ID (update) e usuário */
    public Quadrinho toEntityWithId(Long id, User user) {
        Quadrinho q = toEntity(user);
        q.setId(id);
        return q;
    }
}
