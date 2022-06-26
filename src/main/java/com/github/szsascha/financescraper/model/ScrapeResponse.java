package com.github.szsascha.financescraper.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ScrapeResponse {

    private String ticker;

    private List<ScrapeResponseDetail> data;

}
