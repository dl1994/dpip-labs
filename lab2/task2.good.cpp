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

class Shape {
    public:
        virtual void draw() = 0;
        virtual void move(Point*) = 0;
};

class Circle: public Shape {
    public:
        virtual void draw();
        virtual void move(Point*);
    private:
        double radius_;
        Point center_;
};

class Square: public Shape {
    public:
        virtual void draw();
        virtual void move(Point*);
    private:
        double side_;
        Point center_;
};

class Rhombus: public Shape {
    public:
        virtual void draw();
        virtual void move(Point*);
    private:
        double diagonal_;
        Point center_;
};

void Square::draw() {
    std::cerr << "in drawSquare\n";
}

void Circle::draw() {
    std::cerr << "in drawCircle\n";
}

void Rhombus::draw() {
    std::cerr << "in drawRhombus\n";
}

void drawShapes(Shape** shapes, int n) {
    for (int i = 0; i < n; ++i) {
        shapes[i]->draw();
    }
}

void Square::move(Point* p) {
    std::cerr << "translated square: (" << p->x << ", " << p->y << ")\n";
}

void Circle::move(Point* p) {
    std::cerr << "translated circle: (" << p->x << ", " << p->y << ")\n";
}

void Rhombus::move(Point* p) {
    std::cerr << "translated rhombus: (" << p->x << ", " << p->y << ")\n";
}

void moveShapes(Shape** shapes, Point** points, int n) {
    for (int i = 0; i < n; ++i) {
        shapes[i]->move(points[i]);
    }
}

int main() {
    Shape* shapes[5];

    shapes[0] = (Shape*) new Circle;
    shapes[1] = (Shape*) new Square;
    shapes[2] = (Shape*) new Square;
    shapes[3] = (Shape*) new Circle;
    shapes[4] = (Shape*) new Rhombus;

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
