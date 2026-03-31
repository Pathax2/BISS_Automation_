Feature: Logins of Individual(Add ETF Autorisation) & ETF

  @sanity
  @tmslink=ENTSAGL-9921
  Scenario: TC_01_Logins Of Individual & ETF
    Given user on individual login page
    When clicks on new agent login button
    When Agent Enter "PADDYMORGAN" as Individual's Username
    # G1850473 1013076P 4827195O IE0414005 PADDYMORGAN TRAYNORA 6858291D
    And clicks on Continue button
    And enter password
    And clicks on Continue button
    #Then Agent Enters "1" as the OTP
    Then External User Enters sms OTP
    And clicks on Continue button
    #Then Agent Click on the "Close" personal details dialog issue
    And Click on the Basic Income Support for Sustainability application
    Then Click on the Agent BISS "Transfers" Tab
    When Agent Click on the " Allow Entitlement " button
    Then Agent Click on Transfer Type " Add ETF Authorisation " Button
    And Agent Select "etfAvailable" dropdown value as "ETF00009"
    And Agent Fill "txorEntsAllowed" field value as "1"
    And Agent Fill "txeeEntsAllowed" field value as "1"
    When Agent Click on the " Add Entitlements Transfer Facilitator Authorisation " button
    And Click on Exit BISS Link
    And Click on Logout Button
    When clicks on new agent login button
    When Login with the Partner Username "agr15512"
    And clicks on Continue button
    And enter password
    And clicks on Continue button
    Then Partner Enters MS authenticator OTP
    And clicks on Continue button
    And Click on the Basic Income Support for Sustainability application
    When Agent Search for Herd Number Field and Enter Herd as "J1691122"
    Then Agent Click On View Link for Searched Herd
