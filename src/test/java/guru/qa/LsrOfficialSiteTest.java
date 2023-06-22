package guru.qa;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class LsrOfficialSiteTest {


    static {
        Configuration.pageLoadStrategy = "eager";
        Configuration.browserSize = "1920x1080";
        Configuration.holdBrowserOpen = true;
    }


    static Stream<Arguments>lsrGroupOfElements() {
        return Stream.of(
                Arguments.of(Locale.ru, List.of("Портал закупок", "Купить квартиру", "Купить стройматериалы")),
                Arguments.of(Locale.EN, List.of("Procurement portal", "Buy an apartment", "Buy building materials"))

        );
    }

    @Tags({
            @Tag("smoke"),
            @Tag("Web")
    })

    @MethodSource
    @ParameterizedTest(name="Checking group of elements in two languages")
    void lsrGroupOfElements(Locale locale, List<String> expectedResults) {
        open("https://www.lsrgroup.ru/");
        $$(".link-container a").find(text(locale.name())).click();
        $$x("/html/body/header/div[1]/div[1]/div/a").should(CollectionCondition.texts(expectedResults));

    }
}
