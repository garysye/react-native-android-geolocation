package com.rnandroidgeolocation;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class AndroidGeolocationModule extends ReactContextBaseJavaModule 
  implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
  protected static final String TAG = "GeoLocation";
  protected GoogleApiClient mGoogleApiClient;
  protected Location mLastLocation;

  private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

  @Override
  public String getName() {
    return "AndroidGeolocation";
  }

  public AndroidGeolocationModule(ReactApplicationContext reactContext) {
    super(reactContext);
    buildGoogleApiClient();
  }

  protected synchronized void buildGoogleApiClient() {
    mGoogleApiClient = new GoogleApiClient.Builder(getReactApplicationContext())
      .addConnectionCallbacks(this)
      .addOnConnectionFailedListener(this)
      .addApi(LocationServices.API)
      .build();
    mGoogleApiClient.connect();
  }

  @ReactMethod
  public void getCurrentLocation(Callback success, Callback error) {
    WritableMap location = Arguments.createMap();
    WritableMap coords = Arguments.createMap();
    String errorMessage = "Location could not be retrieved";
    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    if( mLastLocation != null ) {
      // If a location is returned, will invoke success callback with the locatation using a Javascript object
      coords.putDouble("latitude", mLastLocation.getLatitude());
      coords.putDouble("longitude", mLastLocation.getLongitude());
      location.putMap("coords", coords);
      success.invoke(location);
    } else {
      // Else, the error callback is invoked with an error message
      error.invoke(errorMessage);
    }
  }

  @Override
  public void onConnected(Bundle connectionHint) {
    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
  }


  @Override
  public void onConnectionFailed(ConnectionResult result) {
      // Refer to Google Play documentation for what errors can be logged
      Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
  }

  @Override
    public void onConnectionSuspended(int cause) {
      // Attempts to reconnect if a disconnect occurs
      Log.i(TAG, "Connection suspended");
      mGoogleApiClient.connect();
  }
}
