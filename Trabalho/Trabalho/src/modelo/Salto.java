package modelo;

/**
 * Um salto de captura: qual peca foi pulada e em qual casa a peca parou.
 */
public class Salto {
    private final Posicao capturada;
    private final Posicao destino;

    public Salto(Posicao capturada, Posicao destino) {
        this.capturada = capturada;
        this.destino = destino;
    }

    public Posicao getCapturada() {
        return capturada;
    }

    public Posicao getDestino() {
        return destino;
    }
}
