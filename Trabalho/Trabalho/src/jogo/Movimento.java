package jogo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import modelo.Posicao;

/**
 * Uma jogada completa. Um movimento simples tem uma etapa e nenhuma captura.
 * Uma captura multipla tem uma etapa para cada peca capturada.
 */
public class Movimento {
    private final Posicao origem;
    private final List<Posicao> destinos;
    private final List<Posicao> capturadas;

    Movimento(Posicao origem, Posicao destino) {
        this.origem = origem;
        this.destinos = Collections.singletonList(destino);
        this.capturadas = Collections.emptyList();
    }

    Movimento(Posicao origem, List<Posicao> destinos, List<Posicao> capturadas) {
        this.origem = origem;
        this.destinos = new ArrayList<Posicao>(destinos);
        this.capturadas = new ArrayList<Posicao>(capturadas);
    }

    public Posicao getOrigem() {
        return origem;
    }

    // Casa de onde a peca sai naquela etapa (na etapa 0 e a origem).
    public Posicao getSaidaDaEtapa(int etapa) {
        return etapa == 0 ? origem : destinos.get(etapa - 1);
    }

    public Posicao getDestinoDaEtapa(int etapa) {
        return destinos.get(etapa);
    }

    public int getQuantidadeEtapas() {
        return destinos.size();
    }

    public int getQuantidadeCapturas() {
        return capturadas.size();
    }

    public boolean ehCaptura() {
        return !capturadas.isEmpty();
    }

    public List<Posicao> getCapturadas() {
        return Collections.unmodifiableList(capturadas);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(origem.toString());
        for (Posicao p : destinos) {
            sb.append(ehCaptura() ? "x" : "-").append(p);
        }
        return sb.toString();
    }
}
