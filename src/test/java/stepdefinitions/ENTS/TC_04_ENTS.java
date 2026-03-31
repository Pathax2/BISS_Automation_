// ===================================================================================================================================
// File          : TC_04_ENTS.java
// Package       : stepdefinitions.ENTS
// Description   : Step definitions for TC_04_ENTS — Transfer Application E2E (Agent 4 / Cross-Agent).
//
//                 ╔═══════════════════════════════════════════════════════════════════════════╗
//                 ║  THIS FILE CONTAINS ZERO NEW STEP DEFINITIONS.                           ║
//                 ║                                                                           ║
//                 ║  Every step in TC_04_ENTS.feature is already defined in existing classes. ║
//                 ║  This file exists solely as a documentation anchor for traceability.      ║
//                 ╚═══════════════════════════════════════════════════════════════════════════╝
//
//                 Full reuse map:
//                 ┌──────────────────────────────────────────────────────────────────┬────────────────────┐
//                 │ Step                                                             │ Defined In         │
//                 ├──────────────────────────────────────────────────────────────────┼────────────────────┤
//                 │ the agent user is on the login page                              │ TC_03.java         │
//                 │ the agent logs into the application...                           │ TC_03.java         │
//                 │ the agent opens the {string} application                         │ TC_03.java         │
//                 │ the agent should land on the BISS Home page                      │ TC_03.java         │
//                 │ the agent navigates to ... Left Menu Link                        │ TC_03.java         │
//                 │ the agent switches to the {string} tab...                        │ TC_06.java         │
//                 │ the agent creates a transfer application with the following...   │ TC_01_ENTS.java    │
//                 │ the agent uploads the transferor signature document              │ TC_01_ENTS.java    │
//                 │ the agent sends the transfer for acceptance                      │ TC_01_ENTS.java    │
//                 │ the transfer key should be captured                              │ TC_01_ENTS.java    │
//                 │ the transfer should be submitted successfully                    │ TC_01_ENTS.java    │
//                 │ the agent logs out and re-logs in as the transferee agent        │ TC_03_ENTS.java    │
//                 │ the agent logs out and re-logs in as the transferor agent        │ TC_03_ENTS.java    │
//                 │ the agent completes the cross-agent transferee acceptance flow   │ TC_03_ENTS.java    │
//                 └──────────────────────────────────────────────────────────────────┴────────────────────┘
//
//                 Transfer types covered (all using existing parameterised steps):
//                   211 — Lease (+ leaseYear flag)    |  201 — Inheritance
//                   205 — Change of Registration      |  206 — Change of Legal Entity
//                   202 — Gift
//
//                 The transfer type code is passed as data in the DataTable — no code change
//                 needed regardless of which type is used. TC_01_ENTS.theAgentCreatesATransferApplication()
//                 handles all codes via a dynamic XPath radio button click.
//
//                 If a future TC_04_ENTS scenario needs a step that doesn't exist yet,
//                 add it to this class rather than modifying TC_01_ENTS or TC_03_ENTS.
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date Created  : 31-03-2026
// ===================================================================================================================================

package stepdefinitions.ENTS;

// No imports needed — no step definitions in this class.
// Kept as a placeholder for future TC_04_ENTS-specific steps if needed.

public class TC_04_ENTS
{
    // -------------------------------------------------------------------------------------------------------------------------------
    // All steps for TC_04_ENTS.feature are defined in:
    //   - stepdefinitions.TC_03           (login, portal nav, BISS home)
    //   - stepdefinitions.TC_06           (tab switching)
    //   - stepdefinitions.TC_01_ENTS      (transferor flow, upload, send, capture key, submit)
    //   - stepdefinitions.ENTS.TC_03_ENTS (cross-agent logout/re-login, transferee acceptance)
    //
    // Cucumber's glue path scans all packages and binds steps automatically.
    // No additional step definitions are required for TC_04_ENTS.
    // -------------------------------------------------------------------------------------------------------------------------------
}