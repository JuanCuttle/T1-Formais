import java.util.ArrayList;



public class Principal {

	public static void main(String[] args) {
		char[] alfabeto = {'a', 'b'};
		
		Estado q0 = new Estado("q0");
		Estado q1 = new Estado("q1");
		Estado q2 = new Estado("q2");
		
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
		
		ArrayList<Transicao> transicoes = new ArrayList<>();
		
		transicoes.add(a);
		transicoes.add(b);
		transicoes.add(c);
		transicoes.add(d);
		transicoes.add(e);
		transicoes.add(f);
		
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

}
