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
		for (Estado e : g.getNaoTerminais()) {
			System.out.print(e.getNome() + " -> ");
			for (Transicao t : g.getProducoes()) {
				if (t.getInicial() == e) {
					// Para cada transicao do estado, imprimir ela e " | "
					if (t.get_final() == null) {
						// Se a transicao vai só para letra, imprime só a letra
						System.out.print(t.getLeitura() + " | ");
					} else {
						// Se vai para outro estado, mostra ele também
						System.out.print(t.getLeitura()
								+ t.get_final().getNome() + " | ");
					}
				}
			}
			System.out.println("");
		}
		// Terminaram as transicoes, imprime nova linha e reinicia o processo no
		// proximo estado
		System.out.println("");
	}
}
