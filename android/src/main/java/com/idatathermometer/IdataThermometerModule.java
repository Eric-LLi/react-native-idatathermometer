package com.idatathermometer;

import android.util.Log;
import android.view.KeyEvent;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.idatachina.imeasuresdk.IMeasureSDK;

public class IdataThermometerModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

	private final ReactApplicationContext reactContext;
	private IMeasureSDK mIMeasureSDK = null;
	private final String TAG = "iData";
	private static IdataThermometerModule instance = null;
	private final String KEY_EVENTS = "keyevents";
	private final String STATUS_EVENTS = "statusevents";
	private final String TEMPERATURE_EVENTS = "temperatureevents";

	private static boolean is_reading = false;

	public IdataThermometerModule(ReactApplicationContext reactContext) {
		super(reactContext);
		this.reactContext = reactContext;

		this.reactContext.addLifecycleEventListener(this);

		instance = this;
	}

	public static IdataThermometerModule getInstance() {
		return instance;
	}

	public void onKeyDownEvent(int keyCode, KeyEvent event) {
		Log.d(TAG, "onKeyDown: " + keyCode);

		if (keyCode == 600 || keyCode == 601 || keyCode == 602) {
			WritableMap map = Arguments.createMap();
			map.putString("key", "down");
			sendEvent(KEY_EVENTS, map);
		}
	}

	public void onKeyUpEvent(int keyCode, KeyEvent event) {
		Log.v("Up keyCode", keyCode + "");
		if (keyCode == 190 || keyCode == 188 || keyCode == 189) {
			WritableMap map = Arguments.createMap();
			map.putString("key", "up");
			sendEvent(KEY_EVENTS, map);
		}
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
		if (mIMeasureSDK != null) {
			mIMeasureSDK.close();
			mIMeasureSDK = null;
		}
	}

	private void sendEvent(
			String eventName,
			@Nullable WritableMap params) {
		this.reactContext
				.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
				.emit(eventName, params);
	}

	private void sendEvent(
			String eventName,
			@Nullable String msg) {
		this.reactContext
				.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
				.emit(eventName, msg);
	}

	@ReactMethod
	public void init() {
		try {
			if (mIMeasureSDK == null) {
				//Please pay attention to the life cycle of the incoming Context. At the end of the life cycle of the Context, please call mIMeasureSDK.close ()
				mIMeasureSDK = new IMeasureSDK(this.reactContext);
				mIMeasureSDK.init(initCallback);
			}
		} catch (Exception ex) {
			WritableMap map = Arguments.createMap();
			map.putBoolean("status", false);
			map.putString("error", ex.getMessage());
			sendEvent(STATUS_EVENTS, map);
		}
	}

	@ReactMethod
	public void disconnect() {
		try {
			if (mIMeasureSDK != null) {
				mIMeasureSDK.close();
				mIMeasureSDK = null;
			}
		} catch (Exception ex) {
			WritableMap map = Arguments.createMap();
			map.putBoolean("status", false);
			map.putString("error", ex.getMessage());
			sendEvent(STATUS_EVENTS, map);
		}
	}

	@ReactMethod
	public void readTemp() {
		if (mIMeasureSDK != null && !is_reading) {
			is_reading = true;
			
			mIMeasureSDK.read(new IMeasureSDK.TemperatureCallback() {
				@Override
				public void success(final double temp) {
					is_reading = false;
					WritableMap map = Arguments.createMap();
					map.putString("temp", temp + "");
					sendEvent(TEMPERATURE_EVENTS, map);
				}

				@Override
				public void failed(int code, final String msg) {
					is_reading = false;
					WritableMap map = Arguments.createMap();
					map.putString("temp", null);
					map.putInt("code", code);
					map.putString("error", msg);
					sendEvent(TEMPERATURE_EVENTS, map);
				}
			});
		}
	}

	private IMeasureSDK.InitCallback initCallback = new IMeasureSDK.InitCallback() {
		@Override
		public void success() {
			Log.d(TAG, "success:Power on successfully");
			WritableMap map = Arguments.createMap();
			map.putBoolean("status", true);
			sendEvent(STATUS_EVENTS, map);
		}

		@Override
		public void failed(int code, String msg) {
			Log.d(TAG, "failed: Power on failed," + msg);
			WritableMap map = Arguments.createMap();
			map.putBoolean("status", false);
			map.putString("error", msg);
			sendEvent(STATUS_EVENTS, map);
		}

		@Override
		public void disconnect() {
			//Toast.makeText(getBaseContext(), "Service disconnect", Toast.LENGTH_SHORT).show();
			Log.d(TAG, "disconnect:Service disconnect");
			mIMeasureSDK.reconect();
		}
	};
}
