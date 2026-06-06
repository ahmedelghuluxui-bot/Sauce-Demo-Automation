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
    private By productLinks = By.className("inventory_item_name");

    public int getProductsCount() {
        return driver.findElements(productLinks).size();
    }

    public void clickProductByIndex(int index) {
        driver.findElements(productLinks).get(index).click();
    }
 // أضف هذه اللوكيتورز في الأعلى إذا لم تكن موجودة
    private By firstProductName = By.cssSelector(".inventory_item_name");
    private By firstProductPrice = By.cssSelector(".inventory_item_price");

    // دالة لجلب اسم أول منتج
    public String getFirstProductName() {
        return driver.findElement(firstProductName).getText();
    }

    // دالة لجلب سعر أول منتج
    public String getFirstProductPrice() {
        return driver.findElement(firstProductPrice).getText();
    }
}