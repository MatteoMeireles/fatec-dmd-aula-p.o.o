package modelo;

import java.util.List;
import java.util.Set;

import jogo.Tabuleiro;

/**
 * O que toda peca tem em comum: uma cor e a capacidade de dizer
 * para onde pode ir. Cada tipo de peca responde do seu jeito.
 */
public abstract class Peca {
    protected static final int[][] DIAGONAIS = { { 1, -1 }, { 1, 1 }, { -1, -1 }, { -1, 1 } };

    private final Cor cor;

    protected Peca(Cor cor) {
        this.cor = cor;
    }

    public Cor getCor() {
        return cor;
    }

    public boolean ehAdversariaDe(Peca outra) {
        return outra != null && outra.cor != cor;
    }

    // Letra usada no ESTADO e no arquivo salvo (b, p, B, P).
    public abstract char getSimbolo();

    // Casas para onde a peca pode ir sem capturar nada.
    public abstract List<Posicao> destinosSemCaptura(Tabuleiro tabuleiro, Posicao origem);

    // Capturas que a peca consegue fazer a partir da casa atual.
    // As pecas em jaCapturadas continuam no tabuleiro, mas nao podem ser puladas de novo.
    public abstract List<Salto> saltosDeCaptura(Tabuleiro tabuleiro, Posicao origem, Set<Posicao> jaCapturadas);

    // Se a peca deve virar dama ao terminar a jogada nessa casa.
    public abstract boolean devePromoverEm(Posicao destino);
}
