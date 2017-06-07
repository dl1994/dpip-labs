package drawer

import drawer.geometry.Point
import drawer.graphics.{GraphicalObject, GraphicalObjectListener}
import scala.collection.mutable.ListBuffer
import scala.collection.{IterableView, SeqView, mutable}

class DocumentModel extends GraphicalObjectListener {

    final val SELECTION_PROXIMITY: Double = 10.0

    private val objects: ListBuffer[GraphicalObject] = new ListBuffer()
    private val listeners: ListBuffer[DocumentModelListener] = new ListBuffer()
    private val selectedObjects: mutable.Set[GraphicalObject] = new mutable.HashSet()
    private val objectsProxy: SeqView[GraphicalObject, ListBuffer[GraphicalObject]] = this.objects.view
    private val selectedObjectsProxy: IterableView[GraphicalObject, mutable.Set[GraphicalObject]] =
        this.selectedObjects.view

    def clear(): Unit = {
        this.objects.foreach(_.removeGraphicalObjectListener(this))
        this.objects.clear()
    }

    def addGraphicalObject(graphicalObject: GraphicalObject): Unit = {
        this.objects += graphicalObject

        if (graphicalObject.selected) {
            this.selectedObjects += graphicalObject
        }

        graphicalObject.addGraphicalObjectListener(this)

        this.notifyListeners()
    }

    def removeGraphicalObject(graphicalObject: GraphicalObject): Unit = {
        this.objects -= graphicalObject
        this.selectedObjects -= graphicalObject

        graphicalObject.removeGraphicalObjectListener(this)

        this.notifyListeners()
    }

    def selectGraphicalObject(graphicalObject: GraphicalObject): Unit = {
        graphicalObject.selected = true

        this.selectedObjects += graphicalObject
        this.notifyListeners()
    }

    def deselectGraphicalObject(graphicalObject: GraphicalObject): Unit = {
        graphicalObject.selected = false

        this.selectedObjects -= graphicalObject
        this.notifyListeners()
    }

    def clearSelection(): Unit = {
        this.selectedObjects.foreach(_.selected = false)
        this.selectedObjects.clear()
        this.notifyListeners()
    }

    def list(): SeqView[GraphicalObject, ListBuffer[GraphicalObject]] = this.objectsProxy

    def addDocumentModelListener(listener: DocumentModelListener): Unit = this.listeners += listener

    def removeDocumentModelListener(listener: DocumentModelListener): Unit = this.listeners -= listener

    def getSelectedObjects: IterableView[GraphicalObject, mutable.Set[GraphicalObject]] = this.selectedObjectsProxy

    def increaseZ(graphicalObject: GraphicalObject): Unit = changeZ(graphicalObject, 1)

    def decreaseZ(graphicalObject: GraphicalObject): Unit = changeZ(graphicalObject, -1)

    private def changeZ(graphicalObject: GraphicalObject, change: Int): Unit = {
        val size = this.objects.size

        if (size == 0 || size == 1) {
            return
        }

        val index = this.objects.indexOf(graphicalObject)

        if (index == -1) {
            return
        }

        val newIndex = 0 max (index + change) min (size - 1)
        this.objects.remove(index)
        this.objects.insert(newIndex, graphicalObject)
        this.notifyListeners()
    }

    def findSelectedGraphicalObject(mousePoint: Point): GraphicalObject = {
        val withDistances = this.objects.map(p => (p, p.selectionDistance(mousePoint)))
            .filter(t => t._2 <= SELECTION_PROXIMITY)

        if (withDistances.isEmpty) {
            return null
        }

        withDistances.reverse.minBy(_._2)._1
    }

    def findSelectedHotPoint(graphicalObject: GraphicalObject, mousePoint: Point): Int = {
        val withDistances = (0 until graphicalObject.numberOfHotPoints)
            .map(i => (i, graphicalObject.getHotPointDistance(i, mousePoint)))
            .filter(t => t._2 <= SELECTION_PROXIMITY)

        if (withDistances.isEmpty) {
            return -1
        }

        withDistances.minBy(_._2)
            ._1
    }

    private def notifyListeners() = this.listeners.foreach(_.documentChange())

    override def graphicalObjectChanged(graphicalObject: GraphicalObject): Unit = this.notifyListeners()

    override def graphicalObjectSelectionChanged(graphicalObject: GraphicalObject): Unit = this.notifyListeners()
}
