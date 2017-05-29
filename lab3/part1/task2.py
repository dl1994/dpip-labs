import imp, os

def myfactory(moduleName):
    file, pathname, description = imp.find_module(moduleName, ["plugins"])
    return getattr(imp.load_module(moduleName, file, pathname, description), moduleName)

def printGreeting(pet):
    print(pet.name() + " greets: " + pet.greet())

def printMenu(pet):
    print(pet.name() + " likes " + pet.menu() + ".")

def test():
    pets = []

    for mymodule in os.listdir('plugins'):
        moduleName, moduleExt = os.path.splitext(mymodule)

        if moduleExt == '.py':
            pet = myfactory(moduleName)('Pet ' + str(len(pets)))
            pets.append(pet)
    for pet in pets:
        printGreeting(pet)
        printMenu(pet)

test()
