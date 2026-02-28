package com.crawler.adapters.http

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.http4k.core.with
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CrawlHandlerTest {

    @Test
    fun `should respond OK when given a request with a seed url`() {
        val seedUrl = "https://crawlme.monzo.com"
        val requestBody = CrawlRequestDTO(url = seedUrl)
        val response = app(
            Request(Method.POST, "/crawl")
                .with(CrawlRoute.crawlRequestDTOLens of requestBody)
        )

        assertEquals(Status.OK, response.status)
    }

    @Test
    fun `should respond  400 Bad Request when given a request without a seed url`() {
        val response = app(Request(Method.POST, "/crawl"))

        assertEquals(Status.BAD_REQUEST, response.status)
    }

    @Test
    fun `should respond 500 Internal Server Error when the app is down`() {
        val response = app(Request(Method.POST, "/crawl"))

        assertEquals(Status.INTERNAL_SERVER_ERROR, response.status)
    }
}