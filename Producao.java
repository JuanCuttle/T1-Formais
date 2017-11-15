
public class Producao {
	private NaoTerminal vn;
	private char leitura;
	private NaoTerminal vn1;
	public Producao(NaoTerminal vn, char leitura, NaoTerminal vn1) {
		this.vn = vn;
		this.leitura = leitura;
		this.vn = vn1;
	}
	public NaoTerminal getVn() {
		return vn;
	}
	public char getLeitura() {
		return leitura;
	}
	public NaoTerminal getVn1() {
		return vn1;
	}
	public void setVn1(NaoTerminal vn1) {
		this.vn1 = vn1;
	}
	
	
}
