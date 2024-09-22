package com.onkonfeton.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.UUID;

@Data
@EqualsAndHashCode
@ToString
public class Product {
    private UUID id;
    private String name;
    private Double price;
}

