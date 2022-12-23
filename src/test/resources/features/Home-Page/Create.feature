@Create
Feature: Creating a RD list using automation

  Background: 
    * the url should be "https://dopagent.indiapost.gov.in"

  Scenario: 
  	* we wait for 2 seconds
  	############LOGIN############
    And we are on LoginPage
  	Then we enter "DOP.MI4140030100023" into the AgentID element
  	Then we enter "$ckulkarni@8719" into the AgentPassword element
  	###THIS AREA WILL BE FOCUSED NEXT TIME###
    And we enter CAPTCHA in the alert and its inserted in the CaptchaInput box
    And we click on LoginBtn
    #########################################	
    #########START CREATING LIST OF ACCOUNTS#############
    Then the title should be "Department of Post Agent Login : Dashboard"
    And we are on DashboardPage
    Then we click on AccountsLink
    And we wait for 2 seconds
    Then we click on AccountsEnquireLink
    And we wait for 10 seconds
    Then we click on CashRadionBtn
    And we read the AddNew.txt file and enter into AccountIdInputArea input box
    And we click on FetchBtn
    And we wait for 5 seconds
    And we need open dates for all given accounts and create excel file
    Then we selected the given account Ids
    And we click on SaveBtn
    And we wait for 2 seconds
    Then we click on PayAccountsBtn
    #####################################################
    #############SAVING THE LIST TO LOCAL################
    And we wait for 1 seconds
    And we copied the reference number of given list
    And we click on ReportsLink
    And we wait for 2 seconds
    Then we are on ReportsPage
    Then we enter copied ref number into input box
    And we select "Success" from Status dropdown
    And we click on SearchBtn
    And we wait for 1 seconds
    Then we select "XLS file" from Format dropdown
    And we click on DownloadOkBtn
    #####################################################
    And we wait for 100 seconds
    