package com.bea.service;

import com.bea.api.definition.IBEAApiService;
import com.bea.api.objects.KeyCode;
import com.bea.api.objects.Results;
import com.bea.constants.BeaConstants;
import com.bea.deserializers.KeyCodeCollectionDeserializer;
import com.bea.deserializers.ResultsDeserializer;
import com.bea.helpers.KeyCodeCollection;
import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.ConnectionPool;
import com.squareup.okhttp.OkHttpClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author pmurugesan
 *
 */
public class BeaService
{
	private static final Log log = LogFactory.getLog(BeaService.class);

	private String apiKey;
	private String endPoint;
	final static String RETURNTYPE = "json";
	private static Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
			.setDateFormat("yyyy-MM-dd").registerTypeAdapter(Results.class, new ResultsDeserializer()).
			registerTypeAdapter(KeyCodeCollection.class, new KeyCodeCollectionDeserializer()).create();

	private static RestAdapter restAdapter = null;
	private static IBEAApiService service = null;

	final static String ENDPOINT = "http://www.bea.gov/api/";


	public BeaService(String apiKey)
	{
		this(apiKey, ENDPOINT);
	}

	public BeaService(String apiKey, @Nullable String optEndPoint)
	{
		this(apiKey, optEndPoint, 300);
	}

	public BeaService(String apiKey, @Nullable String optEndPoint, double requestsPerMinute)
	{
		this(apiKey, optEndPoint, RateLimiter.create(requestsPerMinute / 60));
	}

	public BeaService(String apiKey, @Nullable String optEndPoint, RateLimiter rateLimiter)
	{
		if (rateLimiter == null)
		{
			rateLimiter = RateLimiter.create(Double.MAX_VALUE);
		}

		this.apiKey = apiKey;
		if (optEndPoint != null && !optEndPoint.isEmpty())
		{
			this.endPoint = optEndPoint;
		}
		else
		{
			this.endPoint = ENDPOINT;
		}

		OkHttpClient client = new OkHttpClient();
		client.setConnectionPool(new ConnectionPool(0, 5 * 60 * 1000));

		restAdapter =
				new RestAdapter.Builder().setClient(new OkClient(client)).setEndpoint(endPoint).setRequestInterceptor(new HttpRequestLimiter(rateLimiter)).setLogLevel(LogLevel.NONE).setErrorHandler(new RetrofitErrorHandler()).setConverter(new GsonConverter(gson))
						.build();
		service = restAdapter.create(IBEAApiService.class);
	}


	/**
	 * @param method
	 * @param datasetname
	 * @param keyCode
	 * @param resultFormat
	 * @return Results
	 * @throws Exception
	 */
	public Results getResults(String method, String datasetname, String keyCode, String resultFormat) throws Exception
	{
		Results results = null;
		try
		{
			String geoFips = getGeoFips(keyCode);
			
			results = service.getResults(apiKey, method, datasetname, keyCode, geoFips, resultFormat);
		}
		catch (Exception e)
		{
			log.error(e);
			throw e;
		}
		return results;
	}
	
	
	/**
	 * @param method
	 * @param datasetname
	 * @param parameterName
	 * @param resultFormat
	 * @return List<KeyCode>
	 * @throws Exception
	 */
	public List<KeyCode> getKeyCodesList(String method, String datasetname, String parameterName, String resultFormat) throws Exception
	{
		KeyCodeCollection keyCodeCollection = null;
		try
		{
			keyCodeCollection = service.getKeyCodes(apiKey, method, datasetname, parameterName, resultFormat);
		}
		catch (Exception e)
		{
			log.error(e);
			throw e;
		}
		return keyCodeCollection.getKeyCodeList();
	}
	
	private String getGeoFips(final String keyCode){
		String geoFips = null;
		
		if(keyCode.indexOf(BeaConstants.KEY_CODE_WITH_S) > -1 || keyCode.indexOf(BeaConstants.KEY_CODE_WITH_Q) > -1){
			geoFips = BeaConstants.GEO_FIPS_STATE;
		}else if(keyCode.indexOf(BeaConstants.KEY_CODE_WITH_C) > -1){
			geoFips = BeaConstants.GEO_FIPS_COUNTY;
		}else if(keyCode.indexOf(BeaConstants.KEY_CODE_WITH_M) > -1){
			geoFips = BeaConstants.GEO_FIPS_MSA;
		}
		
		return geoFips;
	}
}
