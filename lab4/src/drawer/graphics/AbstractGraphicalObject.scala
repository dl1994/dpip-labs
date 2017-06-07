package drawer.graphics

import drawer.geometry.{GeometryUtil, Point}
import scala.collection.mutable.ListBuffer

abstract class AbstractGraphicalObject(hp: Array[Point]) extends GraphicalObject {

    val hotPoints: Array[Point] = hp.clone()
    val hotPointSelected: Array[Boolean] = new Array(hp.length)
    val listeners: ListBuffer[GraphicalObjectListener] = new ListBuffer()
    override var selected: Boolean = false
    override val numberOfHotPoints: Int = hotPoints.length

    override def getHotPoint(index: Int): Point = this.hotPoints(index)

    override def setHotPoint(index: Int, point: Point): Unit = {
        this.hotPoints(index) = point
        this.notifyListeners()
    }

    override def getHotPointDistance(index: Int, mousePoint: Point): Double = GeometryUtil.distanceFromPoint(
        this.hotPoints(index), mousePoint
    )

    override def isHotPointSelected(index: Int): Boolean = this.hotPointSelected(index)

    override def setHotPointSelected(index: Int, selected: Boolean): Unit = {
        this.hotPointSelected(index) = selected
        this.notifySelectionListeners()
    }

    override def translate(delta: Point): Unit = {
        for (index <- this.hotPoints.indices) {
            this.hotPoints(index) = this.hotPoints(index).translate(delta)
        }

        this.notifyListeners()
    }

    override def addGraphicalObjectListener(listener: GraphicalObjectListener): Unit = this.listeners += listener

    override def removeGraphicalObjectListener(listener: GraphicalObjectListener): Unit =
        this.listeners -= listener

    protected def notifyListeners(): Unit = this.listeners.foreach(_.graphicalObjectChanged(this))

    protected def notifySelectionListeners(): Unit = this.listeners.foreach(_.graphicalObjectSelectionChanged(this))

    override def save(rows: ListBuffer[String]): Unit = rows +=
        this.shapeId + " " + this.hotPoints.map(p => p.x + " " + p.y).reduce((l, r) => l + " " + r) + "\n"
}
