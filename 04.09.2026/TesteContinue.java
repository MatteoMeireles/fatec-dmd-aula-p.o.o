package projeto;

public class TesteContinue {
	
	public static void main(String[] args) {
		
		int i = 1;
		
		while (i < 5) {
			if (i == 4) {
				i++;
				continue;
				
}
			
			System.out.println("Valor de i = " + i);
			i++;
			
		}
	}

}
