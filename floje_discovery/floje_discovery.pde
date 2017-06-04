import java.io.IOException; //<>//
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;

import oscP5.*;
import netP5.*;

OscP5 oscP5;

JmDNS jmdns;

ArrayList<ServiceInfo> flojeDevices = new ArrayList<ServiceInfo>();

float ledValue = 0;
float ledIncrease = 0.1;

void setup()
{
  size(500, 500, FX2D);
  frameRate(25);

  oscP5 = new OscP5(this, 9000);

  // discover jmdns
  try {
    // Create a JmDNS instance
    jmdns = JmDNS.create(InetAddress.getLocalHost());

    // Add a service listener
    jmdns.addServiceListener("_osc._udp.local.", new SampleListener());

    println("Clients: ");
    for (ServiceInfo s : jmdns.list("_osc._udp.local."))
    {
      println(s.getName());
    }
  } 
  catch (UnknownHostException e) {
    System.out.println(e.getMessage());
  } 
  catch (IOException e) {
    System.out.println(e.getMessage());
  }
}

void draw()
{
  background(55);

  fill(255 * ledValue);
  rect(50, 50, 100, 50);

  ledValue += ledIncrease;

  if (ledValue + ledIncrease >= 1)
  {
    ledValue = 0;
  }

  for (ServiceInfo s : flojeDevices)
  {
    //println("Sending to " + s.getHostAddress() + ":" + s.getPort());

    NetAddress remote = new NetAddress(s.getHostAddress(), s.getPort());

    OscMessage myMessage = new OscMessage("/1/fadeLED");
    myMessage.add(ledValue);
    oscP5.send(myMessage, remote);
  }
}

void oscEvent(OscMessage theOscMessage) {
  /* print the address pattern and the typetag of the received OscMessage */
  print("### received an osc message.");
  print(" addrpattern: "+theOscMessage.addrPattern());
  println(" typetag: "+theOscMessage.typetag());
}