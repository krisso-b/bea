package com.bea.api.objects;

import java.io.Serializable;
import java.util.List;

public class Results implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8236730735639022108L;

	private String keyCode;
	private String statistic;
	private String UnitOfMeasure;
	private String PublicTable;
	private String UTCProductionTime;
	private List<RequestParam> requestParam;
	private List<Dimension> dimensons;
	private List<Data> data;

		
	public String getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(String keyCode) {
		this.keyCode = keyCode;
	}

	public String getStatistic()
	{
		return statistic;
	}

	public void setStatistic(String statistic)
	{
		this.statistic = statistic;
	}

	public String getUnitOfMeasure()
	{
		return UnitOfMeasure;
	}

	public void setUnitOfMeasure(String unitOfMeasure)
	{
		UnitOfMeasure = unitOfMeasure;
	}

	public String getPublicTable()
	{
		return PublicTable;
	}

	public void setPublicTable(String publicTable)
	{
		PublicTable = publicTable;
	}

	public String getUTCProductionTime()
	{
		return UTCProductionTime;
	}

	public void setUTCProductionTime(String uTCProductionTime)
	{
		UTCProductionTime = uTCProductionTime;
	}

	public List<Dimension> getDimensons()
	{
		return dimensons;
	}

	public void setDimensons(List<Dimension> dimensons)
	{
		this.dimensons = dimensons;
	}

	public List<Data> getData()
	{
		return data;
	}

	public void setData(List<Data> data)
	{
		this.data = data;
	}

	public List<RequestParam> getRequestParam()
	{
		return requestParam;
	}

	public void setRequestParam(List<RequestParam> requestParam) 
	{
		this.requestParam = requestParam;
	}

}
