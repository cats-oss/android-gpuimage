#!/usr/bin/python

import sys

f = sys.stdin
if len(sys.argv) > 1:
  f = open(sys.argv[1])

lines = f.readlines()
for line in lines[:-1]:
  print '"' + line.rstrip() + '\\n\" +'
print '"' + lines[-1].rstrip() + '\\n\"'

f.close()
