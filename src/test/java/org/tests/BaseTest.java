package org.example.tests;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.pages.RegistroPage;
import org.testng.annotations.*;
import java.time.Duration;
import java.util.Collections;
import jdk.jfr.Description;

@Description("Clase base para la configuración de ciclos de vida de los tests.")

public class BaseTest {

    // Manejo de hilos para ejecución paralela segura
    protected static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    protected int TIME_OUT = 10;
    protected String URL = "https://tutorialsninja.com/demo/index.php?route=common/home";
    public RegistroPage registroPage;

    public WebDriver getDriver() {
        return driverThreadLocal.get();
    }
    @Description ("Configuración previa a cada método de prueba.")
    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser"})
    public void setUp(@Optional("chrome") String browser) throws Exception {
        WebDriver driver;

        if (browser.equalsIgnoreCase("chrome")) {
            // 1. Primero creas las opciones
            ChromeOptions options = new ChromeOptions();

            // 2. Configuras todas las opciones
            options.addArguments("--incognito");
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
            options.setExperimentalOption("useAutomationExtension", false);
            options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36");

            // 3. RECIÉN AQUÍ creas el driver pasándole las opciones (Solo una vez)
            driver = new ChromeDriver(options);
        } else if (browser.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver(new FirefoxOptions());
        } else if (browser.equalsIgnoreCase("edge")) {
            System.setProperty("webdriver.edge.driver", "src/test/msedgedriver.exe");
            EdgeOptions edgeOptions = new EdgeOptions();
            edgeOptions.addArguments("--start-maximized");
            edgeOptions.addArguments("--disable-search-engine-choice-screen");
            edgeOptions.addArguments("--remote-allow-origins=*");
            driver = new EdgeDriver(edgeOptions);
        } else {
            throw new Exception("Browser no soportado: " + browser);
        }

        driverThreadLocal.set(driver);
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(TIME_OUT));
        getDriver().manage().window().maximize();
        getDriver().get(URL);

        registroPage = new RegistroPage(getDriver());
    }
    @Description ("Finalización del driver y limpieza del hilo tras la ejecución del test.")
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        // En Allure no necesitamos el ITestResult aquí para los logs básicos,
        // ya que el Listener del testng.xml captura si falló o pasó.
        if (getDriver() != null) {
            getDriver().quit();
            driverThreadLocal.remove(); // Limpiamos la memoria del hilo
        }
    }

    public byte[] tomarCapturaParaAllure() {
        return ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
    }
}