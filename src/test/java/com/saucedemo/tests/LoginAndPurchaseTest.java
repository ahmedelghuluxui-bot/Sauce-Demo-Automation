package com.saucedemo.tests;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;

public class LoginAndPurchaseTest {
    
    WebDriver driver;
    LoginPage loginPage;
    InventoryPage inventoryPage;

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://www.saucedemo.com/");
        
        loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);
    }

    // WORKFLOW 1: End-to-End Successful Purchase
    @Test(priority = 1, description = "Verify user can login and complete a successful purchase workflow")
    public void testSuccessfulPurchaseWorkflow() {
        loginPage.loginToApplication("standard_user", "secret_sauce");
        
        Assert.assertTrue(inventoryPage.isTitleDisplayed(), "Login failed: Products page title is not displayed!");

        inventoryPage.addBackpackToCart();
        
        Assert.assertEquals(inventoryPage.getCartItemsCount(), "1", "Cart badge count did not update correctly!");

        inventoryPage.clickCart();
        driver.findElement(By.id("checkout")).click();
        driver.findElement(By.id("first-name")).sendKeys("Ahmed");
        driver.findElement(By.id("last-name")).sendKeys("QA");
        driver.findElement(By.id("postal-code")).sendKeys("12345");
        driver.findElement(By.id("continue")).click();
        
        driver.findElement(By.id("finish")).click();
        
        String successMessage = driver.findElement(By.className("complete-header")).getText();
        Assert.assertEquals(successMessage, "Thank you for your order!", "The purchase journey workflow failed to complete successfully!");
    }

    // WORKFLOW 2: Negative Validation (Locked Out User)
    @Test(priority = 2, description = "Verify appropriate error message is displayed for locked out users")
    public void testLockedOutUserWorkflow() {
        loginPage.loginToApplication("not valid user", "secret_sauce");
        
        String actualError = loginPage.getErrorMessageText();
        String expectedError = "Epic sadface: Sorry, this user has been locked out.";
        
        Assert.assertEquals(actualError, expectedError, "The expected validation error for locked out user did not match!");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}