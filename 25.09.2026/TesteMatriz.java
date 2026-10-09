package projeto;

public class TesteMatriz {

    public static void main(String[] args) {

        int[][] sala = new int[7][6];
        sala[0][0] = 1;


        int pessoasPresentes = 0;

        for (int i = 0; i < sala.length; i++) {
            for (int j = 0; j < sala.length; j++) {
                pessoasPresentes += sala[i][j];
            }
        }

        System.out.println("Sao " + pessoasPresentes
                + " pessoas presentes");

    }int[][] sala = {
            {1, 0, 0, 1, 1, 0},
            {1, 1, 0, 0, 0, 0},
            {1, 0, 1, 0, 0, 0},
            {1, 0, 0, 0, 0, 0},
            {0, 1, 0, 0, 0, 0},
            {0, 1, 1, 1, 1, 1},
            {1, 0, 1, 0, 1, 1};
    }
    
