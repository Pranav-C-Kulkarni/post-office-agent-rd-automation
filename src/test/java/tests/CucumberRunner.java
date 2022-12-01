package tests;

import org.junit.runner.RunWith;
import org.testng.annotations.Test;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src\\test\\resources\\features", monochrome = true, plugin = {
        "com.eis.listeners.ExtentCucumberFormatter:" }, glue = {
                "stepDefinitions" })

public class CucumberRunner{
	
}

