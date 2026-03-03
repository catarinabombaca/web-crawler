package com.crawler.acceptance

import com.crawler.adapters.http.CrawlRequestDTO
import com.crawler.adapters.http.CrawlRoute
import com.crawler.adapters.http.app
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class WebCrawlerTest {
    @Test
    fun `Given a seed URL, the crawler should visit each URL it finds on the same domain`() {
        val seedUrl = "https://crawlme.monzo.com"
        val numberOfExpectedVisitedUrls = 12

        val requestBody = CrawlRequestDTO(url = seedUrl)
        app(
            Request(Method.POST, "/crawl")
                .with(CrawlRoute.crawlRequestDTOLens of requestBody)
        )
        val printedUrls = "outputStream.toString().trim().lines().filter { it.isNotBlank() }"
        assertEquals(numberOfExpectedVisitedUrls, printedUrls)
    }

    @Test
    fun `Given a seed URL, the crawler should print each URL visited, and a list of links found on that page`() {
        val seedUrl = "https://crawlme.monzo.com"
        val expectedOutput = """
            Visited: https://crawlme.monzo.com/ -> Found links: https://crawlme.monzo.com/about, https://crawlme.monzo.com/contact, https://crawlme.monzo.com/products
            Visited: https://crawlme.monzo.com/about -> Found links: https://crawlme.monzo.com/team, https://crawlme.monzo.com/careers
            Visited: https://crawlme.monzo.com/contact -> Found links: https://crawlme.monzo.com/support, https://crawlme.monzo.com/feedback
            Visited: https://crawlme.monzo.com/products -> Found links: https://crawlme.monzo.com/products/monzo-card, https://crawlme.monzo.com/products/monzo-app
            Visited: https://crawlme.monzo.com/team -> Found links: N/A
            Visited: https://crawlme.monzo.com/careers -> Found links: N/A
            Visited: https://crawlme.monzo.com/support -> Found links: N/A
            Visited: https://crawlme.monzo.com/feedback -> Found links: N/A
            Visited: https://crawlme.monzo.com/products/monzo-card -> Found links: N/A
            Visited: https://crawlme.monzo.com/products/monzo-app -> Found links: N/A
        """.trimIndent()

        val requestBody = CrawlRequestDTO(url = seedUrl)
        app(
            Request(Method.POST, "/crawl")
                .with(CrawlRoute.crawlRequestDTOLens of requestBody)
        )
        val printedUrls = "outputStream.toString().trim()"
        assertEquals(expectedOutput, printedUrls)
    }
}
