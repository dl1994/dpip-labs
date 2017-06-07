package drawer

import java.awt.Dimension
import java.io.File
import java.nio.file.Files
import drawer.geometry.{Point, SvgRenderer}
import drawer.graphics.{LineSegment, Oval}
import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.swing.BorderPanel.Position._
import scala.swing.event.ButtonClicked
import scala.swing.{BorderPanel, Button, FileChooser, Frame, MainFrame, SimpleSwingApplication, ToolBar}

object GUI extends SimpleSwingApplication {

    override def top: Frame = new MainFrame {

        private val model = new DocumentModel()
        private val currentState = new Field[State](IdleState, _.onLeaving())
        private val canvas = new Canvas(this.model, currentState)

        title = "InkEscape"
        contents = new BorderPanel() {
            layout(new ToolBar() {
                contents += new Button("Load") {
                    reactions += {
                        case ButtonClicked(_) => {
                            val chooser = new FileChooser(new File(".")) {
                                title = "Load"
                                peer.setApproveButtonText("Load")
                            }

                            val result = chooser.showOpenDialog(null)

                            if (result == FileChooser.Result.Approve) {
                                val source = Source.fromFile(chooser.selectedFile)
                                Loader.load(source.getLines()).foreach(model.addGraphicalObject)
                            }
                        }
                    }
                    focusable = false
                }
                contents += new Button("Save") {
                    reactions += {
                        case ButtonClicked(_) => {
                            val chooser = new FileChooser(new File(".")) {
                                title = "Save"
                                peer.setApproveButtonText("Save")
                            }

                            val result = chooser.showOpenDialog(null)

                            if (result == FileChooser.Result.Approve) {
                                val rows = new ListBuffer[String]()
                                model.list().foreach(_.save(rows))
                                val writer = Files.newBufferedWriter(chooser.selectedFile.toPath)
                                rows.foreach(writer.write)
                                writer.close()
                            }
                        }
                    }
                    focusable = false
                }
                contents += new Button("SVG Export") {
                    reactions += {
                        case ButtonClicked(_) => {
                            val chooser = new FileChooser(new File(".")) {
                                title = "SVG Export"
                                peer.setApproveButtonText("Export")
                            }

                            val result = chooser.showOpenDialog(null)

                            if (result == FileChooser.Result.Approve) {
                                val renderer = new SvgRenderer(chooser.selectedFile.toPath)
                                model.list().foreach(_.render(renderer))
                                renderer.close()
                            }
                        }
                    }
                    focusable = false
                }
                contents += new Button("Line") {
                    reactions += {
                        case ButtonClicked(_) => {
                            currentState() = new AddShapeState(
                                new LineSegment(new Point(10, -10), new Point(-10, 10)),
                                model
                            )
                        }
                    }
                    focusable = false
                }
                contents += new Button("Oval") {
                    reactions += {
                        case ButtonClicked(_) => {
                            currentState() = new AddShapeState(
                                new Oval(),
                                model
                            )
                        }
                    }
                    focusable = false
                }
                contents += new Button("Select") {
                    reactions += {
                        case ButtonClicked(_) => {
                            currentState() = new SelectShapeState(model)
                        }
                    }
                    focusable = false
                }
                contents += new Button("Delete") {
                    reactions += {
                        case ButtonClicked(_) => {
                            currentState() = new EraserState(model, canvas)
                        }
                    }
                    focusable = false
                }
            }) = North
            layout(canvas) = Center
        }
        size = new Dimension(680, 420)

        this.canvas.requestFocusInWindow()
        this.model.addDocumentModelListener(this.canvas)
    }
}
