Feature: TC_14_ENTS - ETF Authorisation (Individual adds ETF auth)

  # Migrated from: TC_14_ENTS.feature (1 scenario)
  # Individual logs in, adds ETF authorisation, then ETF partner verifies
  # Author: Aniket Pathare | Created: 31-03-2026

  @sanity @etf-auth
  Scenario: AT-ENTS-ETF-AUTH - Individual adds ETF Authorisation and ETF partner verifies

    # --- Individual adds ETF authorisation ---
    When the individual logs in as transferor "PADDYMORGAN"
    And the individual adds ETF authorisation with the following details
      | etfCode          | ETF00009 |
      | txorEntsAllowed  | 1        |
      | txeeEntsAllowed  | 1        |

    # --- ETF Partner verifies ---
    When the agent logs out and re-logs in as the ETF partner "agr15512"
    And the ETF partner searches for herd "J1691122" and opens it
    Then the ETF authorisation should be visible
