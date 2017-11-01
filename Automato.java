import java.util.ArrayList;

public class Automato {
	private boolean completo = false;
	private boolean afnd;

	private char[] alfabeto;
	private ArrayList<Estado> estados;
	private ArrayList<Estado> finais;
	private ArrayList<Producao> producoes;
	private Estado inicial;

	public Automato(char[] alfabeto, ArrayList<Estado> estados,
			ArrayList<Estado> finais, ArrayList<Producao> producoes,
			Estado inicial) {
		this.alfabeto = alfabeto;
		this.estados = estados;
		this.finais = finais;
		this.producoes = producoes;
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

	public ArrayList<Producao> getProducoes() {
		return producoes;
	}

	public Estado getInicial() {
		return inicial;
	}

	public ArrayList<Estado> transicao(Estado inicial, char leitura) {
		ArrayList<Estado> possiveis = new ArrayList<Estado>();
		for (Producao producao : producoes) {
			if (producao.getInicial() == inicial
					&& producao.getLeitura() == leitura) {
				possiveis.add(producao.get_final());
			}
		}
		return possiveis;
	}

	public void completarAutomato() {
		boolean temProducao = false;
		boolean estadoErro = false;
		Estado estadoDeErro = new Estado("qe");

		if (!completo) {
			for (char caracter : alfabeto) {
				//System.out.println("Caracter atual:"+ caracter);
				for (Estado estado : estados) {
					//System.out.println("Estado atual:"+ estado.getNome());
					temProducao = false;
					for (Producao producao : producoes) {
						if (producao.getInicial() == estado
								&& producao.getLeitura() == caracter) {
							temProducao = true;
						}
					}
					if (temProducao == false) {
						Producao nova = new Producao(estado, caracter,
								estadoDeErro);
						this.producoes.add(nova);
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
}
