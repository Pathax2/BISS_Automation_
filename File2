Feature: TC_14_ENTS - ETF Authorisation (Individual adds ETF auth)

  # Migrated from: TC_14_ENTS.feature (1 scenario)
  # Individual logs in, adds ETF authorisation, then ETF partner verifies
  # Author: Aniket Pathare | Created: 31-03-2026

  @sanity @etf-auth
  Scenario: AT-ENTS-ETF-AUTH - Individual adds ETF Authorisation and ETF partner verifies

    # --- Individual adds ETF authorisation ---
    When the individual logs in as transferor "PAUDYFROG"
    And the agent opens the "Basic Income Support for Sustainability" application
    Then the agent should land on the BISS Home page
    When the agent navigates through the farmer side navigation tabs
      | Transfers                 |
    #
    And the individual adds ETF authorisation with the following details
      | etfCode          | SMYTH, SHEILA - [ETF00009] |
      | txorEntsAllowed  | 2        |
      | txeeEntsAllowed  | 3        |

    # --- ETF Partner verifies ---
    When the agent logs out and re-logs in as the ETF partner "agr15512"
    And the ETF partner searches for herd "Y104069X" and opens it
    Then the ETF authorisation should be visible
