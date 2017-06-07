package drawer.geometry

import java.awt.Color
import scala.swing.Graphics2D

class G2Renderer(graphics: Graphics2D) extends Renderer {

    override def drawLine(start: Point, end: Point): Unit = {
        graphics.setColor(Color.BLUE)
        graphics.drawLine(start.x, start.y, end.x, end.y)
    }

    override def fillPolygon(points: Array[Point]): Unit = {
        val xs = for (point <- points) yield {
            point.x
        }

        val ys = for (point <- points) yield {
            point.y
        }

        graphics.setColor(Color.BLUE)
        graphics.fillPolygon(xs, ys, points.length)
        graphics.setColor(Color.RED)
        graphics.drawPolygon(xs, ys, points.length)
    }
}
