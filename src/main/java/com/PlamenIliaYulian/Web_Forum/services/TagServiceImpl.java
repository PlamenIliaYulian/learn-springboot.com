package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.models.Tag;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.TagRepository;
import com.PlamenIliaYulian.Web_Forum.services.contracts.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }


    @Override
    public Tag getTagByName(String name) {
        return null;
    }

    @Override
    public Tag createTag(Tag tag) {
        return tagRepository.createTag(tag);
    }

    @Override
    public void deleteTag(Tag tag) {

    }

    @Override
    public Tag updateTag(Tag tag) {
        return null;
    }

    @Override
    public Set<Tag> getAllTags() {
        return tagRepository.getAllTags();
    }
}
