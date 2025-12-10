package spec;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomApiListener.withCustomTemplates;
import static io.restassured.http.ContentType.JSON;

public class SpecCustoms {

    public static RequestSpecification requestSpecification = new RequestSpecBuilder()
            .setContentType(JSON)
            .addHeader("x-api-key", "reqres_0f50984334cd42ac8b996d73df03cff2")
            .addFilter(withCustomTemplates())
            .log(LogDetail.ALL)
            .build();


    public static ResponseSpecification responseSpecificationBuilder(int statusCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(statusCode)
                .log(LogDetail.ALL)
                .build();
    }
}
