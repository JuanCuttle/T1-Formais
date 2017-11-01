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
		
		Producao a = new Producao(q0, 'a', q1);
		Producao b = new Producao(q0, 'b', q0);
		Producao c = new Producao(q1, 'a', q2);
		Producao d = new Producao(q1, 'b', q1);
		Producao e = new Producao(q2, 'a', q2);
		Producao f = new Producao(q2, 'b', q2);
		
		ArrayList<Producao> producoes = new ArrayList<>();
		
		producoes.add(a);
		producoes.add(b);
		producoes.add(c);
		producoes.add(d);
		producoes.add(e);
		producoes.add(f);
		
		Automato aut = new Automato(alfabeto, estados, finais, producoes, q0);
		
		aut.completarAutomato();
		
		for (Producao producao : aut.getProducoes()){
			System.out.println(producao.getInicial().getNome()+" "+producao.getLeitura()+" "+producao.get_final().getNome());
		}
	}

}
