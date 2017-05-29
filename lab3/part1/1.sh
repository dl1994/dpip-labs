#!/bin/bash

gcc task1.c myfactory.c -ldl
gcc -shared tiger.c -o tiger.so -fPIC
gcc -shared parrot.c -o parrot.so -fPIC
./a.out
