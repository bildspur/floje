#include <Servo.h>

#define SERVO_X_PIN D6 // GPIO12
#define SERVO_Y_PIN D8 // GPIO15

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

