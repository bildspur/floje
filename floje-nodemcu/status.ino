#define STATUS_PORT 80

WiFiServer server(STATUS_PORT);

void setupStatus()
{
  server.begin();
}

void loopStatus()
{
  // Check if a client has connected
  WiFiClient client = server.available();
  if (!client) {
    return;
  }

  // Wait until the client sends some data
  while (!client.available()) {
    delay(1);
  }

  // Read the first line of the request
  String request = client.readStringUntil('\r');
  Serial.println(request);
  client.flush();

  // Return the response
  client.println("HTTP/1.1 200 OK");
  client.println("Content-Type: text/html");
  client.println(""); //  do not forget this one
  client.println("<!DOCTYPE HTML>");
  client.println("<html>");

  // Match the request
  if (request.indexOf("/status") != -1)  {
    client.println("<h2>");
    client.print(deviceName);
    client.println("</h2>");

    client.print("<p>Uptime: ");
    client.print(millis());
    client.println(" ms<p>");

    client.print("<p>Servo X: ");
    client.print(servoX.read());
    client.println(" degree<p>");

    client.print("<p>Servo Y: ");
    client.print(servoY.read());
    client.println(" degree<p>");
  }

  client.println("</html>");
}
