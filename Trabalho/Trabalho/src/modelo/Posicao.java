package modelo;

/**
 * Uma casa do tabuleiro. Internamente linha e coluna vao de 0 a 7,
 * mas para o usuario aparecem como A1 ate H8.
 * O objeto e imutavel: deslocar devolve uma posicao nova.
 */
public final class Posicao {
    private final int linha;
    private final int coluna;

    public Posicao(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    public static Posicao deTexto(String texto) {
        String t = texto.trim().toUpperCase();
        if (!t.matches("[A-H][1-8]")) {
            throw new IllegalArgumentException("Coordenada invalida: " + texto + ". Use de A1 ate H8.");
        }
        return new Posicao(t.charAt(1) - '1', t.charAt(0) - 'A');
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    public boolean estaNoTabuleiro() {
        return linha >= 0 && linha < 8 && coluna >= 0 && coluna < 8;
    }

    // A1 e escura, entao as escuras sao as casas com linha + coluna par.
    public boolean ehCasaEscura() {
        return (linha + coluna) % 2 == 0;
    }

    public Posicao deslocar(int linhas, int colunas) {
        return new Posicao(linha + linhas, coluna + colunas);
    }

    @Override
    public boolean equals(Object outro) {
        if (!(outro instanceof Posicao)) {
            return false;
        }
        Posicao p = (Posicao) outro;
        return linha == p.linha && coluna == p.coluna;
    }

    @Override
    public int hashCode() {
        return linha * 8 + coluna;
    }

    @Override
    public String toString() {
        return "" + (char) ('A' + coluna) + (linha + 1);
    }
}
