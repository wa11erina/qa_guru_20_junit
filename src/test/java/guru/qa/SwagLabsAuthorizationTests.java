package guru.qa;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class SwagLabsAuthorizationTests {


    @BeforeEach
     void setUp() {
        Configuration.pageLoadStrategy = "eager";
        Configuration.browserSize = "1920x1080";
        Configuration.holdBrowserOpen = true;
        open("https://www.saucedemo.com/");
    }

    @CsvSource ({"standard_user, secret_sauce, Products",
            "locked_out_user, secret_sauce, [Epic sadface: Sorry, this user has been locked out.]"
    })


    @Tags({
            @Tag("smoke"), // Blocker
            @Tag("Web")
    })
    @ParameterizedTest(name="Authorization with correct login data")
        void testSwagLabsSuccessAuthorization(String username, String password, String successText) {
        // Enter correct username into username field
        $(byName("user-name")).setValue(username);
        // Enter correct password into password field
        $(byName("password")).setValue(password);
        // Click "Login" button
        $(byName("login-button")).click();
        // Check presence of "Products" text in the header and presence of products to buy so to verify successful authorization
        $("#header_container").shouldHave(text(successText));
        $("#inventory_container").shouldBe(visible);

    }

    @Tags({
            @Tag("smoke"), // Blocker
            @Tag("Web")
    })
    @ParameterizedTest(name="Authorization with username of blocked user")
    void testSwagLabsAuthorizationFailed(String username, String password, String failureText) {
        // Enter correct username into username field
        $(byName("user-name")).setValue(username);
        // Enter correct password into password field
        $(byName("password")).setValue(password);
        // Click "Login" button
        $(byName("login-button")).click();
        // Check presence of error field with "Epic sadface: Sorry, this user has been locked out." text under the login form
        $(".error-message-container").should(appear)
        .shouldHave(text(failureText));

   }
}
