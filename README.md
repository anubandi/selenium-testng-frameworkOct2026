# Selenium TestNG Test Automation Framework

![Selenium Tests](https://github.com/anubandi/selenium-testng-frameworkOct2026/actions/workflows/tests.yml/badge.svg)

A UI test automation framework in Java for the practice site [saucedemo.com](https://www.saucedemo.com), built with the Page Object Model and run automatically in CI on every push.

## Tech stack
- Java 21, Selenium WebDriver
- TestNG (groups, data providers)
- Maven
- Extent Reports (HTML report)
- GitHub Actions (CI)

## Features
- **Page Object Model:** `LoginPage` and `InventoryPage` hold locators and page steps, so tests stay readable.
- **Reusable actions:** a `CommonActions` class wraps explicit waits, typing, clicking, and reading text.
- **Base test class:** `BaseTest` launches and closes the browser and creates the page objects for each test.
- **Config-driven:** browser and base URL come from `config.properties` (Chrome, Firefox, or Edge).
- **Data-driven tests:** TestNG `@DataProvider` covers several invalid-login combinations.
- **Test groups:** `testng.xml` separates `smoke` and `regression` tests.
- **Failure screenshots:** a screenshot is saved automatically when a test fails and is attached to the report.
- **HTML report:** Extent Reports output in `reports/ExtentReport.html`.
- **CI:** a GitHub Actions workflow runs the suite headless on every push and uploads the report and screenshots as an artifact.

## Project structure
```
src/test/java
 ├─ BaseTest.java            browser setup/teardown, screenshots, reporting
 ├─ CommonActions.java       reusable Selenium actions with waits
 ├─ ConfigReader.java        reads config.properties
 ├─ ExtentManager.java       report setup
 ├─ LoginPage.java           page object
 ├─ LoginTest_DataProvider.java   tests
 └─ practsise/               additional page objects and tests
src/test/resources
 └─ config.properties        browser, URL, test data
testng.xml                   test suite and groups
.github/workflows/tests.yml  CI pipeline
```

## How to run
Run the full suite:
```
mvn test
```
Run headless (as CI does):
```
mvn test -Dheadless=true
```
To change the browser, edit `browser=` in `src/test/resources/config.properties`.

After a run, open `reports/ExtentReport.html`. Failure screenshots are saved in `screenshots/`.

## CI
Every push to `main` triggers the workflow in `.github/workflows/tests.yml`, which installs Java, runs `mvn test -Dheadless=true`, and uploads the report as a build artifact.

## Author
Anu Bandi, QA Engineer , SDET.