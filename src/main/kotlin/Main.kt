import javax.swing.SwingUtilities

fun main() {
    SwingUtilities.invokeLater {
        WeatherUI().createAndShowGUI()
    }
}