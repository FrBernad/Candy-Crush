package game.backend;
//TIENE LOS VALORES DE LAS POSIBLES POSICIONES DE LOS CARAMELOS
public enum Checkpoint {

	U(-1,0, 1),//ARRIBA              ARRIBA ES NEGATIVO PQ EL SISTEMA DE REFERENCIA ES ESTE
	UU(-2,0, 2),//ARRIBA ARRIBA
	D(1,0, 4),//ABAJO									-
	DD(2,0, 8),//ABAJO ABAJO							|
	R(0,1, 16),///DER 							   - ---|--- +
	RR(0,2, 32),///DER DER								|
	L(0,-1, 64),//IZQ									+
	LL(0,-2, 128);//IZQ IZQ
	
	private int i;
	private int j;
	private int value;
	
	Checkpoint(int i, int j, int value) {
		this.i = i;
		this.j = j;
		this.value = value;
	}
	
	public int getI() {
		return i;
	}
	
	public int getJ() {
		return j;
	}
	
	public int getValue() {
		return value;
	}

}
