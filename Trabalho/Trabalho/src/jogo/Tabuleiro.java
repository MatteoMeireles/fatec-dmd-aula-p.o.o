package jogo;

import java.util.ArrayList;
import java.util.List;

import modelo.Cor;
import modelo.Dama;
import modelo.Peca;
import modelo.PecaComum;
import modelo.Posicao;

/**
 * Guarda as 64 casas. Qualquer classe pode consultar o tabuleiro,
 * mas so as classes do pacote jogo (Partida e RegrasMovimento) podem
 * alterar as pecas, depois de validar a jogada.
 */
public class Tabuleiro {
    private final Peca[][] casas = new Peca[8][8];

    private Tabuleiro() {
    }

    static Tabuleiro inicial() {
        Tabuleiro t = new Tabuleiro();
        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                if ((linha + coluna) % 2 != 0) {
                    continue;
                }
                if (linha <= 2) {
                    t.casas[linha][coluna] = new PecaComum(Cor.BRANCAS);
                } else if (linha >= 5) {
                    t.casas[linha][coluna] = new PecaComum(Cor.PRETAS);
                }
            }
        }
        return t;
    }

    // Monta o tabuleiro a partir de 8 linhas de texto, da linha 8 ate a linha 1.
    static Tabuleiro deTexto(String[] linhas) {
        if (linhas.length != 8) {
            throw new IllegalArgumentException("o tabuleiro precisa ter 8 linhas");
        }
        Tabuleiro t = new Tabuleiro();
        for (int i = 0; i < 8; i++) {
            String texto = linhas[i];
            int linha = 7 - i;
            if (texto.length() != 8) {
                throw new IllegalArgumentException("a linha " + (linha + 1) + " do tabuleiro precisa ter 8 caracteres");
            }
            for (int coluna = 0; coluna < 8; coluna++) {
                char simbolo = texto.charAt(coluna);
                if (simbolo == '.') {
                    continue;
                }
                Posicao pos = new Posicao(linha, coluna);
                if (!pos.ehCasaEscura()) {
                    throw new IllegalArgumentException("peca em casa clara (" + pos + ")");
                }
                t.casas[linha][coluna] = criarPeca(simbolo, pos);
            }
        }
        return t;
    }

    private static Peca criarPeca(char simbolo, Posicao pos) {
        switch (simbolo) {
            case 'b': return new PecaComum(Cor.BRANCAS);
            case 'p': return new PecaComum(Cor.PRETAS);
            case 'B': return new Dama(Cor.BRANCAS);
            case 'P': return new Dama(Cor.PRETAS);
            default:
                throw new IllegalArgumentException("simbolo desconhecido '" + simbolo + "' em " + pos);
        }
    }

    public Peca getPeca(Posicao pos) {
        if (!pos.estaNoTabuleiro()) {
            return null;
        }
        return casas[pos.getLinha()][pos.getColuna()];
    }

    public boolean estaLivre(Posicao pos) {
        return pos.estaNoTabuleiro() && getPeca(pos) == null;
    }

    public List<Posicao> posicoesDasPecas(Cor cor) {
        List<Posicao> lista = new ArrayList<Posicao>();
        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                if (casas[linha][coluna] != null && casas[linha][coluna].getCor() == cor) {
                    lista.add(new Posicao(linha, coluna));
                }
            }
        }
        return lista;
    }

    public int contarPecas(Cor cor) {
        return posicoesDasPecas(cor).size();
    }

    // Linhas no formato do ESTADO: da linha 8 ate a linha 1.
    public String[] comoTexto() {
        String[] linhas = new String[8];
        for (int linha = 7; linha >= 0; linha--) {
            StringBuilder sb = new StringBuilder();
            for (int coluna = 0; coluna < 8; coluna++) {
                Peca p = casas[linha][coluna];
                sb.append(p == null ? '.' : p.getSimbolo());
            }
            linhas[7 - linha] = sb.toString();
        }
        return linhas;
    }

    void moverPeca(Posicao origem, Posicao destino) {
        Peca peca = getPeca(origem);
        casas[origem.getLinha()][origem.getColuna()] = null;
        casas[destino.getLinha()][destino.getColuna()] = peca;
    }

    void colocarPeca(Posicao pos, Peca peca) {
        casas[pos.getLinha()][pos.getColuna()] = peca;
    }

    void removerPeca(Posicao pos) {
        casas[pos.getLinha()][pos.getColuna()] = null;
    }

    void promoverPeca(Posicao pos) {
        Peca atual = getPeca(pos);
        casas[pos.getLinha()][pos.getColuna()] = new Dama(atual.getCor());
    }
}
