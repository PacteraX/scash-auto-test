package com.senko.scash.trm;

import com.senko.scash.trm.controller.MessageController;
import com.senko.scash.trm.controller.TestClass;
import org.testng.annotations.Test;

public class SimpleTest {
    @Test
    public void doGetListByCodesTest() {
        TestClass simpleClass = new TestClass();
        simpleClass.getListByCodes();
        System.out.println("Test is running!");
    }
}
