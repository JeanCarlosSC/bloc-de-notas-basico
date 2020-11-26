import swingRAD.*
import swingRAD.sMenuBar.SMenuBar
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.io.FileReader
import java.io.BufferedReader
import java.lang.StringBuilder
import java.io.IOException
import java.io.FileWriter
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger
import javax.swing.*
import kotlin.system.exitProcess

class BlocNotas: JFrame() {

    private var ventana: JFrame = JFrame("Mi bloc de Notas")
    private var notas: JTextArea

    private val pila: Stack<String> = Stack()

    private fun abrirArchivo() {
        val fileChooser = JFileChooser()
        fileChooser.fileSelectionMode = JFileChooser.FILES_ONLY
        if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(ventana)) {
            val archivo = fileChooser.selectedFile
            var lector: FileReader? = null
            try {
                lector = FileReader(archivo)
                val bfReader = BufferedReader(lector)
                var lineaFichero: String?
                val contenidoFichero = StringBuilder()

                // Recupera el contenido del fichero
                while (bfReader.readLine().also { lineaFichero = it } != null) {
                    contenidoFichero.append(lineaFichero)
                    contenidoFichero.append("\n")
                }

                // Pone el contenido del fichero en el area de texto
                notas.text = contenidoFichero.toString()
            } catch (ex: IOException) {
                Logger.getLogger(BlocNotas::class.java.name).log(Level.SEVERE, null, ex)
            } finally {
                try {
                    assert(lector != null)
                    lector!!.close()
                } catch (ex: IOException) {
                    Logger.getLogger(BlocNotas::class.java.name).log(Level.SEVERE, null, ex)
                }
            }
        }
    }

    private fun guardarArchivo() {
        val fileChooser = JFileChooser()
        fileChooser.fileSelectionMode = JFileChooser.FILES_ONLY
        if (JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(ventana)) {
            val archivo = fileChooser.selectedFile
            var escritor: FileWriter? = null
            try {
                escritor = FileWriter(archivo)
                escritor.write(notas.text)
            } catch (ex: IOException) {
                Logger.getLogger(BlocNotas::class.java.name).log(Level.SEVERE, null, ex)
            } finally {
                try {
                    assert(escritor != null)
                    escritor!!.close()
                } catch (ex: IOException) {
                    Logger.getLogger(BlocNotas::class.java.name).log(Level.SEVERE, null, ex)
                }
            }
        }
    }

    init {
        // pila
        pila.add("")

        // menu
        val menu = SMenuBar()
        add(menu)

        val archivo = JMenu("Archivo")
        menu.add(archivo)

        val nuevo = JMenuItem("Nuevo")
        archivo.add(nuevo)

        val abrir = JMenuItem("Abrir...")
        archivo.add(abrir)

        val guardar = JMenuItem("Guardar")
        archivo.add(guardar)

        val salir = JMenuItem("Salir")
        archivo.add(salir)

        val tema = JMenu("Tema")
        menu.add(tema)

        val claro = JMenuItem("Claro")
        tema.add(claro)

        val oscuro = JMenuItem("Oscuro")
        tema.add(oscuro)

        val ayuda = JMenu("Ayuda")
        menu.add(ayuda)

        val acercaDe = JMenuItem("Acerca de...")
        ayuda.add(acercaDe)

        // Crea un area de texto con scroll y lo añade a la ventana
        notas = JTextArea()
        notas.setProperties(5, 5, 1260, 643, border = null)
        notas.addKeyListener(object: KeyListener{
            override fun keyTyped(e: KeyEvent?) {
            }

            override fun keyPressed(e: KeyEvent?) {
                if (e!!.keyCode == KeyEvent.VK_SPACE) {
                    pila.add(notas.text)
                }
                if(e.keyCode == KeyEvent.VK_Z && e.isControlDown) {
                    if (pila.size > 1)
                        pila.removeLast()
                    notas.text = pila.lastElement()
                }
            }

            override fun keyReleased(e: KeyEvent?) {
                if (e!!.keyCode == KeyEvent.VK_BACK_SPACE) {
                    pila.add(notas.text)
                }
            }
        })

        val scrollNotas = JScrollPane(notas)
        scrollNotas.setProperties(2, 60, 1270, 653)
        add(scrollNotas)

        // Asigna a cada menuItem su listener
        nuevo.addActionListener { notas.text = "" }
        abrir.addActionListener { abrirArchivo() }
        guardar.addActionListener { guardarArchivo() }
        salir.addActionListener { exitProcess(0) }

        // frame
        setMainBar("bloc de notas")
        setProperties()
    }

}