package guru.qa;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class WikipediaSearchTest {


    static {
        Configuration.pageLoadStrategy = "eager";
        Configuration.browserSize = "1920x1080";
        Configuration.holdBrowserOpen = true;
    }


    static Stream<Arguments> WikipediaSearchTest() {
        return Stream.of(
                Arguments.of(Locale.English, List.of("Main Page", "Talk", "Read", "View source", "View history")),
                Arguments.of(Locale.Español, List.of("Portada", "Discusión", "Leer", "Ver código fuente", "Ver historial"))

        );
    }

    @Tags({
            @Tag("smoke"),
            @Tag("Web")
    })

    @MethodSource
    @ParameterizedTest(name="Collecting Wikipedia Sections in {0}")
    void WikipediaSearchTest(Locale locale, List<String> expectedResults) {
        open("https://www.wikipedia.org/");
        $$(".central-featured a").find(text(locale.name())).click();
        $$(".vector-page-toolbar a").should(CollectionCondition.sizeGreaterThan(5));
        $$(".vector-page-toolbar a").should(CollectionCondition.containExactTextsCaseSensitive(expectedResults));

    }
}
