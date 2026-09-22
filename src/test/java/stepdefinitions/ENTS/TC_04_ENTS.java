// ===================================================================================================================================
// File          : TC_04_ENTS.java
// Package       : stepdefinitions.ENTS
// Description   : Step definitions for TC_04_ENTS - Transfer Application E2E (Agent 4 / cross-agent).
//
//                 THIS FILE CONTAINS NO STEP DEFINITIONS.
//                 Every step of TC_04_ENTS.feature is already defined elsewhere; the file is kept as the traceability
//                 anchor for the test case, and as the place for any TC_04-only step added later.
//
//                 Reuse map (Playwright, 22-09-2026):
//                   the agent user is on the login page                            -> stepdefinitions.TC_03
//                   the agent logs into the application ...                        -> stepdefinitions.TC_03
//                   the agent opens the {string} application                       -> stepdefinitions.TC_03
//                   the agent should land on the BISS Home page                    -> stepdefinitions.TC_03
//                   the agent navigates to ... Left Menu Link                      -> stepdefinitions.TC_03
//                   the agent switches to the {string} tab ...                     -> stepdefinitions.TC_06
//                   the agent creates a transfer application with the following... -> stepdefinitions.ENTS.TC_01_ENTS
//                   the agent uploads the transferor signature document            -> stepdefinitions.ENTS.TC_01_ENTS
//                   the agent sends the transfer for acceptance                    -> stepdefinitions.ENTS.TC_01_ENTS
//                   the transfer key should be captured                            -> stepdefinitions.ENTS.TC_01_ENTS
//                   the transfer should be submitted successfully                  -> stepdefinitions.ENTS.TC_01_ENTS
//                   the agent logs out and re-logs in as the transferee agent      -> stepdefinitions.ENTS.TC_03_ENTS
//                   the agent logs out and re-logs in as the transferor agent      -> stepdefinitions.ENTS.TC_03_ENTS
//                   the agent completes the cross-agent transferee acceptance flow -> stepdefinitions.ENTS.TC_03_ENTS
//
//                 Transfer types covered, all through the same parameterised steps:
//                   211 Lease (with the leaseYear flag) | 201 Inheritance | 205 Change of Registration
//                   206 Change of Legal Entity          | 202 Gift
//                 The type is a DataTable value, and TC_01_ENTS selects it with a dynamic XPath, so no code change is
//                 needed for a new type.
//
//                 Runtime data: this test case is cross-agent, so each create DataTable needs a transfereeAgent row
//                 (the login used by the "re-logs in as the transferee agent" step). The transferor herd then comes
//                 from the logged-in agent's pool and the transferee herd from that agent's pool.
//
//                 If TC_04_ENTS ever needs a step of its own, add it here rather than to TC_01_ENTS or TC_03_ENTS.
//
// Author        : Aniket Pathare | aniket.pathare@government.ie
// Date Created  : 31-03-2026 | Updated: 22-09-2026 (Playwright)
// ===================================================================================================================================

package stepdefinitions.ENTS;

public class TC_04_ENTS
{
    // Cucumber scans the whole glue path, so the steps of TC_04_ENTS.feature bind from the classes listed above.
    // No step definitions are needed here.
}
