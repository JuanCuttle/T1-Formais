import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Interface {
	
	// Gera uma string para facilitar a visualizacao do automato
	public static String mostraAutomato(Automato aut) {
		String tabela = "";
		for (Character c : aut.getAlfabeto()) {
			//if (!c.equals('\0')){
				tabela = tabela + ("      |   " + c);
				System.out.print("      |   " + c);
			//}
		}
		tabela += ("\n");
		System.out.println("");

		for (Estado e : aut.getEstados()) {
			if (aut.getFinais().contains(e)) {
				tabela += ("*");
				System.out.print("*");
			}
			if (e == aut.getInicial()) {
				tabela += ("->");
				System.out.print("->");
			}
			tabela += (e.getNome() + "  |     ");
			System.out.print(e.getNome() + "  |     ");
			for (char c : aut.getAlfabeto()) {
				// Para cada estado, para cada letra do alfabeto, mostrar:

				for (Transicao t : aut.getTransicoes()) {
					if (t.getInicial() == e && t.getLeitura() == c) {
						// Se a transicao sai do estado em questao, pelo simbolo
						// em questao, imprimir o final e virgula
						tabela += (t.get_final().getNome() + ", ");
						System.out.print(t.get_final().getNome() + ", ");
					}
				}
				// Terminaram os estados por este simbolo, imprimir espaço e
				// avançar
				tabela += ("        ");
				System.out.print("        ");
			}
			// Terminaram os símbolos, imprime nova linha e reinicia o processo
			// para o proximo estado
			tabela += ("\n");
			System.out.println("");
		}
		//JOptionPane.showMessageDialog(null, tabela);
		return tabela;
	}

	// Gera uma string para faciliar a visualizacao de uma gramatica
	public static String mostraGramatica(Gramatica g) {
		String gram = "";
		for (Estado e : g.getNaoTerminais()) {
			gram = gram + e.getNome() + " -> ";
			System.out.print(e.getNome() + " -> ");
			for (Transicao t : g.getProducoes()) {
				if (t.getInicial() == e) {
					// Para cada transicao do estado, imprimir ela e " | "
					if (t.get_final() == null) {
						// Se a transicao vai só para letra, imprime só a letra
						gram = gram +t.getLeitura() + " | ";
						System.out.print(t.getLeitura() + " | ");
					} else {
						// Se vai para outro estado, mostra ele também
						gram = gram + t.getLeitura()
								+ t.get_final().getNome() + " | ";
						System.out.print(t.getLeitura()
								+ t.get_final().getNome() + " | ");
					}
				}
			}
			// Terminaram as transicoes, imprime nova linha e reinicia o processo no
			// proximo estado
			gram = gram + "\n";
			System.out.println("");
		}
		//JOptionPane.showMessageDialog(null, gram);
		gram = gram + "\n";
		System.out.println("");
		return gram;
	}
	
	// Permite que o usuario interaja com o modelo, para construir uma gramatica nova
	public static Gramatica criarGramatica(){
		Gramatica g = null;
		ArrayList<Estado> naoTerminais = new ArrayList<>();
		ArrayList<Transicao> producoes = new ArrayList<>();
		
		ArrayList<Character> alfabeto = new ArrayList<>();
		//int i = -1;
		int mais = JOptionPane.showConfirmDialog(null, "Deseja adicionar um símbolo (terminal) ao alfabeto?");
		while(mais == 0) {
			//i++;
			String caracter = JOptionPane.showInputDialog("Digite o caracter (simbolo único, minusculo ou digito):");
			char c = caracter.charAt(0);
			alfabeto.add(c);
			mais = JOptionPane.showConfirmDialog(null, "Deseja adicionar mais um símbolo terminal?");
		}
		
		String nomeEstado = JOptionPane.showInputDialog("Digite o nome do simbolo inicial:");
		Estado inicial = new Estado(nomeEstado);
		naoTerminais.add(inicial);
/*		int inicialFinal = JOptionPane.showConfirmDialog(null, "O simbolo inicial é final?");
		if (inicialFinal == 0){
			finais.add(inicial);
		}*/
		int confirmE = JOptionPane.showConfirmDialog(null, "Deseja criar um simbolo não-terminal?");
		while (confirmE == 0){
			String nome = JOptionPane.showInputDialog("Digite o nome do simbolo não-terminal novo:");
			Estado novo = new Estado(nome);
			naoTerminais.add(novo);
			
			confirmE = JOptionPane.showConfirmDialog(null, "Deseja criar mais um simbolo não-terminal?");
		}
		
		for (int z = 0; z < naoTerminais.size(); z++){
			Estado nulo = naoTerminais.get(z);
			if (nulo.getNome() == null){
				naoTerminais.remove(nulo);
			}
		}
		
		int confirmP = JOptionPane.showConfirmDialog(null, "Deseja criar uma produção?");
		while (confirmP == 0){
			String nomeI = JOptionPane.showInputDialog("Digite o nome do estado no lado esquerdo da produção nova:");
			
			Estado ladoEsquerdo = Principal.getEstadoPorNome(nomeI, naoTerminais);
			if (ladoEsquerdo != null){
				String letra = JOptionPane.showInputDialog("Digite o caracter gatilho da produção (simbolo único, minusculo ou digito):");
				char l = letra.charAt(0);
				if (Principal.letraPertenceAoAlfabeto(l, alfabeto)){
					String nomeF = JOptionPane.showInputDialog("Digite o nome do estado no lado direito da produção nova (se não houver, deixe em branco):");
					if (!nomeF.equalsIgnoreCase("")){
						Estado ladoDireito = Principal.getEstadoPorNome(nomeF, naoTerminais);
						if (ladoDireito != null) {
							Transicao nova = new Transicao(ladoEsquerdo, l, ladoDireito);
							if (!Principal.possuiProducao(producoes, ladoEsquerdo, l, ladoDireito)){
								producoes.add(nova);
							}
						}
					} else{
						Transicao nova = new Transicao(ladoEsquerdo, l, null);
						if (!Principal.possuiProducao(producoes, ladoEsquerdo, l, null)){
							producoes.add(nova);
						}
					}
					
				}
			}
			String gram = Interface.mostraGramatica(new Gramatica(naoTerminais, alfabeto, producoes, inicial));
			confirmP = JOptionPane.showConfirmDialog(null, "Deseja criar mais uma produção?\n"+"Gramatica atual:\n"+gram);
		}
		// Adicionar os proprios estados na composicao de cada estado
		for (Estado nt : naoTerminais){
			nt.getEstadosInternos().add(nt);
		}
		
		g = new Gramatica(naoTerminais, alfabeto, producoes, inicial);
		//g.setPosicaoTerminais(i);
		System.out.println("Gramatica gerada: ");
		Interface.mostraGramatica(g);
		return g;
	}

	// Permite ao usuario interagir com o modelo, de forma a editar uma gramatica previamente criada
	public static void editarGramatica(Gramatica gramatica) {
		ArrayList<Estado> naoTerminais = gramatica.getNaoTerminais();
		ArrayList<Transicao> producoes = gramatica.getProducoes();
		
		ArrayList<Character> alfabeto = gramatica.getTerminais();
		//int i = gramatica.getPosicaoTerminais();
/*		ArrayList<Character> novoAlfabeto = new ArrayList<>();
		for (int a = 0; a < alfabeto.size(); a++){
				novoAlfabeto.add(alfabeto[a]);
		}*/
		int mais = JOptionPane.showConfirmDialog(null, "Deseja adicionar um símbolo (terminal) ao alfabeto?");
		while(mais == 0) {
			String caracter = JOptionPane.showInputDialog("Digite o caracter (simbolo único, minusculo ou digito):");
			char c = caracter.charAt(0);
			boolean jahExiste = false;
			for (char letras : alfabeto){
				if (letras == c){
					jahExiste = true;
				}
			}
			if (!jahExiste){
				//i++;
				alfabeto.add(c);
				//novoAlfabeto.add(c);
			}
			mais = JOptionPane.showConfirmDialog(null, "Deseja adicionar mais um símbolo terminal?");
		}
		
		int menos = JOptionPane.showConfirmDialog(null, "Deseja remover um símbolo (terminal) do alfabeto?");
		while(menos == 0) {
			String caracter = JOptionPane.showInputDialog("Digite o caracter (simbolo único, minusculo ou digito):");
			char c = caracter.charAt(0);
			boolean jahExiste = false;
			for (char letras : alfabeto){
				if (letras == c){
					jahExiste = true;
				}
			}
			if (jahExiste){
				alfabeto.remove(c);
			}
			menos = JOptionPane.showConfirmDialog(null, "Deseja remover mais um símbolo terminal?");
		}
		//System.out.println(novoAlfabeto);
		//gramatica.setTerminais(novoAlfabeto);
		for (int index = 0; index < producoes.size();index++){
			Transicao trans = producoes.get(index);
			if(!alfabeto.contains(trans.getLeitura())){
				producoes.remove(trans);
			}
		}
		//gramatica.setPosicaoTerminais(i);
		
		
/*		String nomeEstado = JOptionPane.showInputDialog("Digite o nome do simbolo inicial:");
		Estado inicialNovo = Principal.getEstadoPorNome(nomeEstado, naoTerminais); 
		if (inicialNovo != null){
			System.out.println(inicialNovo.getNome());
			gramatica.setInicial(inicialNovo);
		} else {
			System.out.println("nulo");
			Estado inicial = new Estado(nomeEstado);
			naoTerminais.add(inicial);
			gramatica.setInicial(inicialNovo);
		}*/
/*		int inicialFinal = JOptionPane.showConfirmDialog(null, "O simbolo inicial é final?");
		if (inicialFinal == 0){
			finais.add(inicial);
		}*/
		int confirmE = JOptionPane.showConfirmDialog(null, "Deseja criar um simbolo não-terminal?");
		while (confirmE == 0){
			String nome = JOptionPane.showInputDialog("Digite o nome do simbolo não-terminal novo:");
			Estado estadoNovo = Principal.getEstadoPorNome(nome, naoTerminais);
			if (estadoNovo == null){
				Estado novo = new Estado(nome);
				novo.getEstadosInternos().add(novo);
				naoTerminais.add(novo);
			} else {
				JOptionPane.showMessageDialog(null, "Este estado já existe!");
			}
			
			confirmE = JOptionPane.showConfirmDialog(null, "Deseja criar mais um simbolo não-terminal?");
		}
		
		int confirmE2 = JOptionPane.showConfirmDialog(null, "Deseja remover um simbolo não-terminal?");
		while (confirmE2 == 0){
			String nome = JOptionPane.showInputDialog("Digite o nome do simbolo não-terminal a remover:");
			Estado estadoNovo = Principal.getEstadoPorNome(nome, naoTerminais);
			if (estadoNovo != null){
				naoTerminais.remove(estadoNovo);
			} else {
				JOptionPane.showMessageDialog(null, "Este simbolo não existe!");
			}
			
			confirmE2 = JOptionPane.showConfirmDialog(null, "Deseja remover mais um simbolo não-terminal?");
		}
		
		for (int z = 0; z < naoTerminais.size(); z++){
			Estado nulo = naoTerminais.get(z);
			if (nulo.getNome() == null || nulo == null){
				naoTerminais.remove(nulo);
			}
		}
		
		for (int index = 0; index < producoes.size();index++){
			Transicao trans = producoes.get(index);
			if(!naoTerminais.contains(trans.getInicial()) || (!naoTerminais.contains(trans.get_final()) && trans.get_final() != null) || trans.getInicial() == null){
				producoes.remove(trans);
			}
		}
		
		String gram = Interface.mostraGramatica(gramatica);
		int confirmP = JOptionPane.showConfirmDialog(null, "Deseja criar uma produção?\n"+"Gramatica atual:\n"+gram);
		while (confirmP == 0){
			String nomeI = JOptionPane.showInputDialog("Digite o nome do estado no lado esquerdo da produção nova:");
			
			Estado ladoEsquerdo = Principal.getEstadoPorNome(nomeI, naoTerminais);
			if (ladoEsquerdo != null){
				String letra = JOptionPane.showInputDialog("Digite o caracter gatilho da produção (simbolo único, minusculo ou digito):");
				char l = letra.charAt(0);
				if (Principal.letraPertenceAoAlfabeto(l, alfabeto)){
					String nomeF = JOptionPane.showInputDialog("Digite o nome do estado no lado direito da produção nova (se não houver, deixe em branco):");
					if (!nomeF.equalsIgnoreCase("")){
						Estado ladoDireito = Principal.getEstadoPorNome(nomeF, naoTerminais);
						if (ladoDireito != null) {
							Transicao nova = new Transicao(ladoEsquerdo, l, ladoDireito);
							//if (!gramatica.getProducoes().contains(nova)){
								if (!Principal.possuiProducao(producoes, ladoEsquerdo, l, ladoDireito)){
									producoes.add(nova);
								}
							 //}
						}
					} else{
						Transicao nova = new Transicao(ladoEsquerdo, l, null);
						//if (!gramatica.getProducoes().contains(nova)){
							if (!Principal.possuiProducao(producoes, ladoEsquerdo, l, null)){
								producoes.add(nova);
							}
						//}
					}
					
				}
			}
			gram = Interface.mostraGramatica(gramatica);
			confirmP = JOptionPane.showConfirmDialog(null, "Deseja criar mais uma produção?\n"+"Gramatica atual:\n"+gram);
		}
		
		int confirmP2 = JOptionPane.showConfirmDialog(null, "Deseja remover uma produção?\n"+"Gramatica atual:\n"+gram);
		while (confirmP2 == 0){
			String nomeI = JOptionPane.showInputDialog("Digite o nome do estado no lado esquerdo da produção a remover:");
			
			Estado ladoEsquerdo = Principal.getEstadoPorNome(nomeI, naoTerminais);
			if (ladoEsquerdo != null){
				String letra = JOptionPane.showInputDialog("Digite o caracter gatilho da produção (simbolo único, minusculo ou digito):");
				char l = letra.charAt(0);
				if (Principal.letraPertenceAoAlfabeto(l, alfabeto)){
					String nomeF = JOptionPane.showInputDialog("Digite o nome do estado no lado direito da produção a remover (se não houver, deixe em branco):");
					if (!nomeF.equalsIgnoreCase("")){
						Estado ladoDireito = Principal.getEstadoPorNome(nomeF, naoTerminais);
						if (ladoDireito != null) {
							//Transicao nova = new Transicao(ladoEsquerdo, l, ladoDireito);
							//if (gramatica.getProducoes().contains(nova)){
								if (Principal.possuiProducao(producoes, ladoEsquerdo, l, ladoDireito)){
									producoes.remove(Principal.getProducao(producoes, ladoEsquerdo, l, ladoDireito));
								}
							 //}
						}
					} else{
						//Transicao nova = new Transicao(ladoEsquerdo, l, null);
						//if (gramatica.getProducoes().contains(nova)){
							if (Principal.possuiProducao(producoes, ladoEsquerdo, l, null)){
								producoes.remove(Principal.getProducao(producoes, ladoEsquerdo, l, null));
							}
						//}
					}
					
				}
			}
			gram = Interface.mostraGramatica(gramatica);
			confirmP2 = JOptionPane.showConfirmDialog(null, "Deseja remover mais uma produção?\n"+"Gramatica atual:\n"+gram);
		}
		// Adicionar os proprios estados na composicao de cada estado
/*		for (Estado nt : naoTerminais){
			nt.getEstadosInternos().add(nt);
		}	*/	
	}
	
	// Permite ao usuario criar uma expressao regular
	public static Expressao criarER() {
		String nome = JOptionPane.showInputDialog("Digite a expressão regular");
		Expressao e = new Expressao(nome);
		return e;
	}

	// Permite ao usuario editar uma expressao regular jah criada
	public static void editarER(Expressao exp) {
		String nomeNovo = JOptionPane.showInputDialog("Digite a expressão regular", exp.getNome());
		exp.setNome(nomeNovo);
		
	}

	// Gera uma string para facilitar a visualizacao de expressoes regulares
	public static String mostraER(Expressao exp) {
		String ER = exp.getNome();
		ER += "\n";
		return ER;
	}
	
	public static Gramatica criaGramaticaParse(String entrada){
		ArrayList<Character> auxiliar = new ArrayList<>();
		for (Character c : entrada.toCharArray()){
			if((int)c != 32){
				auxiliar.add(c);
			}
		}
		
		//System.out.println(auxiliar);
		
		ArrayList<Estado> naoTerminaisAux = new ArrayList<>();
		ArrayList<Character> terminaisAux = new ArrayList<>();
		ArrayList<Transicao> producoesAux = new ArrayList<>();
		int i = 0;
		for (int k = 0; k < auxiliar.size(); k++){
			Character aux0 = auxiliar.get(k);
			if ((int)aux0 <= 90 && (int) aux0 >=65){
				if (i+1 < auxiliar.size() && auxiliar.get(i+1) == '-'){
					if (Principal.getEstadoPorNome(aux0.toString(), naoTerminaisAux) == null){
						Estado e = new Estado(aux0.toString());
						naoTerminaisAux.add(e);
					}
				}
			}
		}
		do{
			Character aux = auxiliar.get(i);
			if (i+1 < auxiliar.size() && auxiliar.get(i+1) == '-'){
				if (Principal.getEstadoPorNome(aux.toString(), naoTerminaisAux) == null){
					Estado e = new Estado(aux.toString());
					naoTerminaisAux.add(e);
				}
			} else if(aux == '>' || aux == '|'){
				int r = i;
				Character retorno = auxiliar.get(r);
				do{
					r--;
					retorno = auxiliar.get(r);
				}while(retorno != '-');
				retorno = auxiliar.get(r-1);
				//System.out.println(retorno);
				
				int j = 1;
				Character leitura = new Character(' ');
				String destino = "";
				while(auxiliar.get(i+j) != '|' && auxiliar.get(i+j) != ',' && i+j < auxiliar.size()-1){
					Character aux2 = auxiliar.get(i+j);
					//System.out.println(aux2);
					leitura = aux2;
					if (!terminaisAux.contains(aux2)){
						terminaisAux.add(aux2);
					}
					if (i+j+1 < auxiliar.size()){
						if (auxiliar.get(i+j+1) != '|' && auxiliar.get(i+j+1) != ','){
							destino = auxiliar.get(i+j+1).toString();
							//System.out.println(destino);
						}
					}
					break;
					
				}
				
	/*			if (i+j == auxiliar.size()-1){
					Character aux2 = auxiliar.get(i+j);
					//System.out.println(aux2);
					destino += aux2.toString();
					if((int)aux2 <= 90 && (int) aux2 != 38){
						if (Principal.getEstadoPorNome(aux2.toString(), naoTerminaisAux) == null){
							naoTerminaisAux.add(new Estado(aux2.toString()));
						}
					} else{
						if (!terminaisAux.contains(aux2)){
							terminaisAux.add(aux2);
						}
					}
				}*/
				
				Estado origem =  Principal.getEstadoPorNome(retorno.toString(), naoTerminaisAux);
				Estado destinoE =  Principal.getEstadoPorNome(destino, naoTerminaisAux);
				if (!Principal.possuiProducao(producoesAux, origem, leitura, destinoE)){
					Transicao p = new Transicao(origem, leitura ,destinoE);
					producoesAux.add(p);
				}
				//System.out.println(destino);
				
				//System.out.println(producoesAux.get(0).getInicial().getNome()+" -> "+ producoesAux.get(0).get_final());
				//break;
			}
			i++;
		}while(i < auxiliar.size());
		
		String inicialS = "";
		Estado inicial = null;
		do {
			inicialS = JOptionPane.showInputDialog("Digite o nome do estado inicial: ");
			if (inicialS != null && inicialS != ""){
				inicial = Principal.getEstadoPorNome(inicialS, naoTerminaisAux);
			}
			
		}while(inicial == null || inicialS == "");
		System.out.println(inicial.getNome());
		
		System.out.println(naoTerminaisAux);
		System.out.println(terminaisAux);
		
		Gramatica gramatica = new Gramatica(naoTerminaisAux, terminaisAux, producoesAux, inicial);
		Interface.mostraGramatica(gramatica);
		return gramatica;
}
}
