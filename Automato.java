import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JOptionPane;

public class Automato {
	private boolean completo = false;
	private boolean afnd = true;

	private ArrayList<Character> alfabeto;
	private ArrayList<Estado> estados;
	private ArrayList<Estado> finais;
	private ArrayList<Transicao> transicoes;
	private Estado inicial;

	public Automato(ArrayList<Character> alfabeto, ArrayList<Estado> estados,
			ArrayList<Estado> finais, ArrayList<Transicao> transicoes,
			Estado inicial) {
		this.alfabeto = alfabeto;
		this.estados = estados;
		this.finais = finais;
		this.transicoes = transicoes;
		this.inicial = inicial;
	}

	public ArrayList<Character> getAlfabeto() {
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
	
	// Cria o automato M1 interseccao ~M2 (se M1 está contido em M2, a LR será vazia), e o automato M2 interseccao ~M1
	// (Se M2 está contido em M1, a LR do resultante será vazia). Se ambas forem vazias, são iguais.
	public boolean linguagensIguais(Automato aut){
		Automato primeiro = this.interseccao(aut.complemento());
		Automato segundo = aut.interseccao(this.complemento());
/*		System.out.println(primeiro.linguagemVazia());
		System.out.println(primeiro.linguagemVazia());*/
		return primeiro.linguagemVazia() && segundo.linguagemVazia();
	}
	
	// Se M1 interseccao ~M2, o automato resultante terá linguagem vazia
	public boolean contida(Automato aut){
		Automato fora = this.interseccao(aut.complemento());
		return fora.linguagemVazia();
	}
	
	// Torna os estados nao-finais em finais, e vice-versa
	public Automato complemento(){
		ArrayList<Estado> novosFinais = new ArrayList<>();
		for (Estado e : this.estados){
			if(!this.finais.contains(e)){
				novosFinais.add(e);
			}
		}
		return new Automato(this.alfabeto, this.estados, novosFinais, this.transicoes, this.inicial);
	}

	// Cria um novo inicial, e copia as transicoes dos iniciais para o novo (thompson sem epsilon-transicao)
	public Automato uniao(Automato aUnir){
		Estado novoInicial = new Estado("Qinicial");
		Estado inicial1 = this.getInicial();
		Estado inicial2 = aUnir.getInicial();
		ArrayList<Transicao> transicoesNovoInicial = new ArrayList<>();
		for (Transicao t1 : this.getTransicoes()){
			if(t1.getInicial() == inicial1){
				Transicao nova1 = new Transicao(novoInicial, t1.getLeitura(), t1.get_final());
				transicoesNovoInicial.add(nova1);
			}
		}
		for (Transicao t2 : aUnir.getTransicoes()){
			if(t2.getInicial() == inicial2){
				Transicao nova2 = new Transicao(novoInicial, t2.getLeitura(), t2.get_final());
				transicoesNovoInicial.add(nova2);
			}
		}
		transicoesNovoInicial.addAll(this.getTransicoes());
		transicoesNovoInicial.addAll(aUnir.getTransicoes());
		ArrayList<Estado> novosEstados = new ArrayList<>();
		
		novosEstados.addAll(this.getEstados());
		novosEstados.addAll(aUnir.getEstados());
		novosEstados.add(novoInicial);
		
		ArrayList<Estado> novosFinais = new ArrayList<>();
		novosFinais.addAll(this.getFinais());
		novosFinais.addAll(aUnir.getFinais());
		
		return new Automato(this.getAlfabeto(), novosEstados, novosFinais, transicoesNovoInicial, novoInicial);
	}
	
	// Usa a propriedade da teoria de conjuntos para fazer a interseccao a partir do complemento da uniao dos complementos
	public Automato interseccao(Automato aIntersec){
		// L1 ^ L2 = not(not(L1)U(not(L2)))
		Automato intersec = (this.complemento().uniao(aIntersec.complemento())).complemento();
		return intersec;
	}
	
	// Nao usado, apenas transita entre estados como se o automato estivesse sendo usado
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

	// Para todos simbolos onde nao há transicao, criar a transicao para um estado novo (de erro)
	public void completarAutomato() {
		boolean temtransicao = false;
		boolean estadoErro = false;
		Estado estadoDeErro = new Estado("qe");

		if (!completo) {
			for (Character caracter : alfabeto) {
				if (caracter != '&' && !caracter.equals('\0')){
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
			}

			if (estadoErro) {
				this.estados.add(estadoDeErro);
				for (Character possiveis : alfabeto){
					Transicao nova = new Transicao(estadoDeErro, possiveis, estadoDeErro);
					this.getTransicoes().add(nova);
				}
			}
			
			this.completo = true;

		} else {
			System.out.println("O automato já está completo!");
		}

	}

	// Copia os conjuntos de automato para gramatica, apenas interpretando os estados finais como novas transicoes na gramatica
	//, onde apenas um simbolo eh gerado e ela encerra as producoes
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
	// Na ordem para evitar ter que repetir passos, remove os estados mortos, os estados inalcancaveis e os estados equivalentes
	public void minimizar(){
		this.removerMortos();
		this.removerInalcancaveis();
		this.removerEquivalentes();
	}
	
	// Comeca no estado inicial. Adiciona os estados alcancaveis a partir do atual e marca eles. Continua por todos os estados até sobrarem apenas estados marcados
	public void removerInalcancaveis() {
		ArrayList<Estado> acessados = new ArrayList<>();
		ArrayList<Estado> aux = new ArrayList<>();
		
		Estado atual = this.inicial;
		aux.add(atual);
		do {
			acessados = (ArrayList<Estado>) aux.clone();
			
			for (int i = 0; i<acessados.size();i++){
				atual = acessados.get(i);
				for (Transicao t : this.getTransicoes()){
					if(t.getInicial() == atual){
						if (!aux.contains(t.get_final())){
							aux.add(t.get_final());
						}
					}
				}
			}
/*			aux.add(atual);
			for (int i = 0; i<this.getTransicoes().size();i++){
				Transicao t = this.getTransicoes().get(i);
			for (Transicao t : this.transicoes){
				if (t.getInicial() == atual && !aux.contains(t.get_final())){
					atual = t.get_final();
					System.out.println(atual.getNome());
					aux.add(atual);
				}
			}*/
			
		}while(!acessados.containsAll(aux));
		
		this.estados = (ArrayList<Estado>) acessados.clone();
		
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
	
	// Similar a remocao de inalcancaveis, porem comeca nos finais
	public void removerMortos() {
		ArrayList<Estado> acessados = new ArrayList<>();
		ArrayList<Estado> aux = new ArrayList<>();
		
		aux.addAll(this.getFinais());
		
		do{
			acessados = (ArrayList<Estado>) aux.clone();
			for (int i = 0; i<aux.size();i++){
				for(Transicao t : this.getTransicoes()){
					Estado atual = t.get_final();
					if(atual==aux.get(i)){
						if(!aux.contains(t.getInicial())){
							aux.add(t.getInicial());
						}
					}
				}
			}
		}while(!acessados.containsAll(aux));
/*		for (Estado e : finais){
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
		}*/
		
		this.estados = (ArrayList<Estado>) aux.clone();
/*		for (Transicao t1 : this.transicoes) {
			if (!estados.contains(t1.getInicial()) || !estados.contains(t1.get_final())){
				this.transicoes.remove(t1);
			}
		}*/
	}
	
	// Checa por equivalencias entre estados, usando o metodo visto em sala, o qual separa os estados em conjuntos
	// de equivalencia, até nenhum conjunto novo ser criado
	public void removerEquivalentes() {
		// TODO Auto-generated method stub
		ArrayList<ArrayList<Estado>> gruposDeEquivalencia = new ArrayList<>();
		ArrayList<Estado> naoFinais = this.getNaoFinais();
		gruposDeEquivalencia.add((ArrayList<Estado>) this.finais.clone());
		gruposDeEquivalencia.add((ArrayList<Estado>) naoFinais.clone());
		boolean criouNovasClasses = false;
		do {
			criouNovasClasses = false;
			for (int i = 0; i < gruposDeEquivalencia.size();i++){
				ArrayList<Estado> grupo = gruposDeEquivalencia.get(i);
				ArrayList<Estado> possivelNovoGrupo = new ArrayList<>();
				for (int j = 0; j < grupo.size();j++){
					Estado e = grupo.get(j);
					
					// Para cada estado do grupo, comparar com os outros
					for (Estado comparado : grupo){
						if (!this.mesmasTransicoes(getTransicoesDoEstado(e), getTransicoesDoEstado(comparado))){
							grupo.remove(e);
							possivelNovoGrupo.add(e);
							criouNovasClasses = true;
							break;
						// Se as transicoes pertencem ao mesmo grupo, trocar o grupo
						} else if (!this.mesmoGrupo(gruposDeEquivalencia, getTransicoesDoEstado(e), getTransicoesDoEstado(comparado))){
							grupo.remove(e);
							possivelNovoGrupo.add(e);
							criouNovasClasses = true;
							break;
						}
					}
				}
				if (!possivelNovoGrupo.isEmpty()){
					gruposDeEquivalencia.add(possivelNovoGrupo);
				} else {
					break;
				}
			}
		} while(criouNovasClasses == true);
		// Remover os estados a mais (mais de um no mesmo grupo) (substituir as transicoes de um pelo que sobrar
		System.out.println(gruposDeEquivalencia);
/*		for (ArrayList<Estado> a : gruposDeEquivalencia){
			for (Estado e : a){
				System.out.print(e.getNome());
			}
		}*/
		for (ArrayList<Estado> a : gruposDeEquivalencia){
			if(a.size() > 0){
				Estado doGrupo = a.get(0);
				a.remove(doGrupo);
				for(Estado resto : a){
					for (Transicao t : this.getTransicoesDoEstado(resto)){
						Transicao tNew = new Transicao(t.getInicial(), t.getLeitura(), doGrupo);
						this.transicoes.add(tNew);
						this.transicoes.remove(t);
					}
					if(this.finais.contains(resto)){
						this.finais.remove(resto);
					}
					this.estados.remove(resto);
				}
			}
		}
	}
	
	// funcao que verifica se dois estados pertencem ao mesmo grupo de equivalencia, com base em suas transicoes
	private boolean mesmoGrupo(
			ArrayList<ArrayList<Estado>> gruposDeEquivalencia,
			ArrayList<Transicao> transicoesDoEstado,
			ArrayList<Transicao> transicoesDoEstado2) {
		for (Character c : this.getAlfabeto()){
			for (Transicao t1 : transicoesDoEstado){
				for (Transicao t2 : transicoesDoEstado2){
					if(t1.getLeitura() == c && t2.getLeitura() == c){
						Estado final1 = t1.get_final();
						Estado final2 = t2.get_final();
						for (ArrayList<Estado> array : gruposDeEquivalencia){
							if (array.contains(final1)){
								if (!array.contains(final2)){
									return false;
								}
							} else {
								if (array.contains(final2)){
									return false;
								}
							}
						}
					}
				}
			}
		}
		
/*		for (Transicao t : transicoesDoEstado){
			for (Transicao t2 : transicoesDoEstado2){
				if (t.getLeitura() == t2.getLeitura()){
					Estado final1 = t.get_final();
					Estado final2 = t2.get_final();
					//System.out.println(t.getInicial());
					for (ArrayList<Estado> array : gruposDeEquivalencia){
							if ((array.contains(final1) && !array.contains(final2))
									|| (array.contains(final2) && !array.contains(final1))){
								return false;
							}
					}
				}
			}
		}*/
		return true;
	}
	
	// verifica se um estado possui as mesmas transicoe de outro
	private boolean mesmasTransicoes(ArrayList<Transicao> transicoesDoEstado,
			ArrayList<Transicao> transicoesDoEstado2) {
		ArrayList<Boolean> iguais = new ArrayList<>();
		
		for (Transicao t : transicoesDoEstado){
			boolean possuiTransicao = false;
			for (Transicao t2 : transicoesDoEstado2){
				if (t.get_final() == t2.get_final() && t.getLeitura() == t2.getLeitura()){
					possuiTransicao = true;
				}
			}
			iguais.add(possuiTransicao);
		}
		
		for (Transicao t : transicoesDoEstado2){
			boolean possuiTransicao = false;
			for (Transicao t2 : transicoesDoEstado){
				if (t.get_final() == t2.get_final() && t.getLeitura() == t2.getLeitura()){
					possuiTransicao = true;
				}
			}
			iguais.add(possuiTransicao);
		}
		for (Boolean b : iguais){
			if(b == false){
				return false;
			}
		}
		return true;
	}

	private ArrayList<Estado> getNaoFinais() {
		ArrayList<Estado> naoFinais = new ArrayList<>();
		for (Estado e : estados){
			if(!finais.contains(e)){
				naoFinais.add(e);
			}
		}
		return naoFinais;
	}

	//verifica se o estado inicial eh infertil
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

	// Procura por recursoes no automato, se tiver, a linguagem eh infinita
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
	
	// Gera estados compostos a partir das indeterminacoes, e adapta o automato para transitar para estes novos estados, determinizando-o
	public void determinizar() throws CloneNotSupportedException{
		this.completarAutomato();
		//Interface.mostraAutomato(this);
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
				ArrayList<Character> cAux = this.getAlfabeto();
				//for (char c : this.getAlfabeto()){
				//while(erro2.hasNext()) {
				for(int j = 0; j < cAux.size();j++){
					//para cada par
					char c = cAux.get(j);
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
							for (Estado internoNovo : nomeNovo.get_final().getEstadosInternos()){
								nomeNovoEstado += internoNovo.getNome()+",";
							}
						}
						nomeNovoEstado += "]";
						//System.out.println(nomeNovoEstado);
						//if (!nomeNovoEstado.contains("],]") && !nomeNovoEstado.contains("[[") && nomeNovoEstado.contains(",q")){
						if (!nomeNovoEstado.contains("],]") && !nomeNovoEstado.contains("[[")){
							if (!jahExiste(auxiliar, nomeNovoEstado)){
								//if(true){
								Estado nd = new Estado(nomeNovoEstado);
								auxiliar.estados.add(nd);
								//System.out.println(nd.getEstadosInternos());
								// Criar a transicao para este estado
								Transicao nova = new Transicao(e, c, nd);
								//auxiliar.transicoes.add(nova);
								//if(!auxiliar.getTransicoes().contains(nova)){
								transicoesNovas.add(nova);
								//}

								//Remover as transicoes para os estados antigos (agora, todas vão para o estado conjunto)
								for (Transicao t1 : transicoesTemp){
									transicoesRemover.add(t1);
									//if(!nd.getEstadosInternos().contains(t1.get_final())){
										nd.getEstadosInternos().add(t1.get_final());
									//}
								}
								
								// Se o estado composto possuir um estado final, ele eh final
								boolean estadoEhFinal = false;
								for (Estado verificarFinal : nd.getEstadosInternos()){
									if (this.getFinais().contains(verificarFinal)){
										estadoEhFinal = true;
									}
								}
								if (estadoEhFinal){
									this.getFinais().add(nd);
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
								//if(!auxiliar.getTransicoes().contains(nova)){
									transicoesNovas.add(nova);
								//}

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
						

					} else if (c == '&'){
						String nomeNovoEstado1 = "[";
						nomeNovoEstado1 += e.getNome()+",";
						for (Transicao nomeNovo : transicoesTemp){
							for (Estado internoNovo : nomeNovo.get_final().getEstadosInternos()){
								nomeNovoEstado1 += internoNovo.getNome()+",";
							}
						}
						nomeNovoEstado1 += "]";
						//if (!nomeNovoEstado1.contains("],]") && !nomeNovoEstado1.contains("[[")&& nomeNovoEstado1.contains(",q")){
						if (!nomeNovoEstado1.contains("],]") && !nomeNovoEstado1.contains("[[")){
							if (!jahExiste(auxiliar, nomeNovoEstado1)){
								//if(true){
								Estado nd = new Estado(nomeNovoEstado1);
								if (!nd.getEstadosInternos().contains(e)){
									nd.getEstadosInternos().add(e);
								}
								//nd.getEstadosInternos().add

								
								auxiliar.estados.add(nd);
								// Criar a transicao para este estado
								//Transicao nova = new Transicao(e, c, nd);
								//auxiliar.transicoes.add(nova);
								//transicoesNovas.add(nova);

								//Remover as transicoes para os estados antigos (agora, todas vão para o estado conjunto)
								for (Transicao t1 : transicoesTemp){
									//transicoesRemover.add(t1);
									Estado internoComp = t1.get_final();
									if (!nd.getEstadosInternos().contains(internoComp)){
										nd.getEstadosInternos().add(t1.get_final());
									}
								}
								for (Estado interno : nd.getEstadosInternos()){
									System.out.println(interno.getNome());
								}
								// Se o estado composto possuir um estado final, ele eh final
								boolean estadoEhFinal = false;
								for (Estado verificarFinal : nd.getEstadosInternos()){
									if (this.getFinais().contains(verificarFinal)){
										estadoEhFinal = true;
									}
								}
								if (estadoEhFinal){
									this.getFinais().add(nd);
								}
								//System.out.println(e.getNome());
								//System.out.println("epsilon e novo");
								ArrayList<Transicao> transicoesDoEstadoQueDerivaEpsilon = this.getTransicoesDoEstado(e);
								for (Transicao transicao : transicoesDoEstadoQueDerivaEpsilon){
									Transicao transicaoDoInicialParaOCompostoEpsilon = new Transicao(nd, c, transicao.get_final());
									if(!this.possuiTransicao(auxiliar.getTransicoes(), transicaoDoInicialParaOCompostoEpsilon))
									{
										auxiliar.getTransicoes().add(transicaoDoInicialParaOCompostoEpsilon);
									}
								}
								//Para cada estado que compoe o estado novo, criar a transicao deste estado novo,
								// para o final de cada transicao deste estado antigo
								for (int k = 0; k < nd.getEstadosInternos().size();k++){
									ArrayList<Transicao> transicoesInternas = this.getTransicoes();
									for(int l = 0; l< transicoesInternas.size();l++){
										Transicao interna = transicoesInternas.get(l);
										if (interna.getInicial().getNome().equals(nd.getEstadosInternos().get(k).getNome())){
											if(!this.getTransicoesDoEstado(nd).contains(interna)){
												Transicao novaComposto = new Transicao(nd, interna.getLeitura(), interna.get_final());
												//if(!auxiliar.getTransicoesDoEstado(nd).contains(novaComposto)){
												if(!transicoesNovas.contains(novaComposto)){
													transicoesNovas.add(novaComposto);
												}
											}
										}
									}
								}
								//for (Transicao tnovas : transicoesTemp){
/*								for (int k = 0; k < transicoesTemp.size(); k++){
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
								}*/
							
								// Se o estado nao eh errado, mas ja existe, passar as transicoes para esse estado
							} else {
								//if(true){
								Estado nd = this.getEstadoPorNome(nomeNovoEstado1);
								//auxiliar.estados.add(nd);
								// Criar a transicao para este estado
								//Transicao nova = new Transicao(e, c, nd);
								//auxiliar.transicoes.add(nova);
								//transicoesNovas.add(nova);

								//Remover as transicoes para os estados antigos (agora, todas vão para o estado conjunto)
/*								for (Transicao t1 : transicoesTemp){
									transicoesRemover.add(t1);
								}*/
								//System.out.println("epsilon mas ja existe");
								Interface.mostraAutomato(this);
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
												Transicao n = new Transicao(nd, novasTransicoes.getLeitura(), novasTransicoes.get_final());
												if (!auxiliar.getTransicoesDoEstado(nd).contains(n)){
													transicoesNovas.add(n);
												}
										}
									}
								}
							}
						}
					}
				}
			}
			//this.determinizar();
			//this.removerEquivalentes();
			//this.removerInalcancaveis();
			this.estados = auxiliar.getEstados();
			this.transicoes.addAll(transicoesNovas);
			this.transicoes.removeAll(transicoesRemover);
			while(this.possuiNaoDeterminacao()){
				this.redeterminizar();
			}
			//this.removerEquivalentes();
/*			this.completo = false;
			this.completarAutomato();*/
			this.afnd = false;
		} else {
			JOptionPane.showMessageDialog(null, "O autômato já é determinístico");
		}
		
	}

	// Verifica se o automato eh nao-deterministico
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

	// Pega o estado pelo nome. Auxiliar em algumas funcoes maiores
	private Estado getEstadoPorNome(String nomeNovoEstado) {
		for (Estado e : this.estados){
			if(e.getNome().equalsIgnoreCase(nomeNovoEstado)){
				return e;
			}
		}
		return null;
	}

	// auxiliar para a determinizacao, essencialmente serve para repetir o procedimento ateh nao haverem mais indeterminizacoes
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
			ArrayList<Character> cAux = this.getAlfabeto();
			//for (char c : this.getAlfabeto()){
			//while(erro2.hasNext()) {
			for(int j = 0; j < cAux.size();j++){
				//para cada par
				char c = cAux.get(j);
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
						for (Estado internoNovo : nomeNovo.get_final().getEstadosInternos()){
							nomeNovoEstado += internoNovo.getNome()+",";
						}
					}
					nomeNovoEstado += "]";
					//System.out.println(nomeNovoEstado);
					//if (!nomeNovoEstado.contains("],]") && !nomeNovoEstado.contains("[[") && nomeNovoEstado.contains(",q")){
					if (!nomeNovoEstado.contains("],]") && !nomeNovoEstado.contains("[[")){
						if (!jahExiste(auxiliar, nomeNovoEstado)){
							//if(true){
							Estado nd = new Estado(nomeNovoEstado);
							if (!nd.getEstadosInternos().contains(e)){
								nd.getEstadosInternos().add(e);
							}
							
							auxiliar.estados.add(nd);
							// Criar a transicao para este estado
							Transicao nova = new Transicao(e, c, nd);
							//auxiliar.transicoes.add(nova);
							if(!auxiliar.getTransicoes().contains(nova)){
								transicoesNovas.add(nova);
							}

							//Remover as transicoes para os estados antigos (agora, todas vão para o estado conjunto)
							for (Transicao t1 : transicoesTemp){
								transicoesRemover.add(t1);
								Estado internoComp = t1.get_final();
								if (!nd.getEstadosInternos().contains(internoComp)){
									nd.getEstadosInternos().add(t1.get_final());
								}
							}
							// Se o estado composto possuir um estado final, ele eh final
							boolean estadoEhFinal = false;
							for (Estado verificarFinal : nd.getEstadosInternos()){
								if (this.getFinais().contains(verificarFinal)){
									estadoEhFinal = true;
								}
							}
							if (estadoEhFinal){
								this.getFinais().add(nd);
							}
							
							for (Estado interno : nd.getEstadosInternos()){
								System.out.println(interno.getNome());
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
							if (!auxiliar.getTransicoes().contains(nova)){
								transicoesNovas.add(nova);
							}

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
					

				} else if (c == '&'){
					String nomeNovoEstado1 = "[";
					nomeNovoEstado1 += e.getNome()+", ";
					for (Transicao nomeNovo : transicoesTemp){
						for (Estado internoNovo : nomeNovo.get_final().getEstadosInternos()){
							nomeNovoEstado1 += internoNovo.getNome()+",";
						}
					}
					nomeNovoEstado1 += "]";
					//if (!nomeNovoEstado1.contains("],]") && !nomeNovoEstado1.contains("[[")&& nomeNovoEstado1.contains(",q")){
					if (!nomeNovoEstado1.contains("],]") && !nomeNovoEstado1.contains("[[")){
						if (!jahExiste(auxiliar, nomeNovoEstado1)){
							//if(true){
							Estado nd = new Estado(nomeNovoEstado1);
							auxiliar.estados.add(nd);
							
							nd.getEstadosInternos().add(e);
							// Criar a transicao para este estado
							//Transicao nova = new Transicao(e, c, nd);
							//auxiliar.transicoes.add(nova);
							//transicoesNovas.add(nova);

							//Remover as transicoes para os estados antigos (agora, todas vão para o estado conjunto)
							for (Transicao t1 : transicoesTemp){
								//transicoesRemover.add(t1);
								if (!nd.getEstadosInternos().contains(t1.get_final())){
									nd.getEstadosInternos().add(t1.get_final());
								}
							}
							
							// Se o estado composto possuir um estado final, ele eh final
							boolean estadoEhFinal = false;
							for (Estado verificarFinal : nd.getEstadosInternos()){
								if (this.getFinais().contains(verificarFinal)){
									estadoEhFinal = true;
								}
							}
							if (estadoEhFinal){
								this.getFinais().add(nd);
							}
							
/*							ArrayList<Transicao> transicoesDoEstadoQueDerivaEpsilon = this.getTransicoesDoEstado(e);
							for (Transicao transicao : transicoesDoEstadoQueDerivaEpsilon){
								transicoesTemp.add(transicao);
							}*/
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
											if (!this.getTransicoesDoEstado(nd).contains(n)){
												transicoesNovas.add(n);
											}
										}
									}
								}
							}
						
							// Se o estado nao eh errado, mas ja existe, passar as transicoes para esse estado
						} else {
							//if(true){
							Estado nd = this.getEstadoPorNome(nomeNovoEstado1);
							//auxiliar.estados.add(nd);
							// Criar a transicao para este estado
							Transicao nova = new Transicao(e, c, nd);
							//auxiliar.transicoes.add(nova);
							//transicoesNovas.add(nova);

							//Remover as transicoes para os estados antigos (agora, todas vão para o estado conjunto)
/*							for (Transicao t1 : transicoesTemp){
								transicoesRemover.add(t1);
							}*/
							Interface.mostraAutomato(this);
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
										
										Transicao n = new Transicao(nd, novasTransicoes.getLeitura(), novasTransicoes.get_final());
										if(!this.getTransicoesDoEstado(nd).contains(n)){
											transicoesNovas.add(n);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		//this.determinizar();
		//this.removerEquivalentes();
		//this.removerInalcancaveis();
		this.estados = auxiliar.getEstados();
		this.transicoes.addAll(transicoesNovas);
		this.transicoes.removeAll(transicoesRemover);
	}

	// Verifica se o automato possui determinada transicao
	private boolean possuiTransicao(ArrayList<Transicao> transicoesNovas,
			Transicao n) {
		for (Transicao t : transicoesNovas){
			if (t.get_final().getNome().equals(n.get_final().getNome()) && t.getLeitura() == n.getLeitura()){
				return true;
			}
		}
		return false;
	}

	// Verifica se um estado com o nome "nomeNovoEstado" jah existe. Auxilia na determinizacao
	private boolean jahExiste(Automato aux, String nomeNovoEstado) {
		for (Estado e : aux.getEstados()){
			if(e.getNome().equals(nomeNovoEstado)){
				return true;
			}
		}
		return false;
	}

	// Pega as transicoes do estado e
	private ArrayList<Transicao> getTransicoesDoEstado(Estado e) {
		ArrayList<Transicao> tPorEstado = new ArrayList<>();
		for (Transicao t : this.getTransicoes()){
			if (t.getInicial() == e){
				tPorEstado.add(t);
			}
		}
		return tPorEstado;
	}
	
	// Troca os estadoss finais por nao-finais e vice-versa, sem retornar um novo automato
	@SuppressWarnings("unchecked")
	public void complementar() {
		ArrayList<Estado> novosFinais = new ArrayList<>();
		for (Estado e : this.estados){
			if(!this.finais.contains(e)){
				novosFinais.add(e);
			}
		}
		this.finais = new ArrayList<Estado>();
		this.finais = (ArrayList<Estado>) novosFinais.clone();
		
	}

}
