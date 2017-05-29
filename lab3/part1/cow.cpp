#include "animal.hpp"
#include "myfactory.hpp"

class Cow: public Animal {
    private:
        const std::string _name;
    public:
        Cow(const std::string n): _name(n) {};

        virtual const std::string name() {
            return this->_name;
        }

        virtual const std::string greet() {
            return std::string("mooo!");
        }

        virtual const std::string menu() {
            return std::string("hay");
        }
};

static void* cowCreator(const std::string& arg) {
    return new Cow(arg);
}

static int hreg = myfactory::instance().registerCreator("cow", cowCreator);
