import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class ReqresTests {

    static Header header;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://reqres.in/api";
        header = new Header("x-api-key", "reqres_0f50984334cd42ac8b996d73df03cff2");
    }

    // 1. Проверяем GET /api/users?page=2
    @Test
    void getUsersTest() {
        given()
                .header(header)
                .log().uri()
                .log().headers()
                .when()
                .get("/users?page=2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("page", equalTo(2));
    }

    // 2. Проверяем GET /api/users/2
    @Test
    void getUserByIdTest() {
        given()
                .header(header)
                .log().uri()
                .log().headers()
                .when()
                .get("/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.id", equalTo(2));
    }

    // 3. Проверяем успешную POST /api/register
    @Test
    void registerUserTest() {
        String body = """
                    {
                        "email": "eve.holt@reqres.in",
                        "password": "pistol"
                    }
                """;

        given()
                .header(header)
                .contentType(ContentType.JSON)
                .body(body)
                .log().uri()
                .log().body()
                .when()
                .post("/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", notNullValue());
    }


    // 4. Проверяем неуспешную POST /api/register
    @Test
    void unsuccessfulRegisterTest() {
        String body = """
                {
                    "email": "sydney@fife"
                }
                """;

        given()
                .header(header)
                .contentType(ContentType.JSON)
                .body(body)
                .log().uri()
                .log().body()
                .when()
                .post("/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }


    // 5. Проверяем POST /api/users
    @Test
    void createUserTest() {
        String body = """
                    {
                        "name": "Tom",
                        "job": "cleaner"
                    }
                """;

        given()
                .header(header)
                .contentType(ContentType.JSON)
                .body(body)
                .log().uri()
                .log().body()
                .when()
                .post("/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", equalTo("Tom"))
                .body("job", equalTo("cleaner"))
                .body("id", notNullValue());
    }
}
