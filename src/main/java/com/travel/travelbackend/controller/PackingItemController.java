package com.travel.travelbackend.controller;

import com.travel.travelbackend.dto.PackingItemRequest;
import com.travel.travelbackend.dto.PackingItemPackedRequest;
import com.travel.travelbackend.dto.PackingItemResponse;
import com.travel.travelbackend.service.PackingItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/packing-items")
public class PackingItemController {

    private final PackingItemService packingItemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PackingItemResponse create(@RequestBody PackingItemRequest request) {
        return packingItemService.create(request);
    }

    @PutMapping("/{id}")
    public PackingItemResponse edit(@PathVariable Long id, @RequestBody PackingItemRequest request) {
        return packingItemService.edit(id, request);
    }

    @PatchMapping("/{id}/packed")
    public PackingItemResponse updatePacked(@PathVariable Long id, @RequestBody PackingItemPackedRequest request) {
        return packingItemService.updatePacked(id, request.isPacked());
    }

    @GetMapping("/trip/{tripId}")
    public List<PackingItemResponse> getByTrip(@PathVariable Long tripId) {
        return packingItemService.getByTrip(tripId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        packingItemService.delete(id);
    }
}
