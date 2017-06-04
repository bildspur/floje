#define DEFAULT_LOOP_DELAY 10

void setup() {
  // setup all controllers
  setupInfo();

  setupServo();

  setupNetwork();
  setupOSC();

  ledBlink(100);
}

void loop() {
  loopInfo();
  loopNetwork();

  delay(DEFAULT_LOOP_DELAY);
}

void softReset() {
  setup();
}

