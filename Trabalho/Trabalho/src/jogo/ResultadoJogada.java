package jogo;

/**
 * O que aconteceu depois de um MOVER aceito, para o terminal
 * mostrar a mensagem certa.
 */
public class ResultadoJogada {
    private final boolean capturaContinua;
    private final int pecasCapturadas;
    private final boolean promoveu;

    ResultadoJogada(boolean capturaContinua, int pecasCapturadas, boolean promoveu) {
        this.capturaContinua = capturaContinua;
        this.pecasCapturadas = pecasCapturadas;
        this.promoveu = promoveu;
    }

    public boolean isCapturaContinua() {
        return capturaContinua;
    }

    public int getPecasCapturadas() {
        return pecasCapturadas;
    }

    public boolean isPromoveu() {
        return promoveu;
    }
}
