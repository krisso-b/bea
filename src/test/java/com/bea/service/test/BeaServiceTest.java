package com.bea.service.test;

import com.bea.api.objects.KeyCode;
import com.bea.api.objects.Results;
import com.bea.service.BeaService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class BeaServiceTest {
	private static BeaService beaService = new BeaService("someApiKey", null, 300);

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
			System.out.println("done");
		}


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
