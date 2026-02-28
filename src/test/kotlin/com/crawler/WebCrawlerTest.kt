package com.crawler

import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class WebCrawlerTest {

    @Test
    fun `Ping test`() {
        assertEquals(Response(OK).body("pong"), app(Request(GET, "/ping")))
    }
    
    @Test
    fun `Given a seed URL, the crawler should visit each URL it finds on the same domain`() {}
    
    @Test
    fun `Given a seed URL, the crawler should print each URL visited, and a list of links found on that page`() {}

    @Test
    fun `Given a seed URL, the crawler should not follow external links`() {}
}
