Specification
=============

The intention is that this specification will describe testable definitions of behaviour,
using BDD.

GIVEN: the software has been installed
GIVEN: the software has has been configured for background processing
WHEN: the device is rebooted
THEN: the service should be kicked-off according to the reschedule time.
TESTID: (manual only, 30/07/2013)

GIVEN: The app has started running
When: the app is terminated by pressing the Back button, and agreeing to "Really Quit"
THEN: the service should remain running, and should be kicked-off according to the reschedule time.
TESTID: (manual only, 30/07/2013)

TODO: Define all the other app behaviour!
