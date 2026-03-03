package com.crawler.acceptance

import com.crawler.main
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.server.Undertow
import org.http4k.server.asServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class WebCrawlerTest {
    private val outputStream = ByteArrayOutputStream()
    private val originalOut = System.out

    private val testServer = { request: Request ->
        when (request.uri.path) {
            "/"        -> Response(OK).body("<html><body><a href='/about'>About</a><a href='/contact'>Contact</a><a href='https://external.com/'>External</a></body></html>")
            "/about"   -> Response(OK).body("<html><body></body></html>")
            "/contact" -> Response(OK).body("<html><body></body></html>")
            else       -> Response(NOT_FOUND)
        }
    }.asServer(Undertow(0))

    @BeforeEach
    fun setUp() {
        testServer.start()
        System.setOut(PrintStream(outputStream))
    }

    @AfterEach
    fun tearDown() {
        System.setOut(originalOut)
        outputStream.reset()
        testServer.stop()
    }

    @Test
    fun `Given a seed URL, the crawler should visit each URL it finds on the same domain`() {
        val port = testServer.port()

        main(arrayOf("http://localhost:$port/"))

        assertEquals(3, outputStream.toString().trim().lines().size)
    }

    @Test
    fun `Given a seed URL, the crawler should print each URL visited, and a list of links found on that page`() {
        val port = testServer.port()
        val base = "http://localhost:$port"
        val expectedOutput = """
            Visited: $base/ -> Found links: $base/about, $base/contact, https://external.com/
            Visited: $base/about -> Found links: N/A
            Visited: $base/contact -> Found links: N/A
        """.trimIndent()

        main(arrayOf("$base/"))

        assertEquals(expectedOutput, outputStream.toString().trim())
    }
}