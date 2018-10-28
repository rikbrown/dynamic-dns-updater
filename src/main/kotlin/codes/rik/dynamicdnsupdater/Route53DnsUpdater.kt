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
        private val dnsLookup: (String) -> IpAddress = { dnsName -> IpAddress(InetAddress.getByName(dnsName).hostAddress) }): IpChangeListener {

    override fun onIpChange(ipAddress: IpAddress) = update(ipAddress)

    /**
     * Update the IP address configured for [record].
     * Will only perform an update if it is required (DNS and AWS both disagree with the new address).
     */
    private fun update(ipAddress: IpAddress) {
        logger.debug { "Current IP address: $ipAddress" }

        logger.info { "Change detected, updating IP address to $ipAddress" }
        route53.changeResourceRecordSets(ChangeResourceRecordSetsRequest()
                .withHostedZoneId(hostedZoneId)
                .withChangeBatch(ChangeBatch()
                        .withChanges(Change()
                                .withAction(ChangeAction.UPSERT)
                                .withResourceRecordSet(ResourceRecordSet()
                                        .withName(record)
                                        .withType(RRType.A)
                                        .withTTL(60)
                                        .withResourceRecords(ResourceRecord(ipAddress.value))))))
    }

}