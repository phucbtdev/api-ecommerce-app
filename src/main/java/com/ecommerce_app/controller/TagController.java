package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.TagCreationRequest;
import com.ecommerce_app.dto.request.TagUpdateRequest;
import com.ecommerce_app.dto.response.TagResponse;
import com.ecommerce_app.service.interfaces.TagService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public ResponseEntity<TagResponse> createTag(@Valid @RequestBody TagCreationRequest request) {
        TagResponse response = tagService.createTag(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagResponse> getTag(@PathVariable UUID id) {
        TagResponse response = tagService.getTagById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TagResponse>> getAllTags() {
        List<TagResponse> responses = tagService.getAllTags();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagResponse> updateTag(@PathVariable UUID id, @Valid @RequestBody TagUpdateRequest request) {
        TagResponse response = tagService.updateTag(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
