package com.example.dits.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestStatisticByUser {
    private String testName;
    private int count;
    private int avgProc;
}
