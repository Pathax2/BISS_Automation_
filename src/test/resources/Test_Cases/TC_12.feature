Feature:  BISS_25.4_Sprint_2 TC1 for BISSAGL-21680 - Login Page

  @regression
  Scenario: AT-TC-01 - Individual Login page
    Given user on individual login page
    When clicks on new agent login button
    When Agent Enter "MERVSTEP1" as Individual's Username
    # G1850473 1013076P 4827195O IE0414005 PADDYMORGAN TRAYNORA 6858291D
    And clicks on Login button
    And enter password
    And clicks on Login button
    Then External User Enters sms OTP
    And clicks on Login button
    #Then Agent Click on the "Close" personal details dialog issue
    And Click on the Basic Income Support for Sustainability application
    Then Click on the Agent BISS "Home" Tab

  @regression
  Scenario: AT-TC-02 - Individual Login page Negative Flow
    Given user on individual login page
    When clicks on new agent login button
    When Agent Enter "MERVST" as Individual's Username
    # G1850473 1013076P 4827195O IE0414005 PADDYMORGAN TRAYNORA 6858291D
    And clicks on Login button
    And enter password
    And clicks on Login button
    Then agent gets error message
    And user clicks on Cancel button
    And user verifies "Privacy Statement" link
    And user verifies "Need Help" link
    When Agent Enter "6858291D" as Individual's Username
    And clicks on Login button
    And enter password
    And verifies eye icon is present
    And user verifies "Forgot Password" link
    And user verifies "Back to Login" element is present
    And user verifies "terms-link" button of "Terms"
    And user verifies "helpdesk-link" button of "Helpdesk"
    And clicks on Login button
    Then External User Enters sms OTP
    And clicks on Login button
    #Then Agent Click on the "Close" personal details dialog issue
    And Click on the Basic Income Support for Sustainability application
    Then Click on the Agent BISS "Home" Tab

  @regression
  Scenario: AT-TC-03 - Agent Login page
    Given user on login page
    When clicks on new agent login button
    When enters username
    And clicks on Login button
   # And Agent Enters the Pin Number
    And enter password
    And clicks on Login button
    Then External User Enters sms OTP
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Click on the Agent BISS "Home" Tab

  @regression
  Scenario: AT-TC-04 - Agent Login page Negative Flow
    Given user on login page
    When clicks on new agent login button
    When Enters Incorrect Username
    And clicks on Login button
    And enter password
    And clicks on Login button
    Then agent gets error message
    And user clicks on Cancel button
    And user verifies "Privacy Statement" link
    And user verifies "Need Help" link
    When enters username
    And clicks on Login button
   # And Agent Enters the Pin Number
    And enter password
    And verifies eye icon is present
    And user verifies "Forgot Password" link
    And user verifies "Cancel" element is present
    And user verifies "terms-link" button of "Terms"
    And user verifies "helpdesk-link" button of "Helpdesk"
    And clicks on Login button
    Then External User Enters sms OTP
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Click on the Agent BISS "Home" Tab

  @regression
  Scenario: AT-TC-05 - Agent Dashboard
    Given user on login page
    When clicks on new agent login button
    When enters username
    And clicks on Login button
   # And Agent Enters the Pin Number
    And enter password
    And clicks on Login button
    Then External User Enters sms OTP
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Click on the Agent BISS "Home" Tab
    And Agent can see "BISS Applications 2026" frame on the Agent Dashboard
    And Agent can see "Quick Links" frame on the Agent Dashboard
    And Agent can see "News" frame on the Agent Dashboard
    And Agent can see "Recent Correspondence" frame on the Agent Dashboard
    Then Click on the Agent BISS "Home" Tab
    And Agent verifies icon "contact-us" is present on Dashboard Page
    And Agent verifies icon "help" is present on Dashboard Page
    And Agent verifies icon "profile" is present on Dashboard Page

  @regression
  Scenario: AT-TC-06 - Display MyClients Screen
    Given user on login page
    When clicks on new agent login button
    When enters username
    And clicks on Login button
   # And Agent Enters the Pin Number
    And enter password
    And clicks on Login button
    Then External User Enters sms OTP
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Click on the Agent BISS "Home" Tab
    Then Click on the Agent BISS "My Clients" Tab

  @regression
  Scenario: AT-TC-07 - Sort MyClients Screen
    Given user on login page
    When clicks on new agent login button
    When enters username
    And clicks on Login button
   # And Agent Enters the Pin Number
    And enter password
    And clicks on Login button
    Then External User Enters sms OTP
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Click on the Agent BISS "Home" Tab
    Then Click on the Agent BISS "My Clients" Tab
    And sort name column in ascending and descending order
    And sort herd number column in ascending and descending order

  @regression
  Scenario: AT-TC-08 - Pagination of MyClients Screen
    Given user on login page
    When clicks on new agent login button
    When enters username
    And clicks on Login button
   # And Agent Enters the Pin Number
    And enter password
    And clicks on Login button
    Then External User Enters sms OTP
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Click on the Agent BISS "Home" Tab
    Then Click on the Agent BISS "My Clients" Tab
    And user verifies each page from items per page dropdown

  @regression
  Scenario: AT-TC-09 - Filtering of MyClients Screen
    Given user on login page
    When clicks on new agent login button
    When enters username
    And clicks on Login button
   # And Agent Enters the Pin Number
    And enter password
    And clicks on Login button
    Then External User Enters sms OTP
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Click on the Agent BISS "Home" Tab
    Then Click on the Agent BISS "My Clients" Tab
    And Agent Select " Not Started " Quick Filter
    And Agent Select " Draft " Quick Filter
    And Agent Select " Submitted " Quick Filter
    And Agent Select " View all " Quick Filter
    And Agent Select " Herd Expired " Quick Filter


  @regression
  Scenario: AT-TC-10 - Row Accordian - Submitted Herd
    Given user on login page
    When clicks on new agent login button
    When enters username
    And clicks on Login button
   # And Agent Enters the Pin Number
    And enter password
    And clicks on Login button
    Then External User Enters sms OTP
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Click on the Agent BISS "Home" Tab
    Then Click on the Agent BISS "My Clients" Tab
    And Agent Select " Submitted " Quick Filter
    And user clicks "keyboard_arrow_down" accordion arrow icon at the far right of the row of any submitted Herd
    And user sees Payment Details
    And Agent Click On "View payments" Farmer Dashboard Button
    And User navigates back
    And user clicks "keyboard_arrow_down" accordion arrow icon at the far right of the row of any submitted Herd

  @regression
  Scenario: AT-TC-11 - Row Accordian - Not Submitted Herd
    Given user on login page
    When clicks on new agent login button
    When enters username
    And clicks on Login button
   # And Agent Enters the Pin Number
    And enter password
    And clicks on Login button
    Then External User Enters sms OTP
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Click on the Agent BISS "Home" Tab
    Then Click on the Agent BISS "My Clients" Tab
    And Agent Select " Not Started " Quick Filter
    And user clicks "keyboard_arrow_down" accordion arrow icon at the far right of the row of any submitted Herd
    And user sees Payment Details
    And Agent Click On "View payments" Farmer Dashboard Button
    And User navigates back
    And user clicks "keyboard_arrow_down" accordion arrow icon at the far right of the row of any submitted Herd

  @regression
  Scenario: AT-TC-12 - Search by Herd Number
    Given user on login page
    When clicks on new agent login button
    When enters username
    And clicks on Login button
   # And Agent Enters the Pin Number
    And enter password
    And clicks on Login button
    Then External User Enters sms OTP
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Click on the Agent BISS "Home" Tab
    Then Click on the Agent BISS "My Clients" Tab
    Then Agent Search for Herd Number "5"
    And user click on X icon to clear search bar

  @regression
  Scenario: AT-TC-13 - Search by Herd Name
    Given user on login page
    When clicks on new agent login button
    When enters username
    And clicks on Login button
   # And Agent Enters the Pin Number
    And enter password
    And clicks on Login button
    Then External User Enters sms OTP
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Click on the Agent BISS "Home" Tab
    Then Click on the Agent BISS "My Clients" Tab
    Then Agent Search for Herd Number "!£$"
    And user should see no list of herds are showing
    And user click on X icon to clear search bar

  @regression
  Scenario: AT-TC-14 - Export to Excel
    Given user on login page
    When clicks on new agent login button
    When enters username
    And clicks on Login button
   # And Agent Enters the Pin Number
    And enter password
    And clicks on Login button
    Then External User Enters sms OTP
    And clicks on Login button
    And Click on the Basic Income Support for Sustainability application
    Then Click on the Agent BISS "Home" Tab
    Then Click on the Agent BISS "My Clients" Tab
    And Agent Select " Export to Excel " Quick Filter
    And user should see excel file has been downloaded
