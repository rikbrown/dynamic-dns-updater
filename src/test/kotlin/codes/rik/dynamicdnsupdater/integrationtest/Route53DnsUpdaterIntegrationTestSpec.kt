package codes.rik.dynamicdnsupdater.integrationtest

import codes.rik.dynamicdnsupdater.Route53DnsUpdater
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.route53.AmazonRoute53ClientBuilder
import com.google.common.net.InetAddresses
import org.amshove.kluent.`should equal`
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.jetbrains.spek.subject.SubjectSpek
import java.util.*

const val TEST_HOSTED_ZONE_ID = "Z39HKRRJQ8AJ0"
const val TEST_RECORD = "dns-updater-integration-test.rik.systems"

object Route53DnsUpdaterIntegrationTestSpec: SubjectSpek<Route53DnsUpdater>({
    val route53 = AmazonRoute53ClientBuilder.standard()
            .withCredentials(ProfileCredentialsProvider("rik-systems-dynamic-dns-updater"))
            .build()

    var ipAddress: String? = null

    describe("Route53DnsUpdater") {
        subject { Route53DnsUpdater(route53,
                hostedZoneId = TEST_HOSTED_ZONE_ID,
                record = TEST_RECORD,
                ipProvider = { ipAddress!! }) }

        on("update() with new IP") {
            ipAddress = InetAddresses.fromInteger(Random().nextInt()).hostAddress
            subject.update()

            it("should update the DNS") {
                val updatedIpAddress = subject.getExistingIpAddress()
                updatedIpAddress `should equal` ipAddress
            }
        }

    }

})