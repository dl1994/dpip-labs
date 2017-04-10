/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * The MIT License (MIT)                                                           *
 *                                                                                 *
 * Copyright © 2017 Domagoj Latečki                                                *
 *                                                                                 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy    *
 * of this software and associated documentation files (the "Software"), to deal   *
 * in the Software without restriction, including without limitation the rights    *
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell       *
 * copies of the Software, and to permit persons to whom the Software is           *
 * furnished to do so, subject to the following conditions:                        *
 *                                                                                 *
 * The above copyright notice and this permission notice shall be included in all  *
 * copies or substantial portions of the Software.                                 *
 *                                                                                 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR      *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,        *
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE     *
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER          *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,   *
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE   *
 * SOFTWARE.                                                                       *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
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
