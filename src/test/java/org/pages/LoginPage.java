package org.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import jdk.jfr.Description;
import org.openqa.selenium.support.ui.ExpectedConditions;

@Description ("Clase que contiene los elementos y acciones de la página de Login y recuperación de cuenta.")

public class LoginPage extends BasePage{
    public LoginPage(WebDriver driver){
        super(driver);
    }

    // Locators
    private By inputEmail = By.id("input-email");
    private By inputPassword = By.id("input-password");
    private By botonLogin = By.xpath("//*[@id=\"content\"]/div/div[2]/div/form/input");
    private By mensajeCredencialesIncorrectas = By.xpath("//*[@id=\"account-login\"]/div[1]");
    private By botonMyAccount = By.className("dropdown");
    private By botonRegister = By.xpath("//*[@id=\"top-links\"]/ul/li[2]/ul/li[1]/a");
    private By forgetPassword = By.xpath("//a[text()='Forgotten Password']");
    private By botonContinue = By.cssSelector("input[type='submit'][value='Continue']");
    private By mensajeCorreoNoRegis = By.xpath("//*[@id=\"account-forgotten\"]/div[1]");
    private By mensajeEnvioDeCorreo = By.xpath("//div[contains(text(), 'An email with a confirmation link')]");

    // Métodos de Acción

    @Step ("Iniciar sesión con credenciales: Usuario {0} y Clave {1}")
    public void LoginExitoso (String usuario, String clave){
        writeText(inputEmail, usuario);
        writeText(inputPassword, clave);
        click(botonLogin);
    }


    @Step ("Intentar inicio de sesión con credenciales incorrectas")
    public void credendialesIncorrectos (String usuario, String clave){
        writeText(inputEmail, usuario);
        writeText(inputPassword, clave);
        click(botonLogin);
    }

    @Step ("Proceso de recuperación de contraseña para el correo: {0}")
    public void olvideContrasena(String email){
        click(botonMyAccount);
        click(botonRegister);
        click(forgetPassword);
        writeText(inputEmail,email);
        click(botonContinue);
    }

    @Step("Intentar recuperación de contraseña con correo no registrado")
    public void correoNoRegistrado(String email){
        click(botonMyAccount);
        click(botonRegister);
        click(forgetPassword);
        writeText(inputEmail,email);
        click(botonContinue);
    }

    // Métodos de Validación

    @Step ("Obtener mensaje de credenciales incorrectas")
    public String mensajeCredencialesIncorrectas (){

        By locator = By.xpath("//*[@id='account-login']/div[1]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return driver.findElement(locator).getText();
    }

    @Step ("Obtener el mensaje de error para usuario no registrado")
    public String mensajeUsuarioNoRegistrado(){
        return getText(mensajeCorreoNoRegis);
    }

    @Step ("Obtener el mensaje de confirmación de envío de correo")
    public String mensajeEnvioCorreo(){
        return getText(mensajeEnvioDeCorreo);
    }

    @Step("Validar la URL actual de la página de Login")
    public String obtenerUrlActual() {
        return driver.getCurrentUrl();
    }


}
