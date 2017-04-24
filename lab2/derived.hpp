#ifndef DERIVED_HPP
#define DERIVED_HPP
#include "base.hpp"

class Derived: public Base {
    public:
        virtual int solve();
};

#endif
