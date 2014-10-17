package com.bea.service;

import java.util.List;

import com.bea.api.objects.Data;
import com.bea.api.objects.Dimension;
import com.bea.api.objects.KeyCode;
import com.bea.api.objects.RequestParam;
import com.bea.api.objects.Results;


public class BeaServiceExample
{
	final static String RETURNTYPE = "json";

	public static void main(String[] args)
	{
		String userID = "12599C10-6F87-44D2-8904-91FED7D6F77D";
		String method = "GetData";
		String datasetname = "RegionalData";
		String keyCode = "GDP_SP";
		String resultFormat = RETURNTYPE;
		try
		{
			BeaService beaService = new BeaService(userID, null, null);
			Results results = beaService.getResults(keyCode, method, datasetname,resultFormat);
			
			System.out.println("KeyCode:" + results.getKeyCode());
			
			for(RequestParam requestParam:results.getRequestParam()){
				System.out.println(requestParam.getParameterName()+":"+requestParam.getParameterValue());
			}

			for (Dimension dimension : results.getDimensons())
			{
				System.out.println(dimension.getOrdinal() + ":" + dimension.getName());
			}

			for (Data data : results.getData())
			{
				System.out.println(data.getGeoFips() + ":" + data.getGeoName() + ":" + data.getCode() + ":"
						+ data.getTimePeriod() + ":" + data.getClUnit() + ":" + data.getUnitMult() + ":" + data.getDataValue()
						+ ":" + data.getAsOfDate() + ":" + data.getObservedValue());
			}
			
			// Get the list of 'keyCode's
			String methodGetParameterValues = "GetParameterValues";
			String parameterName = "keycode";

			List<KeyCode> keyCodeList = beaService.getKeyCodesList( parameterName, methodGetParameterValues, datasetname,resultFormat);
			
			for(KeyCode keyCodeObj:keyCodeList)
			{
				System.out.println(keyCodeObj.getKeyCode() +":" + keyCodeObj.getDescription());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
