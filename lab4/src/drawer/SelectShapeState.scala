package drawer

import drawer.geometry.{Point, Rectangle, Renderer}
import drawer.graphics.{CompositeShape, GraphicalObject}
import scala.swing.event.Key

class SelectShapeState(model: DocumentModel) extends State {

    private var selectedHotPoint: Int = -1

    override def mouseDown(mousePoint: Point, shiftDown: Boolean, ctrlDown: Boolean): Unit = {
        val objectToSelect = model.findSelectedGraphicalObject(mousePoint)

        if (objectToSelect == null) {
            this.selectedHotPoint = -1

            if (!ctrlDown) {
                model.clearSelection()
            }

            return
        }

        if (ctrlDown) {
            this.selectedHotPoint = -1

            if (objectToSelect.selected) {
                model.deselectGraphicalObject(objectToSelect)
            } else {
                model.selectGraphicalObject(objectToSelect)
            }
        } else if (objectToSelect.selected) {
            this.selectedHotPoint = model.findSelectedHotPoint(objectToSelect, mousePoint)

            model.selectGraphicalObject(objectToSelect)
        } else {
            this.selectedHotPoint = -1

            model.clearSelection()
            model.selectGraphicalObject(objectToSelect)
        }
    }

    override def mouseUp(mousePoint: Point, shiftDown: Boolean, ctrlDown: Boolean): Unit = Unit

    override def mouseDragged(mousePoint: Point): Unit = {
        if (this.selectedHotPoint != -1 && model.getSelectedObjects.size == 1) {
            model.getSelectedObjects.foreach(_.setHotPoint(this.selectedHotPoint, mousePoint))
        }
    }

    override def keyPressed(keyCode: Key.Value): Unit = {
        val translation: Option[Point] = keyCode match {
            case Key.Plus | Key.Add => {
                if (model.getSelectedObjects.size == 1) {
                    model.getSelectedObjects.foreach(model.increaseZ)
                }

                None
            }
            case Key.Minus | Key.Subtract => {
                if (model.getSelectedObjects.size == 1) {
                    model.getSelectedObjects.foreach(model.decreaseZ)
                }

                None
            }
            case Key.Up => {
                Some(new Point(0, -1))
            }
            case Key.Down => {
                Some(new Point(0, 1))
            }
            case Key.Left => {
                Some(new Point(-1, 0))
            }
            case Key.Right => {
                Some(new Point(1, 0))
            }
            case Key.G => {
                if (model.getSelectedObjects.size > 1) {
                    val selectedObjectsList = model.getSelectedObjects.toList
                    val composite = new CompositeShape(selectedObjectsList)

                    composite.selected = true
                    selectedObjectsList.foreach(model.removeGraphicalObject)
                    model.addGraphicalObject(composite)
                }

                None
            }
            case Key.U => {
                if (model.getSelectedObjects.size == 1) {
                    val composite = model.getSelectedObjects
                        .filter(_.isInstanceOf[CompositeShape])
                        .map(_.asInstanceOf[CompositeShape])
                        .reduce((l, r) => l)

                    model.removeGraphicalObject(composite)
                    composite.getChildren.foreach(c => {
                        c.selected = true
                        model.addGraphicalObject(c)
                    })
                }

                None
            }
            case _ => {
                None
            }
        }

        if (translation.isDefined) {
            model.getSelectedObjects.foreach(_.translate(translation.get))
        }
    }

    override def afterDraw(renderer: Renderer, graphicalObject: GraphicalObject): Unit = {
        if (graphicalObject.selected) {
            this.drawRectangle(graphicalObject.getBoundingBox, renderer)

            if (model.getSelectedObjects.size == 1) {
                for (i <- 0 until graphicalObject.numberOfHotPoints) {
                    val point = graphicalObject.getHotPoint(i)
                    this.drawRectangle(new Rectangle(point.x - 4, point.y + 4, 8, 8), renderer)

                    if (this.selectedHotPoint == i) {
                        this.drawRectangle(new Rectangle(point.x - 2, point.y + 2, 4, 4), renderer)
                    }
                }
            }
        }
    }

    private def drawRectangle(rectangle: Rectangle, renderer: Renderer) = {
        val point1 = new Point(rectangle.x, rectangle.y)
        val point2 = new Point(rectangle.x + rectangle.width, rectangle.y)
        val point3 = new Point(rectangle.x + rectangle.width, rectangle.y - rectangle.height)
        val point4 = new Point(rectangle.x, rectangle.y - rectangle.height)

        renderer.drawLine(point1, point2)
        renderer.drawLine(point2, point3)
        renderer.drawLine(point3, point4)
        renderer.drawLine(point4, point1)
    }

    override def afterDraw(renderer: Renderer): Unit = Unit

    override def onLeaving(): Unit = model.clearSelection()
}
