package scratch.cucumber.example.test.page;

import org.openqa.selenium.By;

import static java.lang.String.format;

public class Bys {

    public static By byLabel(String text) {
        return By.xpath(format("//label[text()[contains(.,'%s')]]", text));
    }

    public static By byValue(String value) {
        return By.xpath(format("//input[@value='%s']", value));
    }
}
