package com.claude.api.clients;

import com.claude.api.specs.RequestSpecFactory;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Base HTTP client wrapping Rest Assured's {@code given()} entry point.
 *
 * <p>Endpoint classes (see {@code com.claude.api.endpoints}) compose this
 * class rather than calling Rest Assured directly, keeping request-spec
 * construction and logging in a single, reusable place.</p>
 */
public class BaseApiClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseApiClient.class);

    /**
     * @return a fresh {@link RequestSpecification} pre-configured with the
     * default base URL, content type, and logging behavior.
     */
    protected RequestSpecification request() {
        return RestAssured.given().spec(RequestSpecFactory.defaultSpec());
    }

    /**
     * Performs a GET request.
     *
     * @param path the resource path, relative to the configured base URL
     * @return the raw {@link Response}
     */
    public Response get(final String path) {
        LOGGER.info("GET {}", path);
        return request().when().get(path);
    }

    /**
     * Performs a GET request with query parameters.
     *
     * @param path        the resource path
     * @param queryParams query parameters to append
     * @return the raw {@link Response}
     */
    public Response get(final String path, final Map<String, ?> queryParams) {
        LOGGER.info("GET {} params={}", path, queryParams);
        return request().queryParams(queryParams).when().get(path);
    }

    /**
     * Performs a POST request with a JSON body.
     *
     * @param path the resource path
     * @param body the request payload, serialized as JSON
     * @return the raw {@link Response}
     */
    public Response post(final String path, final Object body) {
        LOGGER.info("POST {}", path);
        return request().body(body).when().post(path);
    }

    /**
     * Performs a PUT request with a JSON body.
     *
     * @param path the resource path
     * @param body the request payload, serialized as JSON
     * @return the raw {@link Response}
     */
    public Response put(final String path, final Object body) {
        LOGGER.info("PUT {}", path);
        return request().body(body).when().put(path);
    }

    /**
     * Performs a PATCH request with a JSON body.
     *
     * @param path the resource path
     * @param body the request payload, serialized as JSON
     * @return the raw {@link Response}
     */
    public Response patch(final String path, final Object body) {
        LOGGER.info("PATCH {}", path);
        return request().body(body).when().patch(path);
    }

    /**
     * Performs a DELETE request.
     *
     * @param path the resource path
     * @return the raw {@link Response}
     */
    public Response delete(final String path) {
        LOGGER.info("DELETE {}", path);
        return request().when().delete(path);
    }
}
