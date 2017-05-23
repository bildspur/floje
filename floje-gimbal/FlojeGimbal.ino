#include <Servo.h>

void setup()
{
  Serial.begin(9600);

  Serial.print("waiting for motor...");
  delay(3000);
  Serial.println("done!");

  motorSetup();
  servoSetup();
}

void loop()
{
  motorLoop();
  servoLoop();

  delay(100);
}
