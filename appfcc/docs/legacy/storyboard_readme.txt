Storyboard Analysis
===================

Methodology:
- Find all instances of startActivity*
- Find all instances of finish()
- From this, identify all unused activities.

Note:
- if you extend/change the app, you MUST update this document.

Future:
- Consider how to UNIT TEST this user interface flow; so we can maintain the app flow logic
  without breaking the flow.

App start -> Activity: SamKnowsWelcome
  ... from AndroidManifest.xml ...
  <action android:name="android.intent.action.MAIN" />
  <category android:name="android.intent.category.LAUNCHER" />

Activity: SamKnowsWelcome
  When onCreate entered:
    LoginHelper.openMainScreen()

    If the settings is such that preferences.anonymous=false:
      If logged-in:
        LoginHelper.openMainScreen()
          -> SamKnowsAggregateStatViewerActivity (FLAG_ACTIVITY_CLEAR_TASK ...!!)
          finish()
      If no password found, then:
      -> SamKnowsResetPassword
  
    else: (preferences.anonymous=true ... THIS IS THE DEFAULT CASE AT THE MOMENT!)
      if the settings is such that preferences.agreement is defined, and is equal to VERSION_NAME:
        LoginHelper.openMainScreen()
          -> SamKnowsAggregateStatViewerActivity (FLAG_ACTIVITY_CLEAR_TASK ...!!)
          finish()
      else
        *** ANONYMOUS IS TRUE, AGREEMENT NOT MATCHED: SHOW WELCOME_ANONYMOUS
        setContentView(R.layout.welcome_anonymous);

  When: INTENT_ACTION_LOGOUT broadcast event is received, via onReceiveMethod:
    finish();

  When register pressed:
  -> SamKnowsRegister

  When login pressed:
  -> SamKnowsLogin

  GIVEN: **** ANONYMOUS IS TRUE, AGREEMENT NOT MATCHED: SHOW WELCOME_ANONYMOUS
    When changePage is pressed in the WebView:
      if (last page exceeded):
        if (AppSettings.data_cap_welcome)
          -> SamKnowsInitialSettings (FLAG_ACTIVITY_CLEAR_TASK ...!!)
        else
          -> SamKnowsActivating (FLAG_ACTIVITY_CLEAR_TASK ...!!)
      else:
        display a different web view page...!

Activity: RunTestActivity

Activity: SamKnowsAbout

Activity: SamKnowsActivating
  When JSON_COMPLETED:
    LoginHelper.openMainScreen()
      -> SamKnowsAggregateStatViewerActivity (FLAG_ACTIVITY_CLEAR_TASK ...!!)
      finish()
  When MainService not working for some reason (!)
    LoginHelper.openMainScreen()
      -> SamKnowsAggregateStatViewerActivity (FLAG_ACTIVITY_CLEAR_TASK ...!!)
      finish()

Activity: SamKnowsAggregateStatViewerActivity

  When execute pressed:
    -> SamKnowsTestViewerActivity
  When menu_about pressed:
    -> SamKnowsAbout
  When menu_settings pressed:
    -> SKPreferenceActivity
  When menu_system_info pressed:
    -> SamKnowsInfoActivity
  When activiation pressed:
    -> SamKnowsActivating
    finish()
  When map pressed:
    -> SamKnowsMapActivity
  When menu_terms_and_condition pressed:
    -> SamKnowsTermsOfUse
  When menu_logout pressed:
  	LoginHelper.logout(this):
      -> SamKnowsLogin()
    finish()
  When RunChoice called...
    -> SamKnowsTestViewerActivity (startActivityForResult)
  When onBackPressed()
    if (isTaskRoot):
      call the new implementation in SamKnowsBaseActivity.onBackPressed()
    else:
      if (on_aggregate_page):
        finish()

Activity: SamKnowsInfoActivity

Activity: SamKnowsInitialSettings
  When btn_continue is pressed, and fi datacap in min/max range:
  -> SamKnowsActivating (FLAG_ACTIVITY_CLEAR_TASK ...!!)
     finish()

Activity: SamKnowsLogin
  When onCreate entered, and if we're already logged-in:
    LoginHelper.openMainScreen()
    -> SamKnowsAggregateStatViewerActivity (FLAG_ACTIVITY_CLEAR_TASK ...!!)
  When register pressed:
  -> SamKnowsRegister
  When recover pressed:
  -> SamKnowsRecoverPassword
  When login button pressed:
    LoginHelper.login()
      openActiviatingScreen()
        -> SamKnowsActivating
        finish()
  When: INTENT_ACTION_LOGOUT broadcast event is received, via onReceiveMethod:
    finish();

Activity: SamKnowsMainStatActivity

Activity: SamKnowsMainStatTabActivity

Activity: SamKnowsMapActivity
  When: onInfoWindowClick (via google maps API):
    When: dialog()... OK button pressed:
      finish() ... back to SamKnowsAggregateStatViewerActivity

Activity: SamKnowsRecoverPassword
  When btn_goto_reset pressed:
  -> SamKnowsResetPassword

Activity: SamKnowsRegister
  When: login_button pressed:
    finish() ... back to SamKnowsLogin or SamKnowsWelcome

Activity: SamKnowsResetPassword
  ... back to SamKnowsRecoverPassword or SamKnowsWelcome

Activity: SamKnowsTermsOfUse
  ... back to SamKnowsAggregateStatViewerActivity

Activity: SamKnowsTestViewerActivity
  When: Json "completed":
    finish() ... back to TestResultsActivity
  When: manual_test_error dialog ok pressed:
    finish() ... back to TestResultsActivity
  When: data_cap_exceeded dialog NO pressed:
    finish() ... back to TestResultsActivity
  When: single choice cancel pressed:
    finish() ... back to TestResultsActivity
  When: onBackPressed
    if (not isTaskRoot):
      if dialog YES pressed:
        finish() ... back to TestResultsActivity

Activity: SKLogsActivity
  ... back to SKPerformanceActivity

DEPRECATED!!! Activity: SKPerformanceActivity ... IS THIS USED IN THE EAQ APP???
  When btn_logs pressed:
    -> SKLogsActivity
  When btn_system_info pressed:
    -> SamKnowsInfoActivity
  When btn_test_results pressed:
    -> TestResultsTabActivity
  When menu_settings selected from options menu:
    -> SKPreferenceActivity
  When onCancel pressed in the onCreateDialog method:
    finish()

Activity: SKPreferenceActivity
  ... back to SamKnowsAggregateStatViewerActivity or SKPerformanceActivity

Activity: TestResultsActivity
  THIS IS EMBEDDED IN TestResultsTabActivity, in various tabs. WILL THIS BREAK THE BACK BUTTON BEHAVIOUR?!

Activity: TestResultsTabActivity
  ... back to SKPerformanceActivity.
  THIS EMBEDS TestResultsActivity, in various tabs. WILL THIS BREAK THE BACK BUTTON BEHAVIOUR?!

BASE CLASS OF SOME ACTIVITIES: BaseLogoutActivity
  When: INTENT_ACTION_LOGOUT broadcast event is received, via onReceiveMethod:
    finish();

BASE CLASS OF ALL ACTIVITIES: SamKnowsBaseActivity
  onKeyDown()
    If back key pressed, and if isTaskRoot, show dialog; and allow user to decide if they
    really want to quit or not!
