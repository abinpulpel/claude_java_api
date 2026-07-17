package com.claude.api.endpoints;

import com.claude.api.clients.BaseApiClient;
import io.restassured.response.Response;

/**
 * Resource-oriented gateway to the {@code /users} endpoint family.
 */
public class UsersEndpoints extends BaseApiClient {

    private static final String BASE_PATH = "/users";

    /**
     * @return all users.
     */
    public Response getAllUsers() {
        return get(BASE_PATH);
    }

    /**
     * @param userId the user identifier
     * @return the user matching {@code userId}
     */
    public Response getUserById(final int userId) {
        return get(BASE_PATH + "/" + userId);
    }

    /**
     * @param username the username to filter by
     * @return users matching {@code username}
     */
    public Response getUserByUsername(final String username) {
        return get(BASE_PATH, java.util.Map.of("username", username));
    }
}
