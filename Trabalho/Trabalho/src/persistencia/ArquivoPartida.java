package persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import jogo.Partida;
import jogo.StatusPartida;
import modelo.Cor;

/**
 * Salva e carrega partidas em arquivos de texto.
 *
 * Formato:
 * JOGO_DE_DAMAS
 * BRANCAS=Ana
 * PRETAS=Pedro
 * TURNO=PRETAS
 * STATUS=EM_ANDAMENTO
 * TABULEIRO_INICIO
 * (8 linhas, da linha 8 ate a linha 1)
 * TABULEIRO_FIM
 */
public class ArquivoPartida implements RepositorioPartida {
    private static final String CABECALHO = "JOGO_DE_DAMAS";
    private static final String CODIFICACAO = "UTF-8";

    private final File pastaPartidas;
    private final File pastaCenarios;

    // Salva sempre em pastaPartidas. Para carregar, procura primeiro em
    // pastaPartidas e depois em pastaCenarios (arquivos de teste).
    public ArquivoPartida(String pastaPartidas, String pastaCenarios) {
        this.pastaPartidas = new File(pastaPartidas);
        this.pastaCenarios = new File(pastaCenarios);
    }

    @Override
    public void salvar(Partida partida, String nomeArquivo) throws IOException {
        validarNome(nomeArquivo);
        if (partida.emCapturaMultipla()) {
            throw new IOException("nao e possivel salvar no meio de uma captura multipla.");
        }
        if (!pastaPartidas.exists() && !pastaPartidas.mkdirs()) {
            throw new IOException("nao foi possivel criar a pasta " + pastaPartidas.getPath());
        }
        File arquivo = new File(pastaPartidas, nomeArquivo);
        try (BufferedWriter escritor = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(arquivo), CODIFICACAO))) {
            for (String linha : gerarLinhas(partida)) {
                escritor.write(linha);
                escritor.newLine();
            }
        }
    }

    @Override
    public Partida carregar(String nomeArquivo) throws IOException {
        validarNome(nomeArquivo);
        File arquivo = new File(pastaPartidas, nomeArquivo);
        if (!arquivo.isFile()) {
            arquivo = new File(pastaCenarios, nomeArquivo);
        }
        if (!arquivo.isFile()) {
            throw new FileNotFoundException("arquivo " + nomeArquivo + " nao encontrado em "
                    + pastaPartidas.getPath() + "/ nem em " + pastaCenarios.getPath() + "/.");
        }

        List<String> linhas = lerLinhas(arquivo);
        if (linhas.isEmpty()) {
            throw new IOException("o arquivo " + nomeArquivo + " esta vazio.");
        }
        try {
            return interpretar(linhas);
        } catch (IllegalArgumentException e) {
            throw new IOException("conteudo invalido em " + nomeArquivo + ": " + e.getMessage());
        }
    }

    private List<String> gerarLinhas(Partida partida) {
        List<String> linhas = new ArrayList<String>();
        linhas.add(CABECALHO);
        linhas.add("BRANCAS=" + partida.getJogador(Cor.BRANCAS).getNome());
        linhas.add("PRETAS=" + partida.getJogador(Cor.PRETAS).getNome());
        linhas.add("TURNO=" + partida.getTurno());
        linhas.add("STATUS=" + partida.getStatus());
        linhas.add("TABULEIRO_INICIO");
        for (String linha : partida.getTabuleiro().comoTexto()) {
            linhas.add(linha);
        }
        linhas.add("TABULEIRO_FIM");
        return linhas;
    }

    private List<String> lerLinhas(File arquivo) throws IOException {
        List<String> linhas = new ArrayList<String>();
        try (BufferedReader leitor = new BufferedReader(
                new InputStreamReader(new FileInputStream(arquivo), CODIFICACAO))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                if (!linha.trim().isEmpty()) {
                    linhas.add(linha.trim());
                }
            }
        }
        return linhas;
    }

    private Partida interpretar(List<String> linhas) {
        if (!CABECALHO.equals(linhas.get(0))) {
            throw new IllegalArgumentException("a primeira linha deveria ser " + CABECALHO);
        }
        if (linhas.size() != 15) {
            throw new IllegalArgumentException("esperadas 15 linhas, encontradas " + linhas.size());
        }
        String nomeBrancas = valor(linhas.get(1), "BRANCAS");
        String nomePretas = valor(linhas.get(2), "PRETAS");
        Cor turno = lerCor(valor(linhas.get(3), "TURNO"));
        StatusPartida status = lerStatus(valor(linhas.get(4), "STATUS"));
        if (!"TABULEIRO_INICIO".equals(linhas.get(5)) || !"TABULEIRO_FIM".equals(linhas.get(14))) {
            throw new IllegalArgumentException("tabuleiro sem TABULEIRO_INICIO/TABULEIRO_FIM");
        }
        String[] tabuleiro = linhas.subList(6, 14).toArray(new String[8]);
        return Partida.restaurar(nomeBrancas, nomePretas, turno, status, tabuleiro);
    }

    private String valor(String linha, String chave) {
        if (!linha.startsWith(chave + "=")) {
            throw new IllegalArgumentException("esperado " + chave + "=..., encontrado \"" + linha + "\"");
        }
        return linha.substring(chave.length() + 1);
    }

    private Cor lerCor(String texto) {
        for (Cor cor : Cor.values()) {
            if (cor.name().equals(texto)) {
                return cor;
            }
        }
        throw new IllegalArgumentException("turno desconhecido: " + texto);
    }

    private StatusPartida lerStatus(String texto) {
        for (StatusPartida s : StatusPartida.values()) {
            if (s.name().equals(texto)) {
                return s;
            }
        }
        throw new IllegalArgumentException("status desconhecido: " + texto);
    }

    // So aceita nome simples, sem pastas, para nao gravar fora de partidas/.
    private void validarNome(String nome) throws IOException {
        if (nome == null || nome.trim().isEmpty() || nome.contains("/") || nome.contains("\\") || nome.contains("..")) {
            throw new IOException("informe apenas o nome do arquivo, sem pastas (exemplo: partida.txt).");
        }
    }
}
