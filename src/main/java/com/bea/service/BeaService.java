package com.bea.service;

import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.converter.GsonConverter;

import com.bea.api.definition.IBEAApiService;
import com.bea.api.objects.KeyCode;
import com.bea.api.objects.Results;
import com.bea.deserializers.ResultsDeserializer;
import com.bea.deserializers.KeyCodeCollectionDeserializer;
import com.bea.helpers.KeyCodeCollection;
import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
	private final RateLimiter rateLimiter;
	final static String ENDPOINT = "http://www.bea.gov/api/";


	public BeaService(String apiKey)
	{
		this(apiKey, ENDPOINT);
	}

	public BeaService(String apiKey, @Nullable String optEndPoint)
	{
		this(apiKey, optEndPoint, 2);
	}

	public BeaService(String apiKey, @Nullable String optEndPoint, double requestsPerMinute)
	{
		this(apiKey, optEndPoint, RateLimiter.create(requestsPerMinute / 60));
	}

	public BeaService(String apiKey, @Nullable String optEndPoint, RateLimiter rateLimiter)
	{
		if (rateLimiter == null)
		{
			this.rateLimiter = RateLimiter.create(Double.MAX_VALUE);
		}
		else
		{
			this.rateLimiter = rateLimiter;
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
		restAdapter =
				new RestAdapter.Builder().setEndpoint(endPoint).setLogLevel(LogLevel.NONE).setConverter(new GsonConverter(gson))
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
		rateLimiter.acquire();

		Results results = null;
		try
		{
			results = service.getDataResults(apiKey, method, datasetname, keyCode, resultFormat);
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
		rateLimiter.acquire();

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
}
