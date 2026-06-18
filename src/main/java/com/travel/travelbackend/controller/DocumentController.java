package com.travel.travelbackend.controller;

import com.travel.travelbackend.dto.DocumentRequest;
import com.travel.travelbackend.dto.DocumentResponse;
import com.travel.travelbackend.service.DocumentService;
import com.travel.travelbackend.service.StoredDocumentFile;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/documents")
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DocumentResponse create(@Valid @RequestBody DocumentRequest request) {
        return documentService.create(request);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public DocumentResponse upload(
            @RequestParam Long tripId,
            @RequestParam String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String number,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expirationDate,
            @RequestParam MultipartFile file
    ) {
        return documentService.upload(tripId, name, type, number, expirationDate, file);
    }

    @PutMapping("/{id}")
    public DocumentResponse edit(@PathVariable Long id, @Valid @RequestBody DocumentRequest request) {
        return documentService.edit(id, request);
    }

    @GetMapping("/trip/{tripId}")
    public List<DocumentResponse> getByTrip(@PathVariable Long tripId) {
        return documentService.getByTrip(tripId);
    }

    @GetMapping("/{id}/file")
    public ResponseEntity<?> download(@PathVariable Long id) {
        StoredDocumentFile file = documentService.loadFile(id);
        MediaType contentType = file.contentType() == null || file.contentType().isBlank()
                ? MediaType.APPLICATION_OCTET_STREAM
                : MediaType.parseMediaType(file.contentType());

        return ResponseEntity.ok()
                .contentType(contentType)
                .contentLength(file.fileSize() == null ? -1 : file.fileSize())
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename(file.fileName() == null ? "document" : file.fileName())
                        .build()
                        .toString())
                .body(file.resource());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        documentService.delete(id);
    }
}
