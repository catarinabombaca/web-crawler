# Web Crawler

Crawls a website starting from a seed URL, visiting all pages on the same domain and printing each URL visited along with the links found on that page.

## Requirements

Java 21 (Gradle wrapper is bundled — no need to install Gradle separately).

## Run

```bash
./gradlew run --args="https://example.com"
```

Or build a standalone CLI and run it directly:

```bash
./gradlew installDist
./build/install/WebCrawler/bin/WebCrawler https://example.com
```

## Test

```bash
./gradlew test
```