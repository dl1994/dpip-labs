#!/bin/bash

g++ -O0 -S -masm=intel task6.cpp
c++filt < task6.s > task6.filt
g++ task6.cpp
./a.out
