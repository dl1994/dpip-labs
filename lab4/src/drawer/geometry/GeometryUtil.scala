package drawer.geometry

object GeometryUtil {

    private def sq(n: Int) = n * n

    def distanceFromPoint(point1: Point, point2: Point): Double = Math.sqrt(
        sq(point2.x - point1.x) + sq(point2.y - point1.y)
    )

    def distanceFromLineSegment(start: Point, end: Point, point: Point): Double = {
        val a = end.y - start.y
        val b = start.x - end.x
        val c = start.y * (end.x - start.x) - start.x * (end.y - start.y)

        if (a == 0 && b == 0) {
            distanceFromPoint(point, start)
        } else {
            val v1 = (start.x - point.x, start.y - point.y)
            val v2 = (end.x - start.x, end.y - start.y)
            val v3 = (end.x - point.x, end.y - point.y)
            val v4 = (start.x - end.x, start.y - end.y)
            val dot1 = v1._1 * v2._1 + v1._2 * v2._2
            val dot2 = v3._1 * v4._1 + v3._2 * v4._2

            if (dot1 < 0 && dot2 < 0) {
                val coeff = a * point.x + b * point.y + c

                if (coeff == 0) {
                    0
                } else {
                    (coeff.abs / Math.sqrt(sq(a) + sq(b))) + 1.0
                }
            } else {
                distanceFromPoint(point, start) min distanceFromPoint(point, end)
            }
        }
    }
}
