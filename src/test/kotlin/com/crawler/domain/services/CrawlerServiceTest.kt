package com.crawler.domain.services

import com.crawler.domain.ports.HtmlFetcher
import com.crawler.domain.ports.OutputPrinter
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CrawlerServiceTest {

    @Test
    fun `should visit each url it finds on the same domain and return a list of all urls visited`() {
        val seedUrl = "https://example.com/"
        val expectedUrls = listOf("https://example.com/", "https://example.com/about", "https://example.com/contact")
        val htmlFetcher = mockk<HtmlFetcher>()
        val printer = mockk<OutputPrinter>()

        val seedHtml = """
            <html><body>
                <a href="/about">About</a>
                <a href="/contact">Contact</a>
                <a href="https://potato.com/">Potato</a>
            </body></html>
        """.trimIndent()

        every { htmlFetcher.fetch(seedUrl) } returns seedHtml
        every { htmlFetcher.fetch("https://example.com/about") } returns "dummy html"
        every { htmlFetcher.fetch("https://example.com/contact") } returns "dummy html"
        every { printer.print(any(), any()) } returns Unit

        val crawlerService = CrawlerService(htmlFetcher, printer)

        val actual = crawlerService.crawl(seedUrl)

        assertEquals(expectedUrls, actual)
        verify(exactly = 3) { htmlFetcher.fetch(any()) }
        verify(exactly = 3) { printer.print(any(), any()) }
    }
}