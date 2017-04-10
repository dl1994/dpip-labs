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
#include <iostream>
#include <assert.h>
#include <stdlib.h>

struct Point {
    int x;
    int y;
};

struct Shape {
    enum EType { circle, square, rhombus };
    EType type_;
};

struct Circle {
    Shape::EType type_;
    double radius_;
    Point center_;
};

struct Square {
    Shape::EType type_;
    double side_;
    Point center_;
};

struct Rhombus {
    Shape::EType type_;
    double diagonal_;
    Point center_;
};

void drawSquare(struct Square*) {
    std::cerr << "in drawSquare\n";
}

void drawCircle(struct Circle*) {
    std::cerr << "in drawCircle\n";
}

void drawRhombus(struct Rhombus*) {
    std::cerr << "in drawRhombus\n";
}

void drawShapes(Shape** shapes, int n) {
    for (int i = 0; i < n; ++i) {
        struct Shape* s = shapes[i];

        switch (s->type_) {
            case Shape::square:
                drawSquare((struct Square*) s);
                break;
            case Shape::circle:
                drawCircle((struct Circle*) s);
                break;
            case Shape::rhombus:
                drawRhombus((struct Rhombus*) s);
                break;
            default:
                assert(0); 
                exit(0);
        }
    }
}

void moveSquare(Square*, Point* p) {
    std::cerr << "translated square: (" << p->x << ", " << p->y << ")\n";
}

void moveCircle(Circle*, Point* p) {
    std::cerr << "translated circle: (" << p->x << ", " << p->y << ")\n";
}

void moveRhombus(Rhombus*, Point* p) {
    std::cerr << "translated rhombus: (" << p->x << ", " << p->y << ")\n";
}

void moveShapes(Shape** shapes, Point** points, int n) {
    for (int i = 0; i < n; ++i) {
        struct Shape* s = shapes[i];

        switch (s->type_) {
            case Shape::square:
                moveSquare((struct Square*) s, points[i]);
                break;
            case Shape::circle:
                moveCircle((struct Circle*) s, points[i]);
                break;
            default:
                assert(0); 
                exit(0);
        }
    }
}

int main() {
    Shape* shapes[5];

    shapes[0] = (Shape*) new Circle;
    shapes[0]->type_ = Shape::circle;

    shapes[1] = (Shape*) new Square;
    shapes[1]->type_ = Shape::square;

    shapes[2] = (Shape*) new Square;
    shapes[2]->type_ = Shape::square;

    shapes[3] = (Shape*) new Circle;
    shapes[3]->type_ = Shape::circle;

    shapes[4] = (Shape*) new Rhombus;
    shapes[4]->type_ = Shape::rhombus;

    Point* translations[5];

    translations[0] = new Point;
    translations[0]->x = 1;
    translations[0]->y = 2;

    translations[1] = new Point;
    translations[1]->x = 6;
    translations[1]->y = 7;

    translations[2] = new Point;
    translations[2]->x = -4;
    translations[2]->y = 2;

    translations[3] = new Point;
    translations[3]->x = -10;
    translations[3]->y = -2;

    translations[4] = new Point;
    translations[4]->x = 17;
    translations[4]->y = 21;

    drawShapes(shapes, 5);
    moveShapes(shapes, translations, 5);
}
