package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class InventoryPage {
    
    WebDriver driver;

    // Locators
    private By pageTitle = By.className("title");
    private By addToCartBackpack = By.id("add-to-cart-sauce-labs-backpack");
    private By cartBadge = By.className("shopping_cart_badge");
    private By cartIcon = By.className("shopping_cart_link");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
    }

    // Actions
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
}