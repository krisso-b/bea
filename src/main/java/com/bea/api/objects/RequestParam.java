package com.bea.api.objects;

import java.io.Serializable;

public class RequestParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1753628970749479057L;
	
	private String parameterName;
	private String parameterValue;
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	public String getParameterValue() {
		return parameterValue;
	}
	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}
	

}
