package com.bea.service.test;

import com.bea.api.objects.KeyCode;
import com.bea.api.objects.Results;
import com.bea.service.BeaService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class BeaServiceTest {
	private static BeaService beaService = new BeaService("12599C10-6F87-44D2-8904-91FED7D6F77D", null, 300);

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetResults() throws Exception {
		for (int i = 0; i < 10; i++)
		{
			String method = "GetData";
			String datasetname = "RegionalData";
			String keyCode = "GDP_SP";
			String resultFormat = "json";

			Results results = beaService.getResults(keyCode,method, datasetname,
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
			System.out.println("done");
		}


	}

	@Test
	public void testGetKeyCodesList() throws Exception {
		String method = "GetParameterValues";
		String datasetname = "RegionalData";
		String parameterName = "keycode";
		String resultFormat = "json";

		List<KeyCode> keyCodeList = beaService.getKeyCodesList(parameterName, method,
				datasetname, resultFormat);
		
		for(KeyCode keyCode:keyCodeList){
			Assert.assertNotNull(keyCode);
			Assert.assertNotNull(keyCode.getKeyCode());
			Assert.assertNotNull(keyCode.getDescription());		
			System.out.println(keyCode.getKeyCode() +":" + keyCode.getDescription());
		}
	}
	
	@Test
	public void testGetResultsForAllKeyCodes() throws Exception {
		String methodGetParameterValues = "GetParameterValues";
		String datasetname = "RegionalData";
		String parameterName = "keycode";
		String resultFormat = "json";
		String methodGetData = "GetData";

		List<KeyCode> keyCodeList = beaService.getKeyCodesList( parameterName, methodGetParameterValues,
				datasetname, resultFormat);
		int index = 1;
		for(KeyCode keyCode:keyCodeList){
			Assert.assertNotNull(keyCode);
			Assert.assertNotNull(keyCode.getKeyCode());
			Assert.assertNotNull(keyCode.getDescription());		
			System.out.println(++index +". "+keyCode.getKeyCode() +", " + keyCode.getDescription());
			
			Results results = beaService.getResults(keyCode.getKeyCode(), methodGetData, datasetname, 
					resultFormat);
			
			Assert.assertNotNull(results);
			Assert.assertNotNull(results.getRequestParam());
			Assert.assertFalse(results.getRequestParam().isEmpty());
			Assert.assertNotNull(results.getDimensons());
			Assert.assertFalse(results.getDimensons().isEmpty());
			Assert.assertNotNull(results.getData());
			Assert.assertFalse(results.getData().isEmpty());
			System.out.println("Data size:" + results.getData().size());
		}
	}


}
