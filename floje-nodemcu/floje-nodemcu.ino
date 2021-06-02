extern "C" {
#include <user_interface.h>
}

#include <Arduino.h>

#include <OSCBundle.h>
#include <OSCData.h>
#include <OSCMatch.h>
#include <OSCMessage.h>

// NodeMCU Digital Pins
#define D0  16
#define D1  5  // I2C Bus SCL (clock)
#define D2  4  // I2C Bus SDA (data)
#define D3  0
#define D4  2  // Same as "LED_BUILTIN", but inverted logic
#define D5  14 // SPI Bus SCK (clock)
#define D6  12 // SPI Bus MISO 
#define D7  13 // SPI Bus MOSI
#define D8  15 // SPI Bus SS (CS)
#define D9  3  // RX0 (Serial console)
#define D10 1  // TX0 (Serial console)

#define DEFAULT_LOOP_DELAY 10

const char *version = "1.4";

void setup() {
  // setup all controllers
  setupInfo();

  // network related
  setupNetwork();
  setupOTA();

  setupOSC();
  setupStatus();

  // mechanical
  setupServo();

  // clean up led state
  ledStopBlink();
  ledOFF();
}

void loop() {
  loopInfo();

  loopNetwork();
  loopStatus();

  loopOTA();

  servoLoop();
}

void softReset() {
  ESP.restart();
}

void wait(unsigned long waitTime)
{
  unsigned long previousMillis = millis();
  while (millis() - previousMillis <= waitTime)
  {
    loopInfo();
    delay(DEFAULT_LOOP_DELAY);
  }
}
