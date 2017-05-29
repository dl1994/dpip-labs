#include <sstream>
#include <iostream>
#include "animal.hpp"
#include "myfactory.hpp"

void printGreeting(Animal* pa) {
    std::cout << pa->name() << " greets: " << pa->greet() << "\n";
}

void printMenu(Animal* pa) {
    std::cout << pa->name() << " likes " << pa->menu() << ".\n";
}

int main(void) {
    myfactory& fact(myfactory::instance());
    std::vector<std::string> vecIds = fact.getIds();

    for (int i = 0; i < vecIds.size(); ++i){
        std::ostringstream oss;
        oss << "Pet " << i;
        Animal* pa = (Animal*) fact.create(vecIds[i], oss.str());

        printGreeting(pa);
        printMenu(pa);
      
        delete pa; 
    }
}
