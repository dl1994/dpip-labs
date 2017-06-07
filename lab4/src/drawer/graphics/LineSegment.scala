package drawer.graphics

import drawer.geometry.{GeometryUtil, Point, Rectangle, Renderer}
import scala.collection.mutable.ListBuffer

class LineSegment(start: Point, end: Point) extends AbstractGraphicalObject(Array(start, end)) {

    override val shapeId: String = "@LINE"
    override val shapeName: String = "Line"

    def this() = this(new Point(0, 0), new Point(10, 0))

    override def getBoundingBox: Rectangle = {
        val start = this.hotPoints(0)
        val end = this.hotPoints(1)
        val x = start.x min end.x
        val y = start.y max end.y
        val width = Math.abs(start.x - end.x)
        val height = Math.abs(start.y - end.y)

        new Rectangle(x, y, width, height)
    }

    override def selectionDistance(mousePoint: Point): Double = GeometryUtil.distanceFromLineSegment(
        this.hotPoints(0), this.hotPoints(1), mousePoint
    )

    override def duplicate(): GraphicalObject = new LineSegment(this.hotPoints(0), this.hotPoints(1))

    override def render(renderer: Renderer): Unit = renderer.drawLine(this.hotPoints(0), this.hotPoints(1))

    override def load(stack: ListBuffer[GraphicalObject], data: String): Unit = {
        val splitData = data.split(" ", 4)
        stack += new LineSegment(
            new Point(splitData(0).toInt, splitData(1).toInt),
            new Point(splitData(2).toInt, splitData(3).toInt)
        )
    }
}
