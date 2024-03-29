package com.PlamenIliaYulian.Web_Forum.controllers.helpers;

import com.PlamenIliaYulian.Web_Forum.controllers.helpers.contracts.ModelsMapper;
import com.PlamenIliaYulian.Web_Forum.models.*;
import com.PlamenIliaYulian.Web_Forum.models.dtos.*;
import com.PlamenIliaYulian.Web_Forum.services.contracts.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class ModelsMapperImpl implements ModelsMapper {

    private final TagService tagService;

    private final CommentService commentService;

    private final PostService postService;

    private final UserService userService;
    private final AvatarService avatarService;
    private final RoleService roleService;

    @Autowired
    public ModelsMapperImpl(TagService tagService,
                            CommentService commentService,
                            PostService postService,
                            UserService userService,
                            AvatarService avatarService,
                            RoleService roleService) {
        this.tagService = tagService;
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
        this.avatarService = avatarService;
        this.roleService = roleService;
    }


    @Override
    public Post postFromDto(PostDto postDto, Post postWeGotFromTitle) {
        postWeGotFromTitle.setContent(postDto.getContent());
        postWeGotFromTitle.setTitle(postDto.getTitle());
        return postWeGotFromTitle;
    }
    @Override
    public Post postFromDto(PostDto postDto, int id) {
        Post post = postService.getPostById(id);
        post.setContent(postDto.getContent());
        post.setTitle(postDto.getTitle());
        return post;
    }

    @Override
    public User userFromAdministrativeDto(UserAdministrativeDto userAdministrativeDto, String username) {
        User user = userService.getUserByUsername(username);
        user.setBlocked(userAdministrativeDto.isBlocked());
        user.setDeleted(userAdministrativeDto.isDeleted());
        user.setRoles(userAdministrativeDto.getRoles());
        return user;
    }

    @Override
    public User userFromAdministrativeDto(UserAdministrativeDto userAdministrativeDto, int id) {
        User user = userService.getUserById(id);
        user.setBlocked(userAdministrativeDto.isBlocked());
        user.setDeleted(userAdministrativeDto.isDeleted());
        if (userAdministrativeDto.getRoles() != null && !userAdministrativeDto.getRoles().isEmpty()) {
            user.setRoles(userAdministrativeDto.getRoles());
        } else {
            Set<Role> roleSet = new HashSet<>();
            roleSet.add(roleService.getRoleById(3));
            user.setRoles(roleSet);
        }
        return user;
    }

    @Override
    public User userFromDtoUpdate(UserDtoUpdate userDtoUpdate, String username) {
        User user = userService.getUserByUsername(username);
        user.setFirstName(userDtoUpdate.getFirstName());
        user.setLastName(userDtoUpdate.getLastName());
        user.setEmail(userDtoUpdate.getEmail());
        user.setPassword(userDtoUpdate.getPassword());
        return user;
    }

    @Override
    public User userFromDtoUpdate(UserDtoUpdate userDtoUpdate, int id) {
        User user = userService.getUserById(id);
        user.setFirstName(userDtoUpdate.getFirstName());
        user.setLastName(userDtoUpdate.getLastName());
        user.setEmail(userDtoUpdate.getEmail());
        user.setPassword(userDtoUpdate.getPassword());
        return user;
    }

    @Override
    public User userFromMvcDtoUpdate(UserMvcDtoUpdate userMvcDtoUpdate, int id) {
        User user = userService.getUserById(id);
        user.setFirstName(userMvcDtoUpdate.getFirstName());
        user.setLastName(userMvcDtoUpdate.getLastName());
        user.setEmail(userMvcDtoUpdate.getEmail());
        user.setPassword(userMvcDtoUpdate.getPassword());
        user.setPhoneNumber(userMvcDtoUpdate.getPhoneNumber());
        return user;
    }

    @Override
    public UserFilterOptions userFilterOptionsFromDto(UserFilterOptionsDto dto) {
        return new UserFilterOptions(
                dto.getUsername(),
                dto.getEmail(),
                dto.getFirstName(),
                dto.getSortBy(),
                dto.getSortOrder());
    }

    @Override
    public PostFilterOptions postFilterOptionsFromDto(PostFilterOptionsDto dto) {
        return new PostFilterOptions(
                dto.getMinLikes(),
                dto.getMinDislikes(),
                dto.getTitle(),
                dto.getContent(),
                dto.getCreatedBefore(),
                dto.getCreatedAfter(),
                dto.getCreatedBy(),
                dto.getSortBy(),
                dto.getSortOrder(),
                dto.getTag()
        );
    }

    @Override
    public UserFilterOptions userFilterOptionsFromUsernameOptionsDto(UserFilterByUsernameOptionsDto dto) {
        return new UserFilterOptions(dto.getUsername(),
                null,
                null,
                null,
                null);

    }

    @Override
    public User userFromRegisterDto(RegisterDto registerDto) {
        User user = new User();
        user.setUserName(registerDto.getUsername());
        user.setPassword(registerDto.getPassword());
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setAvatar(avatarService.getDefaultAvatar());
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(roleService.getRoleById(2));
        user.setRoles(roleSet);
        return user;
    }

    @Override
    public CommentDto commentDtoFromComment(Comment commentToBeEdited) {
        CommentDto commentDto = new CommentDto();
        commentDto.setComment(commentToBeEdited.getContent());
        return commentDto;
    }

    @Override
    public UserMvcDtoUpdate userMvcDtoFromUser(User userById) {
        UserMvcDtoUpdate userMvcDtoUpdate = new UserMvcDtoUpdate();
        userMvcDtoUpdate.setFirstName(userById.getFirstName());
        userMvcDtoUpdate.setLastName(userById.getLastName());
        userMvcDtoUpdate.setEmail(userById.getEmail());
        userMvcDtoUpdate.setPhoneNumber(userById.getPhoneNumber());
        return userMvcDtoUpdate;
    }

    @Override
    public UserAdministrativeDto userAdministrativeDtoFromUser(User userById) {
        UserAdministrativeDto userAdministrativeDto = new UserAdministrativeDto();
        userAdministrativeDto.setBlocked(userById.isBlocked());
        userAdministrativeDto.setDeleted(userById.isDeleted());
        userAdministrativeDto.setRoles(userById.getRoles());
        return userAdministrativeDto;
    }

    @Override
    public User userFromUserAdministrativeDto(UserAdministrativeDto userAdministrativeDto, int id) {
        User user = userService.getUserById(id);
        user.setBlocked(userAdministrativeDto.isBlocked());
        user.setDeleted(userAdministrativeDto.isDeleted());
        user.setRoles(userAdministrativeDto.getRoles());
        return user;
    }

    @Override
    public UserMvcAdminChangesDto userMvcAdminChangesDtoFromUser(User userById) {
        UserMvcAdminChangesDto userMvcAdminChangesDto = new UserMvcAdminChangesDto();
        userMvcAdminChangesDto.setBlocked(userById.isBlocked());
        userMvcAdminChangesDto.setDeleted(userById.isDeleted());
        return userMvcAdminChangesDto;
    }

    @Override
    public User userFromUserMvcAdminChangesDto(UserMvcAdminChangesDto userMvcAdminChangesDto, int userId) {
        User user = userService.getUserById(userId);
        user.setBlocked(userMvcAdminChangesDto.isBlocked());
        user.setDeleted(userMvcAdminChangesDto.isDeleted());
        return user;
    }

    @Override
    public Role roleFromRoleDto(RoleDto roleDto) {
        return roleService.getRoleById(roleDto.getDtoRoleId());
    }

    @Override
    public Post postFromDto(PostDto postDto) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        return post;

    }

    @Override
    public Comment commentFromDto(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setContent(commentDto.getComment());
        return comment;
    }

    @Override
    public Comment commentFromDto(CommentDto commentDto, String content) {
        Comment comment = commentService.getCommentByContent(content);
        comment.setContent(commentDto.getComment());
        return comment;
    }

    @Override
    public Comment commentFromDto(CommentDto commentDto, int id) {
        Comment comment = commentService.getCommentById(id);
        comment.setContent(commentDto.getComment());
        return comment;
    }

    @Override
    public Tag tagFromDto(TagDto tagDto) {
        Tag tag = new Tag();
        tag.setTag(tagDto.getTag().toLowerCase());
        return tag;
    }

    @Override
    public Tag tagFromDto(TagDto tagDto, String name) {
        Tag tag = tagService.getTagByName(name);
        tag.setTag(tagDto.getTag().toLowerCase());
        return tag;
    }

    @Override
    public Tag tagFromDto(TagDto tagDto, int id) {
        Tag tag = tagService.getTagById(id);
        tag.setTag(tagDto.getTag().toLowerCase());
        return tag;
    }

    @Override
    public User userFromDtoUpdate(UserDto userDto, int id) {
        User user = userService.getUserById(id);
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setUserName(userDto.getUserName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        return user;
    }

    @Override
    public PostDto postDtoFromPost(Post post) {
        PostDto postDto = new PostDto();
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        return postDto;
    }

    @Override
    public User userFromDto(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setUserName(userDto.getUserName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAvatar(avatarService.getDefaultAvatar());
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(roleService.getRoleById(2));
        user.setRoles(roleSet);
        return user;
    }


}
