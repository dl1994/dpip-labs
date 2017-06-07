package drawer

import java.awt.Color
import drawer.geometry.{G2Renderer, Point, Renderer}
import scala.swing.event.{Key, KeyPressed, MouseDragged, MousePressed, MouseReleased}
import scala.swing.{Component, Graphics2D}

class Canvas(model: DocumentModel, state: Field[State]) extends Component with DocumentModelListener {

    override def paintComponent(graphics: Graphics2D): Unit = {
        graphics.setColor(Color.WHITE)
        graphics.fillRect(0, 0, this.bounds.width, this.bounds.height)

        val renderer: Renderer = new G2Renderer(graphics)
        val currentState: State = state()

        model.list().foreach { o =>
            o.render(renderer)
            currentState.afterDraw(renderer, o)
        }

        currentState.afterDraw(renderer)
    }

    private def hasModifier(modifiers: Int, modifier: Int) = (modifiers & modifier) != 0

    private def mouseClickAction(action: (Point, Boolean, Boolean) => Unit,
                                 point: java.awt.Point, modifiers: Int): Unit = {
        action(
            new Point(point.x, point.y),
            hasModifier(modifiers, Key.Modifier.Shift),
            hasModifier(modifiers, Key.Modifier.Control)
        )
    }

    listenTo(keys, mouse.clicks, mouse.moves)
    reactions += {
        case MouseDragged(_, point, _) => {
            state().mouseDragged(new Point(point.x, point.y))
        }
        case MousePressed(_, point, modifiers, _, _) => {
            mouseClickAction(state().mouseDown, point, modifiers)
        }
        case MouseReleased(_, point, modifiers, _, _) => {
            mouseClickAction(state().mouseUp, point, modifiers)
        }
        case KeyPressed(_, Key.Escape, _, _) => {
            state() = IdleState
        }
        case KeyPressed(_, keyCode, _, _) => {
            state().keyPressed(keyCode)
        }
    }
    focusable = true

    override def documentChange(): Unit = this.repaint()
}
