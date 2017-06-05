#include <ArduinoOTA.h>

#define OTA_BLINK_INTERVAL 15

void setupOTA()
{
  setupOTAAuth();
  setupOTAOnStart();
  setupOTAOnEnd();
  setupOTAOnProgress();
  setupOTAOnError();

  ArduinoOTA.begin();
}

void setupOTAAuth()
{
  ArduinoOTA.setPort(8266);
  ArduinoOTA.setHostname(deviceName);
  ArduinoOTA.setPassword("bildspur");
}

void setupOTAOnStart()
{
  ArduinoOTA.onStart([]() {
    // set servo to initial state
    setInitialState();

    Serial.println("Start updating ...");
    ledBlink(OTA_BLINK_INTERVAL);
  });
}

void setupOTAOnEnd()
{
  ArduinoOTA.onEnd([]() {
    Serial.println("\nEnd");
    ledOFF();
  });
}

void setupOTAOnProgress()
{
  ArduinoOTA.onProgress([](unsigned int progress, unsigned int total) {
    loopInfo();
    Serial.printf("Progress: %u%%\r", (progress / (total / 100)));
  });
}

void setupOTAOnError()
{
  ArduinoOTA.onError([](ota_error_t error) {
    Serial.printf("Error[%u]: ", error);
    if (error == OTA_AUTH_ERROR) Serial.println("Auth Failed");
    else if (error == OTA_BEGIN_ERROR) Serial.println("Begin Failed");
    else if (error == OTA_CONNECT_ERROR) Serial.println("Connect Failed");
    else if (error == OTA_RECEIVE_ERROR) Serial.println("Receive Failed");
    else if (error == OTA_END_ERROR) Serial.println("End Failed");

    ESP.restart();
  });
}

void loopOTA()
{
  ArduinoOTA.handle();
}

