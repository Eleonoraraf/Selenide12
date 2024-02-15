package ru.netology.web;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Selenide.*;


public class RegistrationTest {
   private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        String path = "D:\\Programs\\Idea\\driver_\\geckodriver.exe";//где нах. драйвер
        System.setProperty("webdriver.firefox.driver", path);
    }

    @BeforeEach
    void setUp() {
        driver = new FirefoxDriver();
        driver.manage().window().maximize();//вызов метода- браузер на всё окно
        driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);//задержка

    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }
    private String generateDate(int addDays, String pattern){
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Test
    public void positiveRegistration() {
        open ("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Москва");
        String planningDate = generateDate(4,"dd.MM.yyyy");
        $("[data-test-id='date']input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date']input").setValue(planningDate);
        $("[data-test-id='name']input").setValue("Иванов-Иваныч Иван");
        $("[data-test-id='phone']input").setValue("+78569652233");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        $(".notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Встреча успешно забронирована на" + planningDate));
    }
}
