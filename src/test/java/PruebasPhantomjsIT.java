import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PruebasPhantomjsIT {
    private static WebDriver driver = null;

    @Test
    public void tituloIndexTest() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setJavascriptEnabled(true);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "/usr/bin/phantomjs");
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS,
                new String[] { "--web-security=no", "--ignore-ssl-errors=yes" });
        driver = new PhantomJSDriver(caps);
        driver.navigate().to("http://localhost:8080/Baloncesto/");
        assertEquals("Votacion mejor jugador liga ACB", driver.getTitle(),
                "El titulo no es correcto");
        System.out.println(driver.getTitle());
        driver.close();
        driver.quit();
    }

    @Test
    public void comprobarResetVotos() {
        boolean result = true;

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setJavascriptEnabled(true);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "/usr/bin/phantomjs");
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS,
                new String[] { "--web-security=no", "--ignore-ssl-errors=yes" });
        driver = new PhantomJSDriver(caps);
        WebDriverWait wait = new WebDriverWait(driver, 10);

        // Navegamos a la pagina
        driver.navigate().to("http://localhost:8080/Baloncesto/");
        // Esperamos un maximo de 10 segundos a que cargue la pagina
        wait.until(ExpectedConditions.titleContains("Votacion mejor jugador liga ACB"));
        // pulsamos el boton de "Poner votos a cero"
        driver.findElement(By.id("borrarVotos")).click();

        // deshabilitamos la alerta para que no interfiera en el test
        if (driver instanceof PhantomJSDriver) {
            PhantomJSDriver phantom = (PhantomJSDriver) driver;
            // Sobrescribe la función alert() de JavaScript para que no haga nada.
            phantom.executeScript("window.alert = function(){}");
            // Sobrescribe la función confirm() de JavaScript para que siempre devuelva
            // true.
            phantom.executeScript("window.confirm = function(){return true;}");
        } else
            driver.switchTo().alert().accept();

        // Pulsamos sobre el botón "Ver votos"
        driver.findElement(By.id("ver")).click();

        // Obtenemos la tabla de votos
        WebElement tabla = driver.findElement(By.id("tablaDeVotos"));
        // Obtenemos las filas de la tabla
        List<WebElement> filas = tabla.findElements(By.tagName("tr"));

        // Iteramos sobre las filas de la tabla y comprabamos que todos los votos estan
        // a 0
        for (WebElement fila : filas) {
            List<WebElement> celdas = fila.findElements(By.tagName("td"));
            if (celdas.size() != 0 && !celdas.get(1).getText().equals("0")) {
                result = false;
            }
        }
        assertEquals(true, result, "Los votos no se resetean correctamente");
        driver.close();
        driver.quit();
    }

    @Test
    public void comprobarVotoNuevoJugador() {
        
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setJavascriptEnabled(true);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "/usr/bin/phantomjs");
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS,
                new String[] { "--web-security=no", "--ignore-ssl-errors=yes" });
        driver = new PhantomJSDriver(caps);
        WebDriverWait wait = new WebDriverWait(driver, 10);

        // Navegamos a la pagina
        driver.navigate().to("http://localhost:8080/Baloncesto/");
        // Esperamos un maximo de 10 segundos a que cargue la pagina
        wait.until(ExpectedConditions.titleContains("Votacion mejor jugador liga ACB"));

        // Introduce el nombre de un nuevo jugador y selecciona "Otros"
        driver.findElement(By.name("txtOtros")).sendKeys("JugadorPrueba");
        driver.findElement(By.id("radioOtros")).click();

        // Envía el formulario votando por el nuevo jugador
        driver.findElement(By.name("B1")).click();

        // Esperamos un maximo de 10 segundos a que cargue la pagina
        wait.until(ExpectedConditions.titleContains("Votación mejor jugador liga ACB"));
        //Volvemos a la página principal
        driver.findElement(By.id("home")).click();
        // Esperamos un maximo de 10 segundos a que cargue la pagina principal
        wait.until(ExpectedConditions.titleContains("Votacion mejor jugador liga ACB"));

        // Pulsamos sobre el botón "Ver votos"
        driver.findElement(By.id("ver")).click();

        // Comprueba que el nuevo jugador aparece con 1 voto
        boolean isPresent = driver.findElements(By.xpath("//td[contains(text(), 'JugadorPrueba')]")).size() > 0;
        assertTrue(isPresent, "El jugador no se encuentra o no tiene el voto esperado.");

        // Cerrar el navegador
        driver.close();
        driver.quit();
    }
}
