package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jogo.Tabuleiro;

public class Dama extends Peca {

    public Dama(Cor cor) {
        super(cor);
    }

    @Override
    public char getSimbolo() {
        return getCor() == Cor.BRANCAS ? 'B' : 'P';
    }

    // Anda quantas casas livres quiser, em qualquer diagonal.
    @Override
    public List<Posicao> destinosSemCaptura(Tabuleiro tabuleiro, Posicao origem) {
        List<Posicao> destinos = new ArrayList<Posicao>();
        for (int[] d : DIAGONAIS) {
            Posicao atual = origem.deslocar(d[0], d[1]);
            while (tabuleiro.estaLivre(atual)) {
                destinos.add(atual);
                atual = atual.deslocar(d[0], d[1]);
            }
        }
        return destinos;
    }

    // Captura a distancia: procura a primeira peca da diagonal e,
    // se for adversaria, pode parar em qualquer casa livre depois dela.
    @Override
    public List<Salto> saltosDeCaptura(Tabuleiro tabuleiro, Posicao origem, Set<Posicao> jaCapturadas) {
        List<Salto> saltos = new ArrayList<Salto>();
        for (int[] d : DIAGONAIS) {
            Posicao atual = origem.deslocar(d[0], d[1]);
            while (tabuleiro.estaLivre(atual)) {
                atual = atual.deslocar(d[0], d[1]);
            }
            if (!ehAdversariaDe(tabuleiro.getPeca(atual)) || jaCapturadas.contains(atual)) {
                continue;
            }
            Posicao pouso = atual.deslocar(d[0], d[1]);
            while (tabuleiro.estaLivre(pouso)) {
                saltos.add(new Salto(atual, pouso));
                pouso = pouso.deslocar(d[0], d[1]);
            }
        }
        return saltos;
    }

    // Dama ja e dama.
    @Override
    public boolean devePromoverEm(Posicao destino) {
        return false;
    }
}
