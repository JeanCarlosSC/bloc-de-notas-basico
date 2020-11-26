import swingRAD.setProperties
import javax.swing.JFrame
import javax.swing.JTextArea
import javax.swing.JFileChooser
import java.io.FileReader
import java.io.BufferedReader
import java.lang.StringBuilder
import java.io.IOException
import java.io.FileWriter
import javax.swing.JMenuBar
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.JScrollPane
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.system.exitProcess

class BlocNotas {

    private var ventana: JFrame = JFrame("Mi bloc de Notas")
    private var notas: JTextArea

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

        // Inicializa todos los elementos del menu
        val menu = JMenuBar()
        val archivo = JMenu("Archivo")
        val ayuda = JMenu("Ayuda")
        val nuevo = JMenuItem("Nuevo")
        val abrir = JMenuItem("Abrir...")
        val guardar = JMenuItem("Guardar")
        val salir = JMenuItem("Salir")
        val acercaDe = JMenuItem("Acerca de...")

        // Añade los elementos al menu
        archivo.add(nuevo)
        archivo.add(abrir)
        archivo.add(guardar)
        archivo.add(salir)
        ayuda.add(acercaDe)
        menu.add(archivo)
        menu.add(ayuda)

        // Añade la barra de menu a la ventana
        ventana.jMenuBar = menu

        // Cra un area de texto con scroll y lo añade a la ventana
        notas = JTextArea()
        val scrollNotas = JScrollPane(notas)
        ventana.add(scrollNotas)

        // Asigna a cada menuItem su listener
        nuevo.addActionListener { notas.text = "" }
        abrir.addActionListener { abrirArchivo() }
        guardar.addActionListener { guardarArchivo() }
        salir.addActionListener { exitProcess(0) }

        // Hace visible la ventana
        ventana.setSize(1366, 728)
        ventana.isVisible = true
        ventana.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    }

}