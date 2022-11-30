package tests;

import org.testng.annotations.Test;
import io.cucumber.junit.CucumberOptions;

@Test
@CucumberOptions(
		monochrome= true,
		features = { "src/test/resources/features/" },
		glue = { "stepDefinations" }	
		)

public class CucumberRunner{
	
}