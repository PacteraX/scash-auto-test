package com.senko.scash;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import test.BaseTest;

public class BaseScashTest extends BaseTest {

    @Parameters({"testProperties"})
    @BeforeSuite
    public void doInit(String testPropertiesPath) {
        super.doInit(testPropertiesPath);
    }
}
