package com.crawler.adapters.http

import com.crawler.domain.ports.Crawler
import com.crawler.domain.ports.CrawlerFailure
import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Success
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.core.with
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CrawlRouteTest {
    private val mockCrawler = mockk<Crawler>()

    @Test
    fun `should respond OK when given a request with a seed url`() {
        val seedUrl = "https://example.com/"
        val requestBody = CrawlRequestDTO(url = seedUrl)
        val request = Request(Method.POST, "/crawl")
            .with(CrawlRoute.crawlRequestDTOLens of requestBody)
        every { mockCrawler.crawl(seedUrl) } returns Success(listOf("Dummy list"))

        val response = CrawlRoute(mockCrawler).contractRoutes().first()(request)

        assertEquals(Status.OK, response.status)
        verify(exactly = 1) { mockCrawler.crawl(seedUrl) }
    }

    @Test
    fun `should respond 400 Bad Request when given a request without a seed url`() {
        val request = Request(Method.POST, "/crawl")

        val response = CrawlRoute(mockCrawler).contractRoutes().first()(request)

        assertEquals(Status.BAD_REQUEST, response.status)
        verify(exactly = 0) { mockCrawler.crawl(any()) }
    }

    @Test
    fun `should respond 500 Internal Server Error when crawling fails`() {
        val seedUrl = "https://example.com/"
        val request = Request(Method.POST, "/crawl")
            .with(CrawlRoute.crawlRequestDTOLens of CrawlRequestDTO(url = seedUrl))
        every { mockCrawler.crawl(seedUrl) } returns Failure(CrawlerFailure("Dummy error"))

        val response = CrawlRoute(mockCrawler).contractRoutes().first()(request)

        assertEquals(Status.INTERNAL_SERVER_ERROR, response.status)
    }
}