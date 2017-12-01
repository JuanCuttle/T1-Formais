import java.util.ArrayList;



public class Principal {

	static ArrayList<Gramatica> gramaticasCriadas = new ArrayList<>();
	static ArrayList<Expressao> expressoesCriadas = new ArrayList<>();
	static ArrayList<Automato> automatosCriados = new ArrayList<>();
	
	public static void main(String[] args) throws CloneNotSupportedException {
		
		//Interface.criarGramatica();
		
		ArrayList<Character> alfabeto = new ArrayList<>();
		alfabeto.add('a');
		alfabeto.add('b');
		
		Estado q0 = new Estado("q0");
		Estado q1 = new Estado("q1");
		Estado q2 = new Estado("q2");
		
		q0.getEstadosInternos().add(q0);
		q1.getEstadosInternos().add(q1);
		q2.getEstadosInternos().add(q2);
		
		ArrayList<Estado> estados = new ArrayList<>();
		
		estados.add(q0);
		estados.add(q1);
		estados.add(q2);
		
		ArrayList<Estado> finais = new ArrayList<>();
		finais.add(q2);
		
		Transicao a = new Transicao(q0, 'a', q1);
		Transicao b = new Transicao(q0, 'b', q0);
		Transicao c = new Transicao(q1, 'a', q2);
		Transicao d = new Transicao(q1, 'b', q1);
		Transicao e = new Transicao(q2, 'a', q2);
		Transicao f = new Transicao(q2, 'b', q2);
		
		Transicao nd = new Transicao(q0, 'a', q0);
		
		//Transicao epsilon = new Transicao(q0, '&', q1);
		
		ArrayList<Transicao> transicoes = new ArrayList<>();
		
		transicoes.add(a);
		transicoes.add(b);
		transicoes.add(c);
		transicoes.add(d);
		transicoes.add(e);
		transicoes.add(f);
		
		//transicoes.add(nd);
		
		//transicoes.add(epsilon);
		
		Automato aut = new Automato(alfabeto, estados, finais, transicoes, q0);
		
		aut.completarAutomato();
		
		System.out.println("Transicoes do automato inicial: ");
		
		Interface.mostraAutomato(aut);
		
		if (aut.linguagemVazia()){
			System.out.println("O estado inicial não é fértil, logo a linguagem é vazia");
		} else if (aut.linguagemFinita()){
			System.out.println("Não há recursão direta ou indireta, portanto a linguagem é finita");
		} else {
			System.out.println("Há recursão direta ou indireta, portanto a linguagem é infinita");
		}
		
		
		aut.minimizar();
		
		System.out.println("Transicoes do automato minimizado: ");
		
		Interface.mostraAutomato(aut);
		
/*		for (Transicao transicao : aut.getTransicoes()){
			System.out.println(transicao.getInicial().getNome()+" "+transicao.getLeitura()+" "+transicao.get_final().getNome());
		}*/
		
		Gramatica g = aut.gerarGramatica();
		System.out.println("Producoes da gramatica gerada: ");
		
		Interface.mostraGramatica(g);
		
		Automato aut1 = g.gerarAutomato();
		
		System.out.println("Transicoes do automato gerado: ");
		
		Interface.mostraAutomato(aut1);
		
		aut.determinizar();
		
		Interface.mostraAutomato(aut);
		//////////////////////////////////////////////////////////////////////////
		ArrayList<Estado> estados2 = new ArrayList<>();
		Estado q3 = new Estado("q3");
		Estado q4 = new Estado("q4");
		q3.getEstadosInternos().add(q3);
		q4.getEstadosInternos().add(q4);
		
		estados2.add(q3);
		estados2.add(q4);
		
		ArrayList<Estado> finais2 = new ArrayList<>();
		finais2.add(q4);
		
		ArrayList<Transicao> transicoes2 = new ArrayList<>();
		Transicao t10 = new Transicao(q3, 'a', q4);
		Transicao t11 = new Transicao(q3, 'b', q3);
		Transicao t12 = new Transicao(q4, 'a', q4);
		Transicao t13 = new Transicao(q4, 'b', q4);
		transicoes2.add(t10);
		transicoes2.add(t11);
		transicoes2.add(t12);
		transicoes2.add(t13);
		
		Automato aut3 = new Automato(alfabeto, estados2, finais2, transicoes2, q3);
		Automato autU = aut.uniao(aut3);
		autU.determinizar();
		//autU.removerInalcancaveis();
		//autU.removerMortos();
		autU.minimizar();
		//autU.removerEquivalentes();
		Interface.mostraAutomato(autU);
		
		//aut.linguagensIguais(aut);
		System.out.println(aut.linguagensIguais(aut));
		
		
		String string = "S -> aA | bS | a | b, A-> aS | bA";
		
		Interface.criaGramaticaParse(string);
		
/*		Estado s = new Estado("S");
		//Estado a5 = new Estado("A");
		ArrayList<Estado> e1 = new ArrayList<>();
		ArrayList<Estado> f1 = new ArrayList<>();
		e1.add(s);
		//e1.add(a5);
		//f1.add(a5);
		ArrayList<Transicao> tn = new ArrayList<>();
		tn.add(new Transicao(s, 'a', s));
		Automato teste = new Automato(alfabeto, e1, f1, tn, s);
		System.out.println(teste.linguagemVazia());
		
		Interface.mostraGramatica(teste.gerarGramatica());*/
		
		
/*		for (Transicao transicao : g.getProducoes()){
			if (transicao.get_final() != null){
				System.out.println(transicao.getInicial().getNome()+" "+transicao.getLeitura()+" "+transicao.get_final().getNome());
			} else{
				System.out.println(transicao.getInicial().getNome()+" "+transicao.getLeitura()+" termina");
			}
		}
		
		for (Transicao transicao : aut1.getTransicoes()){
			System.out.println(transicao.getInicial().getNome()+" "+transicao.getLeitura()+" "+transicao.get_final().getNome());
		}*/
	}
	
	public static Estado getEstadoPorNome(String nomeE, ArrayList<Estado> estados){
		for (Estado e : estados){
			if(e.getNome().equalsIgnoreCase(nomeE)){
				return e;
			}
		}
		return null;
	}
	
	public static boolean letraPertenceAoAlfabeto(char c, ArrayList<Character> letras){
		for (char l : letras){
			if(c == l){
				return true;
			}
		}
		return false;
	}
	
	public static boolean possuiProducao(ArrayList<Transicao> producoes, Estado i, char l, Estado f) {
		for (Transicao t : producoes){
			if(f != null){
				if (t.getInicial().getNome().equals(i.getNome()) && t.getLeitura() == l && t.get_final().getNome().equals(f.getNome())){
					return true;
				}
			} else {
				if (t.getInicial().getNome().equals(i.getNome()) && t.getLeitura() == l && t.get_final() == null){
					return true;
				}
			}
		}
		return false;
	}
	
	public static Transicao getProducao(ArrayList<Transicao> producoes, Estado i, char l, Estado f) {
		for (Transicao t : producoes){
			if(f != null){
				if (t.getInicial().getNome().equals(i.getNome()) && t.getLeitura() == l && t.get_final().getNome().equals(f.getNome())){
					return t;
				}
			} else {
				if (t.getInicial().getNome().equals(i.getNome()) && t.getLeitura() == l && t.get_final() == null){
					return t;
				}
			}
		}
		return null;
	}

}
