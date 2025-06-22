package com.example.orderservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class InventoryServiceClient {

    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;

    /**
     * Sends a PUT request to the Inventory Service to update the ticket capacity for a specific event.
     *
     * <p>This method uses a {@link RestTemplate} to call the remote Inventory Service endpoint at:
     * <code>PUT /event/{eventId}/capacity/{ticketCount}</code>. It is expected that the Inventory Service
     * handles the ticket capacity update on its side.</p>
     *
     * <p>Since no response body is expected from the Inventory Service, this method returns
     * a {@link ResponseEntity} with HTTP status 200 OK and no content ({@code Void}).</p>
     *
     * @param eventId      the ID of the event whose inventory should be updated
     * @param ticketCount  the number of tickets to subtract or update
     * @return a {@link ResponseEntity} with HTTP 200 status indicating the update request was sent
     */
    public ResponseEntity<Void> updateInventory(final Long eventId,
                                                final Long ticketCount) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(inventoryServiceUrl + "/event/" + eventId + "/capacity/" + ticketCount, null);
        return ResponseEntity.ok().build();
    }
}
