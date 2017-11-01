
public class Producao {
	private Estado inicial;
	private char leitura;
	private Estado _final;
	
	public Producao (Estado inicial, char leitura, Estado _final){
		this.inicial = inicial;
		this.leitura = leitura;
		this._final = _final;
	}

	public Estado getInicial() {
		return inicial;
	}

	public void setInicial(Estado inicial) {
		this.inicial = inicial;
	}

	public char getLeitura() {
		return leitura;
	}

	public void setLeitura(char leitura) {
		this.leitura = leitura;
	}

	public Estado get_final() {
		return _final;
	}

	public void set_final(Estado _final) {
		this._final = _final;
	}
	
	
}
