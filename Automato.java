import java.util.ArrayList;
import java.util.HashMap;

public class Automato {
	private boolean completo = false;
	private boolean afnd;

	private char[] alfabeto;
	private ArrayList<Estado> estados;
	private ArrayList<Estado> finais;
	private ArrayList<Transicao> transicoes;
	private Estado inicial;

	public Automato(char[] alfabeto, ArrayList<Estado> estados,
			ArrayList<Estado> finais, ArrayList<Transicao> transicoes,
			Estado inicial) {
		this.alfabeto = alfabeto;
		this.estados = estados;
		this.finais = finais;
		this.transicoes = transicoes;
		this.inicial = inicial;
	}

	public char[] getAlfabeto() {
		return alfabeto;
	}

	public ArrayList<Estado> getEstados() {
		return estados;
	}

	public ArrayList<Estado> getFinais() {
		return finais;
	}

	public ArrayList<Transicao> getTransicoes() {
		return transicoes;
	}

	public Estado getInicial() {
		return inicial;
	}

	public ArrayList<Estado> transicao(Estado inicial, char leitura) {
		ArrayList<Estado> possiveis = new ArrayList<Estado>();
		for (Transicao transicao : transicoes) {
			if (transicao.getInicial() == inicial
					&& transicao.getLeitura() == leitura) {
				possiveis.add(transicao.get_final());
			}
		}
		return possiveis;
	}

	public void completarAutomato() {
		boolean temtransicao = false;
		boolean estadoErro = false;
		Estado estadoDeErro = new Estado("qe");

		if (!completo) {
			for (char caracter : alfabeto) {
				//System.out.println("Caracter atual:"+ caracter);
				for (Estado estado : estados) {
					//System.out.println("Estado atual:"+ estado.getNome());
					temtransicao = false;
					for (Transicao transicao : transicoes) {
						if (transicao.getInicial() == estado
								&& transicao.getLeitura() == caracter) {
							temtransicao = true;
						}
					}
					if (temtransicao == false) {
						Transicao nova = new Transicao(estado, caracter,
								estadoDeErro);
						this.transicoes.add(nova);
						estadoErro = true;
					}
				}
			}

			if (estadoErro) {
				this.estados.add(estadoDeErro);
			}
			
			this.completo = true;

		} else {
			System.out.println("O automato já está completo!");
		}

	}

	public Gramatica gerarGramatica() {
		ArrayList<Transicao> novaTransicoes = (ArrayList<Transicao>) this.transicoes.clone();
		//Estado a = new Estado("A");
		for (Transicao t : this.transicoes){
			if (this.getFinais().contains(t.get_final())){
				Transicao t1 = new Transicao(t.getInicial(), t.getLeitura(), null);
				novaTransicoes.add(t1);
			}
		}
		Gramatica g = new Gramatica(estados, alfabeto, novaTransicoes, inicial);
		return g;
/*		ArrayList<NaoTerminal> naoTerminais = new ArrayList<>();
		ArrayList<Terminal> terminais = new ArrayList<>();
		NaoTerminal s = new NaoTerminal(this.getInicial().getNome());
		ArrayList<Producao> producoes = new ArrayList<>();

		for(Estado estado : estados){
			NaoTerminal vn = new NaoTerminal(estado.getNome());
			naoTerminais.add(vn);
		}
		for(char c : alfabeto){
			Terminal vt = new Terminal(c);
			terminais.add(vt);
		}
		int i = 0;
		for (Transicao transicao : transicoes){
			if(!producoes.contains(transicao.getInicial())){
				NaoTerminal n = new NaoTerminal(""+i);
				++i;
				naoTerminais.add(n);
			} else {
				producoes.
			}
			Producao p = new Producao();
			
			if(naoTerminais.contains(transicao.getInicial())){
				naoTerminais.lastIndexOf(transicao.getInicial());
				Producao p = new Producao();
			}
			
			Producao p = new Producao(new NaoTerminal(transicao.getInicial().getNome()), transicao.getLeitura(), new NaoTerminal(transicao.get_final().getNome()));
		}*/
		
	}

	public void determinizar(){
		
	}
	public void minimizar(){
		//this.removerInalcancaveis();
		this.removerMortos();
		this.removerEquivalentes();
	}
	private void removerInalcancaveis() {
		ArrayList<Estado> acessados = new ArrayList<>();
		ArrayList<Estado> aux = new ArrayList<>();
		
		Estado atual = this.inicial;
		do {
			acessados = (ArrayList<Estado>) aux.clone();
			
			aux.add(atual);
			for (Transicao t : this.transicoes){
				if (t.getInicial() == atual && !aux.contains(t.get_final())){
					atual = t.get_final();
					aux.add(atual);
				}
			}
			
		}while(!aux.containsAll(acessados));
		
		this.estados = (ArrayList<Estado>) aux.clone();
		
	}

	private void removerMortos() {
		ArrayList<Estado> acessados = new ArrayList<>();
		//acessados.add(finais.get(0));
		ArrayList<Estado> aux = new ArrayList<>();
		
		//for (Estado e : finais){
			Estado atual = finais.get(0);
			do {
				acessados = (ArrayList<Estado>) aux.clone();
				
				if (!aux.contains(atual)){
					aux.add(atual);
				}
				for (Transicao t : this.transicoes){
					if (t.get_final() == atual && !aux.contains(t.getInicial())){
						
						atual = t.getInicial();
						//System.out.println(atual.getNome());
						aux.add(atual);
					}
				}
				//System.out.println(atual.getNome());
				//System.out.println(aux);
			
			}while(!acessados.containsAll(aux));
		//}
		
		this.estados = (ArrayList<Estado>) aux.clone();
	}
	
	private void removerEquivalentes() {
		// TODO Auto-generated method stub
		
	}

}
