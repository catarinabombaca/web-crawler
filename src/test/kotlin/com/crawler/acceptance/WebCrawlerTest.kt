package com.crawler.acceptance

import com.crawler.adapters.http.app
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class WebCrawlerTest {

    @Test
    fun `Ping test`() {
        Assertions.assertEquals(
            Response.Companion(Status.Companion.OK).body("pong"),
            app(Request.Companion(Method.GET, "/ping"))
        )
    }

    @Test
    fun `Given a seed URL, the crawler should visit each URL it finds on the same domain`() {
        val seedUrl = "https://crawlme.monzo.com"
        val numberOfExpectedUrls = 10

        app(
            Request.Companion(Method.POST, "/crawl")
                .body(seedUrl)
        )

        val output = generateSequence(::readlnOrNull).toList()
        assertEquals(numberOfExpectedUrls, output.size)
    }

    @Test
    fun `Given a seed URL, the crawler should print each URL visited, and a list of links found on that page`() {}

    @Test
    fun `Given a seed URL, the crawler should not follow external links`() {}
}