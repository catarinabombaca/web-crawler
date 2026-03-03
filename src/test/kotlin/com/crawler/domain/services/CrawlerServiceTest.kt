package com.crawler.domain.services

import com.crawler.CrawlerFailure
import com.crawler.domain.ports.HtmlFetcher
import com.crawler.domain.ports.OutputPrinter
import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Success
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class CrawlerServiceTest {

    @Test
    fun `should visit each url it finds on the same domain and print the links found`() {
        val seedUrl = "https://example.com/"
        val htmlFetcher = mockk<HtmlFetcher>()
        val printer = mockk<OutputPrinter>()

        val seedHtml = """
            <html><body>
                <a href="/about">About</a>
                <a href="/contact">Contact</a>
                <a href="https://potato.com/">Potato</a>
            </body></html>
        """.trimIndent()

        every { htmlFetcher.fetch(seedUrl) } returns Success(seedHtml)
        every { htmlFetcher.fetch("https://example.com/about") } returns Success("dummy html")
        every { htmlFetcher.fetch("https://example.com/contact") } returns Success("dummy html")
        every { printer.print(any(), any()) } returns Unit

        CrawlerService(htmlFetcher, printer).crawl(seedUrl)

        verify(exactly = 3) { htmlFetcher.fetch(any()) }
        verify(exactly = 0) { htmlFetcher.fetch("https://potato.com/") }
        verify(exactly = 3) { printer.print(any(), any()) }
    }

    @Test
    fun `should only visit a url once regardless of trailing slash`() {
        val seedUrl = "https://example.com/"
        val htmlFetcher = mockk<HtmlFetcher>()
        val printer = mockk<OutputPrinter>()

        val seedHtml = """
            <html><body>
                <a href="/about">About</a>
                <a href="/about/">About with trailing slash</a>
            </body></html>
        """.trimIndent()

        every { htmlFetcher.fetch(seedUrl) } returns Success(seedHtml)
        every { htmlFetcher.fetch("https://example.com/about") } returns Success("dummy html")
        every { htmlFetcher.fetch("https://example.com/about/") } returns Success("dummy html")
        every { printer.print(any(), any()) } returns Unit

        CrawlerService(htmlFetcher, printer).crawl(seedUrl)

        verify(exactly = 1) { htmlFetcher.fetch(seedUrl) }
        verify(exactly = 1) { htmlFetcher.fetch("https://example.com/about") }
        verify(exactly = 0) { htmlFetcher.fetch("https://example.com/about/") }
    }

    @Test
    fun `should skip a url that fails to fetch and continue crawling`() {
        val seedUrl = "https://example.com/"
        val htmlFetcher = mockk<HtmlFetcher>()
        val printer = mockk<OutputPrinter>()

        val seedHtml = """
            <html><body>
                <a href="/about">About</a>
                <a href="/contact">Contact</a>
            </body></html>
        """.trimIndent()

        every { htmlFetcher.fetch(seedUrl) } returns Success(seedHtml)
        every { htmlFetcher.fetch("https://example.com/about") } returns Failure(CrawlerFailure("Server error: HTTP 404"))
        every { htmlFetcher.fetch("https://example.com/contact") } returns Success("dummy html")
        every { printer.print(any(), any()) } returns Unit

        CrawlerService(htmlFetcher, printer).crawl(seedUrl)

        verify(exactly = 3) { htmlFetcher.fetch(any()) }
        verify(exactly = 2) { printer.print(any(), any()) }
    }
}