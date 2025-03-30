package com.qa.opencart.base;

import java.util.Properties;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.microsoft.playwright.Page;
import com.qa.opencart.factory.PlaywrightFactory;
import com.qa.opencart.pages.HomePage;
import com.qa.opencart.pages.LoginPage;

import org.testng.annotations.Optional;

public class BaseTest {

	protected PlaywrightFactory pf;
	protected Page page;
	protected Properties prop;

	protected HomePage homePage;
	protected LoginPage loginPage;

	@Parameters({ "browser" })
	@BeforeTest
	public void setup(@Optional("edge") String browserName) {
		pf = new PlaywrightFactory();
		prop = pf.init_prop();
		if (browserName != null) {
			prop.setProperty("browser", browserName);
		} else {
			prop.setProperty("browser", "edge"); // Default to Edge if browserName is not provided
		}
		page = pf.initBrowser(prop);
		homePage = new HomePage(page);
		loginPage = new LoginPage(page); // Initialize loginPage if needed
	}

	@AfterTest
	public void tearDown() {
		if (page != null) {
			page.context().browser().close();
		}
	}
}