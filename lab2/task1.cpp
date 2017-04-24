#include "client.hpp"
#include "derived.hpp"
#include "derived2.hpp"
#include "derived3.hpp"

int Derived::solve() {
    return 1;
}

int Derived2::solve() {
    return 2;
}

int Derived3::solve() {
    return 3;
}

int main() {
    Derived3 d;
    Client c(d);
    
    c.operate();
}
