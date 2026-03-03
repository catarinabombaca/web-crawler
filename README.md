# Web crawler

Crawls a website starting from a seed URL, visiting all pages on the same domain and printing each URL visited along
with the links found on that page. The output of successful visits are printed to stdout whilst errors are printed to stderr.

## How to run:

### Prerequisites:

- Java 21 (Gradle wrapper is bundled — no need to install Gradle separately).
- After unzipping, make the Gradle wrapper executable: `chmod +x gradlew`

### Run

```bash
./gradlew run --args="https://example.com"
```

Or build a standalone CLI and run it directly:

```bash
./gradlew installDist
./build/install/WebCrawler/bin/WebCrawler https://crawlme.monzo.com/
```

### Test

```bash
./gradlew test
```

## Improvements

These things could be improved if I had more time:

- Make the number of concurrent url fetches configurable via the CLI, so we can crawl a website faster. The parallelFetches parameter is a tuning knob, we can start conservative to avoid hammering servers, and increase it to speed up the crawl, or reduce it if we reach rate limits.
- Also include a timeout after which we give up crawling a domain, which would be configurable via the CLI
- The constraint around which links to visit/not visit could be improved. For example, we could allow the user to specify a regex pattern for which links to visit, instead of being hardcoded to only visit links on the same domain.

## Decisions along the way

- **Concurrency**: running requests serially was very slow (2min +). I used Kotlin coroutines with a semaphore to fetch 
pages concurrently. `coroutineScope { }` handles termination, it only returns once every coroutine it started has finished. 
The semaphore helps you cap the number of concurrent requests, so you don't overwhelm the server.

- **Error handling**: I used `result4k` to make fetch failures explicit in the `HtmlFetcher` port. Callers are forced to 
handle both success and failure rather than relying on catching exceptions. When a URL fails to fetch, the crawler prints 
the error to stderr and continues, so one bad URL never stops the rest of the crawl.

- **URL normalisation**: I normalised URLs to strip trailing slashes to avoid visiting the same page twice (e.g. `/about` and `/about/`). 
I intentionally did not strip anchor fragments (`#section`) as the test site doesn't use them and I didn't want to 
overengineer the solution but it would be a straightforward addition to the normalisation step.

- **Print immediately**: rather than holding everything in memory and printing at the end, each visited URL is printed to 
stdout as soon as it is fetched. This gives immediate visibility of progress and avoids memory growth on large sites.

- **Hexagonal architecture**: I decoupled domain logic from implementation details using ports (`HtmlFetcher`, `OutputPrinter`, `Crawler`) 
and adapters. This makes the code testable at each layer independently and means the entry point can be swapped. Right now 
it's a CLI, but it could just as easily be a web server.
