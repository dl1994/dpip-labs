#ifndef ANIMAL_HPP
#define ANIMAL_HPP
#include <string>

class Animal {
    public:
        virtual const std::string name() = 0;
        virtual const std::string greet() = 0;
        virtual const std::string menu() = 0;
};

#endif
