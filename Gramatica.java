import java.util.ArrayList;


public class Gramatica {
	
	private int posicaoTerminais;

	private ArrayList<Estado> naoTerminais;
	private ArrayList<Character> terminais;
	private ArrayList<Transicao> producoes;
	private Estado inicial;

	public Gramatica(ArrayList<Estado> estados, ArrayList<Character> alfabeto,
			ArrayList<Transicao> transicoes, Estado inicial) {
		this.naoTerminais = estados;
		this.terminais = alfabeto;
		this.producoes = transicoes;
		this.inicial = inicial;
	}

	public ArrayList<Estado> getNaoTerminais() {
		return naoTerminais;
	}

	public ArrayList<Character> getTerminais() {
		return terminais;
	}

	public ArrayList<Transicao> getProducoes() {
		return producoes;
	}

	public Estado getInicial() {
		return inicial;
	}
	
	// Copia os conjuntos para os de automato, e gera o estado final "A", o qual seria o final (jah que gramaticas nao explicitam estado final)
	public Automato gerarAutomato(){
		ArrayList<Transicao> novaTransicoes = (ArrayList<Transicao>) this.producoes.clone();
		Estado a = new Estado("A");
		a.getEstadosInternos().add(a);
		
		ArrayList<Estado> finais = new ArrayList<>();
		finais.add(a);
		for (Transicao t : this.producoes){
			if (t.get_final() == null){
				Transicao t1 = new Transicao(t.getInicial(), t.getLeitura(), a);
				novaTransicoes.add(t1);
				novaTransicoes.remove(t);
			}
		}
		
		ArrayList<Estado> novaEstados = (ArrayList<Estado>) this.getNaoTerminais().clone();
		novaEstados.add(a);
		Automato aut = new Automato(this.terminais, novaEstados, finais, novaTransicoes, inicial);
		return aut;
	}

	public void setInicial(Estado inicialNovo) {
		this.inicial = inicialNovo;
	}

	public int getPosicaoTerminais() {
		return posicaoTerminais;
	}

	public void setPosicaoTerminais(int posicaoTerminais) {
		this.posicaoTerminais = posicaoTerminais;
	}

	public void setTerminais(ArrayList<Character> novoAlfabeto) {
		this.terminais = novoAlfabeto;
	}


}
