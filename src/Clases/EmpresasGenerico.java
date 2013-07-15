package Clases;

public class EmpresasGenerico {
	
	//Propiedades
	protected String nombre;
	protected String direccion;
	protected String telefono;
	
	//Constructor
	public EmpresasGenerico(String name, String dir, String tel){
		this.nombre = name;
		this.direccion= dir;
		this.telefono = tel;
	}
	
	//M�todos para acceder a nombre
	public void setNombre(String name){
		this.nombre = name;
	}
	public String getNombre(){
		return this.nombre;
	}
	
	//M�todos para acceder a direcci�n
	public void setDireccion(String dir){
		this.direccion= dir;
	}
	public String getDireccion(){
		return this.direccion;
	}
	
	//M�todos para acceder a tel�fono
	public void setTelefono(String tel){
		this.telefono = tel;
	}
	public String getTelefono(){
		return this.telefono;
	}

}
