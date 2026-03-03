package com.crawler.domain.services

import com.crawler.domain.ports.HtmlFetcher
import com.crawler.domain.ports.OutputPrinter
import com.crawler.fakes.exampleHtml
import dev.forkhandles.result4k.Success
import dev.forkhandles.result4k.asSuccess
import dev.forkhandles.result4k.get
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import net.bytebuddy.matcher.ElementMatchers.any
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CrawlerServiceTest {

    @Test
    fun `should visit each url it finds on the same domain and return a list of all urls visited`() {
        val seedUrl = "https://example.com/"
        val expectedUrls = listOf("https://example.com/", "https://example.com/about", "https://example.com/contact")
        val htmlFetcher = mockk<HtmlFetcher>()
        val printer = mockk<OutputPrinter>()

        every { htmlFetcher.fetch(seedUrl) } returns Success(exampleHtml)
        every { htmlFetcher.fetch("https://example.com/about") } returns Success("dummy html")
        every { htmlFetcher.fetch("https://example.com/contact") } returns Success("dummy html")
        every { printer.print(any(), any()) } returns Unit

        val crawlerService = CrawlerService(htmlFetcher, printer)

        val actual = crawlerService.crawl(seedUrl)

        assertEquals(expectedUrls, actual.get())
        verify(exactly = 3) { htmlFetcher.fetch(any()) }
        verify(exactly = 3) { printer.print(any(), any()) }
    }
}
