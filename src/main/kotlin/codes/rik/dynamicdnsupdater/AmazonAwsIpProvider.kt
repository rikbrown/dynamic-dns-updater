package codes.rik.dynamicdnsupdater

import java.net.URL
import kotlin.text.Charsets.US_ASCII

private val checkIpUrl = URL("http://checkip.amazonaws.com")

/**
 * Retrieves current IP address via AWS's checkip service
 */
object AmazonAwsIpProvider {
    fun getIpAddress() = checkIpUrl.readText(US_ASCII).trim()
}
