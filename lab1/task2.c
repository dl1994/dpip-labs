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
#include <stdlib.h>

struct Unary_Function;
typedef void (*VPTRFUN)(struct Unary_Function*);
typedef double (*PTRFUN)(struct Unary_Function*, double);
typedef enum { false, true } bool;

void* new(size_t size) {
    return malloc(size);
}
 
// Base class
struct Unary_Function {
    PTRFUN* fun_table;
    double lower_bound;
    double upper_bound;
};

// Virtual functions
#define TABLE_SIZE 2
PTRFUN uf_table[TABLE_SIZE];

#define VALUE_AT 0
double value_at_uf(struct Unary_Function* f, double x) {
    return 0;
}

#define NEGATIVE_VALUE_AT 1
double negative_value_at_uf(struct Unary_Function* f, double x) {
    return -(f->fun_table[VALUE_AT](f, x));
}

// Init
void init_Unary_Function() {
    uf_table[VALUE_AT] = value_at_uf;
    uf_table[NEGATIVE_VALUE_AT] = negative_value_at_uf;
}

// Constructors
struct Unary_Function* construct_Unary_Function(void* ptr, double lb, double ub) {
    struct Unary_Function* uf;
    uf = (struct Unary_Function*) ptr;
    uf->lower_bound = lb;
    uf->upper_bound = ub;
    uf->fun_table = uf_table;
    return uf;
}

// Non-virtual functions
void tabulate(struct Unary_Function* f) {
    int x;
    for(x = f->lower_bound; x <= f->upper_bound; x++) {
        printf("f(%d)=%lf\n", x, f->fun_table[VALUE_AT](f, x));
    }
}

// Static functions
bool same_functions_for_ints(struct Unary_Function *f1, struct Unary_Function *f2, double tolerance) {
    if (f1->lower_bound != f2->lower_bound) { 
        return false;
    }

    if (f1->upper_bound != f2->upper_bound) {
        return false;
    }

    int x;
    for (x = f1->lower_bound; x <= f1->upper_bound; x++) {
        double delta = f1->fun_table[VALUE_AT](f1, x) - f2->fun_table[VALUE_AT](f2, x);
        
        if (delta < 0) {
            delta = -delta;
        }

        if (delta > tolerance) {
            return false;
        }
    }

    return true;
}

// Square
struct Square {
    PTRFUN* fun_table;
    double lower_bound;
    double upper_bound;
};

// Virtual functions
PTRFUN sq_table[TABLE_SIZE];

double value_at_sq(struct Unary_Function* f, double x) {
    return x * x;
}

// Constructors
struct Square* construct_Square(void* ptr, double lb, double ub) {
    struct Square* f;
    f = (struct Square*) construct_Unary_Function(ptr, lb, ub);
    f->fun_table = sq_table;
    return f;
}

// Init
void init_Square() {
    sq_table[VALUE_AT] = value_at_sq;
    sq_table[NEGATIVE_VALUE_AT] = uf_table[NEGATIVE_VALUE_AT];
}

// Linear
struct Linear {
    PTRFUN* fun_table;
    double lower_bound;
    double upper_bound;
    double a;
    double b;
};

// Virtual functions
PTRFUN ln_table[TABLE_SIZE];

double value_at_ln(struct Unary_Function* f, double x) {
    return ((struct Linear*) f)->a * x + ((struct Linear*) f)->b;
}

// Init
void init_Linear() {
    ln_table[VALUE_AT] = value_at_ln;
    ln_table[NEGATIVE_VALUE_AT] = uf_table[NEGATIVE_VALUE_AT];
}

// Constructors
struct Linear* construct_Linear(void* ptr, double lb, double ub, double a, double b) {
    struct Linear* f;
    f = (struct Linear*) construct_Unary_Function(ptr, lb, ub);
    f->fun_table = ln_table;
    f->a = a;
    f->b = b;
    return f;
}

int main(void) {
    init_Unary_Function();
    init_Square();
    init_Linear();

    struct Unary_Function *f1;
    
    f1 = (struct Unary_Function*) construct_Square(new(sizeof(struct Square)), -2, 2);
    tabulate(f1);

    struct Unary_Function *f2;

    f2 = (struct Unary_Function*) construct_Linear(new(sizeof(struct Linear)),-2, 2, 5, -2);
    tabulate(f2);

    printf("f1==f2: %s\n", same_functions_for_ints(f1, f2, 1E-6) ? "YES" : "NO");
    printf("neg_val f2(1) = %lf\n", f2->fun_table[NEGATIVE_VALUE_AT](f2, 1.0));

    free(f1);
    free(f2);
    
    return 0;
}
