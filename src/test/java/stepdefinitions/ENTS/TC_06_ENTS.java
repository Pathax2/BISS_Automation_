// ===================================================================================================================================
// File          : TC_06_ENTS.java
// Package       : stepdefinitions.ENTS
// Description   : Step definitions for TC_06_ENTS — Transfer Application E2E (Agent 6 to Individual).
//
//                 ╔═══════════════════════════════════════════════════════════════════════════╗
//                 ║  THIS FILE CONTAINS ZERO NEW STEP DEFINITIONS.                           ║
//                 ║                                                                           ║
//                 ║  Every step in TC_06_ENTS.feature is already defined in existing classes. ║
//                 ║  This file exists solely as a documentation anchor for traceability.      ║
//                 ╚═══════════════════════════════════════════════════════════════════════════╝
//
//                 Full reuse map:
//                 ┌──────────────────────────────────────────────────────────────────────────┬────────────────────┐
//                 │ Step                                                                      │ Defined In         │
//                 ├──────────────────────────────────────────────────────────────────────────┼────────────────────┤
//                 │ the agent user is on the login page                                       │ TC_03.java         │
//                 │ the agent logs into the application...                                    │ TC_03.java         │
//                 │ the agent opens the {string} application                                  │ TC_03.java         │
//                 │ the agent should land on the BISS Home page                               │ TC_03.java         │
//                 │ the agent navigates to ... Left Menu Link                                 │ TC_03.java         │
//                 │ the agent switches to the {string} tab...                                 │ TC_06.java         │
//                 │ the agent creates a transfer application with the following...            │ TC_01_ENTS.java    │
//                 │ the agent uploads the transferor signature document                       │ TC_01_ENTS.java    │
//                 │ the agent sends the transfer for acceptance                               │ TC_01_ENTS.java    │
//                 │ the transfer key should be captured                                       │ TC_01_ENTS.java    │
//                 │ the transfer should be submitted successfully                             │ TC_01_ENTS.java    │
//                 │ the agent logs out and re-logs in as the transferor agent                 │ TC_03_ENTS.java    │
//                 │ the agent logs out and re-logs in as the individual transferee {string}   │ TC_05_ENTS.java    │
//                 │ the individual completes the transferee acceptance flow                   │ TC_05_ENTS.java    │
//                 └──────────────────────────────────────────────────────────────────────────┴────────────────────┘
//
//                 Transfer types covered (all via existing parameterised steps):
//                   203 — Merger     |  204 — Division     |  212 — Sale     |  206 — Change of Legal Entity
//
//                 Individual transferees used:
//                   DANIELPAUL (Y1310159) — Sections 1, 2
//                   TERENCE1   (Y1041344) — Section 3
//                   PAUDYFROG  (Y104069X) — Section 4
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date Created  : 31-03-2026
// ===================================================================================================================================

package stepdefinitions.ENTS;

// No imports needed — no step definitions in this class.
// Kept as a placeholder for future TC_06_ENTS-specific steps if needed.

public class TC_06_ENTS
{
    // -------------------------------------------------------------------------------------------------------------------------------
    // All steps for TC_06_ENTS.feature are defined in:
    //   - stepdefinitions.TC_03              (login, portal nav, BISS home)
    //   - stepdefinitions.TC_06              (tab switching)
    //   - stepdefinitions.TC_01_ENTS         (transferor flow, upload, send, capture key, submit)
    //   - stepdefinitions.ENTS.TC_03_ENTS    (transferor re-login between sections)
    //   - stepdefinitions.ENTS.TC_05_ENTS    (individual login, individual acceptance)
    //
    // Cucumber's glue path scans all packages and binds steps automatically.
    // No additional step definitions are required for TC_06_ENTS.
    // -------------------------------------------------------------------------------------------------------------------------------
}