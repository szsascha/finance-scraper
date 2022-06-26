package com.github.szsascha.financescraper;

import com.github.szsascha.financescraper.model.ScrapeRequest;
import com.github.szsascha.financescraper.model.ScrapeResponse;
import com.github.szsascha.financescraper.model.ScrapeResponseDetail;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@SpringBootApplication
public class FinanceScraperApplication {

	private final String URL = "https://query1.finance.yahoo.com/v7/finance/download/%s?period1=0&period2=%s&interval=1d&events=history&includeAdjustedClose=true";

	public static void main(String[] args) {
		SpringApplication.run(FinanceScraperApplication.class, args);
	}

	@Bean
	public Function<ScrapeRequest, ScrapeResponse> scrape() {
		return request -> {
			final String url = String.format(URL, request.getTicker(), Instant.now().getEpochSecond());

			try {
				final HttpRequest httpRequest = HttpRequest.newBuilder()
						.uri(new URI(url))
						.GET()
						.build();

				final HttpResponse<String> httpResponse = HttpClient.newBuilder()
						.proxy(ProxySelector.getDefault())
						.build()
						.send(httpRequest, HttpResponse.BodyHandlers.ofString());

				final String body = httpResponse.body();

				final List<ScrapeResponseDetail> details = Arrays.stream(body.split(System.lineSeparator()))
						.skip(1) // Skip header
						.map(row -> {
							final String[] cells = row.split(",");
							return ScrapeResponseDetail.builder()
									.date(LocalDate.parse(cells[0]))
									.open(new BigDecimal(cells[1]))
									.high(new BigDecimal(cells[2]))
									.low(new BigDecimal(cells[3]))
									.close(new BigDecimal(cells[4]))
									.adjustedClose(new BigDecimal(cells[5]))
									.volume(new BigInteger(cells[6]))
									.build();
						}).toList();

				return ScrapeResponse.builder()
						.ticker(request.getTicker())
						.data(details)
						.build();

			} catch (URISyntaxException | InterruptedException | IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		};
	}

}
