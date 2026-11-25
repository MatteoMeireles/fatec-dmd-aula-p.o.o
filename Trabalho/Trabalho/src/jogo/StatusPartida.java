package jogo;

import modelo.Cor;

public enum StatusPartida {
    EM_ANDAMENTO, VITORIA_BRANCAS, VITORIA_PRETAS;

    public static StatusPartida vitoriaDe(Cor cor) {
        return cor == Cor.BRANCAS ? VITORIA_BRANCAS : VITORIA_PRETAS;
    }

    public Cor getVencedor() {
        if (this == VITORIA_BRANCAS) {
            return Cor.BRANCAS;
        }
        if (this == VITORIA_PRETAS) {
            return Cor.PRETAS;
        }
        return null;
    }
}
