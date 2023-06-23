package guru.qa.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class SwagLabsSmokeCsvTests extends TestBase {

    @BeforeEach
    void setUp() {

        open("https://www.saucedemo.com/");    }


    @CsvFileSource(resources = "/SwagLabsSmokeTests.csv")

    @Tags({
            @Tag("smoke"), // Blocker
            @Tag("Web")
    })

    @ParameterizedTest(name="Authorization, choosing product, adding product to cart, buying product")
    void testSwagLabsSmoke(String username, String password, String successLogin,
                           String addToCart, String productTitle, String productPrice,
                           String thankYou) {
        // Enter correct username into username field
        $(byName("user-name")).setValue(username);
        // Enter correct password into password field
        $(byName("password")).setValue(password);
        // Click "Login" button
        $(byName("login-button")).click();
        // Check presence of "Products" text in the header and presence of products to buy so to verify successful authorization
        $("#header_container").shouldHave(text(successLogin));
        $("#inventory_container").shouldBe(visible);

        // Add Product to Cart
        $(byName(addToCart)).click();

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

        // Click "Finish" button
        $(byName("finish")).click();
        // Check presence of "Thank you for your order!" and
        // "Your order has been dispatched, and will arrive just as fast as the pony can get there!" texts on the page
        $("#checkout_complete_container").shouldHave(text(thankYou))
        .shouldHave(text("Your order has been dispatched, and will arrive just as fast as the pony can get there!"));

        // Navigate to the main page
        $(byName("back-to-products")).click();

    }

}
