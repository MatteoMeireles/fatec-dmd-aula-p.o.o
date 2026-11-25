package jogo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import modelo.Cor;
import modelo.Peca;
import modelo.Posicao;
import modelo.Salto;

/**
 * Descobre quais jogadas um jogador pode fazer, ja aplicando
 * a captura obrigatoria e a regra da maior captura.
 * A Partida usa essa lista para validar o MOVER e para saber
 * se o jogador ficou sem movimentos.
 */
public class RegrasMovimento {

    public List<Movimento> jogadasPermitidas(Tabuleiro tabuleiro, Cor cor) {
        List<Movimento> capturas = new ArrayList<Movimento>();
        for (Posicao origem : tabuleiro.posicoesDasPecas(cor)) {
            capturas.addAll(capturasDaPeca(tabuleiro, origem));
        }
        if (!capturas.isEmpty()) {
            return somenteAsMaiores(capturas);
        }

        List<Movimento> simples = new ArrayList<Movimento>();
        for (Posicao origem : tabuleiro.posicoesDasPecas(cor)) {
            Peca peca = tabuleiro.getPeca(origem);
            for (Posicao destino : peca.destinosSemCaptura(tabuleiro, origem)) {
                simples.add(new Movimento(origem, destino));
            }
        }
        return simples;
    }

    private List<Movimento> capturasDaPeca(Tabuleiro tabuleiro, Posicao origem) {
        List<Movimento> encontradas = new ArrayList<Movimento>();
        Peca peca = tabuleiro.getPeca(origem);
        // Durante a busca a peca sai da origem, assim a casa de origem conta
        // como livre caso a sequencia passe por ela de novo.
        tabuleiro.removerPeca(origem);
        try {
            buscarSequencias(tabuleiro, peca, origem, origem,
                    new ArrayList<Posicao>(), new ArrayList<Posicao>(), encontradas);
        } finally {
            tabuleiro.colocarPeca(origem, peca);
        }
        return encontradas;
    }

    // Busca recursiva: tenta cada salto possivel e continua a partir da casa
    // onde a peca parou. Quando nao ha mais saltos, a sequencia esta completa.
    // As pecas capturadas continuam no tabuleiro ate o fim (bloqueando as casas).
    private void buscarSequencias(Tabuleiro tabuleiro, Peca peca, Posicao origem, Posicao atual,
            List<Posicao> destinos, List<Posicao> capturadas, List<Movimento> encontradas) {
        List<Salto> saltos = peca.saltosDeCaptura(tabuleiro, atual, new HashSet<Posicao>(capturadas));
        if (saltos.isEmpty()) {
            if (!capturadas.isEmpty()) {
                encontradas.add(new Movimento(origem, destinos, capturadas));
            }
            return;
        }
        for (Salto salto : saltos) {
            destinos.add(salto.getDestino());
            capturadas.add(salto.getCapturada());
            buscarSequencias(tabuleiro, peca, origem, salto.getDestino(), destinos, capturadas, encontradas);
            destinos.remove(destinos.size() - 1);
            capturadas.remove(capturadas.size() - 1);
        }
    }

    private List<Movimento> somenteAsMaiores(List<Movimento> capturas) {
        int maior = 0;
        for (Movimento m : capturas) {
            maior = Math.max(maior, m.getQuantidadeCapturas());
        }
        List<Movimento> resultado = new ArrayList<Movimento>();
        for (Movimento m : capturas) {
            if (m.getQuantidadeCapturas() == maior) {
                resultado.add(m);
            }
        }
        return resultado;
    }
}
