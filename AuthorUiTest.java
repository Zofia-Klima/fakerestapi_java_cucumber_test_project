import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import java.time.Duration;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import com.google.gson.Gson;
import org.openqa.selenium.JavascriptExecutor;

public class AuthorUiTest {

    public Author[] getAllAuthorsUi()throws InterruptedException{

        //Here, I am retrieving the whole list of authors directly from the swagger UI, using Selenium.

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();

        try {
            driver.get("https://fakerestapi.azurewebsites.net/index.html");

            WebElement getAuthorsEndpoint = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='operations-Authors-get_api_v1_Authors']//div[@class='opblock-summary opblock-summary-get']")));

            getAuthorsEndpoint.click();
            
            WebElement TryItOutButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Try it out')]")));

            TryItOutButton.click();

            WebElement executeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Execute')]")));

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", executeButton);

            executeButton.click();

            By responseBodyLocator =
            By.cssSelector(".responses-wrapper .highlight-code .microlight");

            wait.until(webDriver -> {
                try {
                    WebElement responseBody = webDriver.findElement(responseBodyLocator);

                    String currentJson = responseBody.getText();

                    Gson gson = new Gson();
                    Author[] currentAuthors = gson.fromJson(currentJson, Author[].class);

                    return currentAuthors != null && currentAuthors.length > 1;

                } catch (Exception e) {
                    return false;
                }
            });

            WebElement responseBody = driver.findElement(responseBodyLocator);

            String uiJson = responseBody.getText();

            Gson gson = new Gson();

            Author[] uiAuthors = gson.fromJson(uiJson, Author[].class);


            // WebElement responseBody = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".responses-wrapper .highlight-code .microlight")));
            // Thread.sleep(3000);

            // String uiJson = responseBody.getText();
            // System.out.println(uiJson);
            // Gson gson = new Gson();

            // Author[] uiAuthors = gson.fromJson(uiJson, Author[].class);

            return uiAuthors;

        } finally {
            driver.quit();
        }
    }
}