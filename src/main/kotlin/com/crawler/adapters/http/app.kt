package com.crawler.adapters.http

import com.crawler.domain.services.CrawlerService
import org.http4k.client.OkHttp
import org.http4k.contract.contract
import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status.Companion.INTERNAL_SERVER_ERROR
import org.http4k.core.then
import org.http4k.filter.ServerFilters
import org.http4k.routing.routes

val catchAllFilter = Filter { next ->
    { request ->
        try { next(request) }
        catch (e: Exception) { Response(INTERNAL_SERVER_ERROR) }
    }
}

val app: HttpHandler = ServerFilters.CatchLensFailure()
    .then(catchAllFilter)
    .then(routes(
        contract {
            val htmlFetcher = apiHtmlFetcher(OkHttp())
            routes += CrawlRoute(CrawlerService(htmlFetcher)).contractRoutes()
        }
    ))