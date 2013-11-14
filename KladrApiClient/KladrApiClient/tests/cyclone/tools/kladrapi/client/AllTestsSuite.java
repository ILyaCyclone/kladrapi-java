package cyclone.tools.kladrapi.client;

import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/*
 * JUnit 4 Test Suite
 * List Test cases as comma seperated list in @Suite.SuiteClasses annotation
 * Right click and choose run within JDeveloper
 * Or run from java command line or Ant using main method
 * 
 * This class is not necessary if testing solely within JDeveloper
 */
@RunWith(Suite.class)
@Suite.SuiteClasses( { cyclone.tools.kladrapi.client.KladrApiClientTest.class })
public class AllTestsSuite {
    
      public static void main(String[] args) {
          JUnitCore.runClasses(new Class[] { AllTestsSuite.class });
      }

}