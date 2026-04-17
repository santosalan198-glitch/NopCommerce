package org.pages;
import io.qameta.allure.Step;
import jdk.jfr.Description;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

@Description("Clase base que centraliza la configuración del WebDriver y las interacciones comunes con elementos web.")

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    public BasePage(WebDriver driver){
        this.driver = driver;

        // Configuración de espera explícita estándar (10 segundos)
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    @Step ("Realizar clic en un elemento web identificado por un localizador.")
    protected void click (By locator){
        wait.until(ExpectedConditions.elementToBeClickable(locator));
        driver.findElement(locator).click();
    }
    @Step("Limpiar campo de texto y escribir el valor proporcionado.")
    protected void writeText(By locator, String text){
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        driver.findElement(locator).clear();
        driver.findElement(locator).sendKeys(text);
    }

    @Step("Obtener el texto visible de un elemento web.")
    protected String getText (By locator){
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return driver.findElement(locator).getText();
    }
    @Step("Seleccionar una opción de un menú desplegable mediante su valor (value).")
    protected void selectByValue (By locator, String value){
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        Select select = new Select(driver.findElement(locator));
        select.selectByValue(value);
    }
    @Step("Obtener la URL actual del navegador.")
    public String obtenerUrlActual(){
        return driver.getCurrentUrl();
    }

    public String getValidationMessage (WebElement element){
        return element.getAttribute("validationMessage");
    }
    public WebDriver getDriver() {
        return driver;
    }
}
