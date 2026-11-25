package modelo;

public enum Cor {
    BRANCAS, PRETAS;

    public Cor adversaria() {
        return this == BRANCAS ? PRETAS : BRANCAS;
    }

    // As brancas sobem (linha 1 para a 8) e as pretas descem.
    public int sentidoDeAvanco() {
        return this == BRANCAS ? 1 : -1;
    }

    // Indice da linha onde a peca comum dessa cor vira dama.
    public int linhaDePromocao() {
        return this == BRANCAS ? 7 : 0;
    }

    public String getNomeExibicao() {
        return this == BRANCAS ? "Brancas" : "Pretas";
    }
}
