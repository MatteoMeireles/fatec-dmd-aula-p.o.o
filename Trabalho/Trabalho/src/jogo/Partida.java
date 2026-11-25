package jogo;

import java.util.ArrayList;
import java.util.List;

import modelo.Cor;
import modelo.Jogador;
import modelo.Peca;
import modelo.Posicao;

/**
 * Controla o andamento da partida: de quem e a vez, a execucao das jogadas
 * (inclusive capturas em varias etapas), promocao e fim de jogo.
 */
public class Partida {
    private final Jogador brancas;
    private final Jogador pretas;
    private final Tabuleiro tabuleiro;
    private final RegrasMovimento regras = new RegrasMovimento();
    private Cor turno;
    private StatusPartida status;

    // Usado so durante uma captura multipla: as sequencias que ainda
    // combinam com o que o jogador fez e em qual etapa ele esta.
    private List<Movimento> sequenciasEmAndamento;
    private int etapaAtual;

    public Partida(String nomeBrancas, String nomePretas) {
        this(new Jogador(nomeBrancas, Cor.BRANCAS), new Jogador(nomePretas, Cor.PRETAS),
                Tabuleiro.inicial(), Cor.BRANCAS, StatusPartida.EM_ANDAMENTO);
    }

    private Partida(Jogador brancas, Jogador pretas, Tabuleiro tabuleiro, Cor turno, StatusPartida status) {
        this.brancas = brancas;
        this.pretas = pretas;
        this.tabuleiro = tabuleiro;
        this.turno = turno;
        this.status = status;
    }

    // Recria uma partida salva. Lanca IllegalArgumentException se algum dado estiver errado.
    public static Partida restaurar(String nomeBrancas, String nomePretas, Cor turno,
            StatusPartida status, String[] linhasTabuleiro) {
        Partida partida = new Partida(new Jogador(nomeBrancas, Cor.BRANCAS),
                new Jogador(nomePretas, Cor.PRETAS), Tabuleiro.deTexto(linhasTabuleiro), turno, status);
        if (status == StatusPartida.EM_ANDAMENTO) {
            partida.verificarFimDeJogo();
        }
        return partida;
    }

    public ResultadoJogada mover(Posicao origem, Posicao destino) throws JogadaInvalidaException {
        if (status != StatusPartida.EM_ANDAMENTO) {
            throw new JogadaInvalidaException("a partida ja terminou. Use NOVO para comecar outra.");
        }
        if (!origem.ehCasaEscura() || !destino.ehCasaEscura()) {
            throw new JogadaInvalidaException("as pecas so andam pelas casas escuras.");
        }

        List<Movimento> candidatas;
        int etapa;
        if (emCapturaMultipla()) {
            candidatas = sequenciasEmAndamento;
            etapa = etapaAtual;
        } else {
            candidatas = regras.jogadasPermitidas(tabuleiro, turno);
            etapa = 0;
        }

        List<Movimento> escolhidas = new ArrayList<Movimento>();
        for (Movimento m : candidatas) {
            if (m.getSaidaDaEtapa(etapa).equals(origem) && m.getDestinoDaEtapa(etapa).equals(destino)) {
                escolhidas.add(m);
            }
        }
        if (escolhidas.isEmpty()) {
            throw new JogadaInvalidaException(motivoDaRecusa(candidatas, origem, destino));
        }
        return executarEtapa(escolhidas, etapa, origem, destino);
    }

    private ResultadoJogada executarEtapa(List<Movimento> escolhidas, int etapa, Posicao origem, Posicao destino) {
        Peca peca = tabuleiro.getPeca(origem);
        tabuleiro.moverPeca(origem, destino);

        // Todas as escolhidas tem o mesmo tamanho (a maior captura),
        // entao basta olhar a primeira para saber se ainda falta etapa.
        Movimento referencia = escolhidas.get(0);
        if (etapa + 1 < referencia.getQuantidadeEtapas()) {
            sequenciasEmAndamento = escolhidas;
            etapaAtual = etapa + 1;
            return new ResultadoJogada(true, etapa + 1, false);
        }

        // Fim da jogada: so agora as pecas capturadas saem do tabuleiro.
        for (Posicao capturada : referencia.getCapturadas()) {
            tabuleiro.removerPeca(capturada);
        }
        boolean promoveu = peca.devePromoverEm(destino);
        if (promoveu) {
            tabuleiro.promoverPeca(destino);
        }
        sequenciasEmAndamento = null;
        etapaAtual = 0;
        turno = turno.adversaria();
        verificarFimDeJogo();
        return new ResultadoJogada(false, referencia.getQuantidadeCapturas(), promoveu);
    }

    private String motivoDaRecusa(List<Movimento> candidatas, Posicao origem, Posicao destino) {
        Peca peca = tabuleiro.getPeca(origem);
        if (emCapturaMultipla()) {
            Posicao atual = candidatas.get(0).getSaidaDaEtapa(etapaAtual);
            if (!origem.equals(atual)) {
                return "a captura ainda nao terminou. Continue com a peca que esta em " + atual + ".";
            }
            return "esse destino nao continua a captura. Opcoes: " + opcoes(candidatas, etapaAtual) + ".";
        }
        if (peca == null) {
            return "nao existe peca em " + origem + ".";
        }
        if (peca.getCor() != turno) {
            return "a peca em " + origem + " pertence ao adversario. A vez e das " + turno.getNomeExibicao() + ".";
        }
        if (!candidatas.isEmpty() && candidatas.get(0).ehCaptura()) {
            int quantidade = candidatas.get(0).getQuantidadeCapturas();
            return "captura obrigatoria de " + quantidade + " peca(s). Opcoes: " + opcoes(candidatas, 0) + ".";
        }
        int linhas = Math.abs(destino.getLinha() - origem.getLinha());
        int colunas = Math.abs(destino.getColuna() - origem.getColuna());
        if (linhas != colunas) {
            return "o movimento precisa ser na diagonal.";
        }
        if (!tabuleiro.estaLivre(destino)) {
            return "a casa " + destino + " esta ocupada.";
        }
        return "essa peca nao pode ir de " + origem + " para " + destino + ".";
    }

    private String opcoes(List<Movimento> candidatas, int etapa) {
        List<String> textos = new ArrayList<String>();
        for (Movimento m : candidatas) {
            String texto = m.getSaidaDaEtapa(etapa) + " " + m.getDestinoDaEtapa(etapa);
            if (!textos.contains(texto)) {
                textos.add(texto);
            }
        }
        return String.join(", ", textos);
    }

    // A partida acaba quando o jogador da vez nao tem pecas ou nao tem jogada valida.
    private void verificarFimDeJogo() {
        if (tabuleiro.contarPecas(turno) == 0 || regras.jogadasPermitidas(tabuleiro, turno).isEmpty()) {
            status = StatusPartida.vitoriaDe(turno.adversaria());
        }
    }

    public boolean emCapturaMultipla() {
        return sequenciasEmAndamento != null;
    }

    public Posicao getPosicaoDaCapturaEmAndamento() {
        if (!emCapturaMultipla()) {
            return null;
        }
        return sequenciasEmAndamento.get(0).getSaidaDaEtapa(etapaAtual);
    }

    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public Cor getTurno() {
        return turno;
    }

    public StatusPartida getStatus() {
        return status;
    }

    public boolean estaEncerrada() {
        return status != StatusPartida.EM_ANDAMENTO;
    }

    public Jogador getJogador(Cor cor) {
        return cor == Cor.BRANCAS ? brancas : pretas;
    }

    public Jogador getJogadorDaVez() {
        return getJogador(turno);
    }

    public Jogador getVencedor() {
        Cor cor = status.getVencedor();
        return cor == null ? null : getJogador(cor);
    }
}
