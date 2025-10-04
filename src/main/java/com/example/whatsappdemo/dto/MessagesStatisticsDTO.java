package com.example.whatsappdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessagesStatisticsDTO {
    private String status;
    private Long count;
}
