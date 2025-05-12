package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.request.TagCreationRequest;
import com.ecommerce_app.dto.request.TagUpdateRequest;
import com.ecommerce_app.dto.response.TagResponse;

import java.util.List;
import java.util.UUID;

public interface TagService {
    TagResponse createTag(TagCreationRequest request);
    TagResponse getTagById(UUID id);
    List<TagResponse> getAllTags();
    TagResponse updateTag(UUID id, TagUpdateRequest request);
    void deleteTag(UUID id);
}
