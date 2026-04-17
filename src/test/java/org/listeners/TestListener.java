package org.listeners;

import org.example.tests.BaseTest;
import org.utils.AllureUtils;
import org.utils.Retry;
import org.openqa.selenium.WebDriver;
import org.testng.IAnnotationTransformer;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class TestListener implements ITestListener, IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        annotation.setRetryAnalyzer(Retry.class);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        System.out.println("Fallo detectado en: " + testName + ". Generando evidencias para Allure...");
        Object testClass = result.getInstance();
        if (testClass instanceof BaseTest) {
            WebDriver driver = ((BaseTest) testClass).getDriver();
            if (driver != null) {
                AllureUtils.attachText("Información de Fallo", "El test '" + testName + "' no pudo completarse.");
                AllureUtils.captureScreenshot(driver, "Captura del Fallo - " + testName);
                AllureUtils.attachPageSource(driver);
            } else {
                System.out.println("No se pudo tomar captura: El WebDriver es nulo.");
            }
        }
    }
}