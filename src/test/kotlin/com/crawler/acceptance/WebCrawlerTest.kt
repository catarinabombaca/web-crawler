package com.crawler.acceptance

import com.crawler.adapters.http.CrawlRequestDTO
import com.crawler.adapters.http.CrawlRoute
import com.crawler.adapters.http.app
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.with
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class WebCrawlerTest {
    private val outputStream = ByteArrayOutputStream()
    private val originalOut = System.out

    @BeforeEach
    fun setUp() {
        System.setOut(PrintStream(outputStream))
    }

    @AfterEach
    fun tearDown() {
        System.setOut(originalOut)
        outputStream.reset()
    }

    @Test
    fun `Given a seed URL, the crawler should visit each URL it finds on the same domain`() {
        val seedUrl = "https://example.com/"
        val numberOfExpectedVisitedUrls = 3
        val requestBody = CrawlRequestDTO(url = seedUrl)

        app(
            Request(Method.POST, "/crawl")
                .with(CrawlRoute.crawlRequestDTOLens of requestBody)
        )

        val printedUrls = outputStream.toString().trim().lines().size
        assertEquals(numberOfExpectedVisitedUrls, printedUrls)
    }

    @Test
    fun `Given a seed URL, the crawler should print each URL visited, and a list of links found on that page`() {
        val seedUrl = "https://example.com/"
        val expectedOutput = """
            Visited: https://example.com/ -> Found links: https://example.com/about, https://example.com/contact, https://potato.com/
            Visited: https://example.com/about -> Found links: N/A
            Visited: https://example.com/contact -> Found links: N/A
        """.trimIndent()

        val requestBody = CrawlRequestDTO(url = seedUrl)
        app(
            Request(Method.POST, "/crawl")
                .with(CrawlRoute.crawlRequestDTOLens of requestBody)
        )

        val printedOutput = outputStream.toString().trim()
        assertEquals(expectedOutput, printedOutput)
    }
}