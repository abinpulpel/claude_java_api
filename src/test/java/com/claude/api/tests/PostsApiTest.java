package com.claude.api.tests;

import com.claude.api.models.Post;
import com.claude.api.utils.DataReader;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThan;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Smoke and regression coverage for the {@code /posts} resource, exercising
 * read, create, update, and delete flows against JSONPlaceholder.
 */
@Story("Posts API")
public class PostsApiTest extends BaseTest {

    @Test(groups = "smoke")
    @Severity(SeverityLevel.BLOCKER)
    @Description("GET /posts returns 200 and a non-empty collection")
    public void getAllPostsReturnsNonEmptyCollection() {
        final Response response = postsEndpoints.getAllPosts();

        response.then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test(groups = "smoke")
    @Severity(SeverityLevel.CRITICAL)
    @Description("GET /posts/{id} returns a payload matching the post JSON schema")
    public void getPostByIdMatchesSchema() {
        final Response response = postsEndpoints.getPostById(1);

        response.then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/post-schema.json"));
    }

    @Test(groups = "regression")
    @Severity(SeverityLevel.NORMAL)
    @Description("GET /posts?userId={id} only returns posts owned by that user")
    public void getPostsByUserIdFiltersCorrectly() {
        final int userId = 1;
        final Response response = postsEndpoints.getPostsByUserId(userId);

        response.then()
                .statusCode(200)
                .body("userId", everyItem(equalTo(userId)));
    }

    @Test(groups = "regression", dataProvider = "postFixtures")
    @Severity(SeverityLevel.CRITICAL)
    @Description("POST /posts creates a post and echoes back the submitted fields")
    public void createPostEchoesSubmittedFields(final Post requestPost) {
        final Response response = postsEndpoints.createPost(requestPost);

        response.then().statusCode(201);

        final Post createdPost = response.as(Post.class);
        assertEquals(createdPost.getUserId(), requestPost.getUserId());
        assertEquals(createdPost.getTitle(), requestPost.getTitle());
        assertEquals(createdPost.getBody(), requestPost.getBody());
        assertTrue(createdPost.getId() != null && createdPost.getId() > 0,
                "Created post should be assigned a positive id");
    }

    @Test(groups = "regression")
    @Severity(SeverityLevel.NORMAL)
    @Description("PUT /posts/{id} replaces the post payload")
    public void updatePostReplacesPayload() {
        final Post updated = Post.builder()
                .id(1)
                .userId(1)
                .title("Updated via claude_java_api")
                .body("Body replaced by the PUT regression test.")
                .build();

        final Response response = postsEndpoints.updatePost(1, updated);

        response.then()
                .statusCode(200)
                .body("title", equalTo(updated.getTitle()));
    }

    @Test(groups = "regression")
    @Severity(SeverityLevel.MINOR)
    @Description("DELETE /posts/{id} succeeds")
    public void deletePostSucceeds() {
        postsEndpoints.deletePost(1).then().statusCode(200);
    }

    /**
     * Supplies data-driven {@link Post} fixtures loaded from
     * {@code testdata/posts_data.json}, demonstrating JSON-backed
     * data-driven execution.
     *
     * @return a 2D array of {@link Post} fixtures for TestNG's data provider contract
     */
    @DataProvider(name = "postFixtures")
    public Object[][] postFixtures() {
        final List<Post> posts = DataReader.readJson("testdata/posts_data.json", Post.class);
        final Object[][] data = new Object[posts.size()][1];
        for (int i = 0; i < posts.size(); i++) {
            data[i][0] = posts.get(i);
        }
        return data;
    }
}
