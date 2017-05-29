#include "myfactory.hpp"

myfactory& myfactory::instance() {
    static myfactory theFactory;
    return theFactory;
}

void* myfactory::create(const std::string &id, const std::string &arg) {
    return creators_[id](arg);
}

int myfactory::registerCreator(const std::string &id, pFunCreator pfn) {
    creators_[id] = pfn;
}

std::vector<std::string> myfactory::getIds() {
    std::vector<std::string> v;

    for(MyMap::iterator iter = creators_.begin(); iter != creators_.end(); ++iter) {
        v.push_back(iter->first);
    }

    return v;
}
