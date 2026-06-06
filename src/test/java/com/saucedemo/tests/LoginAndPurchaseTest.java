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
        // 1. إنشاء خيارات مخصصة لمتصفح جوجل كروم
        ChromeOptions options = new ChromeOptions();
        
        // 2. تفعيل وضع التصفح الخفي لمنع النافذة نهائياً وعزل الكاش
        options.addArguments("--incognito");
        
        // 3. الإعدادات الاحتياطية الإضافية لتعطيل النوافذ
        options.addArguments("--disable-features=PasswordLeakDetection");
        options.addArguments("--disable-popup-blocking");
        
        // 4. منع مدير كلمات المرور الداخلي من العمل
        java.util.Map<String, Object> prefs = new java.util.HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);
        
        // 5. تشغيل الكروم وتمرير الإعدادات المحصنة إليه
        driver = new ChromeDriver(options); 
        
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://www.saucedemo.com/");
        
        loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);
        productDetailsPage = new ProductDetailsPage(driver);
        p(); 
    }

    // WORKFLOW 1: End-to-End Successful Purchase
    @Test(priority = 1, description = "Verify user can login and complete a successful purchase workflow")
    public void testSuccessfulPurchaseWorkflow() {
        loginPage.loginToApplication("standard_user", "secret_sauce");
        p();
        
        Assert.assertTrue(inventoryPage.isTitleDisplayed(), "Login failed: Products page title is not displayed!");

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
 // 1. الداتا بروفايدر: يحمل اسم المستخدم، كلمة المرور، ونوع الحالة (success أو اسم الإيرور المتوقع)
    @DataProvider(name = "userMatrixData")
    public Object[][] getUserData() {
        return new Object[][] {
            { "standard_user", "secret_sauce", "success" },
            { "locked_out_user", "secret_sauce", "Epic sadface: Sorry, this user has been locked out." },
            { "problem_user", "secret_sauce", "success" }, // مستخدم يواجه مشاكل في الصور ولكنه يسجل الدخول
            { "performance_glitch_user", "secret_sauce", "success" }, // مستخدم يواجه تأخير زمني ولكنه يدخل
            { "error_user", "secret_sauce", "success" }, // مستخدم يواجه أخطاء برمجية عند الضغط ولكنه يدخل
            { "visual_user", "secret_sauce", "success" } // مستخدم يرى عيوب في التصميم ولكنه يدخل
        };
    }

    // 2. التست كيس الشاملة التي تقرأ من الداتا بروفايدر وتفحص الحالات الستة تلقائياً
    @Test(dataProvider = "userMatrixData", priority = 4, description = "Execute Data-Driven testing for all accepted application user profiles")
    public void testAllUserProfilesMatrix(String username, String password, String expectedResult) {
        
        // تنفيذ عملية تسجيل الدخول
        loginPage.loginToApplication(username, password);
        p(); // انتظر 3 ثوانٍ لتشاهد الحالة أمامك

        if (expectedResult.equals("success")) {
            // إذا كانت الحالة متوقع لها النجاح: نتحقق من فتح صفحة المنتجات بنجاح وعنوانها ظاهر
            Assert.assertTrue(inventoryPage.isTitleDisplayed(), "Data-Driven Failure: User '" + username + "' was expected to login successfully but failed!");
        } else {
            // إذا كانت الحالة متوقع لها الفشل (مثل locked_out_user): نتحقق من رسالة الخطأ المطابقة تماماً
            String actualError = loginPage.getErrorMessageText();
            Assert.assertEquals(actualError, expectedResult, "Data-Driven Failure: Error message for '" + username + "' did not match expectations!");
        }
    }
}
