package drawer.graphics

import drawer.geometry.{Point, Rectangle, Renderer}
import scala.collection.mutable.ListBuffer

trait GraphicalObject {

    var selected: Boolean
    val shapeId: String
    val shapeName: String
    val numberOfHotPoints: Int

    def getHotPoint(index: Int): Point

    def setHotPoint(index: Int, point: Point): Unit

    def isHotPointSelected(index: Int): Boolean

    def setHotPointSelected(index: Int, selected: Boolean): Unit

    def getHotPointDistance(index: Int, mousePoint: Point): Double

    def translate(delta: Point): Unit

    def getBoundingBox: Rectangle

    def selectionDistance(mousePoint: Point): Double

    def render(renderer: Renderer): Unit

    def addGraphicalObjectListener(listener: GraphicalObjectListener): Unit

    def removeGraphicalObjectListener(listener: GraphicalObjectListener): Unit

    def duplicate(): GraphicalObject

    def load(stack: ListBuffer[GraphicalObject], data: String): Unit

    def save(rows: ListBuffer[String]): Unit
}
