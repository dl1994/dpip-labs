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
