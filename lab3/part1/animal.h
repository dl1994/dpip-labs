#ifndef ANIMAL_H
#define ANIMAL_H
#define NAME 0
#define GREET 1
#define MENU 2

typedef char const* (*PTRFUN)();

struct Animal {
    PTRFUN* vtable;
    const char* name;
};

#endif
