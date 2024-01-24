package com.PlamenIliaYulian.Web_Forum.controllers;

import com.PlamenIliaYulian.Web_Forum.helpers.AuthenticationHelper;
import com.PlamenIliaYulian.Web_Forum.helpers.contracts.ModelsMapper;
import com.PlamenIliaYulian.Web_Forum.models.Tag;
import com.PlamenIliaYulian.Web_Forum.models.User;
import com.PlamenIliaYulian.Web_Forum.models.dtos.TagDto;
import com.PlamenIliaYulian.Web_Forum.services.contracts.TagService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/tags")
public class TagRestController {

   private final TagService tagService;
   private final AuthenticationHelper authenticationHelper;

   private final ModelsMapper modelsMapper;


    public TagRestController(TagService tagService, AuthenticationHelper authenticationHelper, ModelsMapper modelsMapper) {
        this.tagService = tagService;
        this.authenticationHelper = authenticationHelper;
        this.modelsMapper = modelsMapper;
    }

    @GetMapping
    public Set<Tag> getAllTags (@RequestHeader HttpHeaders headers) {
        User user = authenticationHelper.tryGetUser(headers);
        return tagService.getAllTags();
    }

    @GetMapping("/{tagName}")
    public Tag getTagByName(@PathVariable String tagName,
                            @RequestHeader HttpHeaders headers) {

        User user = authenticationHelper.tryGetUser(headers);
        return tagService.getTagByName(tagName);
    }
    @PostMapping()
    public Tag createTag(@RequestBody TagDto tagDto,
                         @RequestHeader HttpHeaders headers) {
        User user = authenticationHelper.tryGetUser(headers);
        Tag tag = modelsMapper.tagFromDto(tagDto);
        return tagService.createTag(tag);
    }
    @DeleteMapping("/{tagName}")
    public void deleteTag(@RequestHeader HttpHeaders headers,
                          @PathVariable String tagName) {
        User user = authenticationHelper.tryGetUser(headers);
        Tag tag = tagService.getTagByName(tagName);
        tagService.deleteTag(tag);
    }
    @PutMapping("/{tagName}")
    public Tag updateTag(@RequestHeader HttpHeaders headers,
                         @PathVariable String tagName) {
        User user = authenticationHelper.tryGetUser(headers);
        Tag tag = tagService.getTagByName(tagName);
        return tagService.updateTag(tag);
    }
}