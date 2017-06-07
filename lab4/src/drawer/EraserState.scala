package drawer

import drawer.geometry.{Point, Renderer}
import drawer.graphics.GraphicalObject
import scala.collection.mutable.ListBuffer
import scala.swing.event.Key

class EraserState(model: DocumentModel, canvas: Canvas) extends State {

    private val deletionPoints = new ListBuffer[Point]

    override def mouseDown(mousePoint: Point, shiftDown: Boolean, ctrlDown: Boolean): Unit = Unit

    override def mouseUp(mousePoint: Point, shiftDown: Boolean, ctrlDown: Boolean): Unit = {
        this.deletionPoints.foreach(p => {
            model.list()
                .filter(_.selectionDistance(p) == 0)
                .foreach(model.removeGraphicalObject)
        })
        this.deletionPoints.clear()
        this.canvas.repaint()
    }

    override def mouseDragged(mousePoint: Point): Unit = {
        this.deletionPoints += mousePoint
        this.canvas.repaint()
    }

    override def keyPressed(keyCode: Key.Value): Unit = Unit

    override def afterDraw(renderer: Renderer, graphicalObject: GraphicalObject): Unit = Unit

    override def afterDraw(renderer: Renderer): Unit = {
        this.deletionPoints.foreach(p => renderer.drawLine(p, p))
    }

    override def onLeaving(): Unit = this.deletionPoints.clear()
}
