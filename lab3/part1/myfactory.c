#include "myfactory.h"
#include <dlfcn.h>
#include <string.h>
#include <stdlib.h>

void* myfactory(char const* libname, char const* ctorarg) {
    int nameLen = strlen(libname);
    char* fullName = (char*) malloc((nameLen + 6) * sizeof(char));

    strcat(fullName, "./");
    strcat(fullName, libname);
    strcat(fullName, ".so");

    void* lib = dlopen(fullName, RTLD_LAZY);
    void* constructor = dlsym(lib, "create");

    if (constructor) {
        return ((void* (*)(char const*)) constructor)(ctorarg);
    }

    return NULL;
}
