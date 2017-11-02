import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Interface {
	public static void mostraAutomato(Automato aut) {
		String tabela = "";
		for (char c : aut.getAlfabeto()) {
			tabela = tabela + ("      |   " + c);
			System.out.print("      |   " + c);
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
		// JOptionPane.showMessageDialog(null, tabela);
	}

	public static void mostraGramatica(Gramatica g) {
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
	}
	
	public static Gramatica criarGramatica(){
		Gramatica g = null;
		ArrayList<Estado> naoTerminais = new ArrayList<>();
		ArrayList<Transicao> producoes = new ArrayList<>();
		
		char[] alfabeto = new char[10];
		int i = -1;
		int mais = JOptionPane.showConfirmDialog(null, "Deseja adicionar um símbolo (terminal) ao alfabeto?");
		while(mais == 0) {
			i++;
			String caracter = JOptionPane.showInputDialog("Digite o caracter (simbolo único, minusculo ou digito):");
			char c = caracter.charAt(0);
			alfabeto[i] = c;
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
							producoes.add(nova);
						}
					} else{
						Transicao nova = new Transicao(ladoEsquerdo, l, null);
						producoes.add(nova);
					}
					
				}
			}
			
			confirmP = JOptionPane.showConfirmDialog(null, "Deseja criar mais uma produção?");
		}
		
		g = new Gramatica(naoTerminais, alfabeto, producoes, inicial);
		System.out.println("Gramatica gerada: ");
		Interface.mostraGramatica(g);
		return g;
	}
}
