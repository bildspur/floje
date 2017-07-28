#!/usr/bin/env python
from __future__ import print_function

import sys
import os
import optparse
import subprocess
import time
import socket

version = "1.0"

ip_of_sender = '192.168.1.100'
esp_respond_sender_port = '8255'
sender_to_esp_port = '8266'
update_password = 'bildspur'

def update_esp(address, firmware, debug=False):
    cmd = 'python espota.py -i '+address+' -I '+ip_of_sender+ ' -p '+sender_to_esp_port+' -P '+esp_respond_sender_port+' -a '+update_password+' -f '+firmware
    p = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)

    if(debug):
      for line in p.stdout.readlines():
          print(line, end='')
          sys.stdout.flush()
    retval = p.wait()
    return retval

def parser(unparsed_args):
  parser = optparse.OptionParser(
    usage = "%prog [options]",
    description = "Transmit images over the air to the esp8266 modules with OTA support."
  )

    # image
  group = optparse.OptionGroup(parser, "Image")
  group.add_option("-f", "--file",
    dest = "image",
    help = "Image file.",
    metavar="FILE",
    default = None
  )
  parser.add_option_group(group)

  # image
  group = optparse.OptionGroup(parser, "Devices")
  group.add_option("-d", "--devices",
    dest = "devices",
    help = "List with devices.",
    metavar="FILE",
    default = None
  )
  parser.add_option_group(group)

  (options, args) = parser.parse_args(unparsed_args)

  return options

def main(args):
  print("Multi OTA Updater %s" % version)
  print("#####################")
  print("")

  options = parser(args)

  ip_of_sender = socket.gethostbyname(socket.gethostname())
  print("Host IP Address: %s" % ip_of_sender)
  print("Image file: %s" % options.image)

  with open(options.devices) as f:
    devices = f.read().splitlines()

  update_error = 0
  update_ok = 0

  for esp in devices:
    print("=> '%s' is updating..." % esp, end='')
    sys.stdout.flush()
    retval = update_esp(esp, options.image)
    if(retval == 0):
      print("OK")
      update_ok += 1
    else:
      print("ERROR")
      update_error += 1

  print("")
  print("%s devices updated, %s errors!" % (update_ok, update_error))

if __name__ == '__main__':
  sys.exit(main(sys.argv))
