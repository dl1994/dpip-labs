#ifndef MYFACTORY_HPP
#define MYFACTORY_HPP
#include <map>
#include <string>
#include <vector>

class myfactory {
    public:
        typedef void* (*pFunCreator)(const std::string&);
        typedef std::map<std::string, pFunCreator> MyMap;
    public:
        static myfactory &instance();
    public:
        int registerCreator(const std::string &id, pFunCreator pfn);
    public:
        void *create(const std::string &id, const std::string &arg);
        std::vector<std::string> getIds();
    private:
        myfactory() {};
        ~myfactory() {};
        MyMap creators_;
};

#endif
