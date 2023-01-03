package stepDefinitions;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;

public class ProjectStepDefinitions {
    WebDriver driver;
    FileInputStream fsrc;
    XSSFWorkbook wb;
    XSSFSheet sh;
    String accountNos;
    String refNum;
    ArrayList<Object[]> accountOpenDates;
    Duration duration;

    public ProjectStepDefinitions() {
    }

    public ProjectStepDefinitions(WebDriver driver) {
        this.driver = driver;
    }

    @Before
    public void launchBrowser() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/drivers/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    public static String timestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());
    }

    @Then("^the url should be \"(.*?)\"$")
    public void urlInsertion(String url) {
        driver.get(url);
    }

    @Then("^we are on (.*)$")
    public void pageIdentify(String pageObjectFile) {
        ObjectProperties.initializeObjectProperties(pageObjectFile);
    }

    @Then("^we wait for (.*) seconds$")
    public void explicitWait(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Then("we should see \"(.*?)\"$")
    public void we_should_see(String object) {
        ExpectedConditions.visibilityOfElementLocated(By.xpath(ObjectProperties.getElementProperty(object)));
    }

    @Then("^the title should be \"(.*?)\"$")
    public void titleVerify(String title) throws InterruptedException {
        String currentTitle = driver.getTitle();
        System.err.println(currentTitle);
        Thread.sleep(2000);
        if (!currentTitle.equals(title)) {
            Assert.assertEquals(currentTitle, title);
        }
    }

    @Then("^we click on (.*)$")
    public void click(String object) {
        WebElement objectToClick = driver.findElement(By.xpath(ObjectProperties.getElementProperty(object)));
        ExpectedConditions.elementToBeClickable(objectToClick);
        objectToClick.click();
    }

    @Then("^we enter \"(.*?)\" into the (.*) element")
    public void inputText(String input, String element) {
        try {
            driver.findElement(By.xpath(ObjectProperties.getElementProperty(element))).sendKeys(input);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Then("^we need \"(.*?)\" test cases from \"(.*?)\" sheet to the (.*) element$")
    public void processxlsxsheet(int number, String fileName, String element) throws IOException {
        fsrc = new FileInputStream("src\\test\\resources\\test_data\\" + fileName + ".xlsx");
        wb = new XSSFWorkbook(fsrc);
        sh = wb.getSheetAt(0);
        String dr2c1 = sh.getRow(1).getCell(0).getStringCellValue();
        ProjectStepDefinitions obj = new ProjectStepDefinitions(driver);
        obj.inputText(dr2c1, element);
    }

    @Then("^we took (.*) screenshot$")
    public void screenShot(String scenario) throws IOException {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File fs = ts.getScreenshotAs(OutputType.FILE);
        FileHandler.copy(fs, new File("src/test/resources/screenshots/" + scenario + "_" + timestamp() + ".png"));
    }

    @Then("^we enter CAPTCHA in the alert and its inserted in the (.*) box$")
    public void captchaInsertion(String inputBox) throws InterruptedException, IOException {
        String captcha = null;
        if (driver instanceof JavascriptExecutor) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            while (true) {
                js.executeScript("window.promptResponse = prompt('Please enter the CAPTCHA:');");
                Thread.sleep(15000);
                isAlertPresent();
                captcha = (String) js.executeScript("return window.promptResponse");
                if(captcha == null) {
                    String pass = new String(Files.readAllBytes(Paths.get("src/test/resources/test_data/password.txt")));
                    inputText(pass, "AgentPassword");
                    continue;
                }
                if (captcha != null && captcha.length() == 6) {
                    break;
                }
            }
            System.out.println("CAPTCHA: " + captcha + captcha.length());
            driver.findElement(By.xpath(ObjectProperties.getElementProperty(inputBox))).sendKeys(captcha);
            driver.findElement(By.xpath(ObjectProperties.getElementProperty("LoginBtn"))).click();
            Thread.sleep(2000);
            if (!(driver.getTitle().equals("Department of Post Agent Login : Dashboard"))) {
                String pass = new String(Files.readAllBytes(Paths.get("src/test/resources/test_data/password.txt")));
                inputText(pass, "AgentPassword");
                captchaInsertion(inputBox);
            }
        }
    }

    @Then("^I read the AccountNos file and enter into (.*) input box$")
    public void readTxtFile(String element) throws IOException {
        String data = new String(Files.readAllBytes(Paths.get("src/test/resources/test_data/AddNew.txt")));
        // Assigning value to global variable
        this.accountNos = data;
        System.err.println("These are the accounts: " + accountNos);
        inputText(accountNos, element);
    }

    @Then("^we read the (.*) file and enter into (.*) input box$")
    public void readPassword(String filename, String element) throws IOException {
        String data = new String(Files.readAllBytes(Paths.get("src/test/resources/test_data/" + filename)));
        inputText(data, element);
    }

    @Then("^we selected the given account Ids$")
    public void selectAccounts() throws InterruptedException {
        WebElement checkbox;
        String[] filter = this.accountNos.split(",\\r?\\n");
        for (int i = 0; i < filter.length; i++) {
            if (i == 10 || i == 20) {
                driver.findElement(By.xpath(ObjectProperties.getElementProperty("NextPageBtn"))).click();
                Thread.sleep(1000);
            }
            checkbox = driver.findElement(By.xpath("//input[@value=" + i + "]"));
            checkbox.click();
        }
    }

    @Then("^we copied the reference number of given list$")
    public void copyRefNum() {
        String refNumAlert = driver.findElement(By.xpath("//*[@role='alert']")).getText();
        String[] alertString = refNumAlert.split(" ");
        this.refNum = alertString[7].replace(".", "");
        System.err.println("The RD list ref no: " + refNum);
    }

    @Then("^we enter copied ref number into input box$")
    public void enterRefNum() {
        inputText(this.refNum, "ReferenceNumberInputBox");
    }

    @Then("^we select \"(.*?)\" from (.*) dropdown$")
    public void selectDropdown(String value, String element) {
        Select select = new Select(driver.findElement(By.xpath(ObjectProperties.getElementProperty(element))));
        select.selectByVisibleText(value);
    }

    @Then("^we are adding all ASLAAS numbers for new accounts$")
    public void ASLAAS_Number() throws InterruptedException, IOException {
        int paidMonths;
        String[] filter = this.accountNos.split(",\\r?\\n");
        Arrays.sort(filter);
        System.err.println(Arrays.toString(filter));
        System.err.println(filter.length);
        List<Integer> newAccounts = new ArrayList<>();
        for (int i = 0; i < filter.length; i++) {
            if (i == 10 || i == 20) {
                driver.findElement(By.xpath(ObjectProperties.getElementProperty("NextPageBtn"))).click();
                Thread.sleep(1000);
            }
            paidMonths = Integer.parseInt(driver
                    .findElement(
                            By.xpath("//*[@id='HREF_CustomAgentRDAccountFG.MONTH_PAID_UPTO_ALL_ARRAY[" + i + "]']"))
                    .getText());
            if (paidMonths == 1) {
                newAccounts.add(i);
            }
        }
        Thread.sleep(1000);
        if (newAccounts.size() != 0) {
            driver.findElement(By.xpath(ObjectProperties.getElementProperty("UpdateASLAASLink"))).click();
            Thread.sleep(1000);
            for (int newAccount = 0; newAccount < newAccounts.size(); newAccount++) {
                driver.findElement(By.xpath("//*[@id='CustomAgentAslaasNoFG.RD_ACC_NO']"))
                        .sendKeys(filter[newAccounts.get(newAccount)]);
                driver.findElement(By.xpath("//*[@id='CustomAgentAslaasNoFG.ASLAAS_NO']"))
                        .sendKeys(filter[newAccounts.get(newAccount)]
                                .substring(filter[newAccounts.get(newAccount)].length() - 5));
                driver.findElement(By.xpath("//*[@id='LOAD_CONFIRM_PAGE']")).click();
                driver.findElement(By.xpath("//*[@id='ADD_FIELD_SUBMIT']")).click();
                driver.findElement(By.xpath("//*[@class='greenbg']")).isDisplayed();
            }
            driver.findElement(By.xpath(ObjectProperties.getElementProperty("AccountsEnquireLink"))).click();
            driver.findElement(By.xpath(ObjectProperties.getElementProperty("CashRadionBtn"))).click();
            readTxtFile("AccountIdInputArea");
            driver.findElement(By.xpath(ObjectProperties.getElementProperty("FetchBtn"))).click();
        }

    }

    @Then("^we need open dates for all given accounts and create excel file$")
    public void createExcel() throws InterruptedException, FileNotFoundException {
        accountOpenDate();
        // create blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();
        // Create a blank sheet
        XSSFSheet sheet = workbook.createSheet("AccountIds");
        int rownum = 0;
        Row row;
        Cell cell;
        for (Object[] openDate : accountOpenDates) {
            row = sheet.createRow(rownum++);

            int cellnum = 0;
            for (Object obj : openDate) {
                cell = row.createCell(cellnum);
                if (obj instanceof String) {
                    cell.setCellValue((String) obj);
                }
            }
        }
        try {
            // Write the workbook in file system
            FileOutputStream out = new FileOutputStream(new File("src\\test\\resources\\test_data\\AccountIds.xlsx"));
            workbook.write(out);
            out.close();
            System.out.println("AccountIds.xlsx has been created successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void accountOpenDate() throws InterruptedException {
        int paidMonths;
        List<String> months;
        String nextMonthText;
        String[] nextMonthDateArray;
        String accountOpenDate;
        ArrayList<Object[]> list = new ArrayList<Object[]>();
        String[] filter = this.accountNos.split(",\\r?\\n");
        LocalDate date;
        LocalDate earlier;
        for (int i = 0; i < filter.length; i++) {
            if (i == 10 || i == 20) {
                driver.findElement(By.xpath(ObjectProperties.getElementProperty("NextPageBtn"))).click();
                Thread.sleep(1000);
            }
            paidMonths = Integer.parseInt(driver
                    .findElement(
                            By.xpath("//*[@id='HREF_CustomAgentRDAccountFG.MONTH_PAID_UPTO_ALL_ARRAY[" + i + "]']"))
                    .getText());
            months = Arrays.asList("null", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov",
                    "Dec");
            nextMonthText = driver
                    .findElement(By.xpath(
                            "//*[@id='HREF_CustomAgentRDAccountFG.NEXT_RD_INSTALLMENT_DATE_ALL_ARRAY[" + i + "]']"))
                    .getText();
            nextMonthDateArray = nextMonthText.split("-");
            date = LocalDate.of(Integer.valueOf(nextMonthDateArray[2]), months.indexOf(nextMonthDateArray[1]),
                    Integer.valueOf(nextMonthDateArray[0]));
            earlier = date.minusMonths(paidMonths);
            if (earlier.getMonthValue() < 10) {
                accountOpenDate = nextMonthDateArray[0] + "/0" + earlier.getMonthValue() + "/" + earlier.getYear();
            } else {
                accountOpenDate = nextMonthDateArray[0] + "/" + earlier.getMonthValue() + "/" + earlier.getYear();
            }
            System.err.println("The account open date is: " + accountOpenDate);
            if (i == (filter.length - 1) && filter.length > 10) {
                driver.findElement(By.xpath(ObjectProperties.getElementProperty("GoToPageInputBox"))).sendKeys("1");
                driver.findElement(By.xpath(ObjectProperties.getElementProperty("GoBtn"))).click();
                Thread.sleep(1000);
            }
            list.add(new Object[] { accountOpenDate });
        }
        this.accountOpenDates = list;
    }

    public boolean isAlertPresent() {
        try {
            Alert a = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.alertIsPresent());
            if (a != null) {
                System.out.println("Alert is present");
                driver.switchTo().alert().accept();
                return true;
            } else {
                throw new Throwable();
            }
        } catch (Throwable e) {
            System.err.println("Alert isn't present!!");
            return false;
        }

    }

    @After
    public void afterTest() {
        driver.quit();
    }

}