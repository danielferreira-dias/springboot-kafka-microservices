package com.example.bookingservice.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingRequest {
    private Long userId;
    private Long eventId;
    private Long ticketCount;
}
