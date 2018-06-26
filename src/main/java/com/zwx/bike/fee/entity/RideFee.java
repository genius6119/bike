package com.zwx.bike.fee.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RideFee {
    private Long id;

    private Integer minUnit;

    private BigDecimal fee;

    private Byte bikeType;


}