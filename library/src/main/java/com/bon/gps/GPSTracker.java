package com.bon.gps;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;

import com.bon.logger.Logger;

@SuppressWarnings("ALL")
@SuppressLint("Registered")
public class GPSTracker extends Service implements LocationListener {
    private static final String TAG = GPSTracker.class.getSimpleName();

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    private Context mContext;
    private LocationManager locationManager = null;
    private Location location = null;

    // is turn on location
    private boolean canGetLocation = false;

    public GPSTracker() {
    }

    /**
     * @param context
     */
    public GPSTracker(Context context) {
        this.mContext = context;
        this.setUpLocation();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.mContext = getApplicationContext();
        this.setUpLocation();
        return Service.START_STICKY;
    }

    /**
     * get location of device
     *
     * @return
     */
    private void setUpLocation() {
        try {
            // check permission for location with android
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            this.locationManager = (LocationManager) this.mContext.getSystemService(LOCATION_SERVICE);
            // getting GPS status
            boolean isGPSEnabled = this.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            boolean isNetworkEnabled = this.locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                System.out.println(TAG + ":: setUpLocation:: No network provider is enabled!");
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    System.out.println("Network");
                    if (this.locationManager != null) {
                        this.location = this.locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                }

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (this.location == null) {
                        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        System.out.println("GPS Enabled");
                        if (this.locationManager != null) {
                            this.location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * Stop using GPS listener Calling this function will stop using GPS in your
     * app
     */
    public void stopUsingGPS() {
        try {
            if (this.locationManager != null) {
                // check permission for location with android has version >= 23
                if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    this.locationManager.removeUpdates(GPSTracker.this);
                }
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * get location
     *
     * @return
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        return this.location != null ? this.location.getLatitude() : 0;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        return this.location != null ? this.location.getLongitude() : 0;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            System.out.println("TAG:: " + "Location:: getLatitude:: " + location.getLatitude() + "::getLongitude " + location.getLongitude());
            this.location = location;
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Function to show settings alert dialog On pressing Settings button will launch Settings Options
     *
     * @param title
     * @param message
     * @param messageOpenSettingGps
     * @param messageDontAllowSettingGps
     */
    public void showSettingsAlert(String title, String message,
                                  String messageOpenSettingGps, String messageDontAllowSettingGps) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(title);
            builder.setMessage(message);

            // On pressing Settings button
            builder.setPositiveButton(messageOpenSettingGps, (dialog, which) -> {
                try {
                    dialog.dismiss();
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(intent);
                } catch (Exception e) {
                    Logger.e(TAG, e);
                }
            });

            // on pressing cancel button
            builder.setNegativeButton(messageDontAllowSettingGps, (dialog, which) -> {
                try {
                    dialog.cancel();
                } catch (Exception e) {
                    Logger.e(TAG, e);
                }
            });

            // set cancelable
            builder.setCancelable(true);

            // Showing Alert Message
            builder.show();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }
}
