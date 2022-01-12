package com.blas.SpringBootAplication.Exeptions;

public class UserNameOridNotFound extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3685814395968762734L;
	/**
	 * 
	 */
	public UserNameOridNotFound() {
		// TODO Auto-generated constructor stub
		super("Usuario o Id no encontrado");
	}
	public UserNameOridNotFound(String message) {
		super(message);
	}
}
