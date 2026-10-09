package projeto;

public class AulaTeste3 {


	    public static void main(String[] args) {

	        int[] v = new int[6];

	        for (int i = 0; i < v.length; i++) {
	            v[i] = i + 5;
	        }

	        System.out.print("[");

	        for (int i = 0; i < v.length; i++) {
	            System.out.print(v[i]);

	            if (i < v.length - 1) {
	                System.out.print(", ");
	            }
	        }

	        System.out.println("]");
	    }
	}

