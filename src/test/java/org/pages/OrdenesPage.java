package org.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import jdk.jfr.Description;

@Description("Clase encargada de gestionar los flujos de compra, manipulación del carrito y validación de pedidos.")

public class OrdenesPage extends BasePage{
    public OrdenesPage (WebDriver driver){
        super(driver);
    }
    // --- Locators: E-Commerce (QAFox)
    private By botonQaFox = By.cssSelector("#logo > h1 > a");
    private By productLaptop = By.cssSelector("button[onclick=\"cart.add('43');\"]");
    private By menuComponents = By.xpath("//a[text()='Components']");
    private By productMonitor = By.xpath("//a[contains(@href, 'path=25_28')]");
    private By productSamsungMonitor = By.xpath("//*[@id=\"content\"]/div[3]/div[2]/div/div[2]/div[2]/button[1]");
    private By botonCarrito = By.xpath("//*[@id=\"top-links\"]/ul/li[4]/a");
    private By botonEliminarSamsung = By.xpath("//tr[td[contains(.,'Samsung SyncMaster')]]//button[@data-original-title='Remove']");
    //private By botonEliminarSamsung = By.xpath("//tr[td[contains(.,'Samsung SyncMaster')]]//button[contains(@class, 'btn-danger') or @title='Remove']");
    // --- Locators: E-Commerce (SauceDemo)
    private By usernameInput = By.id("user-name");
    private By passwordInput = By.id("password");
    private By loginButton = By.id("login-button");
    private By botonCart = By.id("shopping_cart_container");
    private By checkOut = By.id("checkout");
    private By firstName = By.id("first-name");
    private By lastName = By.id("last-name");
    private By codigoZip = By.id("postal-code");
    private By botonContinue = By.id("continue");
    private By finishBoton = By.id("finish");
    private By completeHeader = By.className("complete-header");
    private By AddCarBackpack = By.id("add-to-cart-sauce-labs-backpack");
    private By addCartTShirt = By.id("add-to-cart-sauce-labs-bolt-t-shirt");


    // --- Métodos de Flujo: QAFox

    @Step("Agregar Laptop y Monitor Samsung al carrito")
    public void agregarProductosAlcarrito() {
        click(botonQaFox);
        click(productLaptop);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert-success")));
        Actions action = new Actions(driver);
        WebElement menu = wait.until(ExpectedConditions.visibilityOfElementLocated(menuComponents));
        action.moveToElement(menu).perform();
        WebElement monitorLink = wait.until(ExpectedConditions.elementToBeClickable(productMonitor));
        monitorLink.click();
        wait.until(ExpectedConditions.urlContains("path=25_28"));
        WebElement monitorSamsung = wait.until(ExpectedConditions.presenceOfElementLocated(productSamsungMonitor));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", monitorSamsung);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", monitorSamsung);
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class, 'alert-success') and contains(., 'Samsung SyncMaster')]")
        ));
    }
    @Step("Remover el monitor Samsung SyncMaster del carrito")
    public void eliminarProducto() {
        driver.get("https://tutorialsninja.com/demo/index.php?route=checkout/cart");
        try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }
        if (!driver.getPageSource().contains("Samsung SyncMaster")) {
            driver.navigate().refresh();
        }

        try {
            WebElement btnEliminar = wait.until(ExpectedConditions.elementToBeClickable(botonEliminarSamsung));
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", btnEliminar);
            wait.until(ExpectedConditions.stalenessOf(btnEliminar));
        } catch (org.openqa.selenium.TimeoutException e) {
            System.err.println("EL MONITOR NO LLEGÓ AL CARRITO. Revisa el paso de AGREGAR.");
            throw e;
        }
    }
    @Step("Modificar cantidad del producto")
    public void modificarCantidadProducto(String nombreProducto, String nuevaCantidad) {

        By Carrito = By.xpath("//a[@title='Shopping Cart']");
        WebElement btnCarrito = wait.until(ExpectedConditions.presenceOfElementLocated(Carrito));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", btnCarrito);
        wait.until(ExpectedConditions.urlContains("route=checkout/cart"));
        By inputCantidad = By.xpath("//td[contains(., '" + nombreProducto + "')]/ancestor::tr//input[contains(@name, 'quantity')]");
        By botonActualizar = By.xpath("//td[contains(., '" + nombreProducto + "')]/ancestor::tr//button[contains(@class, 'btn-primary')]");
        writeText(inputCantidad, nuevaCantidad);
        click(botonActualizar);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert-success")));
        }

    // --- Métodos de Flujo: SauceDemo

    @Step("Autenticación en SauceDemo con usuario")
    public void LoginExitoso(String usuario, String clave){
        writeText(usernameInput, usuario);
        writeText(passwordInput, clave);
        click(loginButton);
    }
    @Step("Seleccionar productos (T-Shirt y Backpack) y proceder al Checkout")
    public void agregarProductos(){
    click(addCartTShirt);
    click(AddCarBackpack);
    click(botonCart);
    click(checkOut);
    }
    @Step("Completar información de envío y finalizar compra")
    public void ingresarDatos (String nombre, String apellido, String codigo){
        writeText(firstName,nombre);
        writeText(lastName,apellido);
        writeText(codigoZip,codigo);
        click(botonContinue);
        click(finishBoton);
    }
    @Step("Validar mensaje de confirmación de orden finalizada")
    public String mensajeFinal(){
        return getText(completeHeader);
    }

}
