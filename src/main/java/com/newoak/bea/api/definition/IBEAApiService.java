package com.newoak.bea.api.definition;

import retrofit.http.GET;
import retrofit.http.Query;

import com.newoak.bea.api.objects.Results;
import com.newoak.bea.helpers.KeyCodeCollection;

public interface IBEAApiService
{

	@GET("/data/")
	Results getResults(
			@Query("UserID") String userID,
			@Query("KeyCode") String keyCode,
			@Query("GeoFips") String geoFips,
			@Query("method") String method,
			@Query("datasetname") String datasetName,
			@Query("ResultFormat") String resultFormat) throws Exception;
	
	@GET("/data/")
	KeyCodeCollection getKeyCodes(
			@Query("UserID") String userID,
			@Query("ParameterName") String parameterName,
			@Query("method") String method,
			@Query("datasetname") String datasetName,			
			@Query("ResultFormat") String resultFormat);
}
