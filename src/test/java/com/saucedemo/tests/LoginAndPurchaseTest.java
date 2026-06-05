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

    // دالة مساعدة لعمل تpause (انتظار) لمدة 3 ثوانٍ بشكل نظيف
    public void p() {
        try {
            Thread.sleep(3000); // 3000 ميللي ثانية = 3 ثوانٍ
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://www.saucedemo.com/");
        
        loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);
        p(); // انتظر 3 ثوانٍ بعد فتح الموقع
    }

    // ==========================================
    // WORKFLOW 1: End-to-End Successful Purchase
    // ==========================================
    @Test(priority = 1, description = "Verify user can login and complete a successful purchase workflow")
    public void testSuccessfulPurchaseWorkflow() {
        // 1. تسجيل الدخول
        loginPage.loginToApplication("standard_user", "secret_sauce");
        p(); // انتظر 3 ثوانٍ لرؤية صفحة المنتجات
        
        Assert.assertTrue(inventoryPage.isTitleDisplayed(), "Login failed: Products page title is not displayed!");

        // 2. إضافة المنتج إلى السلة
        inventoryPage.addBackpackToCart();
        p(); // انتظر 3 ثوانٍ لرؤية عداد السلة وهو يتحدث
        
        Assert.assertEquals(inventoryPage.getCartItemsCount(), "1", "Cart badge count did not update correctly!");

        // 3. الانتقال إلى السلة وضغط Checkout
        inventoryPage.clickCart();
        p(); // انتظر 3 ثوانٍ داخل صفحة السلة
        
        driver.findElement(By.id("checkout")).click(); 
        p(); // انتظر 3 ثوانٍ لرؤية صفحة تعبئة البيانات
        
        // 4. تعبئة بيانات الشحن
        driver.findElement(By.id("first-name")).sendKeys("Ahmed");
        driver.findElement(By.id("last-name")).sendKeys("QA");
        driver.findElement(By.id("postal-code")).sendKeys("12345");
        p(); // انتظر 3 ثوانٍ لمشاهدة البيانات المكتوبة قبل الانتقال
        
        driver.findElement(By.id("continue")).click();
        p(); // انتظر 3 ثوانٍ في صفحة مراجعة السعر الإجمالي Overview
        
        // 5. تأكيد الطلب النهائي
        driver.findElement(By.id("finish")).click();
        p(); // انتظر 3 ثوانٍ في صفحة النجاح لرؤية رسالة "Thank you" بوضوح
        
        String successMessage = driver.findElement(By.className("complete-header")).getText();
        Assert.assertEquals(successMessage, "Thank you for your order!", "The purchase journey workflow failed to complete successfully!");
    }

    // ==========================================
    // WORKFLOW 2: Negative Validation (Locked Out User)
    // ==========================================
    @Test(priority = 2, description = "Verify appropriate error message is displayed for locked out users")
    public void testLockedOutUserWorkflow() {
        // 1. محاولة تسجيل الدخول بمستخدم محظور
        loginPage.loginToApplication("locked_out_user", "secret_sauce");
        p(); // انتظر 3 ثوانٍ لتتمكن اللجنة من قراءة رسالة الخطأ الحمراء بالكامل
        
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