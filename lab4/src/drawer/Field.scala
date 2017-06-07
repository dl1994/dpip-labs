package drawer

class Field[T](v: T, beforeChange: T => Unit) {

    var value: T = v

    def apply(): T = this.value

    def update(value: T): Unit = {
        this.beforeChange(this.value)
        this.value = value
    }
}
