package com.claude.api.endpoints;

import com.claude.api.clients.BaseApiClient;
import com.claude.api.models.Post;
import io.restassured.response.Response;

/**
 * Resource-oriented gateway to the {@code /posts} endpoint family.
 *
 * <p>Test classes call these methods rather than building raw HTTP requests,
 * so that endpoint paths and payload shapes are defined in exactly one
 * place (Repository pattern).</p>
 */
public class PostsEndpoints extends BaseApiClient {

    private static final String BASE_PATH = "/posts";

    /**
     * @return all posts.
     */
    public Response getAllPosts() {
        return get(BASE_PATH);
    }

    /**
     * @param postId the post identifier
     * @return the post matching {@code postId}
     */
    public Response getPostById(final int postId) {
        return get(BASE_PATH + "/" + postId);
    }

    /**
     * @param userId the owning user's identifier
     * @return posts authored by {@code userId}
     */
    public Response getPostsByUserId(final int userId) {
        return get(BASE_PATH, java.util.Map.of("userId", userId));
    }

    /**
     * @param post the post payload to create
     * @return the raw creation response
     */
    public Response createPost(final Post post) {
        return post(BASE_PATH, post);
    }

    /**
     * @param postId the post identifier to update
     * @param post   the replacement payload
     * @return the raw update response
     */
    public Response updatePost(final int postId, final Post post) {
        return put(BASE_PATH + "/" + postId, post);
    }

    /**
     * @param postId the post identifier to partially update
     * @param fields a partial payload, e.g. {@code Map.of("title", "New title")}
     * @return the raw update response
     */
    public Response patchPost(final int postId, final Object fields) {
        return patch(BASE_PATH + "/" + postId, fields);
    }

    /**
     * @param postId the post identifier to delete
     * @return the raw deletion response
     */
    public Response deletePost(final int postId) {
        return delete(BASE_PATH + "/" + postId);
    }
}
