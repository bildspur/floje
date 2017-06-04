import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;

JmDNS jmdns;

void setup()
{
  size(500, 500, FX2D);

  // discover jmdns
  try {
    // Create a JmDNS instance
    jmdns = JmDNS.create(InetAddress.getLocalHost());

    // Add a service listener
    jmdns.addServiceListener("_osc._udp.local.", new SampleListener());
    
    println("Clients: ");
    for(ServiceInfo s : jmdns.list("_osc._udp.local."))
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
}