package com.blas.SpringBootAplication.Exeptions;

public class CustomeFieldValidationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3849730897900157891L;
	
	private String fieldName;
	public CustomeFieldValidationException(String message, String fieldName) {
		// TODO Auto-generated constructor stub
		super(message);
		this.fieldName=fieldName;
	}
	public String getFieldName() {
		return fieldName;
	}
}
