package com.crawler.adapters.htmlFetcher

import com.crawler.CrawlerFailure
import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Success
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ApiHtmlFetcherTest {

    @Test
    fun `should return the html content when the fetch is successful`() {
        val expected = "<html>hello</html>"
        val url = "https://example.com/"
        val client = { _: Request -> Response(OK).body(expected) }

        val actual = ApiHtmlFetcher(client).fetch(url)

        assertEquals(Success(expected), actual)
    }

    @Test
    fun `should return a failure when it fails to fetch due to a network error`() {
        val url = "https://example.com/"
        val client = { _: Request -> throw RuntimeException("connection refused") }

        val actual = ApiHtmlFetcher(client).fetch(url)

        assertEquals(Failure(CrawlerFailure("Network error: connection refused")), actual)
    }

    @Test
    fun `should return a failure when it fails to fetch due to server error`() {
        val url = "https://example.com/"
        val client = { _: Request -> Response(NOT_FOUND) }

        val actual = ApiHtmlFetcher(client).fetch(url)

        assertEquals(Failure(CrawlerFailure("Server error: HTTP 404")), actual)
    }
}