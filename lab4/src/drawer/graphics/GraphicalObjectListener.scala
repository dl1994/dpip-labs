package drawer.graphics

trait GraphicalObjectListener {

    def graphicalObjectChanged(graphicalObject: GraphicalObject): Unit

    def graphicalObjectSelectionChanged(graphicalObject: GraphicalObject): Unit
}
