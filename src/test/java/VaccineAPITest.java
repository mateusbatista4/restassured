import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;


public class VaccineAPITest {

    @BeforeAll
    public static void setup() {
        baseURI = "http://localhost:8080";
        basePath = "/";
        filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    //add a new vaccine and validate that the vaccine was added
//    graphwalker test:
    //		v_VaccineList();
//		e_createVaccine();
//		v_VaccineCreated();
//		e_findVaccine();
//		v_VaccineList();

    @Test
    public void testAddVaccine() {

        String token = given()
                .contentType(ContentType.JSON)
                .body("{ \"username\": \"admin\", \"password\": \"admin\" }")
                .when()
                .post("auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");


        // vaccine schema for post:
        // {
        //  "code": "1333",
        //  "description": "BCG",
        //  "vaccineType": {
        //    "code": "C",
        //    "description": "Child"
        //  }
        //}


        given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token))
                .body("{ \"code\": \"421\", \"description\": \"NewVaccuna\", \"vaccineType\": { \"code\": \"P\", \"description\": \"Pregnant\" }}")
                .when()
                .post("vaccines")
                .then()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token))
                .when()
                .get("vaccines/check/421")
                .then()
                .body(equalTo("true"))
                .statusCode(200);
    }

    // update a vaccine and validate that the vaccine was updated
//    graphwalker test:
    //		v_VaccineList();
//		e_updateVaccine();
//		v_VaccineUpdated();
//		e_findVaccine();
//		v_VaccineList();
    @Test
    public void testUpdateVaccine() {

        String token = given().
                contentType(ContentType.JSON).
                body("{ \"username\": \"admin\", \"password\": \"admin\" }").
                when().
                post("auth/login").
                then().
                statusCode(200).
                extract().
                path("token");

        // vaccine schema for put:
        // {
        //  "code": "1333",
        //  "description": "BCG",
        //  "vaccineType": {
        //    "code": "C",
        //    "description": "Child"
        //  }
        //}

        given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token))
                .body("{ \"code\": \"421\", \"description\": \"BCGupDate\", \"vaccineType\": { \"code\": \"C\", \"description\": \"Child\" }}")
                .when()
                .put("vaccines")
                .then()
                .statusCode(200)
                .body("description", equalTo("BCGupDate"));
    }

    // delete a vaccine and validate that the vaccine was deleted
    //    graphwalker test:
//    v_VaccineList();
//    e_deleteVaccine();
//    v_VaccineDeleted();
//    e_notFindVaccine();
//    v_VaccineList();

    @Test
    public void testDeleteVaccine() {

        String token = given().
                contentType(ContentType.JSON).
                body("{ \"username\": \"admin\", \"password\": \"admin\" }").
                when().
                post("auth/login").
                then().
                statusCode(200).
                extract().
                path("token");

        given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token))
                .when()
                .delete("vaccines/" + "421")
                .then()
                .statusCode(200);

        given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token))
                .when()
                .get("vaccines/check/421")
                .then()
                .statusCode(200)
                .body(equalTo("false"));
    }

    //list all vaccines and validate that the list is not empty
    //    graphwalker test:
//    v_VaccineList();
//    e_listVaccines();
//    v_VaccineList();

    @Test
    public void testListVaccines() {
        String token = given()
                .contentType(ContentType.JSON)
                .body("{ \"username\": \"admin\", \"password\": \"admin\" }")
                .when()
                .post("auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");

        given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token))
                .when()
                .get("vaccines")
                .then()
                .statusCode(200)
                .body("size()", Matchers.is(Matchers.greaterThan(0)));
    }

}


