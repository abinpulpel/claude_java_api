package com.claude.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

/**
 * Request/response POJO for the {@code /posts} resource. Uses the Builder
 * pattern to keep test-side construction readable, and Jackson annotations
 * so the model is safely forward-compatible with unknown response fields.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Post {

    private Integer id;
    private Integer userId;
    private String title;
    private String body;

    public Post() {
    }

    private Post(final Builder builder) {
        this.id = builder.id;
        this.userId = builder.userId;
        this.title = builder.title;
        this.body = builder.body;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(final Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    /**
     * @return a new {@link Builder} for fluent {@link Post} construction.
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Post post)) {
            return false;
        }
        return Objects.equals(id, post.id)
                && Objects.equals(userId, post.userId)
                && Objects.equals(title, post.title)
                && Objects.equals(body, post.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, title, body);
    }

    @Override
    public String toString() {
        return "Post{id=%s, userId=%s, title='%s', body='%s'}".formatted(id, userId, title, body);
    }

    /** Fluent builder for {@link Post}. */
    public static final class Builder {
        private Integer id;
        private Integer userId;
        private String title;
        private String body;

        private Builder() {
        }

        public Builder id(final Integer value) {
            this.id = value;
            return this;
        }

        public Builder userId(final Integer value) {
            this.userId = value;
            return this;
        }

        public Builder title(final String value) {
            this.title = value;
            return this;
        }

        public Builder body(final String value) {
            this.body = value;
            return this;
        }

        public Post build() {
            return new Post(this);
        }
    }
}
