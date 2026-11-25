package aplicacao;

import java.util.Scanner;

import persistencia.ArquivoPartida;
import persistencia.RepositorioPartida;

public class Main {

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        // Caminhos relativos: o Eclipse executa a partir da pasta do projeto.
        RepositorioPartida repositorio = new ArquivoPartida("partidas", "testes/cenarios");
        InterpretadorComandos interpretador = new InterpretadorComandos(entrada, repositorio);
        interpretador.executar();
        entrada.close();
    }
}
