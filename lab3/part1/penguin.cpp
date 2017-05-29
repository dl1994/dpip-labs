#include "animal.hpp"
#include "myfactory.hpp"

class Penguin: public Animal {
    private:
        const std::string _name;
    public:
        Penguin(const std::string n): _name(n) {};

        virtual const std::string name() {
            return this->_name;
        }

        virtual const std::string greet() {
            return "quack!";
        }

        virtual const std::string menu() {
            return "saltwater fish";
        }
};

static void* penguinCreator(const std::string& arg) {
    return new Penguin(arg);
}

static int hreg = myfactory::instance().registerCreator("penguin", penguinCreator);
