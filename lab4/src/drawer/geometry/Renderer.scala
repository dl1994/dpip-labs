package drawer.geometry

trait Renderer {

    def drawLine(start: Point, end: Point): Unit

    def fillPolygon(points: Array[Point]): Unit
}
