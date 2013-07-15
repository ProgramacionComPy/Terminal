package Clases;

public class Empresas extends EmpresasGenerico{
	
	protected String ruc;
	
	//Constructor
	public Empresas(String name, String dir, String tel, String ruc){
		super(name, dir, tel);
		this.ruc=ruc;
	}

	public void setRuc(String ruc) {
	this.ruc=ruc;
		
	}
	public String getRuc() {
		return ruc;
	}	

}
