package org.tests;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.example.tests.BaseTest;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.pages.OrdenesPage;
import org.utils.AllureUtils;

import java.time.Duration;


@Epic("EPIC: Ecommerce Web - nopCommerce")
@Feature("Creacion de Ordenes")
public class OrdenesTest extends BaseTest{

    @Test (groups = {"Regression"}, priority = 1)
    @Story("TC-Order-01: Añadir productos al carrito")
    @Description("Verificar que el usuario pueda agregar productos al carrito desde el catálogo.")
    public void AgregarProductos (){
        LoginPage loginPage = new LoginPage(getDriver());
        getDriver().get("https://tutorialsninja.com/demo/index.php?route=account/login");
        loginPage.LoginExitoso("asantostest1224@gmail.com","123456");
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("route=account/account"));
        String url = loginPage.obtenerUrlActual();
        Assert.assertTrue(url.contains("route=account/account"),"¡Error! El login fallo");
        OrdenesPage ordenesPage = new OrdenesPage(getDriver());
        ordenesPage.agregarProductosAlcarrito();
    }
    @Test (groups = {"Regression"}, priority = 2)
    @Story("TC-Order-02: Eliminar productos del carrito")
    @Description("Comprobar que el usuario pueda eliminar productos del carrito de compras.")
    public void EliminarProducto (){
        LoginPage loginPage = new LoginPage(getDriver());
        getDriver().get("https://tutorialsninja.com/demo/index.php?route=account/login");
        loginPage.LoginExitoso("asantostest1224@gmail.com","123456");
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("route=account/account"));
        String url = loginPage.obtenerUrlActual();
        Assert.assertTrue(url.contains("route=account/account"),"¡Error! El login fallo");
        OrdenesPage ordenesPage = new OrdenesPage(getDriver());
        ordenesPage.agregarProductosAlcarrito();
        ordenesPage.eliminarProducto();
    }
    @Test (groups = {"Functional","Integration"}, priority = 3)
    @Story("TC-Order-03: Modificar cantidad de productos")
    @Description("Asegurarse de que el usuario pueda ajustar la cantidad de productos antes del pago.")
    public void modificarCantidadProducto(){
        LoginPage loginPage = new LoginPage(getDriver());
        getDriver().get("https://tutorialsninja.com/demo/index.php?route=account/login");
        loginPage.LoginExitoso("asantostest1224@gmail.com","123456");
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("route=account/account"));
        String url = loginPage.obtenerUrlActual();
        Assert.assertTrue(url.contains("route=account/account"),"¡Error! El login fallo");
        OrdenesPage ordenesPage = new OrdenesPage(getDriver());
        ordenesPage.agregarProductosAlcarrito();
        ordenesPage.modificarCantidadProducto("MacBook", "10");
    }
    @Test (groups = {"Regression"}, priority = 4)
    @Story("TC-Order-04: Creación de orden exitosa")
    @Description("Escenario de flujo completo: login, selección, envío y confirmación de pedido.")
    public void realizarCompra(){
        OrdenesPage ordenesPage = new OrdenesPage(getDriver());
        getDriver().get("https://www.saucedemo.com/");
        ordenesPage.LoginExitoso("standard_user","secret_sauce");
        ordenesPage.agregarProductos();
        ordenesPage.ingresarDatos("Alan","Santos","1234");
        ordenesPage.mensajeFinal();
        String mensajeFinalDeCompra = ordenesPage.mensajeFinal();
        AllureUtils.captureScreenshot(getDriver(), "Evidencia - Pantalla Final de compra exitosa");
        Assert.assertEquals(mensajeFinalDeCompra,"Thank you for your order!","Error el mensaje no es el correcto");
    }

}