package projeto;

public class Aula {
	public static void main(String[] args) {
		int i = 5; // i recebe uma copia do valor 5;
		System.out.println(i);
		int j = i; // j recebe uma copia do valor de i;
		System.out.println(j);
		i = i + 1; // i vira 6, j continua 5
		System.out.println(i);
	}
}
