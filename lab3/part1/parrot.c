#include "animal.h"
#include <stdlib.h>

char const* name(struct Animal* this) {
    return this->name;
}

char const* greet(void) {
    return "yaaarrrr!";
}

char const* menu(void) {
    return "brazillian nuts";
}

PTRFUN table[] = {
    name,
    greet,
    menu
};

void construct(struct Animal* memory, const char* name) {
    memory->name = name;
    memory->vtable = table;
}

struct Animal* create(char const* name) {
    struct Animal* parrot = (struct Animal*) malloc(sizeof(struct Animal));
    construct(parrot, name);
    return parrot;
}
