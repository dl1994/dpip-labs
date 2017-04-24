#include <stdio.h>
#include <stdlib.h>

class B {
    public:
        virtual int first() = 0;
        virtual int second() = 0;
};

class D: public B {
    public:
        virtual int first() {
            return 10;
        }

        virtual int second() {
            return 42;
        }
};

typedef int(***b_FPTR)(B*);

void virtualTableCaller(B* pb) {
    b_FPTR b_fptr = (b_FPTR) pb;
    printf("%d\n", ((**b_fptr)(pb)));
    printf("%d\n", ((*(*b_fptr + 1))(pb)));
}

int main(void) {
    B* pb = new D;

    virtualTableCaller(pb);

    return 0;
}
