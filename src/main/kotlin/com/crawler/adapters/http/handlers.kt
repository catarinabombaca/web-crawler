package com.crawler.adapters.http

import com.crawler.formats.JacksonMessage
import com.crawler.formats.jacksonMessageLens
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with

fun healthHandler(request: Request): Response = Response(OK).body("pong")

fun formatHandler(request: Request): Response = Response(OK).with(jacksonMessageLens of JacksonMessage("Barry", "Hello there!"))
