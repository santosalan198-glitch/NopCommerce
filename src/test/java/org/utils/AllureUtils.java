package org.utils;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class AllureUtils {

    @Attachment(value = "{name}", type = "image/png")
    public static byte[] takeScreenshot(WebDriver driver, String name) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @Step("Capturando screenshot: {name}")
    public static void captureScreenshot(WebDriver driver, String name) {
        takeScreenshot(driver, name);
    }

    @Attachment(value = "{name}", type = "text/plain")
    public static String attachText(String name, String message) {
        return message;
    }

    @Attachment(value = "Page Source", type = "text/html")
    public static String attachPageSource(WebDriver driver) {
        return driver.getPageSource();
    }
}