Feature: Regression Pack for Openshift Home Screen Cases

  Background:
    Given user on OpenShift staff login page

  @regression
  Scenario: AT-TC-00 - Run all Queries Required for Tests
    Then Get 15 Herds with Submission from DataBase
    Then Get 15 Reference Numbers from Database
    Then Get 15 LPIS Parcels from Database

  @tmslink=BISSAGL-18521
  @regression
  Scenario: AT-TC-01 - BISS_25.1.Sprint_4 TC1 for BISSAGL-12980 Openshift Home Screen Application Search Regression Test(herd search)
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as Herd Number with Submission from row 7 in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button

  @tmslink=BISSAGL-18524
  @regression
  Scenario: AT-TC-02 - BISS_25.1.Sprint_4 TC2 for BISSAGL-12980 Openshift Home Screen Application Search Regression Test(Invalid herd search validation)
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as "ASUDASHADI" in the "herdNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Check "The herd number you entered is invalid" Error Message without span

  @tmslink=BISSAGL-18541
  @regression
  Scenario: AT-TC-03 - BISS_25.1.Sprint_4 TC3 for BISSAGL-12980 Openshift Home Screen Application Search Regression Test(Herd search validation without data)
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application
    Then Staff Click on the "Search" button
    And Staff Check "The herd number you entered is invalid" Error Message without span
    And Staff Check "The reference number you entered is invalid" Error Message without span

  @tmslink=BISSAGL-18544
  @regression
  Scenario: AT-TC-04 - BISS_25.1.Sprint_4 TC4 for BISSAGL-12980 Openshift Home Screen Application Search Regression Test(Reference no. search)
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application
    Then Staff Enter Value as Reference Number from row 4 in the "referenceNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click On the "application-search-table" table "View" link number 1

  @tmslink=BISSAGL-18546
  @regression
  Scenario: AT-TC-05 - BISS_25.1.Sprint_4 TC5 for BISSAGL-12980 Openshift Home Screen Application Search Regression Test(Farmer Search)
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application
    And Staff Select "Farmer" as Search By Type
    Then Staff Enter Value as "mc" in the "surname" field
    Then Staff Select "county" dropdown value as "Donegal"
    Then Staff Click on the "Search" button

  @tmslink=BISSAGL-18548
  @regression
  Scenario: AT-TC-06 - BISS_25.1.Sprint_4 TC6 for BISSAGL-12980 Openshift Home Screen Application Search Regression Test(Farmer search validation)
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application
    And Staff Select "Farmer" as Search By Type
    Then Staff Enter Value as "123" in the "surname" field
    Then Staff Select "county" dropdown value as "Donegal"
    Then Staff Click on the "Search" button

  @tmslink=BISSAGL-18653
  @regression
  Scenario: AT-TC-07 - BISS_25.1.Sprint_4 TC7 for BISSAGL-12980 Openshift Home Screen Parcel Search Regression Test (Valid parcel no.)
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Parcels" tab
    Then Staff Enter Value as LPIS Parcel Number from row 8 in the "parcelNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    And Staff Click on the "View Claimants" Action Button
    Then Staff Click on the "Cancel" button
    And Staff Click on the "View Commonage" Action Button
    And Staff Click on the "Parcel History" Action Button
    Then Staff Click on the "Close" button
    And Staff Click on the "View Map" Action Button
    Then Agent Click on "Return to Land Details" button in the GIS Map Application NavLink

  @tmslink=BISSAGL-18655
  @regression
  Scenario: AT-TC-08 - BISS_25.1.Sprint_4 TC8 for BISSAGL-12980 Openshift Home Screen Parcel search Regression Test (Parcel no. validations)
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Parcels" tab
    Then Staff Enter Value as "adssdasd" in the "parcelNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    Then Staff Click on the "Clear" button
#    And Staff Check "The herd number you entered is invalid" Error Message without span
    Then Staff Enter Value as " " in the "parcelNumber" field
    Then Staff Click on the "Search" button

  @tmslink=BISSAGL-18657
  @regression
  Scenario: AT-TC-09 - BISS_25.1.Sprint_4 TC9 for BISSAGL-12980 Openshift Home Screen Reference No. Regression Test (Valid reference no. search and filters)
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Parcels" tab
    Then Staff Enter Value as "adssdasd" in the "parcelNumber" field
    Then Staff Select "schemeYear" dropdown value as "2024"
    Then Staff Click on the "Search" button
    Then Staff Click on the "Clear" button
#    And Staff Check "The herd number you entered is invalid" Error Message without span
    Then Staff Enter Value as " " in the "parcelNumber" field
    Then Staff Click on the "Search" button

  @tmslink=BISSAGL-18659
  @regression
  Scenario: AT-TC-10 - BISS_25.1.Sprint_4 TC10 for BISSAGL-12980 Openshift Home Screen Reference No. Regression Test (Search using Ref no.)
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Reference No." tab
    Then Staff Enter Value as Reference Number from row 2 in the "referenceNumber" field
    Then Staff Select "schemeYear" dropdown value as "2023"
    Then Staff Click on the "Search" button

  @tmslink=BISSAGL-18661
  @regression
  Scenario: AT-TC-11 - BISS_25.1.Sprint_4 TC11 for BISSAGL-12980 Openshift Home Screen Reference No. Regression Test (Reference no. Validations)
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Reference No." tab
    Then Staff Enter Value as "adssdasd" in the "referenceNumber" field
    Then Staff Click on the "Search" button
    And Staff Check "Invalid reference no." Error Message without span

  @tmslink=BISSAGL-18665
  @regression
  Scenario: AT-TC-12 - BISS_25.1.Sprint_4 TC12 for BISSAGL-12980 Openshift Home Screen Online Response Regression Test (Search and Filter)
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Online Response" tab
    Then Staff Click on the "Search" button

  @tmslink=BISSAGL-18668
  @regression
  Scenario: AT-TC-13 - BISS_25.1.Sprint_4 TC13 for BISSAGL-12980 Openshift Home Screen Online Response Regression Test (Search using herd and Response date)
    When Login with the Username "agr4442"
    And enter password
    Then Staff Select Data Protection CheckBox
    And clicks on Login button
#    And Click on the Openshift Basic Income Support for Sustainability application
    And Click on the Basic Income Support for Sustainability application
    And Staff switch to the "Online Response" tab
    Then Staff Enter Value as Herd Number with Submission from row 8 in the "herdNumber" field
    Then Staff Click on the "Search" button
