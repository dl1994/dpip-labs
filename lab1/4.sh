#!/bin/bash

g++ -O0 -S -masm=intel task4.cpp
c++filt < task4.s > task4.filt
less task4.filt
