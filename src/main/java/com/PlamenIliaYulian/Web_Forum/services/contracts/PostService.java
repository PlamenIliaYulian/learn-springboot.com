package com.PlamenIliaYulian.Web_Forum.services.contracts;

import com.PlamenIliaYulian.Web_Forum.models.*;

import java.util.List;

public interface PostService {
    /*📌 July 📌- DONE - last updated 25.01.2024*/
    Post createPost(Post post, User authorizedUser);

    /*Plamen*/
    void deletePost(Post post, User authorizedUser);

    /*✔ Iliya ✔*/
    Post updatePost(Post post, User authorizedUser);

    /*📌 July 📌- DONE - last updated 26.01.2024*/
    List<Post> getAllPosts(User userExecutingTheRequest, PostFilterOptions postFilterOptions);

    /*Plamen*/
    Post getPostByTitle(String title);

    /*✔ Iliya ✔*/
    Post getPostById(int id);

    /*📌 July 📌- DONE - last updated 25.01.2024*/
    Post likePost(Post post, User authorizedUser);

    /*Plamen*/
    Post dislikePost(Post post, User authorizedUser);

    /*✔ Iliya ✔*/
    Post addTagToPost(Post post, Tag tag, User authorizedUser);

    /*📌 July 📌- DONE - last updated 25.01.2024*/
    Post removeTagFromPost(Post post, Tag tag, User authorizedUser);

    /*Plamen*/
    Post addCommentToPost(Post postToComment, Comment commentToBeAdded, User userWhoComments);

    /*✔ Iliya ✔*/
    List<Comment> getAllCommentsRelatedToPost(Post postWithComments);

    /*📌 July 📌- DONE - last updated 25.01.2025*/
    Post removeCommentFromPost(Post postToComment, int commentId, User authorizedUser);
}
