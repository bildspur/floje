class SampleListener implements ServiceListener {
  @Override
    public void serviceAdded(ServiceEvent event) {
  }

  @Override
    public void serviceRemoved(ServiceEvent event) {
    ServiceInfo info = event.getInfo();
    if (info.getName().startsWith("floje"))
    {
      flojeDevices.remove(info);
    }
  }

  @Override
    public void serviceResolved(ServiceEvent event) {
    System.out.println("Service resolved: " + event.getInfo());

    ServiceInfo info = event.getInfo();
    if (info.getName().startsWith("floje"))
    {
      flojeDevices.add(info);
    }
  }
}