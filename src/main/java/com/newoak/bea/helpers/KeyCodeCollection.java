package com.newoak.bea.helpers;

import java.util.ArrayList;
import java.util.List;

import com.newoak.bea.api.objects.KeyCode;

public class KeyCodeCollection {

	List<KeyCode> keyCodeList;

	public KeyCodeCollection(){
		keyCodeList = new ArrayList<KeyCode>();
	}
	
	public void addKeyCode(KeyCode keyCode){
		keyCodeList.add(keyCode);
	}

	public List<KeyCode> getKeyCodeList() {
		return keyCodeList;
	}

	public void setKeyCodeList(List<KeyCode> keyCodeList) {
		this.keyCodeList = keyCodeList;
	}

	
}
