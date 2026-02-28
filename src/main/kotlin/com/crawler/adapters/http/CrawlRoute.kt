package com.crawler.adapters.http

import org.http4k.contract.ContractRoute
import org.http4k.contract.bindContract
import org.http4k.contract.div
import org.http4k.contract.meta
import org.http4k.contract.openapi.OpenAPIJackson.auto
import org.http4k.core.Body
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Response.Companion.invoke
import org.http4k.core.Status.Companion.OK
import org.http4k.format.ConfigurableJackson

class CrawlRoute {
    private val spec = "/crawl" meta {
        summary = "Crawl the given seed url"
        description = "The crawler should print each URL visited, and a list of links found on that page"
        receiving(crawlRequestDTOLens to crawlRequestDTOExample)
        returning(OK)
    }

    fun contractRoutes(): List<ContractRoute> =
        listOf(spec bindContract POST to ::crawlHandler)

    private fun crawlHandler(request: Request): Response = Response(OK)

    companion object {
        val crawlRequestDTOLens = Body.auto<CrawlRequestDTO>().toLens()
        val crawlRequestDTOExample = CrawlRequestDTO(url = "https://crawlme.monzo.com")
    }
}

data class CrawlRequestDTO(val url: String)