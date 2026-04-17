package org.utils; // Cambia "org.example" por el nombre real de tu proyecto si es distinto

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry implements IRetryAnalyzer {

    private int count = 0;
    // maxTry = 1 significa que si el test falla, TestNG lo intentará 1 vez más.
    // (Total de ejecuciones por test fallido = 2).
    // Puedes cambiarlo a 2 o 3 si tu entorno es muy inestable.
    private static int maxTry = 1;

    @Override
    public boolean retry(ITestResult iTestResult) {
        if (!iTestResult.isSuccess()) { // Si el test falló
            if (count < maxTry) {
                count++;
                System.out.println("Reintentando el test '" + iTestResult.getName() + "' por " + count + "° vez.");
                return true; // Retorna true para decirle a TestNG "vuelve a ejecutarlo"
            }
        } else {
            iTestResult.setStatus(ITestResult.SUCCESS); // Si pasó en el reintento, lo marca como exitoso
        }
        return false; // Retorna false cuando ya superó el límite de intentos
    }
}