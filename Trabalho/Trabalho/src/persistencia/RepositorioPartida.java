package persistencia;

import java.io.IOException;

import jogo.Partida;

/**
 * Contrato para guardar e recuperar partidas. O resto do programa
 * so conhece essa interface, entao trocar arquivo por outro meio
 * nao mexe no jogo.
 */
public interface RepositorioPartida {

    void salvar(Partida partida, String nomeArquivo) throws IOException;

    Partida carregar(String nomeArquivo) throws IOException;
}
