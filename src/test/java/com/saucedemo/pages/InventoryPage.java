package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class InventoryPage {
    
    WebDriver driver;

    private By pageTitle = By.className("title");
    private By addToCartBackpack = By.id("add-to-cart-sauce-labs-backpack");
    private By cartBadge = By.className("shopping_cart_badge");
    private By cartIcon = By.className("shopping_cart_link");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isTitleDisplayed() {
        return driver.findElement(pageTitle).isDisplayed();
    }

    public void addBackpackToCart() {
        driver.findElement(addToCartBackpack).click();
    }

    public String getCartItemsCount() {
        return driver.findElement(cartBadge).getText();
    }

    public void clickCart() {
        driver.findElement(cartIcon).click();
    }
 // أضف هذا اللوكيتور في الأعلى مع باقي المعرفات
    private By productLinks = By.className("inventory_item_name");

    // دالة لجلب عدد المنتجات الكلي في الصفحة
    public int getProductsCount() {
        return driver.findElements(productLinks).size();
    }

    // دالة للضغط على المنتج بناءً على رقمه في اللوب (يبدأ من 0)
    public void clickProductByIndex(int index) {
        driver.findElements(productLinks).get(index).click();
    }
}