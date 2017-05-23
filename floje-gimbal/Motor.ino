#define BUTTON_PIN 0
#define SERVO_PIN 9
#define POT_PIN 2

Servo esc;  // create servo object to control a servo

int val = 1000;    // variable to read the value from the analog pin

bool isON = true;

void motorSetup()
{
  pinMode(BUTTON_PIN, INPUT);
  esc.attach(SERVO_PIN);

  // send stop
  esc.writeMicroseconds(val);
}

void motorLoop()
{
  int newPotValue = map(analogRead(POT_PIN), 0, 1023, 1000, 1500);

  if (isON && val != newPotValue)
  {
    val = newPotValue;
    esc.writeMicroseconds(val);

    Serial.print("Send value to ESC: ");
    Serial.println(val);
  }

  if (digitalRead(BUTTON_PIN) == LOW)
  {
    isON = !isON;
    Serial.print("Is ON: ");
    Serial.println(isON);

    if (!isON)
    {
      esc.writeMicroseconds(1000);
    }

    delay(500);
  }
}

