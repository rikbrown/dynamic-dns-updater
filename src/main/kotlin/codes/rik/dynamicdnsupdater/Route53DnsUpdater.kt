package codes.rik.dynamicdnsupdater

import com.amazonaws.services.route53.AmazonRoute53
import com.amazonaws.services.route53.model.*
import mu.KotlinLogging
import java.net.InetAddress

private val logger = KotlinLogging.logger {}

/**
 * DNS updater which uses [AmazonRoute53]
 */
class Route53DnsUpdater(
        private val route53: AmazonRoute53,
        private val hostedZoneId: String,
        private val record: String,
        private val ipProvider: () -> String,
        private val dnsLookup: (String) -> String = { dnsName -> InetAddress.getByName(dnsName).hostAddress }) {

    /**
     * Update the IP address configured for [record] with the address provided by [ipProvider]
     * Will only perform an update if it is required (DNS and AWS both disagree with the new address).
     */
    fun update() {
        val currentIpAddress = ipProvider()
        logger.debug { "Current IP address: $currentIpAddress" }

        val existingIpAddressDns = dnsLookup(record)
        logger.debug { "IP address in DNS: $existingIpAddressDns" }
        if (existingIpAddressDns == currentIpAddress) return // DNS IP matches current IP, no change needed

        val existingIpAddressAws = getExistingIpAddress()
        logger.debug { "IP address in AWS: $existingIpAddressAws" }
        if (existingIpAddressAws == currentIpAddress) return // AWS IP matches current IP, no change needed (probably cached)

        logger.info { "Change detected, updating IP address to $currentIpAddress" }
        route53.changeResourceRecordSets(ChangeResourceRecordSetsRequest()
                .withHostedZoneId(hostedZoneId)
                .withChangeBatch(ChangeBatch()
                        .withChanges(Change()
                                .withAction(ChangeAction.UPSERT)
                                .withResourceRecordSet(ResourceRecordSet()
                                        .withName(record)
                                        .withType(RRType.A)
                                        .withTTL(60)
                                        .withResourceRecords(ResourceRecord(currentIpAddress))))))
    }

    /**
     * Returns the existing IP address configured in [AmazonRoute53] for [record], or null if nothing appears to be
     * configured.
     */
    fun getExistingIpAddress(): String? {
        val response = route53.listResourceRecordSets(ListResourceRecordSetsRequest()
                .withHostedZoneId(hostedZoneId)
                .withStartRecordName(record)
                .withMaxItems("1"))

        return response.resourceRecordSets
                .firstOrNull()
                ?.resourceRecords?.firstOrNull()
                ?.value
    }

}