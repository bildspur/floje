class Timer
{
    long interval;

    template<typename Func>
    std::function<void()> updateHandler;

    unsigned long previousMillis;

  public:
    Timer(interval, std::function<void()> f) : updateHandler(f)
    {
      this.interval = interval;
      previousMillis = 0;
    }

    void update()
    {
      unsigned long currentMillis = millis();

      if (currentMillis - previousMillis >= OnTime)
      {
        updateHandler();
      }
    }
};
