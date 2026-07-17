package com.claude.api.tests;

import com.claude.api.config.ConfigManager;
import com.claude.api.endpoints.PostsEndpoints;
import com.claude.api.endpoints.UsersEndpoints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;

/**
 * Base class for all API test classes. Instantiates the shared endpoint
 * gateways once per test class and logs the resolved environment, so
 * individual test classes stay focused on assertions.
 */
public abstract class BaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseTest.class);

    protected PostsEndpoints postsEndpoints;
    protected UsersEndpoints usersEndpoints;

    @BeforeClass(alwaysRun = true)
    public void setUpBaseTest() {
        LOGGER.info("Running against environment: {}", ConfigManager.getInstance().getEnvironment());
        postsEndpoints = new PostsEndpoints();
        usersEndpoints = new UsersEndpoints();
    }
}
