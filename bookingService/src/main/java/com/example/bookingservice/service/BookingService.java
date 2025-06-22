package com.example.bookingservice.service;
import com.example.bookingservice.client.InventoryServiceClient;
import com.example.bookingservice.entity.Customer;
import com.example.bookingservice.repository.CustomerRepository;
import com.example.bookingservice.request.BookingRequest;
import com.example.bookingservice.response.BookingResponse;
import com.example.bookingservice.response.InventoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    private final CustomerRepository customerRepository;
    private final InventoryServiceClient inventoryServiceClient;

    @Autowired
    public BookingService(final CustomerRepository customerRepository, final InventoryServiceClient inventoryServiceClient) {
        this.customerRepository = customerRepository;
        this.inventoryServiceClient = inventoryServiceClient;
    }

    public BookingResponse createBooking(final BookingRequest request){
        // Check if User exists
        final Customer customer = customerRepository.findById(request.getUserId()).orElse(null);
        if(customer == null){
            throw new RuntimeException("Customer not found");
        }

        final InventoryResponse inventoryResponse = inventoryServiceClient.getInventory(request.getEventId());
        System.out.println("Inventory Service Response" + inventoryResponse);



        return BookingResponse.builder().build();
    }



}
