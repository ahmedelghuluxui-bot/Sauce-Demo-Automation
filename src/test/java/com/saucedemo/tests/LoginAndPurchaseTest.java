package com.saucedemo.tests;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions; 
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductDetailsPage;
import org.testng.annotations.DataProvider;

public class LoginAndPurchaseTest {
    
    WebDriver driver;
    LoginPage loginPage;
    InventoryPage inventoryPage;
    ProductDetailsPage productDetailsPage;
    public void p() {
      try {
       Thread.sleep(0);
       } catch (InterruptedException e) {
          e.printStackTrace();
        }
    }

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        
        options.addArguments("--incognito");
        
        options.addArguments("--disable-features=PasswordLeakDetection");
        options.addArguments("--disable-popup-blocking");
        
        java.util.Map<String, Object> prefs = new java.util.HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);
        
        driver = new ChromeDriver(options); 
        
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://www.saucedemo.com/");
        
        loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);
        productDetailsPage = new ProductDetailsPage(driver);
        p(); 
    }

 // WORKFLOW 1: End-to-End Successful Purchase with Product Integrity Validation
    @Test(priority = 1, description = "Verify user can login, add a specific product, and validate its data integrity at checkout")
    public void testSuccessfulPurchaseWorkflow() {
        loginPage.loginToApplication("standard_user", "secret_sauce");
        p();
        Assert.assertTrue(inventoryPage.isTitleDisplayed(), "Login failed: Products page title is not displayed!");

        String expectedName = inventoryPage.getFirstProductName();
        String expectedPrice = inventoryPage.getFirstProductPrice();
        System.out.println("Expected Product: " + expectedName + " | Price: " + expectedPrice);

        inventoryPage.addBackpackToCart();
        p();
        
        Assert.assertEquals(inventoryPage.getCartItemsCount(), "1", "Cart badge count did not update correctly!");

        inventoryPage.clickCart();
        p();
        
        driver.findElement(By.id("checkout")).click(); 
        p();
        
        driver.findElement(By.id("first-name")).sendKeys("Ahmed");
        driver.findElement(By.id("last-name")).sendKeys("QA Solo Project");
        driver.findElement(By.id("postal-code")).sendKeys("12345");
        p(); 
        
        driver.findElement(By.id("continue")).click();
        p();
        
        String actualName = driver.findElement(By.className("inventory_item_name")).getText();
        String actualPrice = driver.findElement(By.className("inventory_item_price")).getText();
        System.out.println("Actual Checkout Product: " + actualName + " | Price: " + actualPrice);

        Assert.assertEquals(actualName, expectedName, "Security/Data Error: The product name at checkout does not match the added product!");
        Assert.assertEquals(actualPrice, expectedPrice, "Financial/Data Error: The product price at checkout does not match the original price!");

        driver.findElement(By.id("finish")).click();
        p();         
        String successMessage = driver.findElement(By.className("complete-header")).getText();
        Assert.assertEquals(successMessage, "Thank you for your order!", "The purchase journey workflow failed to complete successfully!");
    }

    // WORKFLOW 2: Negative Validation (Locked Out User)
    @Test(priority = 2, description = "Verify appropriate error message is displayed for locked out users")
    public void testLockedOutUserWorkflow() {
        loginPage.loginToApplication("locked_out_user", "secret_sauce");
        p();
        
        String actualError = loginPage.getErrorMessageText();
        String expectedError = "Epic sadface: Sorry, this user has been locked out.";
        
        Assert.assertEquals(actualError, expectedError, "The expected validation error for locked out user did not match!");
    }
    @Test(priority = 3, description = "Loop through all products and validate their details page structure")
    public void testAllProductsDynamicStructure() {
        loginPage.loginToApplication("standard_user", "secret_sauce");
        p();
        
        int totalProducts = inventoryPage.getProductsCount();
        System.out.println("Total products found to validate: " + totalProducts);

        for (int i = 0; i < totalProducts; i++) {
            inventoryPage.clickProductByIndex(i);
            p(); 

            Assert.assertTrue(productDetailsPage.isNameDisplayed(), "Product name is missing at index: " + i);
            Assert.assertTrue(productDetailsPage.isDescriptionDisplayed(), "Product description is missing at index: " + i);
            Assert.assertTrue(productDetailsPage.isPriceDisplayed(), "Product price is missing at index: " + i);
            Assert.assertTrue(productDetailsPage.isActionButtonDisplayed(), "Product Add/Remove button is missing at index: " + i);

            productDetailsPage.clickBackToProducts();
            p(); 
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    @DataProvider(name = "userMatrixData")
    public Object[][] getUserData() {
        return new Object[][] {
            { "standard_user", "secret_sauce", "success" },
            { "locked_out_user", "secret_sauce", "Epic sadface: Sorry, this user has been locked out." },
            { "problem_user", "secret_sauce", "success" }, 
            { "performance_glitch_user", "secret_sauce", "success" }, 
            { "error_user", "secret_sauce", "success" }, 
            { "visual_user", "secret_sauce", "success" } 
        };
    }

    @Test(dataProvider = "userMatrixData", priority = 4, description = "Execute E2E Checkout for all valid user profiles dynamic matrix")
    public void testAllUserProfilesMatrix(String username, String password, String expectedResult) {
        
        // 1. محاولة تسجيل الدخول
        loginPage.loginToApplication(username, password);
        p(); 

        if (expectedResult.equals("success")) {
            Assert.assertTrue(inventoryPage.isTitleDisplayed(), "Login failed for: " + username);

            // جلب بيانات المنتج وحفظها ديناميكياً
            String expectedName = inventoryPage.getFirstProductName();
            String expectedPrice = inventoryPage.getFirstProductPrice();

            // 2. إضافة المنتج إلى السلة والذهاب إليها
            inventoryPage.addBackpackToCart();
            p();
            inventoryPage.clickCart();
            p();
            
            // 3. بدء خطوة الشيك أوت
            driver.findElement(By.id("checkout")).click(); 
            p();
            
            // 4. تعبئة بيانات المشتري
            driver.findElement(By.id("first-name")).sendKeys("QA-" + username);
            driver.findElement(By.id("last-name")).sendKeys("Matrix Test");
            driver.findElement(By.id("postal-code")).sendKeys("54321");
            p(); 
            
            driver.findElement(By.id("continue")).click();
            p();
            
            // 🔥 [معالجة الـ Bug الذكية للـ error_user]:
            if (username.equals("error_user")) {
                // نتحقق من ظهور رسالة الخطأ المتوقعة بسبب عيب الموقع، ثم ننهي الفحص بنجاح لهذا الحساب!
                String uiError = driver.findElement(By.cssSelector("[data-test='error']")).getText();
                Assert.assertTrue(uiError.contains("Last Name is required"), "Expected error user bug was not caught!");
                System.out.println("Successfully caught the built-in bug for error_user!");
                return; // اخرج من الدالة وانتقل للمستخدم التالي في المصفوفة
            }
            
            // 5. التحقق من سلامة البيانات للحسابات السليمة الأخرى
            String actualName = driver.findElement(By.className("inventory_item_name")).getText();
            String actualPrice = driver.findElement(By.className("inventory_item_price")).getText();
            
            Assert.assertEquals(actualName, expectedName, "Data Integrity Error for user: " + username);
            Assert.assertEquals(actualPrice, expectedPrice, "Financial Data Error for user: " + username);
            
            // 6. الضغط على Finish وإغلاق الدورة
            driver.findElement(By.id("finish")).click();
            p();         
            
            String successMessage = driver.findElement(By.className("complete-header")).getText();
            Assert.assertEquals(successMessage, "Thank you for your order!", "Checkout journey failed for user: " + username);
            
        } else {
            // الحساب المحظور (Locked out)
            String actualError = loginPage.getErrorMessageText();
            Assert.assertEquals(actualError, expectedResult, "Data-Driven Failure!");
        }
    
    }
    }

