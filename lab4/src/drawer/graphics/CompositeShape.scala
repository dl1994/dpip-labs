package drawer.graphics

import drawer.geometry.{Point, Rectangle, Renderer}
import scala.collection.mutable.ListBuffer

class CompositeShape(children: List[GraphicalObject]) extends AbstractGraphicalObject(Array()) {

    override val shapeId: String = "@COMP"
    override val shapeName: String = "Composite"

    override def getBoundingBox: Rectangle = {
        val (minX, maxX, minY, maxY) = this.children.map(c => {
            val bounds = c.getBoundingBox
            (
                bounds.x min (bounds.x + bounds.width),
                bounds.x max (bounds.x + bounds.width),
                bounds.y min (bounds.y - bounds.height),
                bounds.y max (bounds.y - bounds.height)
            )
        }).reduce((l, r) => {
            (
                l._1 min r._1,
                l._2 max r._2,
                l._3 min r._3,
                l._4 max r._4
            )
        })

        val width = maxX - minX
        val height = maxY - minY

        new Rectangle(minX, maxY, width, height)
    }

    override def selectionDistance(mousePoint: Point): Double = this.children.map(_.selectionDistance(mousePoint)).min

    override def render(renderer: Renderer): Unit = this.children.foreach(_.render(renderer))

    override def duplicate(): GraphicalObject = {
        val childrenCopy = for (child <- children) yield {
            child.duplicate()
        }

        new CompositeShape(childrenCopy)
    }

    override def load(stack: ListBuffer[GraphicalObject], data: String): Unit = {
        val numOfChildren = data.toInt
        val children = stack.takeRight(numOfChildren).toList

        stack.trimEnd(data.toInt)
        stack += new CompositeShape(children)
    }

    override def save(rows: ListBuffer[String]): Unit = {
        this.children.foreach(_.save(rows))

        rows += this.shapeId + " " + this.children.size + "\n"
    }

    def getChildren: List[GraphicalObject] = this.children
}
