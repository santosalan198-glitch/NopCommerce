package org.pages;

import io.qameta.allure.Step;
import jdk.jfr.Description;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.utils.AllureUtils;

@Description("Clase encargada de la gestión de registros de nuevos usuarios y validaciones de formularios.")

public class RegistroPage extends BasePage{
    public RegistroPage (WebDriver driver){
        super(driver);
    }

    // --- Locators: Formulario de Registro
    private By botonMyAccount = By.className("dropdown");
    private By botonRegister = By.xpath("//*[@id=\"top-links\"]/ul/li[2]/ul/li[1]/a");
    private By firstName = By.id("input-firstname");
    private By lastName = By.id("input-lastname");
    private By inputemail = By.id("input-email");
    private By telephone = By.id("input-telephone");
    private By inputpassword = By.id("input-password");
    private By passwordConfirm = By.id("input-confirm");
    private By privacyPolicy = By.xpath("//*[@id=\"content\"]/form/div/div/input[1]");
    private By botonContinue = By.cssSelector("#content > form > div > div > input.btn.btn-primary");

    // --- Métodos de Acción

    @Step("Completar formulario de registro")
    public void RegistrarConCampos(String name, String lastname, String email,
                                   String telefono, String password, String confpassword) {
        click(botonMyAccount);
        click(botonRegister);
        // Validación de campos nulos para pruebas negativas o parciales
        if (name != null) writeText(firstName, name);
        if (lastname != null) writeText(lastName, lastname);
        if (email != null) writeText(inputemail, email);
        if (telefono != null) writeText(telephone, telefono);
        if (password != null) writeText(inputpassword, password);
        if (confpassword != null) writeText(passwordConfirm, confpassword);
        click(privacyPolicy);
        click(botonContinue);
    }
    // --- Métodos de Validación y Utilidad

    @Step("Obtener el contenido de texto completo de la página.")
    public String obtenerTextoDeTodaLaPagina() {
        int intentos = 0;
        // Intentaremos la acción un máximo de 3 veces para evitar que el test falle por lentitud
        while (intentos < 3) {
            try {
                return driver.findElement(By.tagName("body")).getText();
            } catch (StaleElementReferenceException e) {
                intentos++;
            }
        }
        return "";
    }
    @Step("Validar si el campo Email presenta un estado de entrada inválido")
    public boolean elCampoEmailEsInvalido() {
        WebElement emailElement = driver.findElement(By.id("input-email"));
        return (Boolean) ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return !arguments[0].validity.valid;", emailElement);
    }

}
