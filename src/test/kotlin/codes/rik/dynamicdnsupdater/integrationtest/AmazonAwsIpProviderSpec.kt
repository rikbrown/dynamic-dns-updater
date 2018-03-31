package codes.rik.dynamicdnsupdater.integrationtest

import codes.rik.dynamicdnsupdater.AmazonAwsIpProvider
import org.amshove.kluent.shouldMatch
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object AmazonAwsIpProviderIntegrationTestSpec: Spek({

    describe("AmazonAwsIpProvider") {
        on("getIpAddress") {
            val ipAddress = AmazonAwsIpProvider.getIpAddress()

            it("returns an IP") {
                ipAddress shouldMatch """^\d+.\d+.\d+.\d+$"""
            }
        }
    }

})