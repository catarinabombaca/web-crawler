package com.crawler.acceptance

import com.crawler.adapters.htmlFetcher.ApiHtmlFetcher
import com.crawler.adapters.printer.TerminalPrinter
import com.crawler.domain.services.CrawlerService
import com.crawler.fakes.fakeHttpClient
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
        val crawler = CrawlerService(ApiHtmlFetcher(fakeHttpClient), TerminalPrinter())

        crawler.crawl(seedUrl)

        val printedUrls = outputStream.toString().trim().lines().size
        assertEquals(3, printedUrls)
    }

    @Test
    fun `Given a seed URL, the crawler should print each URL visited, and a list of links found on that page`() {
        val seedUrl = "https://example.com/"
        val expectedOutput = """
            Visited: https://example.com/ -> Found links: https://example.com/about, https://example.com/contact, https://potato.com/
            Visited: https://example.com/about -> Found links: N/A
            Visited: https://example.com/contact -> Found links: N/A
        """.trimIndent()
        val crawler = CrawlerService(ApiHtmlFetcher(fakeHttpClient), TerminalPrinter())

        crawler.crawl(seedUrl)

        assertEquals(expectedOutput, outputStream.toString().trim())
    }
}