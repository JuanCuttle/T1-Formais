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
	
	public Automato gerarAutomato(){
		ArrayList<Transicao> novaTransicoes = (ArrayList<Transicao>) this.producoes.clone();
		Estado a = new Estado("A");
		
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

}
