import java.util.ArrayList;



public class Estado {
	private String nome;
	private ArrayList<Estado> estadosInternos = new ArrayList<>();
	
	public Estado (String nome){
		this.setNome(nome);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public ArrayList<Estado> getEstadosInternos() {
		return estadosInternos;
	}

	public void setEstadosInternos(ArrayList<Estado> estadosInternos) {
		this.estadosInternos = estadosInternos;
	}
}
