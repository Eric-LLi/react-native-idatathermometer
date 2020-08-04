package com.idatathermometer;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

public class IdataThermometerModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

	private final ReactApplicationContext reactContext;
	IMeasureSDK mIMeasureSDK;

	public IdataThermometerModule(ReactApplicationContext reactContext) {
		super(reactContext);
		this.reactContext = reactContext;

		this.reactContext.addLifecycleEventListener(this);
	}

	@Override
	public String getName() {
		return "IdataThermometer";
	}

	@ReactMethod
	public void sampleMethod(String stringArgument, int numberArgument, Callback callback) {
		// TODO: Implement some actually useful functionality
		callback.invoke("Received numberArgument: " + numberArgument + " stringArgument: " + stringArgument);
	}

	@Override
	public void onHostResume() {

	}

	@Override
	public void onHostPause() {

	}

	@Override
	public void onHostDestroy() {

	}
}
