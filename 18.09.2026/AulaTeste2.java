package projeto;

public class AulaTeste2 {
	
	public static void main(String[] args) {        
		int[] v;         
		v = new int[5];         
		for (int i = 0; i < 5; i++) {             
			v[i] = i + 5;         
			}         
		System.out.println("[");
		for (int i = 0; i < 5; i++) {  
			System.out.println(v[i]);
		if (i < 4) {
			System.out.println(",");	
		}
	System.out.println("]");
	}

		