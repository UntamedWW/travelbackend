package com.travel.travelbackend.controller;

import com.travel.travelbackend.dto.ItineraryItemRequest;
import com.travel.travelbackend.dto.ItineraryItemResponse;
import com.travel.travelbackend.service.ItineraryItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/itinerary-items")
public class ItineraryItemController {

    private final ItineraryItemService itineraryItemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItineraryItemResponse create(@Valid @RequestBody ItineraryItemRequest request) {
        return itineraryItemService.create(request);
    }

    @PutMapping("/{id}")
    public ItineraryItemResponse edit(@PathVariable Long id, @Valid @RequestBody ItineraryItemRequest request) {
        return itineraryItemService.edit(id, request);
    }

    @GetMapping("/trip/{tripId}")
    public List<ItineraryItemResponse> getByTrip(@PathVariable Long tripId) {
        return itineraryItemService.getByTrip(tripId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        itineraryItemService.delete(id);
    }
}
