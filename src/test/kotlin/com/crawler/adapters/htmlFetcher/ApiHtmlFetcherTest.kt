package com.crawler.adapters.htmlFetcher

import com.crawler.domain.ports.CrawlerFailure
import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Success
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ApiHtmlFetcherTest {

    @Test
    fun `should return Success with body when client responds`() {
        val expected = "<html>hello</html>"
        val url = "https://example.com/"
        val client = { _: Request -> Response(OK).body(expected) }

        val actual = ApiHtmlFetcher(client).fetch(url)

        assertEquals(Success(expected), actual)
    }

    @Test
    fun `should return Failure when client throws an exception`() {
        val expected = Failure(CrawlerFailure("Failed to fetch HTML: connection refused"))
        val url = "https://example.com/"
        val client = { _: Request -> throw RuntimeException("connection refused") }

        val actual = ApiHtmlFetcher(client).fetch(url)

        assertEquals(expected, actual)
    }
}