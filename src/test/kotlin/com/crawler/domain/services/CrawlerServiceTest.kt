package com.crawler.domain.services

import com.crawler.domain.ports.HtmlFetcher
import com.crawler.fakes.exampleHtml
import io.mockk.every
import io.mockk.mockk
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
        every { htmlFetcher.fetch(seedUrl) } returns exampleHtml
        every { htmlFetcher.fetch("https://example.com/about") } returns "dummy html"
        every { htmlFetcher.fetch("https://example.com/contact") } returns "dummy html"

        val crawlerService = CrawlerService(htmlFetcher)

        val actual = crawlerService.crawl(seedUrl)

        assertEquals(expectedUrls, (actual as dev.forkhandles.result4k.Success).value)
    }
}
