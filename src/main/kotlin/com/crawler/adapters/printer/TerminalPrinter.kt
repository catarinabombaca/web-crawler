package com.crawler.adapters.printer

import com.crawler.domain.ports.OutputPrinter

class TerminalPrinter : OutputPrinter {
    override fun print(url: String, links: List<String>) {
        val linksOutput = if (links.isEmpty()) "N/A" else links.joinToString(", ")
        println("Visited: $url -> Found links: $linksOutput")
    }
}