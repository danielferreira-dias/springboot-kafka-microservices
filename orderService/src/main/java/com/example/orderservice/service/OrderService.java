package com.example.orderservice.service;

import com.example.bookingservice.event.BookingEvent;
import com.example.orderservice.client.InventoryServiceClient;
import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


/**
 * Service that handles incoming booking events from Kafka and processes them into persisted orders.
 *
 * <p>This service listens on the {@code booking} Kafka topic. Upon receiving a booking event,
 * it performs the following:
 * <ul>
 *   <li>Creates and saves an {@link Order} entity in the database</li>
 *   <li>Calls the Inventory Service to update the number of available tickets</li>
 *   <li>Logs the entire workflow for observability</li>
 * </ul>
 * </p>
 *
 * <p>It forms part of an event-driven architecture using Apache Kafka for inter-service communication.</p>
 */
@Service
@Slf4j
public class OrderService {

    private OrderRepository orderRepository;
    private InventoryServiceClient inventoryServiceClient;

    /**
     * Constructs the OrderService with required dependencies.
     *
     * @param orderRepository the JPA repository used for order persistence
     * @param inventoryServiceClient HTTP client used to update inventory after an order is placed
     */
    @Autowired
    public OrderService(OrderRepository orderRepository,  InventoryServiceClient inventoryServiceClient) {
        this.orderRepository = orderRepository;
        this.inventoryServiceClient = inventoryServiceClient;
    }

    /**
     * Kafka listener method that is triggered when a new booking event is published.
     * Converts the event into an Order, saves it to the database, and updates inventory.
     *
     * @param bookingEvent the event representing a completed booking
     */
    @KafkaListener(topics = "booking", groupId = "order-service")
    public void orderEvent(BookingEvent bookingEvent) {
        log.info("Received order event: {}", bookingEvent);
        // Create Order object for DB
        Order order = createOrder(bookingEvent);
        orderRepository.saveAndFlush(order);

        // Update Inventory
        inventoryServiceClient.updateInventory(order.getEventId(), order.getTicketCount());
        log.info("Inventory updated for event: {}, less tickets: {}", order.getEventId(), order.getTicketCount());
    }


    /**
     * Converts a {@link BookingEvent} into a persistable {@link Order} entity.
     *
     * @param bookingEvent the Kafka event payload
     * @return a new {@link Order} entity to be saved
     */
    private  Order createOrder(BookingEvent bookingEvent){
        return Order.builder()
                .customerId(bookingEvent.getUserId())
                .eventId(bookingEvent.getEventId())
                .ticketCount(bookingEvent.getTicketCount())
                .totalPrice(bookingEvent.getTotalPrice())
                .build();
    }
}
