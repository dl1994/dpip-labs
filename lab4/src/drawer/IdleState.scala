package drawer

import drawer.geometry.{Point, Renderer}
import drawer.graphics.GraphicalObject
import scala.swing.event.Key

object IdleState extends State {

    override def mouseDown(mousePoint: Point, shiftDown: Boolean, ctrlDown: Boolean): Unit = Unit

    override def mouseUp(mousePoint: Point, shiftDown: Boolean, ctrlDown: Boolean): Unit = Unit

    override def mouseDragged(mousePoint: Point): Unit = Unit

    override def keyPressed(keyCode: Key.Value): Unit = Unit

    override def afterDraw(renderer: Renderer, graphicalObject: GraphicalObject): Unit = Unit

    override def afterDraw(renderer: Renderer): Unit = Unit

    override def onLeaving(): Unit = Unit
}
