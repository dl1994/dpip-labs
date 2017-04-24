#include <stdio.h>
#include <stdlib.h>

class CoolClass {
    public:
        virtual void set(int x) {
            x_=x;
        };

        virtual int get() {
            return x_;
        };
    private:
        int x_;
};

class PlainOldClass {
    public:
        void set(int x) {
            x_=x;
        };

        int get() {
            return x_;
        };
    private:
        int x_;
};

int main(void) {
    printf("%ld\n", sizeof(PlainOldClass));
    printf("%ld\n", sizeof(CoolClass));
}
