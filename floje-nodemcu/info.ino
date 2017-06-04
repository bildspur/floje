#define LED_PIN 13 // GPIO13 / D7

#define DEFAULT_BLINK_INTERVAL 25
#define ERROR_BLINK_INTERVAL 10

#define MIN_LED_BRIGHTNESS 0
#define MAX_LED_BRIGHTNESS 255

boolean isLEDBlinking = false;
boolean blinkState = false;

int blinkInterval = 0;
int blinkCounter = 0;

void setupInfo()
{
  Serial.begin(115200);
}

void loopInfo()
{
  if (isLEDBlinking)
  {
    // flip state
    if (blinkCounter > blinkInterval)
    {
      if (blinkState)
      {
        ledON();
      }
      else
      {
        ledOFF();
      }

      blinkState = !blinkState;
      blinkCounter = 0;
    }

    blinkCounter++;
  }
}

void setLEDBrightness(int value)
{
  analogWrite(LED_PIN, value);
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

void ledBlink(int interval)
{
  isLEDBlinking = true;

  blinkInterval = interval;
  blinkCounter = 0;
  blinkState = true;

  ledON();
}

void ledStopBlink()
{
  isLEDBlinking = false;
  ledOFF();
}



