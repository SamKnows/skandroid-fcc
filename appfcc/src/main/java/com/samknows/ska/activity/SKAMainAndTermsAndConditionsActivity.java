package com.samknows.ska.activity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.CrashManagerListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ScrollView;

import com.samknows.libcore.SKLogger;
import com.samknows.measurement.SK2AppSettings;
import com.samknows.measurement.MainService;
import com.samknows.fcc.R;
import com.samknows.measurement.activity.BaseLogoutActivity;
import com.samknows.measurement.activity.components.Util;
import com.samknows.measurement.util.LoginHelper;
import com.samknows.measurement.util.OtherUtils;

@SuppressLint("InlinedApi")
public class SKAMainAndTermsAndConditionsActivity extends BaseLogoutActivity {

	WebView webview;
	int page_number;
	int total_pages = 3;
	
	// Update this to be the version number at which the T&C text changed most recently.
	final String tandcAgreementVersion = "1.74";
	
	WebAppInterface webinterface;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d(this.getClass().toString(), "*** onCreate ***");
		
//	    // Code to print out the key hash
//	    try {
//	        PackageInfo info = getPackageManager().getPackageInfo(
//	                "com.samknows.fcc", 
//	                PackageManager.GET_SIGNATURES);
//	        for (Signature signature : info.signatures) {
//	            MessageDigest md = MessageDigest.getInstance("SHA");
//	            md.update(signature.toByteArray());
//	            Log.d("KeyHash:", "KeyHash - " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
//	            }
//	    } catch (NameNotFoundException e) {
//
//	    } catch (NoSuchAlgorithmException e) {
//
//	    }

		if (OtherUtils.isDebuggable(this)) {
			Log.d(this.getClass().toString(),
					"OtherUtils.isDebuggable(), not using crash reporting");
		} else {
			Log.d(this.getClass().toString(),
					"This app is NOT debuggable, so setting-up crash reporting!");
			CrashManager.register(this, "3d13669fc03f8ace6693934bc9922c65",
					new CrashManagerListener() {
				@Override
				public boolean shouldAutoUploadCrashes() {
					//Log.d(this.getClass().toString(), "*** CrashManagerListener shouldAutoUploadCrashes ***");
					return true;
				}
				@Override
				public void onConfirmedCrashesFound() {
					Log.d(this.getClass().toString(), "*** CrashManagerListener onConfirmedCrashesFound ***");
				}
				@Override
				public void onCrashesNotSent() {
					Log.d(this.getClass().toString(), "*** CrashManagerListener onCrashesNotSent ***");
				}
				@Override
				public void onCrashesSent() {
					Log.d(this.getClass().toString(), "*** CrashManagerListener onCrashesSent ***");
				}
				@Override
				public void onNewCrashesFound() {
					Log.d(this.getClass().toString(), "*** CrashManagerListener onNewCrashesFound ***");
				}
			});
		}

		final SK2AppSettings appSettings = SK2AppSettings.getSK2AppSettingsInstance();

		final Activity ctx = this;
		//	final Editor e = this.getPreferences(Context.MODE_PRIVATE).edit();

		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		String agreement = prefs.getString("agreement", null);
		
    	// The T&C was last changed at 1.74; but the code to handle detection of
	    // this change kicked-in at 1.89
		// TODO: when T&C is next changed, we *must* delete this block of code.
		if (agreement != null) {
			try {
				if ( Double.valueOf(agreement) >= 1.74)
				{
					agreement = tandcAgreementVersion;
				}
			} catch (Exception e) {
				SKLogger.sAssert(getClass(),  false);
			}
		}
		if (agreement != null && agreement.equals(tandcAgreementVersion)) {
			
			// ALWAYS now go straight to the main screen - activation is handled from there.
			LoginHelper.openMainScreenWithNoTransitionAnimation(ctx, SKAMainResultsActivity.class);
			
			this.setTheme(android.R.style.Theme_NoDisplay);
		} else {
			// The Activity starts invisible (see the AndroidManifest.xml) - we need
			// to make the Activity visible at this point, so that we can show the T&C!
			//this.setTheme(android.R.style.Theme_Holo_Light);
			//this.setTheme(R.style.ApplicationStyle);

			setContentView(R.layout.ska_main_and_terms_and_conditions_activity);
			appSettings.setForceDownload();
			page_number = 1;

			webview = (WebView) findViewById(R.id.webview);
			webview.getSettings().setJavaScriptEnabled(true);

			webview.measure(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

			int target_height = webview.getMeasuredHeight();

			webinterface = new WebAppInterface(this);
			webview.addJavascriptInterface(webinterface, "Android");

			webview.loadUrl("file:///android_asset/notice" + page_number
					+ ".htm");

			Button next_page = (Button) findViewById(R.id.btn_continue);

			next_page.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					webinterface.changePage();
				}
			});

			Util.initializeFonts(this);
			Util.overrideFonts(this, findViewById(android.R.id.content));
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	// Pressing back in this screen, always allows back to close the application.
	public boolean forceBackToAllowClose() {
		if (page_number > total_pages) {
			return true;
		}
		
		if (webinterface == null) {
			return true;
		}
		
		return false;
	}
	
	public boolean wouldBackButtonReturnMeToTheHomeScreen() {
		if (page_number == 1) {
			return true;
		}

		return false;
	}
	
	public void onBackPressed() {
		if (page_number == 1) {
			super.onBackPressed();
		} else {
			if (webinterface == null) {
				super.onBackPressed();
			} else {
				// If on last page, allow back to work again!
				if (page_number >= total_pages) {
					page_number--;
					
				}
				webinterface.changePage(-1);
			}
		}

	}

	public class WebAppInterface {
		Context mContext;

		/** Instantiate the interface and set the context */
		WebAppInterface(Context c) {
			mContext = c;
		}

		@JavascriptInterface
		public void showToast(String toast) {

			final AlertDialog alertDialog = new AlertDialog.Builder(
					SKAMainAndTermsAndConditionsActivity.this).create();
			alertDialog.setMessage(toast);
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

					alertDialog.dismiss();

				}
			});

			Util.overrideFonts(SKAMainAndTermsAndConditionsActivity.this,
					findViewById(android.R.id.content));

			alertDialog.show();

		}

		@JavascriptInterface
		public void changePage() {
			
			changePage(1);
		}
		
		public void changePage(int PAddThisAmount) {

			final Activity ctx = SKAMainAndTermsAndConditionsActivity.this;
			final Editor e = SKAMainAndTermsAndConditionsActivity.this.getPreferences(
					Context.MODE_PRIVATE).edit();

			if (page_number > total_pages) {
				e.putString("agreement", tandcAgreementVersion);
				e.commit();
				
    			// ALWAYS now go straight to the main screen - activation is handled from there.
	    		LoginHelper.openMainScreenWithNoTransitionAnimation(ctx, SKAMainResultsActivity.class);
			}
			
			if ( ((page_number < total_pages) && (PAddThisAmount > 0)) ||
			     ((page_number > 0) && (PAddThisAmount < 0))) {
				page_number += PAddThisAmount;
				ViewGroup vg = (ViewGroup) webview.getParent();
				int index = vg.indexOfChild(webview);
				vg.removeView(webview);
				webview  = new WebView(SKAMainAndTermsAndConditionsActivity.this);
				
				webview.getSettings().setJavaScriptEnabled(true);
				
				webview.loadUrl("file:///android_asset/notice" + page_number
						+ ".htm");
				vg.addView(webview,index);
				webview.addJavascriptInterface(webinterface, "Android");
				webview.setVisibility(View.GONE);
				webview.setVisibility(View.VISIBLE);
				ScrollView scrollView = (ScrollView) findViewById(R.id.webscrollview);
				scrollView.fullScroll(View.FOCUS_UP);
			}

			if (page_number == total_pages) {
				page_number = total_pages + 1;
				if(!SK2AppSettings.getSK2AppSettingsInstance().data_cap_welcome){
					Button btn_next = (Button) findViewById(R.id.btn_continue);
					btn_next.setText(R.string.agree);
				}
			}
		}
	}
}
