package com.w2n.challenge.Utils;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public class PageableUtils {

    static final int DEFAULT_PAGE_NUMBER = 0;
    static final int DEFAULT_PAGE_SIZE = 10;

    public static Pageable PageableOf(
            Integer pageNumber,
            Integer pageSize,
            String orderCriteria,
            String orderField
    ) {
        return PageRequest.of(
                mapperPageNumber(pageNumber),
                mapperPageSize(pageSize),
                mapperSort(orderCriteria, orderField)
        );
    }

    private static Integer mapperPageNumber(Integer pageNumber) {
        return Optional.ofNullable(pageNumber)
                .filter(pn -> pn >= 0)
                .orElse(DEFAULT_PAGE_NUMBER);
    }

    private static Integer mapperPageSize(Integer pageSize) {
        return Optional.ofNullable(pageSize)
                .filter(ps -> ps >= 0)
                .orElse(DEFAULT_PAGE_SIZE);
    }

    private static Sort mapperSort(String orderCriteria, String orderField) {
        return Optional.of(orderCriteria)
                .map(String::toUpperCase)
                .map(String::trim)
                .filter(o -> !o.equals("ASC"))
                .map(o -> Sort.by(Sort.Order.desc(orderField)))
                .orElse(Sort.by(Sort.Order.asc(orderField)));
    }
}

