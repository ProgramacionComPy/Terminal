package Clases;

public class Control{
	
	private int	   id;
	private String salida;
	private String dia;	
	private String empresa;
	private String terminalsal;
	private String terminaldes;
	
	public Control(int id, String sal, String dia, String des, String emp, String ter){
		this.id=id;
		this.salida = sal;
		this.dia= dia;
		this.setTerminaldes(des);
		this.empresa = emp;	
		this.setTerminalsal(ter);	
	}
	
	public String getDia() {
		return dia;
	}
	public void setDia(String dia) {
		this.dia = dia;
	}
	public String getSalida() {
		return salida;
	}
	public void setSalida(String salida) {
		this.salida = salida;
	}
	public String getEmpresa() {
		return empresa;
	}
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public String getTerminaldes() {
		return terminaldes;
	}

	public void setTerminaldes(String terminaldes) {
		this.terminaldes = terminaldes;
	}

	public String getTerminalsal() {
		return terminalsal;
	}

	public void setTerminalsal(String terminalsal) {
		this.terminalsal = terminalsal;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
