#include <Servo.h>

#define SERVO_X_PIN D6 // GPIO12
#define SERVO_Y_PIN D8 // GPIO15

#define INITIAL_X_POS 90
#define INITIAL_Y_POS 90

int SPEED_IN_MS = 25;

int posX = INITIAL_X_POS;
int targetX = INITIAL_X_POS;

int posY = INITIAL_Y_POS;
int targetY = INITIAL_Y_POS;

Servo servoX;
Servo servoY;

unsigned long timeStamp = 0;

void setupServo()
{
  servoX.attach(SERVO_X_PIN);
  servoY.attach(SERVO_Y_PIN);

  delay(100);

  setInitialState();
}

void setInitialState()
{
  // move slowly to initial position
  setXAxis(INITIAL_X_POS);
  setYAxis(INITIAL_Y_POS);
}

void setSpeed(int speed) {
  SPEED_IN_MS = speed;
}

void setXAxis(int value)
{
  //servoX.write(value);
  targetX = value;
}

void setYAxis(int value)
{
  //servoY.write(value);
  targetY = value;
}

void servoLoop() {
  auto t = millis();
  if (t - timeStamp > SPEED_IN_MS) {
    int distX = targetX - posX;
    if (abs(distX) > 0) {
      int signX = getSign(distX);
      posX += signX;
      servoX.write(posX);
    }

    int distY = targetY - posY;
    if (abs(distY) > 0) {
      int signY = getSign(distY);
      posY += signY;
      servoY.write(posY);
    }

    timeStamp = t;
  }

}
