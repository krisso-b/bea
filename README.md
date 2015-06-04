BEA
------
The unofficial [BEA API](http://www.bea.gov/api/) Java Wrapper

	String userID = "12599C10-6F87-44D2-8904-91FED7D6F77D";
		String method = "GetData";
		String datasetname = "RegionalData";
		String keyCode = "GDP_SP";
		String resultFormat = RETURNTYPE;
		try
		{
			BeaService beaService = new BeaService(userID, null, null);
			Results results = beaService.getResults(method, datasetname, keyCode, resultFormat);
			
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

			List<KeyCode> keyCodeList = bea.getKeyCodesList(methodGetParameterValues, datasetname, parameterName, resultFormat);
			
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