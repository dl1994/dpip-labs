#!/bin/bash

find -name "*.java" > java_sources
mkdir out
javac -d out @java_sources
rm java_sources
java -cp out editor.task1
rm -rf out
