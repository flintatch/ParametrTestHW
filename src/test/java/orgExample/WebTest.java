package orgExample;

import com.codeborne.selenide.CollectionCondition;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import orgExample.Data.Locale;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class WebTest {

    @ValueSource(strings={"cyberpunk 2077", "bioshock"})
    @ParameterizedTest(name = "Проверка числа выдаваемого поиском Мыла для запроса {0}")
    void mailSearchGameTest(String testData) {
        open("https://www.mail.ru/");
        $("#q").setValue(testData);
        $("button[type='submit']").click();
        $$("li.result__li")
                .shouldHave(CollectionCondition.size(13))
                .first()
                .shouldHave(text(testData));
    }

    @CsvSource(value = {
            "cyberpunk 2077, (Киберпанк 2077) – масштабный игровой проект, разработанный в жанре приключенческого экшен-РПГ",
            "bioshock, BioShock — компьютерная игра в жанре шутера от первого лица с элементами RPG"
    })
    @ParameterizedTest(name = "Проверка числа выдаваемого поиском Мыла для запроса {0}")
    void mailSearchGameTestDifferentExpectedText(String searchTreasure, String expectedText) {
        open("https://www.mail.ru/");
        $("#q").setValue(searchTreasure);
        $("button[type='submit']").click();
        $$("li.result__li")
                .shouldHave(CollectionCondition.size(13))
                .first()
                .shouldHave(text(expectedText));
    }

    static Stream<Arguments> selenideSiteButtonsText() {
        return Stream.of(
                Arguments.of(Locale.EN, List.of("Quick start", "Docs", "FAQ", "Blog", "Javadoc", "Users", "Quotes")),
                Arguments.of(Locale.RU, List.of("С чего начать?", "Док", "ЧАВО", "Блог", "Javadoc", "Пользователи", "Отзывы"))
        );
    }

    @MethodSource("selenideSiteButtonsText")
    @ParameterizedTest(name = "Проверка языковых раскладок для локали: {0}")
    void selenideSiteButtonsText(Locale locale, List<String> buttonsTexts) {
        open("https://selenide.org/");
        $$("#languages a").find(text(locale.name())).click();
        $$(".main-menu-pages a").filter(visible)
                .shouldHave(CollectionCondition.texts(buttonsTexts));
    }

    @EnumSource(Locale.class)
    @ParameterizedTest()
    void checkLocaleTest(Locale locale) {
        open("https://selenide.org/");
        $$("#languages a").find(text(locale.name())).shouldBe(visible);

    }
}