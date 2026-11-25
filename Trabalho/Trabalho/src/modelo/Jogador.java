package modelo;

public class Jogador {
    private final String nome;
    private final Cor cor;

    public Jogador(String nome, Cor cor) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do jogador nao pode ser vazio.");
        }
        this.nome = nome.trim();
        this.cor = cor;
    }

    public String getNome() {
        return nome;
    }

    public Cor getCor() {
        return cor;
    }

    @Override
    public String toString() {
        return nome + " (" + cor.getNomeExibicao() + ")";
    }
}
