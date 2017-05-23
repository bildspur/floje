#define X_SERVO_PIN 10
#define Y_SERVO_PIN 11

Servo servoX;
Servo servoY;

int x = 90;
int y = 90;

void servoSetup()
{
  servoX.attach(X_SERVO_PIN);
  servoY.attach(Y_SERVO_PIN);

  servoX.write(x);
  servoY.write(y);
}

void servoLoop()
{
  if ( Serial.available()) {
    char ch = Serial.read();

    switch (ch) {
      case ' ':
        x = 90;
        y = 90;
        break;
      case 'w':
        x = 90;
        y = 0;
        break;
      case 'a':
        x = 180;
        y = 90;
        break;
      case 's':
        x = 90;
        y = 180;
        break;
      case 'd':
        x = 0;
        y = 90;
        break;
      case 'q':
        x = 180;
        y = 0;
        break;
      case 'e':
        x = 0;
        y = 0;
          break;
    }

    servoX.write(x);
    servoY.write(y);

    delay(1000);
  }
}

