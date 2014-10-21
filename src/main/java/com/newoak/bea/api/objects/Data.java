package com.newoak.bea.api.objects;

import java.io.Serializable;
import java.util.Date;

public class Data implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4543566827710236378L;

	private String geoFips;
	private String geoName;
	private String code;
	private String timePeriod;
	private Date asOfDate;
	private String clUnit;
	private int unitMult;
	private Double dataValue;
	private Double observedValue;


	public String getGeoFips() {
		return geoFips;
	}

	public void setGeoFips(String geoFips) {
		this.geoFips = geoFips;
	}

	public String getGeoName()
	{
		return geoName;
	}

	public void setGeoName(String geoName)
	{
		this.geoName = geoName;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getTimePeriod()
	{
		return timePeriod;
	}

	public void setTimePeriod(String timePeriod)
	{
		this.timePeriod = timePeriod;
	}

	public Date getAsOfDate()
	{
		return asOfDate;
	}

	public void setAsOfDate(Date asOfDate)
	{
		this.asOfDate = asOfDate;
	}

	public Double getObservedValue()
	{
		return observedValue;
	}

	public void setObservedValue(Double observedValue)
	{
		this.observedValue = observedValue;
	}

	public String getClUnit()
	{
		return clUnit;
	}

	public void setClUnit(String clUnit)
	{
		this.clUnit = clUnit;
	}

	public int getUnitMult()
	{
		return unitMult;
	}

	public void setUnitMult(int unitMult)
	{
		this.unitMult = unitMult;
	}

	public Double getDataValue() {
		return dataValue;
	}

	public void setDataValue(Double dataValue) {
		this.dataValue = dataValue;
	}


}
