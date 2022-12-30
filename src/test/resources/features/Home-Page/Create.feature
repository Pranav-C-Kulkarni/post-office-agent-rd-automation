@Create
Feature: Creating a RD list using automation

  Background: 
    * the url should be "https://dopagent.indiapost.gov.in"

  Scenario: 
  	* we wait for 2 seconds
  	############LOGIN############
    And we are on LoginPage
  	Then we read the userId.txt file and enter into AgentID input box
  	Then we read the password.txt file and enter into AgentPassword input box
    And we enter CAPTCHA in the alert and its inserted in the CaptchaInput box
    #########################################	
    #########START CREATING LIST OF ACCOUNTS#############
    Then the title should be "Department of Post Agent Login : Dashboard"
    And we are on DashboardPage
    Then we click on AccountsLink
    And we wait for 2 seconds
    Then we click on AccountsEnquireLink
    And we wait for 10 seconds
    Then we click on CashRadionBtn
    And I read the AccountNos file and enter into AccountIdInputArea input box
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
    And we wait for 10 seconds
    #####################################################
    ##################LOG OUT############################
    And we click on LogoutLink
    And we wait for 1 seconds
    Then we click on LogoutBtn
    And we wait for 5 seconds
    