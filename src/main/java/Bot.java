import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Bot {

    public void runBot() {
        // Configure item details
        String itemCollection = "";
        String itemName = "";
        String itemSize = "";

        // Configure delivery details
        String email = "";
        String firstName = "";
        String lastName = "";
        String address = "";
        String city = "";
        String country = "";
        String postcode = "";
        String phone = "";

        // Create Chrome web driver and navigate to site
        WebDriver driver = new ChromeDriver();
        driver.get("https://shop.palaceskateboards.com/collections/" + itemCollection);

        // Search for item and click it
        String itemXpath = "//*[@id=\"product-loop\"]/div[1]/div/a/h3";
        boolean itemFound = false;
        while (!itemFound) {
            WebElement element = driver.findElement(By.xpath(itemXpath));
            if (element.getAttribute("innerText").equals(itemName)) {
                element.click();
                itemFound = true;
            } else {
                itemXpath = getXPathOfNextItem(itemXpath);
            }
        }

        // Wait until page has fully loaded
        WebDriverWait wait = new WebDriverWait(driver, 6); //you can play with the time integer  to wait for longer than 15 seconds.`
        wait.until(ExpectedConditions.titleContains(itemName + " | Palace Skateboards"));

        // Select size of item
        selectDropdownOption(itemSize, "product-select", driver);

        // Click add to cart button
        WebElement addToCartButton = driver.findElement(By.className("cart-btn"));
        addToCartButton.click();

        // Click cart button one it becomes visible
        WebElement cartButton = driver.findElement(By.className("cart-heading"));
        wait.until(ExpectedConditions.visibilityOf(cartButton));
        cartButton.click();

        // Wait until page has fully loaded then click checkout button
        wait.until(ExpectedConditions.titleContains("Your Shopping Cart | Palace Skateboards"));
        WebElement checkoutButton = driver.findElement(By.id("checkout"));
        checkoutButton.click();

        // Wait until page has loaded then fill in delivery detail fields
        wait.until(ExpectedConditions.titleContains("Customer information - Palace Skateboards - Checkout"));
        sendKeysToInput(email, "checkout_email", driver);
        WebElement marketingCheckbox = driver.findElement(By.id("checkout_buyer_accepts_marketing"));
        marketingCheckbox.click();
        sendKeysToInput(firstName, "checkout_shipping_address_first_name", driver);
        sendKeysToInput(lastName,"checkout_shipping_address_last_name" , driver);
        sendKeysToInput(address,"checkout_shipping_address_address1" , driver);
        sendKeysToInput(city,"checkout_shipping_address_city" , driver);
        selectDropdownOption(country, "checkout_shipping_address_country", driver);
        sendKeysToInput(postcode,"checkout_shipping_address_zip" , driver);
        sendKeysToInput(phone, "checkout_shipping_address_phone", driver);
    }

    // Use driver to select specified option from dropdown list
    public void selectDropdownOption(String option, String selectId, WebDriver driver) {
        Select select = new Select(driver.findElement(By.id(selectId)));
        select.selectByVisibleText(option);
    }

    // Use driver to input specific text into field
    public void sendKeysToInput(String keys, String inputId, WebDriver driver) {
        WebElement inputElement = driver.findElement(By.id(inputId));
        inputElement.sendKeys(keys);
    }

    // Get the xpath of the next item on the feed
    public String getXPathOfNextItem(String xpath) {
        Pattern pattern = Pattern.compile("div\\[(\\d+)]");
        Matcher matcher = pattern.matcher(xpath);

        if(matcher.find()) {
            int divIndex = Integer.valueOf(matcher.group(1));
            return "//*[@id=\"product-loop\"]/div[" + ++divIndex + "]/div/a/h3";
        }
        return xpath;
    }
}
