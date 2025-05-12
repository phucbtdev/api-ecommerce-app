package com.ecommerce_app.service.implement;

import com.ecommerce_app.dto.request.TagCreationRequest;
import com.ecommerce_app.dto.request.TagUpdateRequest;
import com.ecommerce_app.dto.response.TagResponse;
import com.ecommerce_app.entity.Tag;
import com.ecommerce_app.exception.EntityNotFoundException;
import com.ecommerce_app.mapper.TagMapper;
import com.ecommerce_app.repository.TagRepository;
import com.ecommerce_app.service.interfaces.TagService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public TagServiceImpl(TagRepository tagRepository, TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
    }

    @Override
    public TagResponse createTag(TagCreationRequest request) {
        if (tagRepository.findByName(request.getName()).isPresent()) {
            throw new IllegalArgumentException("Tag with name " + request.getName() + " already exists");
        }
        Tag tag = tagMapper.toEntity(request);
        tag = tagRepository.save(tag);
        return tagMapper.toResponse(tag);
    }

    @Override
    public TagResponse getTagById(UUID id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found with id: " + id));
        return tagMapper.toResponse(tag);
    }

    @Override
    public List<TagResponse> getAllTags() {
        return tagRepository.findAll().stream()
                .map(tagMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TagResponse updateTag(UUID id, TagUpdateRequest request) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found with id: " + id));
        tagMapper.updateEntity(tag, request);
        tag = tagRepository.save(tag);
        return tagMapper.toResponse(tag);
    }

    @Override
    public void deleteTag(UUID id) {
        if (!tagRepository.existsById(id)) {
            throw new EntityNotFoundException("Tag not found with id: " + id);
        }
        tagRepository.deleteById(id);
    }
}
