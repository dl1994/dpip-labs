#ifndef CLIENT_HPP
#define CLIENT_HPP
#include "base.hpp"

class Client {
    public:
        Client(Base& b): solver_(b) {}
        void operate();
    private:
        Base& solver_;
};

#endif
