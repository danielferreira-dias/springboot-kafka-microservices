package com.example.inventoryservice.response;
import com.example.inventoryservice.entity.Venue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


/**
 * Data Transfer Object (DTO) representing the inventory information for a single event.
 *
 * This object is used as a response payload to expose selected details about an event,
 * including its name, available capacity, and associated venue.
 *
 * Lombok annotations:
 * <ul>
 *   <li>{@code @Data} - Generates getters, setters, toString, equals, and hashCode methods</li>
 *   <li>{@code @Builder} - Enables builder-style object creation</li>
 *   <li>{@code @NoArgsConstructor} - Generates a no-argument constructor</li>
 *   <li>{@code @AllArgsConstructor} - Generates a constructor with all fields</li>
 * </ul>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventInventoryResponse {
    private Long eventId;
    private String event;
    private Long capacity;
    private Venue venue;
    private BigDecimal ticketPrice;

}
