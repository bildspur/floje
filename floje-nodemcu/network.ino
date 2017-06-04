#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <WiFiUdp.h>
#include <ESP8266mDNS.h>

#define DEVICE_NAME "FLOJE_"
#define OSC_LOCAL_PORT 8000

#define CONNECTION_WAIT_TIMES 5000

// wifi credentials
const char *ssid = "loading...";
const char *password = "";

String deviceIdentifier = String(DEVICE_NAME) + String(WiFi.macAddress());
char deviceName[25];

int connectionWaitTimes = 0;

WiFiUDP Udp;

void setupNetwork()
{
  setupDeviceName();
  setupWiFi();

  setupUDPServer();
  setupMDNS();

  ledOFF();
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
  Serial.println("trying to connect...");
  while (WiFi.status() != WL_CONNECTED) {
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
  MDNS.addService("osc", "udp", OSC_LOCAL_PORT);
}

void setupUDPServer()
{
  Udp.begin(OSC_LOCAL_PORT);
}

void loopNetwork()
{
  loopUdp();

  // check for connection loss
  if (WiFi.status() != WL_CONNECTED)
  {
    setupNetwork();
    ledError();
    Serial.println("lost connection...");
  }
}

void loopUdp()
{
  OSCMessage msg;
  int size;
  if ((size = Udp.parsePacket()) > 0) {
    while (size--)
      msg.fill(Udp.read());
    if (!msg.hasError()) {
      routeOSCMessage(msg);
    }
  }
}

