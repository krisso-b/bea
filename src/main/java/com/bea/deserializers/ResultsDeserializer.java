package com.bea.deserializers;

import java.lang.reflect.Type;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.bea.api.objects.Data;
import com.bea.api.objects.Dimension;
import com.bea.api.objects.RequestParam;
import com.bea.api.objects.Results;
import com.bea.constants.BeaConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class ResultsDeserializer implements JsonDeserializer<Results>
{
	private static final Logger log = Logger.getLogger(ResultsDeserializer.class);

	@Override
	public Results deserialize(JsonElement json, Type arg1, JsonDeserializationContext arg2) throws JsonParseException
	{
		Results results = new Results();
		JsonObject obj = (JsonObject) json;

		if (obj.has(BeaConstants.BEAAPI))
		{
			JsonObject beaAPIObject = (JsonObject) obj.get(BeaConstants.BEAAPI);
			
			JsonObject jsonRequestObject = (JsonObject) beaAPIObject.get(BeaConstants.REQUEST);
			
			JsonArray requestParamCollection = (JsonArray) jsonRequestObject.get(BeaConstants.REQUESTPARAM);
			
			/**
			 * Creates 'RequestParam' collection
			 * */
			List<RequestParam> requestParamList = new ArrayList<RequestParam>();
			for(JsonElement requestParamElement : requestParamCollection)
			{
				JsonObject requestParamObject = (JsonObject) requestParamElement;
				RequestParam requestParam = new RequestParam();
				requestParam.setParameterName(requestParamObject.get(BeaConstants.PARAMETERNAME).getAsString());
				requestParam.setParameterValue(requestParamObject.get(BeaConstants.PARAMETERVALUE).getAsString());
				requestParamList.add(requestParam);
				
				// Setting 'keyCode' value to results object
				if(BeaConstants.KEYCODE.equalsIgnoreCase(requestParam.getParameterName())){
					results.setKeyCode(requestParam.getParameterValue());
				}
			}
			results.setRequestParam(requestParamList);
			
			/**
			 * Creates 'Dimension' collection
			 * */
			JsonObject jsonResultsObject = (JsonObject) beaAPIObject.get(BeaConstants.RESULTS);

			String statistic = jsonResultsObject.get("Statistic").getAsString();
			if (statistic == null)
			{
				log.error("statistic is empty");
				return null;
			}
			results.setStatistic(statistic);
			results.setUnitOfMeasure(jsonResultsObject.get("UnitOfMeasure").getAsString());
			results.setPublicTable(jsonResultsObject.get("PublicTable").getAsString());
			results.setUTCProductionTime(jsonResultsObject.get("UTCProductionTime").getAsString());

			JsonArray dimensionsObjectCollection = (JsonArray) jsonResultsObject.get(BeaConstants.DIMENSIONS);

			List<Dimension> dimensionsList = new ArrayList<Dimension>();
			for (JsonElement dimensonObjectElement : dimensionsObjectCollection)
			{
				JsonObject dimensionsObject = (JsonObject) dimensonObjectElement;
				Dimension dimension = new Dimension();
				dimension.setOrdinal(dimensionsObject.get("Ordinal").getAsInt());
				dimension.setName(dimensionsObject.get("Name").getAsString());
				dimension.setDataType(dimensionsObject.get("DataType").getAsString());
				dimension.setIsValue(dimensionsObject.get("IsValue").getAsString());
				dimensionsList.add(dimension);
			}
			results.setDimensons(dimensionsList);
			

			/**
			 * Creates 'Data' collection
			 * */
			JsonArray dataObjectCollection = (JsonArray) jsonResultsObject.get("Data");
			List<Data> dataList = new ArrayList<>();
			for (JsonElement dataObjectElement : dataObjectCollection)
			{
				JsonObject dataObject = (JsonObject) dataObjectElement;
				Data data = new Data();
				data.setGeoFips(dataObject.get("GeoFips").getAsInt());
				data.setGeoName(dataObject.get("GeoName").getAsString());
				data.setCode(dataObject.get("Code").getAsString());

				SimpleDateFormat sdf = new SimpleDateFormat(BeaConstants.TIME_PERIOD_FORMAT);
				try
				{
					String timePeriodYear = dataObject.get("TimePeriod").getAsString();
					data.setTimePeriod(getTimePeriodDate(timePeriodYear));

					Date asOfDate = sdf.parse(data.getTimePeriod());
					data.setAsOfDate(asOfDate);
				}
				catch (ParseException e)
				{
					log.error("Gson Parse Error");
					return null;
				}

				data.setClUnit(dataObject.get("CL_UNIT").getAsString());
				data.setUnitMult(dataObject.get("UNIT_MULT").getAsInt());
				data.setDataValue(dataObject.get("DataValue").getAsInt());

				data.setObservedValue(getObservedValue(data.getDataValue(), data.getUnitMult()));

				dataList.add(data);
			}

			results.setData(dataList);
		}
		else
		{
			log.error("No BEA API found during Deserialization.");
			return null;
		}
		return results;
	}

	public String getTimePeriodDate(String timePeriodYear)
	{
		String timePeriodDate = null;
		String timePeriodQuarter = timePeriodYear.substring(4);
		String timePeridMMDD = null;

		switch (timePeriodQuarter)
		{
			case "Q1":
				timePeridMMDD = BeaConstants.TIME_PERIOD_Q1_MM_DD;
				break;
			case "Q2":
				timePeridMMDD = BeaConstants.TIME_PERIOD_Q2_MM_DD;
				break;
			case "Q3":
				timePeridMMDD = BeaConstants.TIME_PERIOD_Q3_MM_DD;
				break;
			case "Q4":
				timePeridMMDD = BeaConstants.TIME_PERIOD_Q4_MM_DD;
				break;
			default:
				timePeridMMDD = BeaConstants.TIME_PERIOD_Q4_MM_DD;
				break;
		}
		timePeriodDate = timePeriodYear + timePeridMMDD;
		return timePeriodDate;
	}

	public Double getObservedValue(int dataValue, int unitMult)
	{

		return dataValue * Math.pow(10, unitMult);
	}
}
