package com.claude.api.tests;

import com.claude.api.utils.DataReader;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.testng.Assert.assertEquals;

/**
 * Smoke and regression coverage for the {@code /users} resource.
 */
@Story("Users API")
public class UsersApiTest extends BaseTest {

    @Test(groups = "smoke")
    @Severity(SeverityLevel.BLOCKER)
    @Description("GET /users returns 200 and at least 10 seeded users")
    public void getAllUsersReturnsSeededCollection() {
        usersEndpoints.getAllUsers().then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(10));
    }

    @Test(groups = "smoke")
    @Severity(SeverityLevel.CRITICAL)
    @Description("GET /users/{id} returns a payload matching the user JSON schema")
    public void getUserByIdMatchesSchema() {
        usersEndpoints.getUserById(1).then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/user-schema.json"));
    }

    @Test(groups = "regression", dataProvider = "userFixtures")
    @Severity(SeverityLevel.NORMAL)
    @Description("GET /users/{id} returns the expected username, driven by CSV fixture data")
    public void getUserByIdReturnsExpectedUsername(final int userId, final String expectedUsername) {
        final Response response = usersEndpoints.getUserById(userId);

        response.then().statusCode(200);
        assertEquals(response.jsonPath().getString("username"), expectedUsername);
    }

    /**
     * Supplies data-driven fixtures loaded from
     * {@code testdata/users_data.csv}, demonstrating CSV-backed data-driven
     * execution.
     *
     * @return a 2D array of {@code [userId, expectedUsername]} pairs
     */
    @DataProvider(name = "userFixtures")
    public Object[][] userFixtures() {
        final List<Map<String, String>> rows = DataReader.readCsv("testdata/users_data.csv");
        final Object[][] data = new Object[rows.size()][2];
        for (int i = 0; i < rows.size(); i++) {
            final Map<String, String> row = rows.get(i);
            data[i][0] = Integer.parseInt(row.get("userId"));
            data[i][1] = row.get("expectedUsername");
        }
        return data;
    }
}
