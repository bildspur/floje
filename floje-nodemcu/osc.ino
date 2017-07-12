#define SERVO_MAX 180

void setupOSC()
{

}

void routeOSCMessage(OSCMessage &msg)
{
  msg.route("/floje/servo/x", setServoX);
  msg.route("/floje/servo/y", setServoY);
  msg.route("/floje/servo/xy", setServoXY);
}

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
  int p = x;

  setXAxis(p);
}


void setServoY(OSCMessage &msg, int addrOffset)
{
  float y = msg.getFloat(0);
  int p = y;

  setYAxis(p);
}

