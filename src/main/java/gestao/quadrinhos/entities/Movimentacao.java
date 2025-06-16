package gestao.quadrinhos.entities;

import java.util.Date;
import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_movimentacao")
public class Movimentacao {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_movimentacao", nullable = false)
    private Date dataMovimentacao;

    @Column(length = 500)
    private String descricao;

    @Column(nullable = false)
    private Integer quantidade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusMovimentacao tipo;

    /**
     * Permite null em caso de deleção do quadrinho
     * Importante garantir que esse comportamento seja coerente com a regra de negócio
     */
    @ManyToOne
    @JoinColumn(name = "quadrinho_id", nullable = true, foreignKey = @ForeignKey(name = "fk_movimentacao_quadrinho"))
    private Quadrinho quadrinho;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Construtor padrão
    public Movimentacao() {
        this.id = UUID.randomUUID();
        this.dataMovimentacao = new Date();
    }

    // Construtor completo
    public Movimentacao(User user, Quadrinho quadrinho, StatusMovimentacao tipo, Integer quantidade, String descricao) {
        this();
        this.user = user;
        this.quadrinho = quadrinho;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.descricao = descricao;
    }

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public StatusMovimentacao getTipo() {
        return tipo;
    }

    public void setTipo(StatusMovimentacao tipo) {
        this.tipo = tipo;
    }

    public Quadrinho getQuadrinho() {
        return quadrinho;
    }

    public void setQuadrinho(Quadrinho quadrinho) {
        this.quadrinho = quadrinho;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
