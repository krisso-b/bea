package com.newoak.bea.service;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * Created by bweidlich on 10/15/2014.
 */
public class RetrofitErrorHandler implements ErrorHandler
{

	@Override
	public Throwable handleError(RetrofitError cause) {

		if (cause.isNetworkError()) {
			if(cause.getMessage().contains("authentication")){
				//401 errors
				return  new Exception("Invalid credentials. Please verify login info.");
			}else if (cause.getCause() instanceof SocketTimeoutException) {
				//Socket Timeout
				return new SocketTimeoutException("Connection Timeout. " +
						"Please verify your internet connection.");
			} else {
				System.out.println("Cause : " + cause.getCause());
				//No Connection
				return new ConnectException("No Connection. " +
						"Please verify your internet connection.");
			}
		} else {

			return cause;
		}
	}

}
