#define SERVO_MAX 180

void setupOSC()
{

}

void routeOSCMessage(OSCMessage &msg)
{
  msg.route("/1/toggleLED", toggleOnOff);
  msg.route("/1/fadeLED", fadeLED);
  msg.route("/1/servo", servoPosition);

  msg.route("/floje/servo/x", setServoX);
  msg.route("/floje/servo/y", setServoY);
  msg.route("/floje/servo/xy", setServoXY);
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
  setLEDState(ledState);
}

void servoPosition(OSCMessage &msg, int addrOffset)
{
  float v = msg.getFloat(0);
  int p = v * 180;

  setXAxis(p);
}

// new methods

void setServoXY(OSCMessage &msg, int addrOffset)
{
  int x = msg.getFloat(0);
  int y = msg.getFloat(1);

  setXAxis(x);
  setYAxis(y);
}

void setServoX(OSCMessage &msg, int addrOffset)
{
  float x = msg.getFloat(0);
  int p = x * SERVO_MAX;

  setXAxis(p);
}


void setServoY(OSCMessage &msg, int addrOffset)
{
  float y = msg.getFloat(0);
  int p = y * SERVO_MAX;

  setYAxis(p);
}

