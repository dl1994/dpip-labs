package drawer.geometry

import java.nio.file.{Files, Path}
import scala.collection.mutable.ListBuffer

class SvgRenderer(path: Path) extends Renderer {

    private val lines = new ListBuffer[String]

    this.lines += """<svg  xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">"""

    override def drawLine(start: Point, end: Point): Unit = this.lines += "\n<line x1=\"" + start.x +
        "\" y1=\"" + start.y + "\" x2=\"" + end.x + "\" y2=\"" + end.y + "\" style=\"stroke:#ff0000\"/>"

    override def fillPolygon(points: Array[Point]): Unit = this.lines += "\n<polygon points=\"" +
        points.mkString(" ") + "\" style=\"stroke:#ff0000; fill: #0000ff\"/>"

    def close(): Unit = {
        val writer = Files.newBufferedWriter(this.path)

        this.lines += "\n</svg>\n"
        this.lines.foreach(writer.write)

        writer.close()
    }
}
