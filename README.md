# 🛒 SauceDemo Automation Project

### 🚀 Project Overview
This is a comprehensive QA automation framework built as a final portfolio project. It validates the end-to-end user journeys and core functional workflows of the **SauceDemo** e-commerce platform.

---

## 🛠️ Tech Stack & Tools
* **Language:** Java
* **Automation Tool:** Selenium WebDriver (v4.18.1)
* **Testing Framework:** TestNG
* **Build Tool:** Maven
* **IDE:** Eclipse
* **Version Control:** Git & GitHub

---

## 📋 Automated Workflows
1. **Workflow 1 (The Happy Path):** End-to-End E-Commerce Purchase Journey (Login ➔ Product Selection ➔ Cart Validation ➔ Checkout Details ➔ Financial Verification).
2. **Workflow 2 (Negative & Account Validation):** Locked-out user error handling and form constraint verification.

---

## 📂 Project Structure
```text
SauceDemoAutomation/
│
├── src/test/java/
│   ├── com.saucedemo.pages/    # Page Object Model Classes (Locators & Actions)
│   └── com.saucedemo.tests/    # TestNG Scripts & Assertions
│
├── docs/                       # Project Documentation (Test Plan & Bug Reports)
├── pom.xml                     # Maven Dependencies
└── README.md                   # Project Documentation Core
