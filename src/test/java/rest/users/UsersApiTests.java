package rest.users;

import models.UserJson;
import models.UserJsonPostResponse;
import models.UserJsonPutResponse;
import models.UserListResponse;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spec.SpecCustoms;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@Tag("users")
public class UsersApiTests extends rest.TestBase {

    private static final String URL = "/users";
    private static final UserJson NEW_USER = new UserJson("morpheus", "leader");
    private static final UserJson UPDATE_USER = new UserJson("morpheus", "zion resident");
    private static final String UNKNOWN = "/unknown/23";

    @Test
    void shouldReturnCorrectEmailOfSecondUser() {
        UserListResponse response = step("Get users list", () -> given()
                .spec(SpecCustoms.requestSpecification)
                .when()
                .get(URL)
                .then()
                .spec(SpecCustoms.responseSpecificationBuilder(200))
                .extract().as(UserListResponse.class));

        step("Check email", () -> assertEquals(
                "janet.weaver@reqres.in",
                response.getData().get(1).getEmail()
        ));
    }

    @Test
    void sizeOfArrayShouldBeCorrect() {
        UserListResponse response = step("Get users list", () -> given()
                .spec(SpecCustoms.requestSpecification)
                .when()
                .get(URL)
                .then()
                .spec(SpecCustoms.responseSpecificationBuilder(200))
                .extract().as(UserListResponse.class));

        step("Check size", () -> assertEquals(6, response.getData().size()));
    }

    @Test
    void shouldCreateUser() {
        UserJsonPostResponse user = step("Create user", () -> given()
                .spec(SpecCustoms.requestSpecification)
                .body(NEW_USER)
                .when()
                .post(URL)
                .then()
                .spec(SpecCustoms.responseSpecificationBuilder(201))
                .extract().as(UserJsonPostResponse.class));

        step("Verify created user", () -> assertAll(
                () -> assertEquals("morpheus", user.getName()),
                () -> assertEquals("leader", user.getJob()),
                () -> assertNotNull(user.getId()),
                () -> assertNotNull(user.getCreatedAt())
        ));
    }

    @Test
    void shouldUpdateUser() {
        UserJsonPutResponse updated = step("Update user", () -> given()
                .spec(SpecCustoms.requestSpecification)
                .body(UPDATE_USER)
                .when()
                .put(URL + "/2")
                .then()
                .spec(SpecCustoms.responseSpecificationBuilder(200))
                .extract().as(UserJsonPutResponse.class));

        step("Verify updated fields", () -> assertAll(
                () -> assertEquals("morpheus", updated.getName()),
                () -> assertEquals("zion resident", updated.getJob()),
                () -> assertNotNull(updated.getUpdatedAt())
        ));
    }

    @Test
    void shouldDeleteUser() {
        step("Delete user", () -> given()
                .spec(SpecCustoms.requestSpecification)
                .when()
                .delete(URL + "/2")
                .then()
                .spec(SpecCustoms.responseSpecificationBuilder(204))
        );
    }

    @Test
    void shouldReturn404ForUnknownResource() {
        String response = step("Request unknown resource", () -> given()
                .spec(SpecCustoms.requestSpecification)
                .when()
                .get(UNKNOWN)
                .then()
                .spec(SpecCustoms.responseSpecificationBuilder(404))
                .extract().asString());

        step("Check empty body", () -> assertEquals("{}", response));
    }
}
