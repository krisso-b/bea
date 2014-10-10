package com.bea.service.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.bea.api.objects.KeyCode;
import com.bea.api.objects.Results;
import com.bea.service.BeaService;

public class BeaServiceTest {
	BeaService beaService = null;

	@Before
	public void setUp() throws Exception {
		String apiKey = "12599C10-6F87-44D2-8904-91FED7D6F77D";
		beaService = new BeaService(apiKey, null, null);
	}

	@Test
	public void testGetResults() throws Exception {
		String method = "GetData";
		String datasetname = "RegionalData";
		String keyCode = "GDP_SP";
		String resultFormat = "json";

		Results results = beaService.getResults(method, datasetname, keyCode,
				resultFormat);
		Assert.assertNotNull(results);
		Assert.assertEquals("GDP in current dollars", results.getStatistic());
		Assert.assertEquals("GDP_SP", results.getKeyCode());
		Assert.assertNotNull(results.getRequestParam());		
		Assert.assertFalse(results.getRequestParam().isEmpty());
		Assert.assertNotNull(results.getDimensons());
		Assert.assertFalse(results.getDimensons().isEmpty());
		Assert.assertNotNull(results.getData());
		Assert.assertFalse(results.getData().isEmpty());
	}

	@Test
	public void testGetKeyCodesList() throws Exception {
		String method = "GetParameterValues";
		String datasetname = "RegionalData";
		String parameterName = "keycode";
		String resultFormat = "json";

		List<KeyCode> keyCodeList = beaService.getKeyCodesList(method,
				datasetname, parameterName, resultFormat);
		
		for(KeyCode keyCode:keyCodeList){
			Assert.assertNotNull(keyCode);
			Assert.assertNotNull(keyCode.getKeyCode());
			Assert.assertNotNull(keyCode.getDescription());		
			System.out.println(keyCode.getKeyCode() +":" + keyCode.getDescription());
		}
	}

}
