#include <stdio.h>
#include <stdlib.h>

class Base {
    public:
        Base() {
            method();
        }

        virtual void virtualMethod() {
            printf("I am the base implementation!\n");
        }

        void method() {
            printf("Method says: ");
            virtualMethod();
        }
};

class Derived: public Base {
    public:
        Derived(): Base() {
            method();
        }

        virtual void virtualMethod() {
            printf("I am the derived implementation!\n");
        }
};

int main(void) {
    Derived* pd = new Derived();
    pd->method();
}
