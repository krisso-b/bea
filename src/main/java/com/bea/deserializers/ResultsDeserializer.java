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

			Object statisticObj = jsonResultsObject.get(BeaConstants.RESULTS_STATISTIC);
			if (statisticObj == null)
			{
				log.error("statistic is empty");
				return null;
			}
			String statistic = jsonResultsObject.get(BeaConstants.RESULTS_STATISTIC).getAsString();
			results.setStatistic(statistic);
			results.setUnitOfMeasure(jsonResultsObject.get(BeaConstants.RESULTS_UNIT_OF_MEASURE).getAsString());
			results.setPublicTable(jsonResultsObject.get(BeaConstants.RESULTS_PUBLICTABLE).getAsString());
			results.setUTCProductionTime(jsonResultsObject.get(BeaConstants.RESULTS_UTC_PRODUCTION_TIME).getAsString());

			JsonArray dimensionsObjectCollection = (JsonArray) jsonResultsObject.get(BeaConstants.DIMENSIONS);

			List<Dimension> dimensionsList = new ArrayList<Dimension>();
			for (JsonElement dimensonObjectElement : dimensionsObjectCollection)
			{
				JsonObject dimensionsObject = (JsonObject) dimensonObjectElement;
				Dimension dimension = new Dimension();
				dimension.setOrdinal(dimensionsObject.get(BeaConstants.DIMENSION_ORDINAL).getAsInt());
				dimension.setName(dimensionsObject.get(BeaConstants.DIMENSION_NAME).getAsString());
				dimension.setDataType(dimensionsObject.get(BeaConstants.DIMENSION_DATA_TYPE).getAsString());
				dimension.setIsValue(dimensionsObject.get(BeaConstants.DIMENSION_IS_VALUE).getAsString());
				dimensionsList.add(dimension);
			}
			results.setDimensons(dimensionsList);
			

			/**
			 * Creates 'Data' collection
			 * */
			JsonArray dataObjectCollection = (JsonArray) jsonResultsObject.get(BeaConstants.RESULTS_DATA);
			List<Data> dataList = new ArrayList<>();
			for (JsonElement dataObjectElement : dataObjectCollection)
			{
				JsonObject dataObject = (JsonObject) dataObjectElement;
				Data data = new Data();
				data.setGeoFips(dataObject.get(BeaConstants.DATA_GEO_FIPS).getAsString());
				data.setGeoName(dataObject.get(BeaConstants.DATA_GEO_NAME).getAsString());
				data.setCode(dataObject.get(BeaConstants.DATA_CODE).getAsString());

				SimpleDateFormat sdf = new SimpleDateFormat(BeaConstants.TIME_PERIOD_FORMAT);
				try
				{
					String timePeriodYear = dataObject.get(BeaConstants.DATA_TIMEPERIOD).getAsString();
					String timePeriodDate = getTimePeriodDate(timePeriodYear);
					
					if(timePeriodDate == null){
						continue;
					}					
					data.setTimePeriod(timePeriodDate);

					Date asOfDate = sdf.parse(data.getTimePeriod());
					data.setAsOfDate(asOfDate);
				}
				catch (ParseException e)
				{
					log.error("Gson Parse Error");
					return null;
				}

				data.setClUnit(dataObject.get(BeaConstants.DATA_CL_UNIT).getAsString());
				data.setUnitMult(dataObject.get(BeaConstants.DATA_UNIT_MULT).getAsInt());
				
				if(dataObject.get(BeaConstants.DATA_DATAVALUE) != null){
					data.setDataValue(dataObject.get(BeaConstants.DATA_DATAVALUE).getAsDouble());
					data.setObservedValue(getObservedValue(data.getDataValue(), data.getUnitMult()));
					dataList.add(data);
				}
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
		String timePeriodQuarter = null;
		String timePeriodYearSplit = null;
		
		if(timePeriodYear == null){
			return null;
		}
		
		if(!timePeriodYear.isEmpty() && timePeriodYear.length()>3){
			
			 timePeriodQuarter = timePeriodYear.substring(4);
			 timePeriodYearSplit = timePeriodYear.substring(0, 4);
		}else{
			return null;
		}
		
		String timePeridMMDD = null;

		switch (timePeriodQuarter)
		{
			case BeaConstants.TIME_PERIOD_Q1:
				timePeridMMDD = BeaConstants.TIME_PERIOD_Q1_MM_DD;
				break;
			case BeaConstants.TIME_PERIOD_Q2:
				timePeridMMDD = BeaConstants.TIME_PERIOD_Q2_MM_DD;
				break;
			case BeaConstants.TIME_PERIOD_Q3:
				timePeridMMDD = BeaConstants.TIME_PERIOD_Q3_MM_DD;
				break;
			case BeaConstants.TIME_PERIOD_Q4:
				timePeridMMDD = BeaConstants.TIME_PERIOD_Q4_MM_DD;
				break;
			default:
				timePeridMMDD = BeaConstants.TIME_PERIOD_Q4_MM_DD;
				break;
		}
		timePeriodDate = timePeriodYearSplit + timePeridMMDD;
		return timePeriodDate;
	}

	public Double getObservedValue(double dataValue, int unitMult)
	{

		return dataValue * Math.pow(10, unitMult);
	}
}
