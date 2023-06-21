package guru.qa;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import com.github.javafaker.Faker;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


public class SwagLabsSmokeTest {

    Faker faker = new Faker();
    @BeforeAll
    public static void BeforeAll() {
        Configuration.pageLoadStrategy = "eager";
        Configuration.browserSize = "1920x1080";
        Configuration.holdBrowserOpen = true;
        open("https://www.saucedemo.com/");
    }

    @CsvSource(value={"standard_user; secret_sauce; Products; Sauce Labs Backpack; $29.99; " +
            "Thank you for your order!; Your order has been dispatched, and will arrive just as fast as the pony can get there!"
    }, delimiter = ';')

    @Test
    @Tags({
            @Tag("smoke"), // Blocker
            @Tag("Web")
    })
    @ParameterizedTest(name="Authorization, choosing product, adding product to cart, buying product")

    void testSwagLabsSmoke(String username, String password, String successLogin,
                           String productTitle, String productPrice, String thankYou, String orderInfo) {

        // Enter correct username into username field
        $(byName("user-name")).setValue(username);
        // Enter correct password into password field
        $(byName("password")).setValue(password);
        // Click "Login" button
        $(byName("login-button")).click();
        // Check presence of "Products" text in the header and presence of products to buy so to verify successful authorization
        $("#header_container").shouldHave(text(successLogin));
        $("#inventory_container").shouldBe(visible);

        // Add Sauce Labs Backpack to Cart
        $(byName("add-to-cart-sauce-labs-backpack")).click();

        // Navigate to Cart
        $("#shopping_cart_container").click();
        // Check that product title and price are correct
        $(".inventory_item_name").shouldHave(text(productTitle));
        $(".inventory_item_price").shouldHave(text(productPrice));

        // Navigate to personal information form
        $(byName("checkout")).click();
        // Fill out the form with faker data
        $(byName("firstName")).setValue(faker.name().firstName());
        $(byName("lastName")).setValue(faker.name().lastName());
        $(byName("postalCode")).setValue(faker.address().zipCode());
        // Click "Continue"
        $(byName("continue")).click();

        // Check that final product title and price are correct
        $(".inventory_item_name").shouldHave(text(productTitle));
        $(".inventory_item_price").shouldHave(text(productPrice));

        // Click "Finish" button
        $(byName("finish")).click();
        // Check presence of "Thank you for your order!" and
        // "Your order has been dispatched, and will arrive just as fast as the pony can get there!" texts on the page
        $("#checkout_complete_container").shouldHave(text(thankYou))
        .shouldHave(text(orderInfo));

        // Navigate to the main page
        $(byName("back-to-products")).click();

        // Check presence of "Products" text in the header and presence of products to buy so to verify the main page
        $("#header_container").shouldHave(text(successLogin));
        $("#inventory_container").shouldBe(visible);

   }
}

