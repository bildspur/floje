#include <OSCBundle.h>
#include <OSCData.h>
#include <OSCMatch.h>
#include <OSCMessage.h>

#define DEFAULT_LOOP_DELAY 10

void setup() {
  // setup all controllers
  setupInfo();

  setupServo();

  setupNetwork();
  setupOSC();

  // clean up led state
  ledStopBlink();
  ledOFF();
}

void loop() {
  loopInfo();
  loopNetwork();

  delay(DEFAULT_LOOP_DELAY);
}

void softReset() {
  setup();
}

