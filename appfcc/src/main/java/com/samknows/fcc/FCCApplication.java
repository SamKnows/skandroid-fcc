package com.samknows.fcc;

import java.io.IOException;

import com.samknows.libcore.SKLogger;
import com.samknows.measurement.SKApplication;
import com.samknows.ska.activity.SKAMainResultsActivity;

public class FCCApplication extends SKApplication {

	public FCCApplication() {
		super();

		setNetworkTypeResults(eNetworkTypeResults.eNetworkTypeResults_Mobile);
	}
	
  @Override
  public String getBaseUrlForUpload() {
    return "http://dcs-mobile-fcc.samknows.com";
  }

	@Override
	public String getEnterpriseId() {
		return "FCC_Public";
	}


  @Override
  public boolean getAnonymous() {
    return true;
  }
                

//  @Override
//  // Return null if not overridden.
//  public Typeface getDefaultTypeface() {
//    //return Typeface.DEFAULT;
//    return SKTypeface.sGetTypefaceWithPathInAssets("typewriter.ttf");
//  }


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

	@Override
	public String getAppName() {
		return getApplicationContext().getString(R.string.app_name);
	}
	
	
	public boolean isSocialMediaExportSupported() {
		return true;
	}
	
	public boolean isSocialMediaImageExportSupported() {
		return true;
	}
	
	public boolean isThrottleQuerySupported() {
		return true;
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

  
  @Override
  public java.io.InputStream getScheduleXml() {
    // Must be overridden!
    java.io.InputStream inputStream = null;
    try {
      inputStream = getAssets().open("Schedule_FCC.xml");
    } catch (IOException e) {
      SKLogger.sAssert(getClass(),  false);
    }
    return inputStream;
  }
}
