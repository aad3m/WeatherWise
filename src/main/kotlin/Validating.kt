
import java.io.FileInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

fun validatingDegree(input: String){
    if (input == "F" || input == "C"){
        return
    }
    else{
        throw IllegalArgumentException("Error: Invalid degree input.")
    }
}

fun validatingAPIKey(input: String){
    try {
        if (input.isBlank()) {
        println("Create One Here -> https://openweathermap.org/api ")
        throw IllegalArgumentException("No API provided.")

    }
    if (input.length != 32 && input.isNotBlank()){
        throw IllegalArgumentException("Invalid API.")
    }
    } catch (e: IllegalFormatException) {
        throw IllegalArgumentException("Error: Could not validate API key: ${e.message}")
    }
}

fun validatingCity(city: String, apiKey: String) {
    try {
        if (city.isBlank()) {
            throw IllegalArgumentException("No city has been inputted")
        }
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$apiKey"
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        if (connection.responseCode != HttpURLConnection.HTTP_OK) {
            throw IllegalArgumentException("Not valid city.")
        }
    } catch (e: IllegalFormatException) {
        throw IllegalArgumentException("Error: Could not validate city: ${e.message}")
    }
}

fun loadApiKey(filePath: String): String {
    val properties = Properties()
    try {
        properties.load(FileInputStream(filePath))
        return properties.getProperty("API_KEY") ?: throw IllegalArgumentException("API Key not found in properties file.")
    } catch (e: IOException) {
        throw IllegalArgumentException("Could not read API key from properties file: ${e.message}")
    }
}