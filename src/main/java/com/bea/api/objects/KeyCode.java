package com.bea.api.objects;

import java.io.Serializable;

public class KeyCode  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1223049271489482228L;
	
	private String keyCode;
	private String description;
	
	public String getKeyCode() {
		return keyCode;
	}
	public void setKeyCode(String keyCode) {
		this.keyCode = keyCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
