package projeto;

import javax.xml.stream.events.StartDocument;

public class Revisao {

    public static void main(String[] args) {
        int[] valores = { 1, 2, 5, 56789, 2 };
        int x = soma(valores);
        System.out.println(x);
    }

    private static int soma(int[] v) {
        int soma = 0;

        for (int i = 1; i < v.length; i=+2) {
            if (i == 2) {
                return soma;
            }

            soma += v[i];
        }

        return soma;
    }
}