package codes.rik.dynamicdnsupdater

import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.route53.AmazonRoute53ClientBuilder

/**
 * Application runner
 */
object App {
    private val route53 = AmazonRoute53ClientBuilder.standard()
            .withCredentials(ProfileCredentialsProvider("rik-systems-dynamic-dns-updater"))
            .build()
    private val dnsUpdater = Route53DnsUpdater(route53,
            hostedZoneId = "Z39HKRRJQ8AJ0",
            record = "seattle.rik.systems",
            ipProvider = AmazonAwsIpProvider::getIpAddress)

    @JvmStatic
    fun main(args: Array<String>) {
        dnsUpdater.update()
    }
}