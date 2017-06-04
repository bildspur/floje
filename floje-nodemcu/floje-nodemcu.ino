

void setup() {
  // setup all controllers
  setupInfo();

  setupNetwork();
  setupOSC();

  setupServo();

  ledOFF();
  ledBlink();
}

void loop() {
  loopInfo();
  
  delay(10);
}
