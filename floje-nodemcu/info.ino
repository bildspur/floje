#define LED_PIN D7 // GPIO13

#define DEFAULT_BLINK_INTERVAL 500
#define ERROR_BLINK_INTERVAL 100

#define MIN_LED_BRIGHTNESS 10
#define MAX_LED_BRIGHTNESS 255

boolean isLEDBlinking = false;
boolean blinkState = false;

long blinkInterval = 0;

unsigned long previousMillis;

void setupInfo()
{
  Serial.begin(115200);
  Serial.println();

  Serial.print("Chip ID: 0x");
  Serial.println(ESP.getChipId(), HEX);
}

void loopInfo()
{
  if (isLEDBlinking)
  {
    unsigned long currentMillis = millis();

    // flip state
    if (currentMillis - previousMillis >= blinkInterval)
    {
      if (blinkState)
      {
        Serial.println("LED ON");
        ledON();
      }
      else
      {
        Serial.println("LED OFF");
        ledOFF();
      }

      blinkState = !blinkState;
      previousMillis = currentMillis;
    }
  }
}

void setLEDBrightness(int value)
{
  analogWrite(LED_PIN, value);
}

void setLEDState(boolean state)
{
  if (state)
    ledON();
  else
    ledOFF();
}

void ledON()
{
  setLEDBrightness(MAX_LED_BRIGHTNESS);
}

void ledOFF()
{
  setLEDBrightness(MIN_LED_BRIGHTNESS);
}

void ledBlink()
{
  ledBlink(DEFAULT_BLINK_INTERVAL);
}

void ledError()
{
  ledBlink(ERROR_BLINK_INTERVAL);
}

void ledBlink(long interval)
{
  isLEDBlinking = true;

  blinkInterval = interval;
  previousMillis = millis();
  blinkState = true;

  ledON();
}

void ledStopBlink()
{
  isLEDBlinking = false;
  ledOFF();
}
