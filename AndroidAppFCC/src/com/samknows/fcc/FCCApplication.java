package com.samknows.fcc;

import com.samknows.libcore.R;
import com.samknows.measurement.SKApplication;
import com.samknows.ska.activity.SKAMainResultsActivity;

public class FCCApplication extends SKApplication {

	public FCCApplication() {
		super();

		setNetworkTypeResults(eNetworkTypeResults.eNetworkTypeResults_Mobile);
	}

	// Get the class of the main activity!
	public Class getTheMainActivityClass() {
		return SKAMainResultsActivity.class;
	}

	// Return the About screen title.
	public String getAboutScreenTitle() {
		return getApplicationContext().getString(R.string.about);
	}

	public boolean hideJitter() {
		return true;
	}

	public boolean hideJitterLatencyAndPacketLoss() {
		return false;
	}
	
	public boolean allowUserToSelectTestToRun() {
		// User selects the test to run!
		return true;
	}
	
	public boolean isExportMenuItemSupported() {
		return true;
	}
	
	public boolean isSocialMediaExportSupported() {
		return true;
	}
	
	public boolean isSocialMediaImageExportSupported() {
		return true;
	}
	
	public boolean isThrottleQuerySupported() {
		return false;
	}
	
	
	// Some versions of the app - including this one! can enable background menu forcing via the menu...
	public boolean isForceBackgroundMenuItemSupported () {
//		if (OtherUtils.isThisDeviceAnEmulator() == true) {
//			if (OtherUtils.isDebuggable(SKAMainResultsActivity.this)) {
//				return true;
//			}
//		}
//      return false;
		
		return false;
	}	
	
	public String getExportFileProviderAuthority() {
		return "com.samknows.fcc.ExportFileProvider.provider";
	}
}
