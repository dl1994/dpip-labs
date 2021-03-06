#include <stdio.h>
#include <stdlib.h>

#define GREET 0
#define MENU 1
#define TABLE_SIZE 2
#define NUM_DOGS 10
//#define PRINT_SIZES
//#define USE_STACK

typedef char const* (*PTRFUN)();

PTRFUN dogTable[TABLE_SIZE];
PTRFUN catTable[TABLE_SIZE];

struct Animal {
    const char* name;
    PTRFUN* funTable;
};

char const* dogGreet(void) {
    return "voof!";
}

char const* dogMenu(void) {
    return "cooked beef";
}

char const* catGreet(void) {
    return "meow!";
}

char const* catMenu(void) {
    return "canned tuna";
}

void animalPrintGreeting(struct Animal* animal) {
    printf("%s greets: %s\n", animal->name, animal->funTable[GREET]());
}

void animalPrintMenu(struct Animal* animal) {
    printf("%s likes %s.\n", animal->name, animal->funTable[MENU]());
}

void constructDog(struct Animal* memory, const char* name) {
    memory->name = name;
    memory->funTable = dogTable;
}

void constructCat(struct Animal* memory, const char* name) {
    memory->name = name;
    memory->funTable = catTable;
}

struct Animal* createDog(char const* name) {
    struct Animal* dog;
    dog = (struct Animal*) malloc(sizeof(struct Animal));
    constructDog(dog, name);
    return dog;
}

struct Animal* createCat(char const* name) {
    struct Animal* cat;
    cat = (struct Animal*) malloc(sizeof(struct Animal));
    constructCat(cat, name);
    return cat;
}

void testAnimals(void) {
    #ifdef USE_STACK
        struct Animal p1, p2, p3;
        constructDog(&p1, "Hamlet");
        constructCat(&p2, "Ofelia");
        constructDog(&p3, "Polon");

        animalPrintGreeting(&p1);
        animalPrintGreeting(&p2);
        animalPrintGreeting(&p3);
    #else
        struct Animal* p1 = createDog("Hamlet");
        struct Animal* p2 = createCat("Ofelia");
        struct Animal* p3 = createDog("Polon");

        animalPrintGreeting(p1);
        animalPrintGreeting(p2);
        animalPrintGreeting(p3);

        animalPrintMenu(p1);
        animalPrintMenu(p2);
        animalPrintMenu(p3);

        free(p1);
        free(p2);
        free(p3);

        #ifdef PRINT_SIZES
            printf("%ld\n", sizeof(*p1));
            printf("%ld\n", sizeof(*p2));
            printf("%ld\n", sizeof(*p3));
        #endif
    #endif
}

void initDog() {
    dogTable[GREET] = dogGreet;
    dogTable[MENU] = dogMenu;
}

void initCat() {
    catTable[GREET] = catGreet;
    catTable[MENU] = catMenu;
}

struct Animal* createDogs(int n) {
    struct Animal* dogs;
    dogs = (struct Animal*) malloc(sizeof(struct Animal) * n);

    int i;
    for (i = 0; i < n; i++) {
        constructDog((dogs + i), "Rex");
    }
    
    return dogs;
}

int main(void) {
    initDog();
    initCat();

    testAnimals();

    struct Animal* dogs;

    dogs = createDogs(NUM_DOGS);

    int i;
    for (i = 0; i < NUM_DOGS; i++) {
        animalPrintGreeting(dogs + i);
    }

    return 0;
}
