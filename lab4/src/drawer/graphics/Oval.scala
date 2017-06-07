package drawer.graphics

import drawer.geometry.{GeometryUtil, Point, Rectangle, Renderer}
import scala.collection.mutable.ListBuffer

class Oval(bottom: Point, right: Point) extends AbstractGraphicalObject(Array(bottom, right)) {

    override val shapeId: String = "@OVAL"
    override val shapeName: String = "Oval"

    def this() = this(new Point(0, 10), new Point(10, 0))

    override def getBoundingBox: Rectangle = {
        val widthDiff = this.hotPoints(1).x - this.hotPoints(0).x
        val heightDiff = this.hotPoints(0).y - this.hotPoints(1).y
        val bottomRight = new Point(this.hotPoints(0).x - widthDiff, this.hotPoints(1).y + heightDiff)

        new Rectangle(bottomRight.x, bottomRight.y, widthDiff * 2, heightDiff * 2)
    }

    private def sq(n: Int) = n * n

    override def selectionDistance(mousePoint: Point): Double = {
        val center = new Point(this.hotPoints(0).x, this.hotPoints(1).y)
        val a = (center.x - this.hotPoints(1).x).abs
        val b = (center.y - this.hotPoints(0).y).abs

        if (sq(mousePoint.x - center.x) / sq(a).toDouble + sq(mousePoint.y - center.y) / sq(b).toDouble <= 1.0) {
            0
        } else {
            val numPoints = 50
            val angleStep = 2.0 * Math.PI / numPoints

            (for (i <- 0 until 50) yield {
                val angle = angleStep * i

                GeometryUtil.distanceFromPoint(mousePoint, new Point(
                    ((a * Math.cos(angle)) + center.x).toInt,
                    ((b * Math.sin(angle)) + center.y).toInt
                ))
            }).min
        }
    }

    override def duplicate(): GraphicalObject = new Oval(this.hotPoints(0), this.hotPoints(1))

    override def render(renderer: Renderer): Unit = {
        val numPoints = 50
        val dx = (this.hotPoints(0).x - this.hotPoints(1).x).abs
        val sdy = this.hotPoints(0).y - this.hotPoints(1).y
        val dy = sdy.abs
        val angleStep = 2.0 * Math.PI / numPoints
        val points: Array[Point] = new Array(numPoints)

        for (i <- points.indices) {
            val angle = angleStep * i
            points(i) = new Point(
                (dx * Math.cos(angle)).toInt + this.hotPoints(0).x,
                (dy * Math.sin(angle)).toInt + (this.hotPoints(0).y - sdy).abs
            )
        }

        renderer.fillPolygon(points)
    }

    override def load(stack: ListBuffer[GraphicalObject], data: String): Unit = {
        val splitData = data.split(" ", 4)
        stack += new Oval(
            new Point(splitData(0).toInt, splitData(1).toInt),
            new Point(splitData(2).toInt, splitData(3).toInt)
        )
    }
}
