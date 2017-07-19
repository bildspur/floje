#include <Servo.h>

#define SERVO_X_PIN D6 // GPIO12
#define SERVO_Y_PIN D8 // GPIO15

#define INITIAL_X_POS 90
#define INITIAL_Y_POS 90
#define INITIAL_STEP 1

#define INITIAL_STATE_INTERVAL 300

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
  // move slowly to initial position
  int xpos = servoX.read();
  int ypos = servoY.read();

  ledBlink(INITIAL_STATE_INTERVAL);

  while (xpos != INITIAL_X_POS || ypos != INITIAL_Y_POS)
  {
    xpos += getSign(INITIAL_X_POS - xpos) * INITIAL_STEP;
    ypos += getSign(INITIAL_Y_POS - ypos) * INITIAL_STEP;

    setXAxis(xpos);
    setYAxis(ypos);

    loopInfo();
    delay(DEFAULT_LOOP_DELAY);

    // read from servo
    xpos = servoX.read();
    ypos = servoY.read();
  }

  ledStopBlink();
}

void setXAxis(int value)
{
  servoX.write(value);
}

void setYAxis(int value)
{
  servoY.write(value);
}

