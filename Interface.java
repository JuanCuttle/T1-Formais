import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Interface {
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
			
			confirmE = JOptionPane.showConfirmDialog(null, "Deseja criar mais um simbolo terminal?");
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
		
		
		String nomeEstado = JOptionPane.showInputDialog("Digite o nome do simbolo inicial:");
		Estado inicialNovo = Principal.getEstadoPorNome(nomeEstado, naoTerminais); 
		if (inicialNovo != null){
			gramatica.setInicial(inicialNovo);
		} else {
			Estado inicial = new Estado(nomeEstado);
			naoTerminais.add(inicial);
			gramatica.setInicial(inicialNovo);
		}
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
		
		for (int index = 0; index < producoes.size();index++){
			Transicao trans = producoes.get(index);
			if(!naoTerminais.contains(trans.getInicial()) || !naoTerminais.contains(trans.get_final()) && trans.get_final() != null){
				producoes.remove(trans);
			}
		}
		
		String gram = Interface.mostraGramatica(new Gramatica(naoTerminais, alfabeto, producoes, inicialNovo));
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
			gram = Interface.mostraGramatica(new Gramatica(naoTerminais, alfabeto, producoes, inicialNovo));
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
			gram = Interface.mostraGramatica(new Gramatica(naoTerminais, alfabeto, producoes, inicialNovo));
			confirmP2 = JOptionPane.showConfirmDialog(null, "Deseja remover mais uma produção?\n"+"Gramatica atual:\n"+gram);
		}
		// Adicionar os proprios estados na composicao de cada estado
		for (Estado nt : naoTerminais){
			nt.getEstadosInternos().add(nt);
		}		
	}
}
