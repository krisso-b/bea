package com.bea.api.objects;

import java.io.Serializable;

public class Dimension implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7735903838178812907L;

	private int ordinal;
	private String name;
	private String dataType;
	private String isValue;

	public int getOrdinal()
	{
		return ordinal;
	}

	public void setOrdinal(int ordinal)
	{
		this.ordinal = ordinal;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDataType()
	{
		return dataType;
	}

	public void setDataType(String dataType)
	{
		this.dataType = dataType;
	}

	public String getIsValue()
	{
		return isValue;
	}

	public void setIsValue(String isValue)
	{
		this.isValue = isValue;
	}


}
