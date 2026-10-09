package br.edu.sp.cps.fatec.brueri.dmd.poo;

public class Gato {

	public String nome;
	public String raca;
	public int quantidadePatas;
	public String cor;
	
	public Gato() {
		
	}

	public Gato(String nome, String raca, int quantidadePatas, String cor) {
		super();
		this.nome = nome;
		this.raca = raca;
		this.quantidadePatas = quantidadePatas;
		this.cor = cor;
	}
	
	public void miar() {
		System.out.println(nome + " MIAUUUUUUU ME DA CARINHO");
				
	}
	

}

