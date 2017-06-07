#!/bin/bash
find -name "*.scala" > sources
scalac @sources
scala -cp . drawer.GUI
rm -rf drawer sources
