package com.onkonfeton.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode
@ToString
public class Order {
    private UUID id;
    private List<Product> products;
    private OffsetDateTime createDate;
}
