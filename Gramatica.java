import java.util.ArrayList;


public class Gramatica {

	private ArrayList<Estado> naoTerminais;
	private char[] terminais;
	private ArrayList<Transicao> producoes;
	private Estado inicial;

	public Gramatica(ArrayList<Estado> estados, char[] alfabeto,
			ArrayList<Transicao> transicoes, Estado inicial) {
		this.naoTerminais = estados;
		this.terminais = alfabeto;
		this.producoes = transicoes;
		this.inicial = inicial;
	}

	public ArrayList<Estado> getNaoTerminais() {
		return naoTerminais;
	}

	public char[] getTerminais() {
		return terminais;
	}

	public ArrayList<Transicao> getProducoes() {
		return producoes;
	}

	public Estado getInicial() {
		return inicial;
	}
	
	

}
