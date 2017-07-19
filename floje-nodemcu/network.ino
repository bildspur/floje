#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <WiFiUdp.h>
#include <ESP8266mDNS.h>

#define DEVICE_IDENTIFIER "floje"
#define OSC_LOCAL_PORT 8000

#define CONNECTION_WAIT_TIMES 5000

#define CONNECTION_ERROR_WAIT_TIME 5000

// wifi credentials
const char *ssid = "Bildspur-ArtNet";
const char *password = "B1ld5pur";

char deviceName[25];

int connectionWaitTimes = 0;

WiFiUDP Udp;

void setupNetwork()
{
  setupDeviceName();
  setupWiFi();

  setupUDPServer();
  setupMDNS();

  // clean up led state
  ledStopBlink();
  ledOFF();
}

void setupDeviceName()
{
  char *mac = "00:00:00:00:00:00N";
  WiFi.macAddress().toCharArray(mac, 18);

  char hex[5];
  sprintf(hex, "%x", ESP.getChipId());
  strcpy(deviceName, concatStr(DEVICE_IDENTIFIER, "-", concatStr(generateName(mac), "-", hex)));

  Serial.print("Device Name: ");
  Serial.println(deviceName);
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

    ESP.wdtFeed();
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
  MDNS.addServiceTxt("osc", "udp", "mac", WiFi.macAddress());

  MDNS.addService("http", "tcp", 80);
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
    ledError();
    Serial.println("lost connection...");
    wait(CONNECTION_ERROR_WAIT_TIME);

    setupNetwork();
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

      // feed because of watchdog
      ESP.wdtFeed();

      ledON();
      routeOSCMessage(msg);
      ledOFF();
    }
  }
}

