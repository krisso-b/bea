package com.bea.service;

import com.google.common.util.concurrent.RateLimiter;
import retrofit.RequestInterceptor;

import javax.annotation.Nonnull;

/**
 * Created by bweidlich on 10/15/2014.
 */
public class HttpRequestLimiter implements RequestInterceptor
{
	private RateLimiter rateLimiter;
	public HttpRequestLimiter(@Nonnull RateLimiter rateLimiter)
	{
		this.rateLimiter = rateLimiter;
	}

	@Override
	public void intercept(RequestFacade request)
	{
		rateLimiter.acquire();
	}
}
