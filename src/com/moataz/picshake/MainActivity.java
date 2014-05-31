package com.moataz.picshake;


import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class MainActivity extends Activity {
	
    SecurePreferences preferences;
    private ProgressDialog progressDialog;
    private final String _USERNAME_ = "userId";
    private final String _PASSWORD_ = "password";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		//TODO:remove these checks and move them only to send
		// activity when the offline token is implemented
		if (!isGPSEnabled()) {
			showGpsSettingsAlert();
		}
		if(!isNetworkAvailable())
		{
			showNoInternetSettingsAlert();
		}
		progressDialog = new ProgressDialog(this);
		preferences = new SecurePreferences(this, "my-preferences", "TopSecretKey123kdd", true);

		// Set progressdialog title
//		progressDialog.setTitle("Download Image");
		// Set progressdialog message
		progressDialog.setMessage("Logging out...");
		progressDialog.setIndeterminate(false);


	}
	
	@Override
	protected void onResume() {
		super.onResume();
		NotificationManager nm = (NotificationManager)  getSystemService(NOTIFICATION_SERVICE);
	    nm.cancel(0);
	}
	
	@Override
	public void onBackPressed() {
	    moveTaskToBack(true);
	}
	
	
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	public boolean isGPSEnabled(){
		// flag for GPS status
		boolean isGPSEnabled = false;

		// flag for network status
		boolean isNetworkEnabled = false;

		// Declaring a Location Manager
		LocationManager locationManager = null;
		// flag for GPS status
		boolean canGetLocation = false;
		try {
			locationManager = (LocationManager) this
					.getSystemService(LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				return false;
			}
			return true;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
		
	}
	
	/**
	 * Function to show settings alert dialog
	 * On pressing Settings button will lauch Settings Options
	 * */
	public void showGpsSettingsAlert(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
   	 
        // Setting Dialog Title
        alertDialog.setTitle("GPS settings");
 
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
 
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            	startActivity(intent);
            }
        });
 
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            android.os.Process.killProcess(android.os.Process.myPid());
            dialog.cancel();
            }
        });
 
        // Showing Alert Message
        alertDialog.show();
	}
	
	public void showNoInternetSettingsAlert(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
   	 
        // Setting Dialog Title
        alertDialog.setTitle("Internet settings");
 
        // Setting Dialog Message
        alertDialog.setMessage("Internet is not enabled. Do you want to go to settings menu?");
 
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	Intent intent = new Intent(Settings.ACTION_SETTINGS);
            	startActivity(intent);
            }
        });
 
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	android.os.Process.killProcess(android.os.Process.myPid());
            dialog.cancel();
            }
        });
 
        // Showing Alert Message
        alertDialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_logout:
	        	preferences.removeValue(_USERNAME_);
				preferences.removeValue(_PASSWORD_);
				preferences.put("CheckBox_Value", "0");
				Intent intent = new Intent(MainActivity.this, SigninPage.class);
            	startActivity(intent);
            	finish();
	            return true;
	        case R.id.action_info:
	        	Utils.showAlert("About PicShake", "<p>Version Beta 1.0</p><p>PicShake</p><p>Copyright 2014 Valyria Inc. All rights reserved.</p><p>This is only a non official Beta Version of the app</p><p><a href='http://hezzapp.appspot.com/terms'>Terms of Use</a></p><p><a href='http://hezzapp.appspot.com/privacy'>Privacy Policy</a></p>", MainActivity.this);
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
	/** Called when the user clicks the Send button */
	public void sendActivity(View view) {
		Animation vanish =AnimationUtils.loadAnimation(this,R.anim.vanish);
	    vanish.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// Do nothing
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// Do nothing
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				Intent intent = new Intent(MainActivity.this, SenderActivity.class);
			    intent.putExtra("selectOrCamera", 0); //camera or select
			    // 0 is select , 1 is camera
			    startActivity(intent);
			}
		});
	    view.startAnimation(vanish);
	}
	
	public void takePic(View view) {
		Animation vanish =AnimationUtils.loadAnimation(this,R.anim.vanish);
	    vanish.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// Do nothing
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// Do nothing
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				Intent intent = new Intent(MainActivity.this, SenderActivity.class);
			    intent.putExtra("selectOrCamera", 1); //camera or select
			     //0 is select , 1 is camera
			    startActivity(intent);	
			}
		});
	    view.startAnimation(vanish);
	}
	
	public void receiveActivity(View view) {
		Animation vanish =AnimationUtils.loadAnimation(this,R.anim.vanish);
	    vanish.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// Do nothing
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// Do nothing
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
			    Intent intent = new Intent(MainActivity.this, ReceiverActivity.class);
			    startActivity(intent);
			}
		});
	    view.startAnimation(vanish);
	}
	
	
		
//	/**
//	 * Function to show alert dialog
//	 * */
//	public void showAlert(String aInTitle, String aInMessage, Context aInContext){
//		AlertDialog.Builder alertDialog = new AlertDialog.Builder(aInContext);
//   	 
//        // Setting Dialog Title
//        alertDialog.setTitle(aInTitle);
// 
//        // Setting Dialog Message
//        alertDialog.setMessage(aInMessage);
// 
//        // on pressing cancel button
//        alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//            dialog.cancel();
//            }
//        });
// 
//        // Showing Alert Message
//        alertDialog.show();
//	}

}
