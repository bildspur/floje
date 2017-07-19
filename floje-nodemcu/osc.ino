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
  Serial.print("Restart reason:");
  Serial.println(ESP.getResetReason());

  rst_info *resetInfo = ESP.getResetInfoPtr();

  OSCMessage msg(concatStr("/floje/status/", deviceName));

  // add software version
  msg.add(version);

  // add reason
  msg.add(resetInfo->reason);

  // add extended information

  //exccause
  msg.add(resetInfo->exccause);

  //epc1
  msg.add(resetInfo->epc1);

  //epc2
  msg.add(resetInfo->epc2);

  //epc3
  msg.add(resetInfo->epc3);

  //excvaddr
  msg.add(resetInfo->excvaddr);

  //depc
  msg.add(resetInfo->depc);

  ESP.wdtFeed();

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

