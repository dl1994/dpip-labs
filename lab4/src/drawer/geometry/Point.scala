package drawer.geometry

class Point(xc: Int, yc: Int) {

    val x: Int = xc
    val y: Int = yc

    def translate(delta: Point): Point = new Point(this.x + delta.x, this.y + delta.y)

    def difference(delta: Point): Point = new Point(this.x - delta.x, this.y - delta.y)

    override def toString: String = this.x + "," + this.y
}
