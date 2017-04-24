#include <stdio.h>

class AbstractDatabase {
    public:
        virtual void getData() = 0;
};

class MockDatabase: public AbstractDatabase {
    private:
        int numOfGetDataCalls;
    public:
        virtual void getData() {
            this->numOfGetDataCalls += 1;
        }

        int getNumOfDataCalls() {
            return numOfGetDataCalls;
        }
};

class Client {
    private:
        AbstractDatabase& myDatabase;
    public:
        Client(AbstractDatabase& db):
            myDatabase(db) {}
            
        void transaction() {
            myDatabase.getData();
        }
};

void assertGetDataWasCalled(MockDatabase* pdb) {
    int numCalls = pdb->getNumOfDataCalls();

    if (numCalls == 1) {
        printf("Database was successfully called one time.\n");
    } else {
        printf("Database was not called right number of times!\n");
        printf("Number of calls was: %d\n", numCalls);
    }
}

int main () {
    MockDatabase* pdb = new MockDatabase();
    Client client(*pdb);
    
    client.transaction();
    assertGetDataWasCalled(pdb);
    client.transaction();
    assertGetDataWasCalled(pdb);
}
