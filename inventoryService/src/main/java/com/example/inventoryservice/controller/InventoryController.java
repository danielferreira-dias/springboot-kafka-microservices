package com.example.inventoryservice.controller;

import com.example.inventoryservice.response.VenueInvetoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.inventoryservice.response.EventInventoryResponse;
import com.example.inventoryservice.service.InventoryService;

import java.util.List;

// This annotation is a specialized controller used to build RESTful web services.
// This annotation is used to map HTTP requests to handler methods or entire classes.
@RestController
@RequestMapping("/api/v1")
public class InventoryController {
    private InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService){
        this.inventoryService = inventoryService;
    }

    // @ResponseBody Tells Spring to serialize the return value (which is a Java object)
    // directly into the HTTP response body — typically as JSON.
    @GetMapping("/inventory/events")
    public @ResponseBody List<EventInventoryResponse> inventoryGetAllEvents(){
        return inventoryService.getAllEvents();
    }

    @GetMapping("/inventory/venue/{venueId}")
    public @ResponseBody VenueInvetoryResponse inventoryByVenueId(@PathVariable("venueId") Long venueId){
        /*
            This means the return value of the method (VenueInvetoryResponse)
            will be automatically converted to JSON and returned in the HTTP response.
         */
        return inventoryService.getVenueInformation(venueId);
    }

    @GetMapping("/inventory/event/{eventId}")
    public @ResponseBody EventInventoryResponse inventoryByEventId(@PathVariable("eventId") Long eventId){
        return inventoryService.getEventInventory(eventId);
    }
}
