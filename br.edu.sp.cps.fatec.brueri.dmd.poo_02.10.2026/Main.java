package br.edu.sp.cps.fatec.brueri.dmd.poo;

public class Main {

	public static void main(String[] args) {
		
		Gato g1 = new Gato();
		
		Gato g2 = new Gato("Abacatinha", "nao sei", 4, "BRANCO");
		
		System.out.println(g1.nome);
		g1.nome = "Amy";
		System.out.println(g1.nome);

		System.out.println(g2.nome);
		
		g1.VARIAVEL_GATO = "2";
		System.out.println(g2.VARIAVEL_GATO);
		

	}

}
