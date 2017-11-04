import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JOptionPane;

public class Automato {
	private boolean completo = false;
	private boolean afnd = true;

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
	
	public void minimizar(){
		this.removerInalcancaveis();
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
		
		ArrayList<Estado> aux2 = new ArrayList<>();
		for (Estado e : this.estados){
			if (this.finais.contains(e)){
				aux2.add(e);
			}
		}
		
		this.finais = (ArrayList<Estado>) aux2.clone();
/*		for (Transicao t1 : transicoes) {
			if (!estados.contains(t1.getInicial()) || !estados.contains(t1.get_final())){
				transicoes.remove(t1);
			}
		}*/
		
	}

	private void removerMortos() {
		ArrayList<Estado> acessados = new ArrayList<>();
		ArrayList<Estado> aux = new ArrayList<>();
		
		for (Estado e : finais){
			//Estado atual = finais.get(0);
			Estado atual = e;
			do {
				acessados = (ArrayList<Estado>) aux.clone();
				
				if (!aux.contains(atual)){
					aux.add(atual);
				}
				for (Transicao t : this.transicoes){
					if (t.get_final() == atual && !aux.contains(t.getInicial())){
						
						atual = t.getInicial();
						aux.add(atual);
					}
				}
			
			}while(!acessados.containsAll(aux));
		}
		
		this.estados = (ArrayList<Estado>) aux.clone();
/*		for (Transicao t1 : this.transicoes) {
			if (!estados.contains(t1.getInicial()) || !estados.contains(t1.get_final())){
				this.transicoes.remove(t1);
			}
		}*/
	}
	
	private void removerEquivalentes() {
		// TODO Auto-generated method stub
		
	}
	
	public boolean linguagemVazia(){
		Automato aux = this;
		aux.completarAutomato();
		aux.removerMortos();
		aux.removerInalcancaveis();
		//boolean fim = this.temFim(); // S -> aS
		return !aux.estados.contains(aux.inicial) || aux.finais.isEmpty();
	}
	
	// Não faz sentido para automatos, só para gramáticas
/*	private boolean temFim() {
		for (Transicao t : transicoes){
			if (t.get_final() == null || this.getFinais().contains(t.get_final())) {
				return true;
			}
		}
		return false;
	}*/

	public boolean linguagemFinita(){
		ArrayList<Estado> aux = new ArrayList<>();
		for (Estado e : estados) {
			for (Transicao t : transicoes){
				if (t.getInicial() == e){
					if (t.get_final() == e){
						return false;
					} /*else {
						for (Estado e1 : aux){
							if (t.get_final() == e1){
								return false;
							}
						}
					}*/
				}
			}
			aux.add(e);
		}
		return true;
	}
	
	public void determinizar() throws CloneNotSupportedException{
		Automato auxiliar = this;
		ArrayList<Transicao> transicoesNovas = new ArrayList<>();
		ArrayList<Transicao> transicoesRemover = new ArrayList<>();
		// Para cada par lado esquerdo - simbolo do alfabeto, colocar os lados direitos em uma lista,
		// criar as transicoes para este novo estado (1 por par lado esquerdo - simbolo do alfabeto),
		// criar as transicoes a partir deste, como sendo uma copia de cada transicao de cada simbolo deste novo estado
		if(this.afnd){
			//Iterator<Estado> erro = this.getEstados().iterator();
			//for(Estado e : this.getEstados()){
			//while (erro.hasNext()) {
			ArrayList<Estado> eAux = this.getEstados();
			for (int i = 0; i<eAux.size();i++){
				Estado e = eAux.get(i);
				//erro.remove();
				char[] cAux = this.getAlfabeto();
				//for (char c : this.getAlfabeto()){
				//while(erro2.hasNext()) {
				for(int j = 0; j < cAux.length;j++){
					//para cada par
					char c = cAux[j];
					ArrayList<Transicao> transicoesTemp = new ArrayList<>();
					for (Transicao t : this.getTransicoes()){
						if(t.getInicial() == e && t.getLeitura() == c){
							// em cada transicao desta, acrescentar na lista
							transicoesTemp.add(t);
						}
					}
					// Encontradas todas as transicoes do par, criar as transicoes para este novo estado
					
					// Apenas se esse par possuir uma relacao nao-deterministica (adicionar epsilon aqui tambem)
					// ex.: S -> aS | aA
					//System.out.println(1);
					if(transicoesTemp.size() > 1){
						String nomeNovoEstado = "[";
						for (Transicao nomeNovo : transicoesTemp){
							nomeNovoEstado += nomeNovo.get_final().getNome()+",";
						}
						nomeNovoEstado += "]";
						//System.out.println(nomeNovoEstado);
						if (!nomeNovoEstado.contains("],]")){
							if (!jahExiste(auxiliar, nomeNovoEstado)){
								//if(true){
								Estado nd = new Estado(nomeNovoEstado);
								auxiliar.estados.add(nd);
								// Criar a transicao para este estado
								Transicao nova = new Transicao(e, c, nd);
								//auxiliar.transicoes.add(nova);
								transicoesNovas.add(nova);

								//Remover as transicoes para os estados antigos (agora, todas vão para o estado conjunto)
								for (Transicao t1 : transicoesTemp){
									transicoesRemover.add(t1);
									nd.getEstadosInternos().add(t1.get_final());
								}
						
								//Para cada estado que compoe o estado novo, criar a transicao deste estado novo,
								// para o final de cada transicao deste estado antigo
								//for (Transicao tnovas : transicoesTemp){
								for (int k = 0; k < transicoesTemp.size(); k++){
									Transicao kt = transicoesTemp.get(k);
									ArrayList<Transicao> tvelhas = this.getTransicoesDoEstado(kt.get_final());
									// Tenho o array das transicoes que saem dos estados que compoem o composto
									// Agora, para cada uma delas, criar as transicoes que saem delas no novo estado
									if (tvelhas != null){
										Interface.mostraAutomato(this);
										for (Transicao novasTransicoes : tvelhas){
											Estado estadoComposto = novasTransicoes.get_final();
											for (Estado internoDoComposto : estadoComposto.getEstadosInternos()){
												Transicao n = new Transicao(nd, novasTransicoes.getLeitura(), internoDoComposto);
												if (!transicoesNovas.contains(n)){
													transicoesNovas.add(n);
												}
											}
										}
									}
								}
							
								// Se o estado nao eh errado, mas ja existe, passar as transicoes para esse estado
							} else {
								//if(true){
								Estado nd = this.getEstadoPorNome(nomeNovoEstado);
								//auxiliar.estados.add(nd);
								// Criar a transicao para este estado
								Transicao nova = new Transicao(e, c, nd);
								//auxiliar.transicoes.add(nova);
								transicoesNovas.add(nova);

								//Remover as transicoes para os estados antigos (agora, todas vão para o estado conjunto)
								for (Transicao t1 : transicoesTemp){
									transicoesRemover.add(t1);
								}
								Interface.mostraAutomato(this);
								//Para cada estado que compoe o estado novo, criar a transicao deste estado novo,
								// para o final de cada transicao deste estado antigo
								//for (Transicao tnovas : transicoesTemp){
/*								for (int k = 0; k < transicoesTemp.size(); k++){
									Transicao kt = transicoesTemp.get(k);
									ArrayList<Transicao> tvelhas = this.getTransicoesDoEstado(kt.get_final());
									// Tenho o array das transicoes que saem dos estados que compoem o composto
									// Agora, para cada uma delas, criar as transicoes que saem delas no novo estado
									if (tvelhas != null){
										Interface.mostraAutomato(this);
										for (Transicao novasTransicoes : tvelhas){
											Transicao n = new Transicao(nd, novasTransicoes.getLeitura(), novasTransicoes.get_final());
											transicoesNovas.add(n);
										}
									}
								}*/
							}
						}
						

					}
				}
			}
			//this.determinizar();
			this.removerEquivalentes();
			//this.removerInalcancaveis();
			this.estados = auxiliar.getEstados();
			this.transicoes.addAll(transicoesNovas);
			this.transicoes.removeAll(transicoesRemover);
			while(this.possuiNaoDeterminacao()){
				this.redeterminizar();
			}
			this.afnd = false;
		} else {
			JOptionPane.showMessageDialog(null, "O autômato já é determinístico");
		}
		
	}

	private boolean possuiNaoDeterminacao() {
		ArrayList<Estado> _finais = new ArrayList<>();
		for (Estado e : this.getEstados()){
			for (char c : this.getAlfabeto()){
				_finais = new ArrayList<>();
				for (Transicao t : this.getTransicoes()){
					if(t.getInicial() == e && t.getLeitura() == c){
						_finais.add(t.get_final());
						//System.out.println(c + "   "+t.get_final().getNome());
					}
				}
				if (_finais.size() > 1){
					return true;
				}
			}
		}
		return false;
	}

	private Estado getEstadoPorNome(String nomeNovoEstado) {
		for (Estado e : this.estados){
			if(e.getNome().equalsIgnoreCase(nomeNovoEstado)){
				return e;
			}
		}
		return null;
	}

	private void redeterminizar() {
		Automato auxiliar = this;
		ArrayList<Transicao> transicoesNovas = new ArrayList<>();
		ArrayList<Transicao> transicoesRemover = new ArrayList<>();
		// Para cada par lado esquerdo - simbolo do alfabeto, colocar os lados direitos em uma lista,
		// criar as transicoes para este novo estado (1 por par lado esquerdo - simbolo do alfabeto),
		// criar as transicoes a partir deste, como sendo uma copia de cada transicao de cada simbolo deste novo estado
		ArrayList<Estado> eAux = this.getEstados();
		for (int i = 0; i<eAux.size();i++){
			Estado e = eAux.get(i);
			//erro.remove();
			char[] cAux = this.getAlfabeto();
			//for (char c : this.getAlfabeto()){
			//while(erro2.hasNext()) {
			for(int j = 0; j < cAux.length;j++){
				//para cada par
				char c = cAux[j];
				ArrayList<Transicao> transicoesTemp = new ArrayList<>();
				for (Transicao t : this.getTransicoes()){
					if(t.getInicial() == e && t.getLeitura() == c){
						// em cada transicao desta, acrescentar na lista
						transicoesTemp.add(t);
					}
				}
				// Encontradas todas as transicoes do par, criar as transicoes para este novo estado
				
				// Apenas se esse par possuir uma relacao nao-deterministica (adicionar epsilon aqui tambem)
				// ex.: S -> aS | aA
				//System.out.println(1);
				if(transicoesTemp.size() > 1){
					String nomeNovoEstado = "[";
					for (Transicao nomeNovo : transicoesTemp){
						nomeNovoEstado += nomeNovo.get_final().getNome()+",";
					}
					nomeNovoEstado += "]";
					//System.out.println(nomeNovoEstado);
					if (!nomeNovoEstado.contains("],]")){
						if (!jahExiste(auxiliar, nomeNovoEstado)){
							//if(true){
							Estado nd = new Estado(nomeNovoEstado);
							auxiliar.estados.add(nd);
							// Criar a transicao para este estado
							Transicao nova = new Transicao(e, c, nd);
							//auxiliar.transicoes.add(nova);
							transicoesNovas.add(nova);

							//Remover as transicoes para os estados antigos (agora, todas vão para o estado conjunto)
							for (Transicao t1 : transicoesTemp){
								transicoesRemover.add(t1);
								nd.getEstadosInternos().add(t1.get_final());
							}
							
							//Para cada estado que compoe o estado novo, criar a transicao deste estado novo,
							// para o final de cada transicao deste estado antigo
							//for (Transicao tnovas : transicoesTemp){
							for (int k = 0; k < transicoesTemp.size(); k++){
								Transicao kt = transicoesTemp.get(k);
								ArrayList<Transicao> tvelhas = this.getTransicoesDoEstado(kt.get_final());
								// Tenho o array das transicoes que saem dos estados que compoem o composto
								// Agora, para cada uma delas, criar as transicoes que saem delas no novo estado
								if (tvelhas != null){
									Interface.mostraAutomato(this);
									for (Transicao novasTransicoes : tvelhas){
										Estado _final = novasTransicoes.get_final();
										for (Estado internoDoComposto : _final.getEstadosInternos()){
											Transicao n = new Transicao(nd, novasTransicoes.getLeitura(), internoDoComposto);
											if (!this.possuiTransicao(transicoesNovas, n)){
												transicoesNovas.add(n);
											}
										}
									}
								}
							}
						

					} else {
							//if(true){
							Estado nd = this.getEstadoPorNome(nomeNovoEstado);
							//auxiliar.estados.add(nd);
							// Criar a transicao para este estado
							Transicao nova = new Transicao(e, c, nd);
							//auxiliar.transicoes.add(nova);
							transicoesNovas.add(nova);

							//Remover as transicoes para os estados antigos (agora, todas vão para o estado conjunto)
							for (Transicao t1 : transicoesTemp){
								transicoesRemover.add(t1);
							}
							Interface.mostraAutomato(this);
							//Para cada estado que compoe o estado novo, criar a transicao deste estado novo,
							// para o final de cada transicao deste estado antigo
							//for (Transicao tnovas : transicoesTemp){
/*							for (int k = 0; k < transicoesTemp.size(); k++){
								Transicao kt = transicoesTemp.get(k);
								ArrayList<Transicao> tvelhas = this.getTransicoesDoEstado(kt.get_final());
								// Tenho o array das transicoes que saem dos estados que compoem o composto
								// Agora, para cada uma delas, criar as transicoes que saem delas no novo estado
								if (tvelhas != null){
									Interface.mostraAutomato(this);
									for (Transicao novasTransicoes : tvelhas){
										Transicao n = new Transicao(nd, novasTransicoes.getLeitura(), novasTransicoes.get_final());
										transicoesNovas.add(n);
									}
								}
							}*/
						}
					}
					

				}
			}
		}
		//this.determinizar();
		this.removerEquivalentes();
		//this.removerInalcancaveis();
		this.estados = auxiliar.getEstados();
		this.transicoes.addAll(transicoesNovas);
		this.transicoes.removeAll(transicoesRemover);
	}

	private boolean possuiTransicao(ArrayList<Transicao> transicoesNovas,
			Transicao n) {
		for (Transicao t : transicoesNovas){
			if (t.get_final().getNome().equals(n.get_final().getNome()) && t.getLeitura() == n.getLeitura()){
				return true;
			}
		}
		return false;
	}

	private boolean jahExiste(Automato aux, String nomeNovoEstado) {
		for (Estado e : aux.getEstados()){
			if(e.getNome().contains(nomeNovoEstado)){
				return true;
			}
		}
		return false;
	}

	private ArrayList<Transicao> getTransicoesDoEstado(Estado e) {
		ArrayList<Transicao> tPorEstado = new ArrayList<>();
		for (Transicao t : this.getTransicoes()){
			if (t.getInicial() == e){
				tPorEstado.add(t);
			}
		}
		return tPorEstado;
	}

}
