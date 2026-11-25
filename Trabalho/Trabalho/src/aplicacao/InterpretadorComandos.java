package aplicacao;

import java.io.IOException;
import java.util.Scanner;

import jogo.JogadaInvalidaException;
import jogo.Partida;
import jogo.ResultadoJogada;
import modelo.Cor;
import modelo.Peca;
import modelo.Posicao;
import persistencia.RepositorioPartida;

/**
 * Le os comandos digitados no console e chama a Partida ou o repositorio.
 * Nao conhece nenhuma regra do jogo, so traduz texto em chamadas e mostra o resultado.
 */
public class InterpretadorComandos {
    private final Scanner entrada;
    private final RepositorioPartida repositorio;
    private Partida partida;

    public InterpretadorComandos(Scanner entrada, RepositorioPartida repositorio) {
        this.entrada = entrada;
        this.repositorio = repositorio;
    }

    public void executar() {
        System.out.println("=================================");
        System.out.println("          JOGO DE DAMAS");
        System.out.println("=================================");
        mostrarComandos();

        boolean continuar = true;
        while (continuar) {
            System.out.print("> ");
            if (!entrada.hasNextLine()) {
                break;
            }
            String linha = entrada.nextLine().trim();
            if (!linha.isEmpty()) {
                continuar = processar(linha);
            }
        }
        System.out.println("Programa encerrado.");
    }

    private boolean processar(String linha) {
        String[] partes = linha.split("\\s+");
        String comando = partes[0].toUpperCase();
        switch (comando) {
            case "NOVO":
                novo(partes);
                break;
            case "MOVER":
                mover(partes);
                break;
            case "ESTADO":
                estado();
                break;
            case "SALVAR":
                salvar(partes);
                break;
            case "CARREGAR":
                carregar(partes);
                break;
            case "AJUDA":
                mostrarComandos();
                break;
            case "SAIR":
                return false;
            default:
                System.out.println("Comando desconhecido: " + partes[0] + ". Digite AJUDA para ver os comandos.");
        }
        return true;
    }

    private void mostrarComandos() {
        System.out.println("Comandos disponiveis:");
        System.out.println("  NOVO <jogador1> <jogador2>   (jogador1 fica com as brancas)");
        System.out.println("  MOVER <origem> <destino>     (exemplo: MOVER A3 B4)");
        System.out.println("  ESTADO");
        System.out.println("  SALVAR <arquivo>");
        System.out.println("  CARREGAR <arquivo>");
        System.out.println("  AJUDA");
        System.out.println("  SAIR");
    }

    private void novo(String[] partes) {
        if (partes.length != 3) {
            System.out.println("Uso: NOVO <jogador1> <jogador2>");
            return;
        }
        partida = new Partida(partes[1], partes[2]);
        System.out.println("Partida iniciada.");
        mostrarTabuleiro();
        mostrarSituacao();
    }

    private void mover(String[] partes) {
        if (partes.length != 3) {
            System.out.println("Uso: MOVER <origem> <destino>  (exemplo: MOVER A3 B4)");
            return;
        }
        if (!existePartida()) {
            return;
        }
        try {
            Posicao origem = Posicao.deTexto(partes[1]);
            Posicao destino = Posicao.deTexto(partes[2]);
            ResultadoJogada resultado = partida.mover(origem, destino);

            if (resultado.isCapturaContinua()) {
                System.out.println("Captura realizada. A captura continua: jogue de novo com a peca em " + destino + ".");
            } else if (resultado.getPecasCapturadas() == 1) {
                System.out.println("Captura realizada.");
            } else if (resultado.getPecasCapturadas() > 1) {
                System.out.println("Captura concluida: " + resultado.getPecasCapturadas() + " pecas capturadas.");
            } else {
                System.out.println("Movimento realizado.");
            }
            if (resultado.isPromoveu()) {
                System.out.println("A peca chegou na ultima linha e virou dama.");
            }
            mostrarTabuleiro();
            mostrarSituacao();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (JogadaInvalidaException e) {
            System.out.println("Jogada recusada: " + e.getMessage());
        }
    }

    private void estado() {
        if (!existePartida()) {
            return;
        }
        System.out.println("TABULEIRO_INICIO");
        for (String linha : partida.getTabuleiro().comoTexto()) {
            System.out.println(linha);
        }
        System.out.println("TABULEIRO_FIM");
        System.out.println("TURNO=" + partida.getTurno());
        System.out.println("STATUS=" + partida.getStatus());
        System.out.println("Brancas: " + partida.getJogador(Cor.BRANCAS).getNome()
                + " | Pretas: " + partida.getJogador(Cor.PRETAS).getNome());
    }

    private void salvar(String[] partes) {
        if (partes.length != 2) {
            System.out.println("Uso: SALVAR <arquivo>");
            return;
        }
        if (!existePartida()) {
            return;
        }
        try {
            repositorio.salvar(partida, partes[1]);
            System.out.println("Partida salva com sucesso.");
        } catch (IOException e) {
            System.out.println("Erro ao salvar: " + e.getMessage());
        }
    }

    private void carregar(String[] partes) {
        if (partes.length != 2) {
            System.out.println("Uso: CARREGAR <arquivo>");
            return;
        }
        try {
            // So troca a partida atual se o arquivo inteiro foi lido sem erro.
            Partida carregada = repositorio.carregar(partes[1]);
            partida = carregada;
            System.out.println("Partida carregada com sucesso.");
            mostrarTabuleiro();
            mostrarSituacao();
        } catch (IOException e) {
            System.out.println("Erro ao carregar: " + e.getMessage());
            if (partida != null) {
                System.out.println("A partida atual foi mantida.");
            }
        }
    }

    private boolean existePartida() {
        if (partida == null) {
            System.out.println("Nenhuma partida em andamento. Use NOVO ou CARREGAR.");
            return false;
        }
        return true;
    }

    private void mostrarSituacao() {
        if (partida.estaEncerrada()) {
            System.out.println("Fim de jogo! Vencedor: " + partida.getVencedor());
        } else if (partida.emCapturaMultipla()) {
            System.out.println("Jogador atual: " + partida.getJogadorDaVez()
                    + " - continue a captura com a peca em " + partida.getPosicaoDaCapturaEmAndamento());
        } else {
            System.out.println("Jogador atual: " + partida.getJogadorDaVez());
        }
    }

    // Tabuleiro com coordenadas, para facilitar a leitura durante o jogo.
    private void mostrarTabuleiro() {
        System.out.println();
        System.out.println("    A B C D E F G H");
        for (int linha = 7; linha >= 0; linha--) {
            StringBuilder sb = new StringBuilder();
            sb.append(' ').append(linha + 1).append("  ");
            for (int coluna = 0; coluna < 8; coluna++) {
                Posicao pos = new Posicao(linha, coluna);
                Peca peca = partida.getTabuleiro().getPeca(pos);
                if (peca != null) {
                    sb.append(peca.getSimbolo());
                } else {
                    sb.append(pos.ehCasaEscura() ? '.' : ' ');
                }
                sb.append(' ');
            }
            sb.append(' ').append(linha + 1);
            System.out.println(sb);
        }
        System.out.println("    A B C D E F G H");
        System.out.println();
    }
}
