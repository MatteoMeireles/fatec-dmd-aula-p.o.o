package projeto;

public class AulaTeste4 {
	    public static void imprimir(int[] v) {
	        System.out.print("[");
	        for (int i = 0; i < v.length; i++) {
	            System.out.print(v[i]);
	            if (i < v.length - 1) {
	                System.out.print(",");
	            }
	        }
	        System.out.print("]");
	    }
	    public static int somar(int[] v) {
	        int soma = 0;
	        for (int i = 0; i < v.length; i++) {
	            soma += v[i];
	        }
	        return soma;
	    }
	    public static void main(String[] args) {
	        int[] v = new int[6];

	        for (int i = 0; i < v.length; i++) {
	            v[i] = i + 5; 
	        }
	        imprimir(v);
	        System.out.println();
	        System.out.println("Soma: " + somar(v));
	    }
	}
