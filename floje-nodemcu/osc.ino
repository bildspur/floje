void setupOSC()
{

}

void routeOSCMessage(OSCMessage &msg)
{
  msg.route("/1/toggleLED", toggleOnOff);
  msg.route("/1/fadeLED", fadeLED);
}

void fadeLED(OSCMessage &msg, int addrOffset)
{ 
  float fade = msg.getFloat(0);
  int brightness = fade * 255;

  setLEDBrightness(brightness);
}

void toggleOnOff(OSCMessage &msg, int addrOffset)
{
  boolean ledState = (boolean) msg.getFloat(0);

  Serial.print("Setting led to ");
  Serial.println(ledState ? "on" : "off");

  setLEDState(ledState);
}

