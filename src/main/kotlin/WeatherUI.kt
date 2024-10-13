
import org.json.JSONObject
import java.awt.BorderLayout
import java.awt.Dimension
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.swing.*
import javax.swing.border.TitledBorder
import kotlin.math.ceil


class WeatherUI {
    private lateinit var cityInput: JTextField
    private lateinit var degreeInput: JTextField
    private lateinit var resultLabel: JLabel
    private lateinit var appLabel: JLabel

    fun createAndShowGUI() {
        val frame = JFrame("Weather App")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.preferredSize = Dimension(500, 400)

        cityInput = JTextField()
        cityInput.columns = 20
        cityInput.toolTipText = "Enter a city"
        cityInput.font = cityInput.font.deriveFont(20f)
        cityInput.border = TitledBorder(BorderFactory.createEtchedBorder(), "City", TitledBorder.CENTER, TitledBorder.TOP, cityInput.font.deriveFont(16f))

        degreeInput = JTextField()
        degreeInput.columns = 2
        degreeInput.toolTipText = "Enter 'C' or 'F'"
        degreeInput.font = degreeInput.font.deriveFont(20f)
        degreeInput.border = TitledBorder(BorderFactory.createEtchedBorder(), "Degree", TitledBorder.CENTER, TitledBorder.TOP, cityInput.font.deriveFont(16f))

        val searchButton = JButton("Get Weather")
        searchButton.addActionListener {
            val city = formatCity(cityInput.text)
            val degree = degreeInput.text.uppercase()

            validatingDegree(degree)
            try {
                val apiKey = loadApiKey("src/main/apikey.properties")
                validatingAPIKey(apiKey)
                validatingCity(city, apiKey) // Validate the city

                getWeather(city, apiKey, degree)
            } catch (e: Exception) {
                resultLabel.text = "Error: ${e.message}"
            }
        }

        resultLabel = JLabel("Weather information will appear here", JLabel.CENTER)
        resultLabel.font = resultLabel.font.deriveFont(15f)
        appLabel = JLabel("Weather App", JLabel.CENTER)
        appLabel.font = appLabel.font.deriveFont(15f)


        val panel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            add(appLabel)
            add(cityInput)
            add(degreeInput)
            add(searchButton)
            add(resultLabel)
        }

        frame.contentPane.add(panel, BorderLayout.CENTER)
        frame.pack()
        frame.isVisible = true
    }

    private fun getWeather(city: String, apiKey: String, degree: String) {
        try {
            val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$apiKey"
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = StringBuilder()
            reader.forEachLine { response.append(it) }
            reader.close()

            val jsonResponse = JSONObject(response.toString())
            val cityName = jsonResponse.getString("name")
            val mainObject = jsonResponse.getJSONObject("main")
            var temp = ceil(mainObject.getDouble("temp") - 273.15)
            var feelsLike = ceil(mainObject.getDouble("feels_like") - 273.15)
            val humidity = mainObject.getInt("humidity")
            val degreeSymbol = if (degree == "C") "\u00b0C" else "\u00b0F"

            if (degree == "F") {
                temp = ceil(temp * 9/5 + 32)
                feelsLike = ceil(feelsLike * 9/5 + 32)
            }

            resultLabel.text = """
                Weather for $cityName:
                Temperature: ${"%.0f".format(temp)} $degreeSymbol
                Feels Like: ${"%.0f".format(feelsLike)} $degreeSymbol
                Humidity: $humidity%
            """.trimIndent()

        } catch (e: Exception) {
            resultLabel.text = "Error: ${e.message}"
        }
    }
}