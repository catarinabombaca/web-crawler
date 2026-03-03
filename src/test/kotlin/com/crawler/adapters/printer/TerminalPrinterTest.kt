package com.crawler.adapters.printer

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class TerminalPrinterTest {
    private val outputStream = ByteArrayOutputStream()
    private val originalOut = System.out

    @BeforeEach
    fun setUp() {
        System.setOut(PrintStream(outputStream))
    }

    @AfterEach
    fun tearDown() {
        System.setOut(originalOut)
        outputStream.reset()
    }

    @Test
    fun `should print visited url with found links`() {
        val url = "https://example.com/"
        val link1 = "https://example.com/about"
        val link2 = "https://example.com/contact"
        val expected = "Visited: $url -> Found links: $link1, $link2"

        TerminalPrinter().print(url, listOf(link1, link2))

        assertEquals(expected, outputStream.toString().trim())
    }

    @Test
    fun `should print NA when no links are found`() {
        val url = "https://example.com/about"
        TerminalPrinter().print(url, emptyList())

        assertEquals("Visited: $url -> Found links: N/A", outputStream.toString().trim())
    }
}