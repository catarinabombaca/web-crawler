package com.crawler.domain.services

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

        val crawlerService = CrawlerService(fakeClient)

        val actual = crawlerService.crawl(seedUrl)

        assertEquals(expectedUrls, actual)
    }
}

val exampleHtml = ClassLoader.getSystemResourceAsStream("example.html")!!.bufferedReader().readText()

val fakeClient: HttpHandler = { request ->
    when (request.uri.toString()) {
        "https://example.com/" -> Response(OK).body(exampleHtml)
        else -> Response(NOT_FOUND)
    }
}