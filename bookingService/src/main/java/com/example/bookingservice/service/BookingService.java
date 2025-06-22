package com.example.bookingservice.service;
import com.example.bookingservice.client.InventoryServiceClient;
import com.example.bookingservice.entity.Customer;
import com.example.bookingservice.event.BookingEvent;
import com.example.bookingservice.repository.CustomerRepository;
import com.example.bookingservice.request.BookingRequest;
import com.example.bookingservice.response.BookingResponse;
import com.example.bookingservice.response.InventoryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class BookingService {

    private final CustomerRepository customerRepository;
    private final InventoryServiceClient inventoryServiceClient;
    private final KafkaTemplate<Object, BookingEvent> kafkaTemplate;

    @Autowired
    public BookingService(final CustomerRepository customerRepository,
                          final InventoryServiceClient inventoryServiceClient,
                          KafkaTemplate<Object, BookingEvent> kafkaTemplate) {
        this.customerRepository = customerRepository;
        this.inventoryServiceClient = inventoryServiceClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    public BookingResponse createBooking(final BookingRequest request){
        // Check if User exists
        final Customer customer = customerRepository.findById(request.getUserId()).orElse(null);
        if(customer == null){
            throw new RuntimeException("Customer not found");
        }

        // Check if there is enough inventory
        final InventoryResponse inventoryResponse = inventoryServiceClient.getInventory(request.getEventId());
        log.info(inventoryResponse.toString());
        if (request.getTicketCount() > inventoryResponse.getCapacity()){
            throw new RuntimeException("Not enough inventory");
        }
        System.out.println("Inventory Service Response" + inventoryResponse);

        // create booking
        final BookingEvent bookingEvent = createBookingEvent(request, customer, inventoryResponse);
        kafkaTemplate.send("booking", bookingEvent);
        log.info("Booking Event sent to Kafka Topic");

        return BookingResponse.builder()
                .userId(bookingEvent.getUserId())
                .eventId(bookingEvent.getEventId())
                .ticketCount(bookingEvent.getTicketCount())
                .getTotalPrice(bookingEvent.getTotalPrice())
                .build();

    }

    private BookingEvent createBookingEvent(BookingRequest request, Customer customer, InventoryResponse inventoryResponse) {
        return BookingEvent.builder()
                .userId(customer.getId())
                .eventId(request.getEventId())
                .ticketCount(request.getTicketCount())
                .totalPrice(inventoryResponse.getTicketPrice().multiply(BigDecimal.valueOf(request.getTicketCount())))
                .build();
    }


}
