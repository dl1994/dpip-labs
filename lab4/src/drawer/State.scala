package drawer

import drawer.geometry.{Point, Renderer}
import drawer.graphics.GraphicalObject
import scala.swing.event.Key

trait State {

    def mouseDown(mousePoint: Point, shiftDown: Boolean, ctrlDown: Boolean): Unit

    def mouseUp(mousePoint: Point, shiftDown: Boolean, ctrlDown: Boolean): Unit

    def mouseDragged(mousePoint: Point): Unit

    def keyPressed(keyCode: Key.Value): Unit

    def afterDraw(renderer: Renderer, graphicalObject: GraphicalObject): Unit

    def afterDraw(renderer: Renderer): Unit

    def onLeaving(): Unit
}
