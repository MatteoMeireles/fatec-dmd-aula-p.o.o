package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jogo.Tabuleiro;

public class PecaComum extends Peca {

    public PecaComum(Cor cor) {
        super(cor);
    }

    @Override
    public char getSimbolo() {
        return getCor() == Cor.BRANCAS ? 'b' : 'p';
    }

    // Anda uma casa na diagonal e so para frente.
    @Override
    public List<Posicao> destinosSemCaptura(Tabuleiro tabuleiro, Posicao origem) {
        List<Posicao> destinos = new ArrayList<Posicao>();
        int frente = getCor().sentidoDeAvanco();
        Posicao esquerda = origem.deslocar(frente, -1);
        Posicao direita = origem.deslocar(frente, 1);
        if (tabuleiro.estaLivre(esquerda)) {
            destinos.add(esquerda);
        }
        if (tabuleiro.estaLivre(direita)) {
            destinos.add(direita);
        }
        return destinos;
    }

    // Captura pulando a peca vizinha, nas quatro diagonais (para tras tambem).
    @Override
    public List<Salto> saltosDeCaptura(Tabuleiro tabuleiro, Posicao origem, Set<Posicao> jaCapturadas) {
        List<Salto> saltos = new ArrayList<Salto>();
        for (int[] d : DIAGONAIS) {
            Posicao vizinha = origem.deslocar(d[0], d[1]);
            Posicao depois = vizinha.deslocar(d[0], d[1]);
            if (ehAdversariaDe(tabuleiro.getPeca(vizinha))
                    && !jaCapturadas.contains(vizinha)
                    && tabuleiro.estaLivre(depois)) {
                saltos.add(new Salto(vizinha, depois));
            }
        }
        return saltos;
    }

    @Override
    public boolean devePromoverEm(Posicao destino) {
        return destino.getLinha() == getCor().linhaDePromocao();
    }
}
