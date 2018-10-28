package codes.rik.dynamicdnsupdater

import com.google.common.util.concurrent.AbstractIdleService
import com.google.common.util.concurrent.AbstractScheduledService
import java.net.URL
import java.util.concurrent.ScheduledExecutorService
import kotlin.text.Charsets.US_ASCII

/**
 * Retrieves current IP address via AWS's checkip service
 */
class AmazonAwsIpSensor(vararg listeners: IpChangeListener): AbstractScheduledService() {



    private fun getIpAddress() = IpAddress(checkIpUrl.readText(US_ASCII).trim())
}

private val checkIpUrl = URL("http://checkip.amazonaws.com")
inline class IpAddress(val value: String)

