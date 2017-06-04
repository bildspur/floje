#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <ESP8266mDNS.h>

#define DEVICE_NAME "FLOJE_"

#define CONNECTION_WAIT_TIMES 30

// wifi credentials
const char *ssid = "BRXP1";
const char *password = "nfl-game";

String deviceIdentifier = String(DEVICE_NAME) + String(WiFi.macAddress());
char deviceName[25];

int connectionWaitTimes = 0;

void setupNetwork()
{
  setupDeviceName();
  setupWiFi();
  setupMDNS();
}

void setupDeviceName()
{
  deviceIdentifier.toCharArray(deviceName, deviceIdentifier.length() + 1);
  Serial.println(deviceIdentifier);
}

void initWiFi()
{
  WiFi.mode(WIFI_STA);
  WiFi.hostname(deviceName);
  WiFi.begin(ssid, password);
}

void setupWiFi()
{
  Serial.print("trying to connect...");
  initWiFi();

  ledBlink();

  // wait till wifi is connected
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");

    loopInfo();
    connectionWaitTimes++;

    // reset wifi
    if (connectionWaitTimes > CONNECTION_WAIT_TIMES)
    {
      Serial.println();
      Serial.println("re-init wifi...");
      initWiFi();
      connectionWaitTimes = 0;
    }

    delay(DEFAULT_LOOP_DELAY);
  }

  Serial.println();

  // print out relevant information
  Serial.print("Local IP address: ");
  Serial.println(WiFi.localIP());

  Serial.print("Mac Address: ");
  Serial.println(WiFi.macAddress());
}

void setupMDNS()
{
  if (!MDNS.begin(deviceName)) {
    Serial.println("Error setting up MDNS responder!");
    ledError();
  }

  // Add service to MDNS-SD
  MDNS.addService("http", "tcp", 80);
}

void loopNetwork()
{
  // check for connection loss
  if (WiFi.status() != WL_CONNECTED)
  {
    softReset();
  }
}


