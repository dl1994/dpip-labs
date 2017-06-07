package drawer

import drawer.graphics.{CompositeShape, GraphicalObject, LineSegment, Oval}
import scala.collection.mutable.ListBuffer

object Loader {

    private val PROTOTYPES = Map(
        "@OVAL" -> new Oval(),
        "@LINE" -> new LineSegment(),
        "@COMP" -> new CompositeShape(List())
    )

    def load(lines: Iterator[String]): ListBuffer[GraphicalObject] = {
        val objects = new ListBuffer[GraphicalObject]()

        lines.map(_.trim)
            .filter(!_.isEmpty)
            .map(_.split(" ", 2))
            .filter(s => PROTOTYPES.contains(s(0)))
            .map(s => (PROTOTYPES(s(0)), s(1)))
            .foreach(t => {
                val prototype = t._1
                val definition = t._2

                prototype.load(objects, definition)
            })

        objects
    }
}
