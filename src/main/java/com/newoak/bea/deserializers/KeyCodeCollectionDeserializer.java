package com.newoak.bea.deserializers;

import java.lang.reflect.Type;

import org.apache.log4j.Logger;

import com.newoak.bea.api.objects.KeyCode;
import com.newoak.bea.constants.BeaConstants;
import com.newoak.bea.helpers.KeyCodeCollection;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class KeyCodeCollectionDeserializer implements JsonDeserializer<KeyCodeCollection>{
	
	private static final Logger log = Logger.getLogger(KeyCodeCollectionDeserializer.class);
	
	@Override
	public KeyCodeCollection deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		
		KeyCodeCollection  keyCodeCollection = new KeyCodeCollection();
		JsonObject obj = (JsonObject) json;

		if (obj.has(BeaConstants.BEAAPI))
		{
			JsonObject beaAPIObject = (JsonObject) obj.get(BeaConstants.BEAAPI);

			JsonObject jsonResultsObject = (JsonObject) beaAPIObject.get(BeaConstants.RESULTS);
			
			JsonArray paramValueCollection = (JsonArray) jsonResultsObject.get(BeaConstants.PARAM_VALUE);
			
			for(JsonElement paramValueElement:paramValueCollection){
				JsonObject paramValueObject = (JsonObject)paramValueElement;
				
				KeyCode keyCode = new KeyCode();
				keyCode.setKeyCode(paramValueObject.get(BeaConstants.KEYCODE).getAsString());
				keyCode.setDescription(paramValueObject.get(BeaConstants.DESCRIPTION).getAsString());				
				keyCodeCollection.addKeyCode(keyCode);
				}
			}
			else
			{
				log.error("No BEA API found during Deserialization.");
				return null;
			}
			
		return keyCodeCollection;
	}
}
