package org.tests;
import io.qameta.allure.*;
import org.example.tests.BaseTest;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.utils.AllureUtils;

import java.time.Duration;

@Epic("EPIC: Ecommerce Web - nopCommerce")
@Feature("Login de Usuario")
public class LoginTest extends BaseTest {

    @Test (groups = {"Integration", "Regression"},priority = 1)
    @Story("TC-Login-01: Inicio de sesión exitoso")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verificar que el sistema permita el acceso a un usuario con credenciales válidas y lo redirija a su cuenta.")
    public void login (){
        LoginPage loginPage = new LoginPage(getDriver());
        getDriver().get("https://tutorialsninja.com/demo/index.php?route=account/login");
        loginPage.LoginExitoso("asantostest1224@gmail.com","123456");
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("route=account/account"));
        String url = loginPage.obtenerUrlActual();
        AllureUtils.captureScreenshot(getDriver(), "Evidencia - Pantalla Final de Login");
        Assert.assertTrue(url.contains("route=account/account"),"¡Error! El login fallo");
    }

    @Test (groups = {"Functional"}, priority = 2)
    @Story("TC-Login-02: Inicio de sesión con credenciales incorrectas")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verificar que el sistema muestre un mensaje de error apropiado ante credenciales inválidas.")
    public void credencialesIncorrectas(){
        LoginPage loginPage = new LoginPage(getDriver());
        getDriver().get("https://tutorialsninja.com/demo/index.php?route=account/login");
        loginPage.credendialesIncorrectos("credencialesincorrectos@gmail.com","error1234");
        String mensajeError = loginPage.mensajeCredencialesIncorrectas();
        Assert.assertTrue(mensajeError.contains("No match for E-Mail Address and/or Password"),"Error el mensaje esperado no coincide");
    }

    @Test (groups = {"Regression"}, priority = 3)
    @Story("TC-Login-04: Correo electrónico no registrado")
    @Severity(SeverityLevel.MINOR)
    @Description("Validar que el sistema maneje el escenario donde un correo no existe en la base de datos.")
    public void correoNoRegistradoEnLaWeb(){
        LoginPage loginPage = new LoginPage(getDriver());
        getDriver().get("https://tutorialsninja.com/demo/index.php?route=common/home");
        loginPage.correoNoRegistrado("santosalan198@gmail.com");
        String mensajeError = loginPage.mensajeUsuarioNoRegistrado();
        Assert.assertTrue(mensajeError.contains("The E-Mail Address was not found in our records, please try again"),"Error.!!, el mensaje esperado no es el correcto");
    }
    @Test (groups = {"Functional", "Integration"}, priority = 4)
    @Story("TC-Login-05: Contraseña olvidada")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verificar el funcionamiento de la recuperación de contraseña mediante envío de link al correo.")
    public void olvidarContrasena(){
        LoginPage loginPage = new LoginPage(getDriver());
        getDriver().get("https://tutorialsninja.com/demo/index.php?route=common/home");
        loginPage.olvideContrasena("asantostest1224@gmail.com");
        String mensajeError = loginPage.mensajeEnvioCorreo();
        Assert.assertTrue(mensajeError.contains("An email with a confirmation link has been sent your email address"),"Error.!!, el mensaje esperado no es el correcto");
    }
}
