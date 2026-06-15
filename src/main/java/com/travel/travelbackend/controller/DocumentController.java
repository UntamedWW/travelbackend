package com.travel.travelbackend.controller;

import com.travel.travelbackend.dto.DocumentRequest;
import com.travel.travelbackend.dto.DocumentResponse;
import com.travel.travelbackend.service.DocumentService;
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
@RequestMapping("api/documents")
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DocumentResponse create(@RequestBody DocumentRequest request) {
        return documentService.create(request);
    }

    @PutMapping("/{id}")
    public DocumentResponse edit(@PathVariable Long id, @RequestBody DocumentRequest request) {
        return documentService.edit(id, request);
    }

    @GetMapping("/trip/{tripId}")
    public List<DocumentResponse> getByTrip(@PathVariable Long tripId) {
        return documentService.getByTrip(tripId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        documentService.delete(id);
    }
}
