Feature: BISS Agent completes an end-to-end farmer application journey

  # --------------------------------------------------------------------------------------------------------------------
  # Purpose:
  #   Single end-to-end regression journey covering the major business flow in the BISS Application Portal.
  #
  # Coverage mapped from old scenarios:
  #   - Login and portal navigation
  #   - Farmer dashboard access
  #   - Side navigation tab validation
  #   - Start application
  #   - Active Farmer step
  #   - Scheme Selection step
  #   - Land Details validations:
  #       * invalid parcel
  #       * archived parcel
  #       * add parcel
  #       * add plot
  #       * add parcel from map
  #       * edit/delete/undo parcel
  #       * delete plot
  #       * mandatory information completion
  #   - GAEC 7 handling
  #   - ACRES warnings flow
  #   - Eco selections
  #   - Review and Submit
  #   - Correspondence document upload
  #
  # Notes:
  #   1. This feature is intentionally designed as one controlled business journey.
  #   2. Test data such as herd rows, parcel IDs, plot references, and eco values should come from your framework.
  #   3. Step wording is kept consistent so step definitions can be designed cleanly in the new framework.
  # --------------------------------------------------------------------------------------------------------------------

  Background:
    Given the agent user is on the login page
    When the agent logs into the application with valid credentials and OTP
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    And the agent navigates to the "My Clients" tab

  @regression @e2e @biss
  Scenario: AT-E2E-01 - Agent completes the end-to-end BISS farmer application flow

    # -----------------------------------------
    # Farmer selection and dashboard validation
    # -----------------------------------------
    When the agent opens a farmer dashboard using herd data from row 4
    Then the farmer dashboard should be displayed

    # -----------------------------------------
    # Side navigation validation
    # -----------------------------------------
    When the agent navigates through the farmer side navigation tabs
      | Applications           |
      | Farm Details           |
      | Entitlements / Usage   |
      | Transfer               |
      | NR-CISYF               |
      | Correspondence         |
      | Farmer Dashboard       |
    Then each requested side navigation tab should open successfully

    # -----------------------------------------
    # Application start
    # -----------------------------------------
    When the agent deletes any existing draft if present
    And the agent starts a new BISS application
    Then the Active Farmer step should be displayed

    # -----------------------------------------
    # Active Farmer step
    # -----------------------------------------
    When the agent selects "Making Hay/Silage/Haylage" in the Active Farmer step
    And the agent proceeds to the next application step
    Then the Scheme Selection step should be displayed

    # -----------------------------------------
    # Scheme Selection step
    # -----------------------------------------
    When the agent selects the "Organics" scheme
    And the agent proceeds to the next application step
    And the agent accepts the scheme selection acknowledgement if displayed
    Then the Land Details step should be displayed

    # -----------------------------------------
    # Land Details - invalid parcel validation
    # -----------------------------------------
    When the agent attempts to add parcel "Invalid789"
    Then the invalid parcel warning message should be displayed
    And the add parcel dialog should remain available for correction or cancellation

    # -----------------------------------------
    # Land Details - archived parcel validation
    # -----------------------------------------
    When the agent attempts to add archived parcel "H19112055"
    Then the archived parcel warning should be displayed if applicable

    # -----------------------------------------
    # Land Details - add valid parcel
    # -----------------------------------------
    When the agent adds parcel "A1190600017" with claimed area "7"
    And the agent sets parcel ownership status to "Owned"
    And the agent sets parcel use to "Apples"
    And the agent sets parcel organic status to "Conventional"
    Then the parcel should be added successfully to Land Details

    # -----------------------------------------
    # Land Details - claimed parcel validation
    # -----------------------------------------
    When the agent attempts to add parcel "A1190600017" again
    Then the already claimed parcel warning should be displayed if applicable

    # -----------------------------------------
    # Land Details - add plot
    # -----------------------------------------
    When the agent adds a plot with the following details
      | county            | Donegal            |
      | townland          | Aghadachor - E22301 |
      | plotReference     | T12345678          |
      | ownershipStatus   | Owned              |
      | organicStatus     | Conventional       |
      | claimedArea       | 15                 |
      | plotUse           | Blackcurrants      |
      | mapChangeOption   | Submit Paper Map By Post |
    Then the plot should be added successfully to Land Details

    # -----------------------------------------
    # Land Details - add parcel from GIS map
    # -----------------------------------------
    When the agent adds a parcel from the GIS map with the following details
      | county            | Clare              |
      | townland          | Addergoole - C11501 |
      | claimedArea       | 4                  |
      | ownershipStatus   | Owned              |
      | parcelUse         | Beet               |
      | organicStatus     | Conventional       |
    Then the GIS-selected parcel should be added successfully

    # -----------------------------------------
    # Land Details - add parcel to edit/delete
    # -----------------------------------------
    When the agent adds parcel "J1650300004" with claimed area "3"
    And the agent sets parcel ownership status to "Owned"
    And the agent sets parcel use to "Clover"
    And the agent sets parcel organic status to "Conventional"
    Then parcel "J1650300004" should be available in Land Details

    # -----------------------------------------
    # Land Details - edit parcel using side drawer
    # -----------------------------------------
    When the agent opens parcel "J1650300004" in the side drawer
    And the agent raises an EH change request with reason "Test Reason"
    And the agent changes parcel use to "Coriander"
    And the agent saves the parcel changes
    Then the parcel update should be saved successfully

    # -----------------------------------------
    # Land Details - delete and undo parcel
    # -----------------------------------------
    When the agent deletes parcel "J1650300004"
    Then parcel "J1650300004" should be marked for deletion

    When the agent undoes deletion for parcel "J1650300004"
    Then parcel "J1650300004" should be restored in Land Details

    # -----------------------------------------
    # Land Details - add plot to delete later
    # -----------------------------------------
    When the agent adds a plot with the following details
      | county            | Donegal              |
      | townland          | Aghadowey - E18601   |
      | plotReference     | T87654321            |
      | ownershipStatus   | Owned                |
      | organicStatus     | Conventional         |
      | claimedArea       | 10                   |
      | plotUse           | Alfalfa              |
      | mapChangeOption   | Submit Paper Map By Post |
    Then plot "T87654321" should be available in Land Details

    # -----------------------------------------
    # Land Details - delete plot
    # -----------------------------------------
    When the agent deletes parcel or plot "T87654321"
    And the agent confirms the deletion
    Then plot "T87654321" should be marked for deletion

    # -----------------------------------------
    # Land Details - complete mandatory information
    # -----------------------------------------
    When the agent completes all mandatory information in Land Details
    And the agent proceeds to the next application step
    Then the next application step should open successfully

    # -----------------------------------------
    # GAEC 7 step
    # -----------------------------------------
    When the agent opens the "GAEC 7" step
    And the agent proceeds from GAEC 7
    And the agent handles any GAEC 7 continue action if present
    Then the application should move beyond the GAEC 7 step

    # -----------------------------------------
    # ACRES step
    # -----------------------------------------
    When the agent opens the "ACRES" step
    And the agent selects "Yes, rescore" on panel 1
    And the agent continues panel 1
    And the agent selects "Accept warnings" on panel 1
    And the agent continues panel 2
    And the agent selects "Accept warnings" on panel 2
    And the agent continues panel 3
    Then the ACRES step should be completed successfully

    # -----------------------------------------
    # Eco step
    # -----------------------------------------
    When the agent opens the "Eco" step
    And the agent selects the "AP2" scheme option
    And the agent selects "Standard" for panel 2
    And the agent saves the selected eco option
    And the agent selects the "AP5" scheme option
    And the agent selects approved spreader manufacturer "Lemken"
    And the agent selects approved spreader model "Polaris 14"
    And the agent enters spreader serial number "A735B78346"
    And the agent saves the selected eco option
    Then the Eco step should be completed successfully

    # -----------------------------------------
    # Eco opt-out validation
    # -----------------------------------------
    When the agent returns to the "Scheme Selection" step
    And the agent opens the "Eco" scheme card
    And the agent proceeds to the next application step
    And the agent accepts the acknowledgement if displayed
    Then the application should continue successfully after eco validation

    # -----------------------------------------
    # Review and Submit
    # -----------------------------------------
    When the agent opens the "Review & Submit" step
    And the agent completes all review page next actions
    And the agent accepts the Terms and Conditions
    And the agent submits the application
    And the agent confirms the application submission
    Then the application should be submitted successfully

    # -----------------------------------------
    # Correspondence and upload document
    # -----------------------------------------
    When the agent navigates to the "Correspondence" tab
    And the agent chooses to upload a document
    And the agent selects document type "Commonage Evidence"
    And the agent uploads the correspondence document
    And the agent confirms the upload
    Then the document should be uploaded successfully in Correspondence
