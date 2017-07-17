#define SERVO_MAX 180

IPAddress broadcastIP(255, 255, 255, 255);
const unsigned int outPort = 9000;

void setupOSC()
{
  // send reset reason boradcast
  sendRestartReason();
}

void sendRestartReason()
{
  // copy reason
  char msgBuffer[32];
  ESP.getResetReason().toCharArray(msgBuffer, 32);

  Serial.print("Restart reason:");
  Serial.println(msgBuffer);

  OSCMessage msg(concatStr("/floje/status/", deviceName));
  msg.add(msgBuffer);

  Udp.beginPacket(broadcastIP, outPort);
  msg.send(Udp); // send the bytes to the SLIP stream
  Udp.endPacket(); // mark the end of the OSC Packet
  msg.empty(); // free space occupied by message
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

