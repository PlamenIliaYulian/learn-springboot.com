package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.models.Post;
import com.PlamenIliaYulian.Web_Forum.models.Tag;
import com.PlamenIliaYulian.Web_Forum.models.User;

import java.util.List;

public interface PostService {
    /*TODO July*/
    Post createPost(Post post, User authorizedUser);
    /*TODO Plamen*/
    void deletePost(Post post, User authorizedUser);
    /*TODO Iliya*/
    Post updatePost(Post post, User authorizedUser);
    /*TODO July*/
    /*TODO implement Post FilterOptions*/
    List<Post> getAllPosts();
    /*TODO Plamen*/
    Post getPostByTitle(String title);
    /*TODO Iliya*/
    Post getPostById(int id);
    /*TODO July*/
    Post likePost(Post post, User authorizedUser);
    /*TODO Plamen*/
    Post dislikePost(Post post, User authorizedUser);
    /*TODO Iliya*/
    Post addTagToPost(Post post, Tag tag);
    /*TODO July*/
    Post removeTagToPost(Post post, Tag tag);
}
