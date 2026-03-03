package com.crawler.adapters.htmlFetcher

import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ApiHtmlFetcherTest {

    @Test
    fun `should return body when client responds`() {
        val expected = "<html>hello</html>"
        val url = "https://example.com/"
        val client = { _: Request -> Response(OK).body(expected) }

        val actual = ApiHtmlFetcher(client).fetch(url)

        assertEquals(expected, actual)
    }

    @Test
    fun `should throw when client throws an exception`() {
        val url = "https://example.com/"
        val client = { _: Request -> throw RuntimeException("connection refused") }

        val exception = assertThrows<RuntimeException> {
            ApiHtmlFetcher(client).fetch(url)
        }

        assertEquals("Failed to fetch HTML: connection refused", exception.message)
    }
}