package gestao.quadrinhos.dto;

import gestao.quadrinhos.entities.Quadrinho;

public class QuadrinhoDTO{

    private Long id;
    private String nome;
    private String codigo;
    private String raridade;
    private Float preco;
    private String urlImagem;
    private Long userId;
    
    public QuadrinhoDTO() {
    }

    public QuadrinhoDTO(Long id, String nome, String codigo, String raridade, Float preco, String urlImagem, Long userId) {
		super();
		this.id = id;
		this.nome = nome;
		this.codigo = codigo;
		this.raridade = raridade;
		this.preco = preco;
		this.urlImagem = urlImagem;
		this.userId = userId;
	}

	public QuadrinhoDTO(Quadrinho quadrinho) {
        this.id = quadrinho.getId();
        this.nome = quadrinho.getNome();
        this.codigo = quadrinho.getCodigo();
        this.raridade = quadrinho.getRaridade();
        this.preco = quadrinho.getPreco();
        this.urlImagem = quadrinho.getUrlImagem();
        this.userId = quadrinho.getUser().getId();
    }

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

	public Float getPreco() {
		return preco;
	}

	public void setPreco(Float preco) {
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

}