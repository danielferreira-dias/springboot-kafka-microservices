package com.example.inventoryservice.service;

import com.example.inventoryservice.entity.Event;
import com.example.inventoryservice.entity.Venue;
import com.example.inventoryservice.repository.EventRepository;
import com.example.inventoryservice.repository.VenueRepository;
import com.example.inventoryservice.response.VenueInvetoryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.inventoryservice.response.EventInventoryResponse;
import java.util.List;
import java.util.stream.Collectors;


// Services classes are responsibility to handle Bussiness Logic
@Slf4j
@Service
public class InventoryService {

    // Each table in database should have a repository class
    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;

    @Autowired
    public InventoryService(final EventRepository eventRepository, final VenueRepository venueRepository){
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
    }

    public List<EventInventoryResponse> getAllEvents(){
        /**
         * Retrieves all events from the database and maps them to response DTOs.
         *
         * @return a list of {@link EventInventoryResponse} containing the event name,
         *         remaining capacity, and venue information for each event.
         */
        final List<Event> events = eventRepository.findAll();

        return events.stream().map(event -> EventInventoryResponse.builder()
                .event(event.getName())
                .capacity(event.getLeftCapacity())
                .venue(event.getVenue())
                .build()).collect(Collectors.toList());
    };

    public VenueInvetoryResponse getVenueInformation(final Long venueId){
        /**
         * Fetches venue information by ID and maps it to a response DTO.
         *
         * @param venueId the ID of the venue to retrieve
         * @return a {@link VenueInvetoryResponse} containing the venue's ID, name, and total capacity,
         *         or {@code null} if the venue was not found.
         */
        final Venue venue = venueRepository.findById(venueId).orElse(null);

        return VenueInvetoryResponse.builder()
                .venueId(venue.getId())
                .venueName(venue.getName())
                .totalCapacity(venue.getTotalCapacity())
                .build();

    }

    public EventInventoryResponse getEventInventory(final Long eventId){
        final Event event = eventRepository.findById(eventId).orElse(null);
        return EventInventoryResponse.builder()
                .event(event.getName())
                .capacity(event.getLeftCapacity())
                .venue(event.getVenue())
                .ticketPrice(event.getTicketPrice())
                .eventId(event.getId())
                .build();
    }

    public void updateEventCapacity(final Long eventId, final Long ticketsBooked){
        final Event event = eventRepository.findById(eventId).orElse(null);
        event.setLeftCapacity(event.getLeftCapacity() - ticketsBooked);
        eventRepository.saveAndFlush(event);
        log.info("Event {} has been updated", event.getName());
    }
}
