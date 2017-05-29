#include "myfactory.h"
#include "animal.h"
#include <stdio.h>
#include <stdlib.h>

// parrots and tigers defined in respective dynamic libraries

void animalPrintGreeting(struct Animal* animal) {
    printf("%s greets: %s\n", animal->vtable[NAME](animal), animal->vtable[GREET]());
}

void animalPrintMenu(struct Animal* animal) {
    printf("%s likes %s.\n", animal->vtable[NAME](animal), animal->vtable[MENU]());
}

int main(void) {
    struct Animal* p1 = (struct Animal*) myfactory("parrot", "Bluebeard");
    struct Animal* p2 = (struct Animal*) myfactory("tiger", "Scaredy-cat");

    if (!p1 || !p2) {
        printf("Creation of plug-in objects failed.\n");
        exit(1);
    }

    animalPrintGreeting(p1); // "yaaarrrr!"
    animalPrintGreeting(p2); // "growl!"

    animalPrintMenu(p1); // "brazillian nuts"
    animalPrintMenu(p2); // "warm milk"

    free(p1);
    free(p2);

    return 0;
}