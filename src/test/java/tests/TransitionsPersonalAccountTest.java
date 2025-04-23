package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import page_object.LoginPage;
import page_object.MainPage;
import page_object.ProfilePage;

import java.util.concurrent.TimeUnit;

@RunWith(Parameterized.class)
public class TransitionsPersonalAccountTest {

    private WebDriver driver;
    private String driverType; //добавила в код
    private final static String EMAIL = "dj-sereg@ya.ru";
    private final static String PASSWORD = "Ajkysirj12!";

    public TransitionsPersonalAccountTest(String driverType) {
        this.driverType = driverType;
    }

    @Before
    public void startUp() {
        if (driverType.equals("chromedriver")) {
            System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
            ChromeOptions options = new ChromeOptions();
            driver = new ChromeDriver(options);
            // Установка неявного ожидания
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            // Переход на тестируемый сайт
            driver.navigate().to("https://stellarburgers.nomoreparties.site/");
        } else if (driverType.equals("yandexdriver")) {
            System.setProperty("webdriver.chrome.driver", "src/main/resources/yandexdriver.exe");
            // Установка пути к браузеру Yandex
            ChromeOptions options = new ChromeOptions();
            options.setBinary("C:\\Users\\Bulgakov\\AppData\\Local\\Yandex\\YandexBrowser\\Application\\browser.exe");
            driver = new ChromeDriver(options);
            // Установка неявного ожидания
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            // Переход на тестируемый сайт
            driver.navigate().to("https://stellarburgers.nomoreparties.site/");
        }
    }

    @Parameterized.Parameters(name = "Результаты проверок браузера: {0}")
    public static Object[][] getDataDriver() {
        return new Object[][]{
                {"chromedriver"},
                {"yandexdriver"},
        };
    }

    @Test
    @DisplayName("Переход в личный кабинет.")
    @Description("Проверка перехода по клику на 'Личный кабинет'.")
    public void transitionToProfilePageTest() {
        MainPage mainPage = new MainPage(driver);
        mainPage.clickOnAccountButton();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForLoadEntrance();
        Assert.assertTrue("Страница авторизации не отобразилась", driver.findElement(loginPage.entrance).isDisplayed());
    }

    @Test
    @DisplayName("Переход в конструктор из личного кабинета.")
    @Description("Проверка перехода на вкладку 'Конструктор' из страницы авторизации пользователя.")
    public void transitionToConstructorFromProfilePageTest() {
        MainPage mainPage = new MainPage(driver);
        mainPage.waitForInvisibilityLoadingAnimation();
        mainPage.clickOnAccountButton();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForLoadEntrance();
        loginPage.clickOnConstructorButton();
        mainPage.waitForLoadMainPage();
        Assert.assertTrue("Переход  в конструктор из личного кабинете не прошел", driver.findElement(mainPage.textBurgerMainPage).isDisplayed());
    }

    @Test
    @DisplayName("Клик по логотипу 'Stellar Burgers'.")
    @Description("Проверка перехода в конструктор при нажатии на логотип 'Stellar Burgers'.")
    public void transitionToStellarBurgersFromProfilePageTest() {
        MainPage mainPage = new MainPage(driver);
        mainPage.clickOnAccountButton();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForLoadEntrance();
        loginPage.clickOnLogo();
        mainPage.waitForLoadMainPage();
        Assert.assertTrue("Конструктор при клике на логотип не загрузился", driver.findElement(mainPage.textBurgerMainPage).isDisplayed());
    }

    @Test
    @DisplayName("Выход из аккаунта")
    @Description("Проверка выхода по кнопке 'Выйти' в личном кабинете.")
    public void exitFromProfileTest() {
        MainPage mainPage = new MainPage(driver);
        mainPage.clickOnAccountButton();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForLoadEntrance();
        loginPage.authorization(EMAIL, PASSWORD);
        mainPage.waitForLoadMainPage();
        mainPage.clickOnAccountButton();
        ProfilePage profilePage = new ProfilePage(driver);
        profilePage.waitForLoadProfilePage();
        profilePage.clickOnExitButton();
        mainPage.waitForInvisibilityLoadingAnimation();
        Assert.assertTrue("Не удалось выйти из аккаунта", driver.findElement(loginPage.entrance).isDisplayed());
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}