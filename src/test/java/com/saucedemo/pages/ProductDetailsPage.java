package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductDetailsPage {
    
    WebDriver driver;

    private By productName = By.className("large_size");
    private By productDescription = By.className("inventory_details_desc");
    private By productPrice = By.className("inventory_details_price");
    private By actionButton = By.cssSelector(".btn_inventory");
    private By backToProductsButton = By.id("back-to-products");

    public ProductDetailsPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isNameDisplayed() {
        return driver.findElement(productName).isDisplayed();
    }

    public boolean isDescriptionDisplayed() {
        return driver.findElement(productDescription).isDisplayed();
    }

    public boolean isPriceDisplayed() {
        return driver.findElement(productPrice).isDisplayed();
    }

    public boolean isActionButtonDisplayed() {
        return driver.findElement(actionButton).isDisplayed();
    }

    public void clickBackToProducts() {
        driver.findElement(backToProductsButton).click();
    }
}