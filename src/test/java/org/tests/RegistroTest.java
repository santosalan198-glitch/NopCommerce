    package org.tests;
    import io.qameta.allure.Epic;
    import io.qameta.allure.Feature;
    import io.qameta.allure.Story;
    import io.qameta.allure.Description;
    import org.testng.Assert;
    import org.testng.annotations.Test;
    import org.example.tests.BaseTest;
    import org.pages.RegistroPage;
    import org.apache.poi.ss.usermodel.*;
    import org.apache.poi.xssf.usermodel.XSSFWorkbook;
    import org.utils.AllureUtils;

    import java.io.FileInputStream;

    @Epic("EPIC: Ecommerce Web - nopCommerce")
    @Feature("Registro de Nuevo Clientes")
    public class RegistroTest extends BaseTest {

        //--- Lee el Excel y busca la fila que coincide con la descripción
        private String[] obtenerDatosDeExcel(String descripcionBuscada) {
            String rutaExcel = "src/test/java/org/utils/Registros.xlsx";
            String[] datosFila = new String[8]; // Array para guardar las 8 columnas
            DataFormatter formatter = new DataFormatter();

            try (FileInputStream archivo = new FileInputStream(rutaExcel);
                 Workbook workbook = new XSSFWorkbook(archivo)) {

                Sheet hoja = workbook.getSheetAt(0);
                for (int i = 1; i <= hoja.getLastRowNum(); i++) {
                    Row fila = hoja.getRow(i);
                    if (fila != null && formatter.formatCellValue(fila.getCell(6)).equalsIgnoreCase(descripcionBuscada)) {
                        for (int j = 0; j < 8; j++) {
                            datosFila[j] = formatter.formatCellValue(fila.getCell(j));
                        }
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return datosFila;
        }

        @Test(groups = {"Functional"}, priority = 1)
        @Story("TC-Register-01: Registro exitoso")
        @Description("Validar que un usuario pueda registrarse con información válida y completa.")
        public void registrarUsuarioExitoso() {
            String[] datos = obtenerDatosDeExcel("Registro exitoso");
            RegistroPage registroPage = new RegistroPage(getDriver());
            getDriver().get("https://tutorialsninja.com/demo/index.php?route=common/home");
            // Motor para correo unico
            String emailUnico = "test" + System.currentTimeMillis() + "@gmail.com";
            registroPage.RegistrarConCampos(datos[0], datos[1], emailUnico, datos[3], datos[4], datos[5]);
            AllureUtils.captureScreenshot(getDriver(), "Evidencia - Pantalla Final de Registro");
            Assert.assertTrue(registroPage.obtenerTextoDeTodaLaPagina().contains(datos[7]));
        }

        @Test(groups = {"Functional"}, priority = 2)
        @Story("TC-Register-02: Registro con campos obligatorios faltantes")
        @Description("Verificar que el sistema valide adecuadamente cuando faltan campos obligatorios.")
        public void registroCamposFaltantes() {
            // Ejecutamos validaciones internas una tras otra
            validarNombreObligatorio();
            validarApellidoObligatorio();
            validarEmailObligatorio();
            validarTelefonoObligatorio();
            validarPasswordObligatorio();
        }

        @Test(groups = {"Functional","Regression"}, priority = 3)
        @Story("TC-Register-03: Formato de correo electrónico válido")
        @Description("Comprobar que el sistema rechace direcciones de correo con formato incorrecto.")
        public void registroEmailInvalido() {
            String[] d = obtenerDatosDeExcel("Ingreso de correo invalido");
            RegistroPage registroPage = new RegistroPage(getDriver());
            getDriver().get("https://tutorialsninja.com/demo/index.php?route=common/home");
            registroPage.RegistrarConCampos(d[0], d[1], d[2], d[3], d[4], d[5]);
            Assert.assertTrue(registroPage.elCampoEmailEsInvalido());
        }

        @Test(groups = {"Functional","Integration"}, priority = 4)
        @Story("TC-Register-05: Confirmación de contraseña")
        @Description("Verificar que el sistema requiera coincidencia entre contraseña y confirmación.")
        public void registroPasswordConfirmFail() {
            String[] d = obtenerDatosDeExcel("Password no coinciden");
            RegistroPage registroPage = new RegistroPage(getDriver());
            getDriver().get("https://tutorialsninja.com/demo/index.php?route=common/home");

            registroPage.RegistrarConCampos(d[0], d[1], d[2], d[3], d[4], "PasswordDistinta123");
            Assert.assertTrue(registroPage.obtenerTextoDeTodaLaPagina().contains(d[7]));
        }

        @Test(groups = {"Integration"}, priority = 5)
        @Story("TC-Register-06: Correo electrónico único")
        @Description("Asegurarse de que el sistema evite registros duplicados con el mismo correo.")
        public void registroEmailDuplicado() {
            String[] d = obtenerDatosDeExcel("Registro con Email unico");
            RegistroPage registroPage = new RegistroPage(getDriver());
            getDriver().get("https://tutorialsninja.com/demo/index.php?route=common/home");

            // Usamos un correo fijo que ya exista
            registroPage.RegistrarConCampos(d[0], d[1], "alansantos10000@gmail.com", d[3], d[4], d[5]);
            Assert.assertTrue(registroPage.obtenerTextoDeTodaLaPagina().contains(d[7]));
        }


        private void validarNombreObligatorio() {
            String[] d = obtenerDatosDeExcel("Registro sin nombre");
            registroPage.RegistrarConCampos(null, d[1], d[2], d[3], d[4], d[5]);
            Assert.assertTrue(registroPage.obtenerTextoDeTodaLaPagina().contains(d[7]), "Fallo: Nombre obligatorio");
        }

        private void validarApellidoObligatorio() {
            String[] d = obtenerDatosDeExcel("Registro sin Apellido");
            registroPage.RegistrarConCampos(d[0], null, d[2], d[3], d[4], d[5]);
            Assert.assertTrue(registroPage.obtenerTextoDeTodaLaPagina().contains(d[7]), "Fallo: Apellido obligatorio");
        }


        private void validarEmailObligatorio() {
        String[] d = obtenerDatosDeExcel("Registro sin Email");
        registroPage.RegistrarConCampos(d[0], d[1], null, d[3], d[4], d[5]);
        String textoPagina = registroPage.obtenerTextoDeTodaLaPagina();
        System.out.println("DEBUG - Texto buscado: " + d[7]);
        System.out.println("DEBUG - Texto encontrado en página: " + textoPagina);
         Assert.assertTrue(textoPagina.contains(d[7]), "Fallo: Email obligatorio. No se encontró: " + d[7]);
        }
        private void validarTelefonoObligatorio() {
            String[] d = obtenerDatosDeExcel("Registro sin telefono");
            registroPage.RegistrarConCampos(d[0], d[1], d[2], null, d[4], d[5]);
            Assert.assertTrue(registroPage.obtenerTextoDeTodaLaPagina().contains(d[7]), "Fallo: Teléfono obligatorio");
        }

        private void validarPasswordObligatorio() {
            String[] d = obtenerDatosDeExcel("Registro sin password");
            registroPage.RegistrarConCampos(d[0], d[1], d[2], d[3], null, null);
            Assert.assertTrue(registroPage.obtenerTextoDeTodaLaPagina().contains(d[7]), "Fallo: Password obligatorio");
        }

    }