#include <Servo.h>

#define SERVO_X_PIN 12 // GPIO12 / D6
#define SERVO_Y_PIN 15 // GPIO15 / D8

Servo servoX;
Servo servoY;

void setupServo()
{
  servoX.attach(SERVO_X_PIN);
  servoY.attach(SERVO_Y_PIN);

  delay(100);

  setInitialState();
}

void setInitialState()
{
  setXAxis(90);
  setYAxis(90);
}

void setXAxis(int value)
{
  servoX.write(value);
}

void setYAxis(int value)
{
  servoY.write(value);
}

