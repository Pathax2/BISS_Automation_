# BISS Automation Framework

**BDD Cucumber · Selenium WebDriver · Java 17 · Bamboo CI**

---

## Project Structure

```
Biss_Automation/
│
├── bamboo-specs/                              ← Bamboo pipeline as code
│   └── src/main/java/
│       └── BissAutomationPlan.java            ← Plan definition (Smoke + Regression stages)
│
├── src/
│   ├── main/java/
│   │   ├── commonFunctions/
│   │   │   └── CommonFunctions.java           ← Core action library (iAction, browser, Word report)
│   │   ├── database/
│   │   │   └── DBRouter.java                  ← JDBC dual-DB routing (DATA + INET)
│   │   ├── reporting/
│   │   │   ├── ReportManager.java             ← Thread-safe result accumulator
│   │   │   ├── HtmlReportGenerator.java       ← Self-contained HTML management dashboard
│   │   │   ├── JUnitXmlGenerator.java         ← JUnit XML for Bamboo test parser
│   │   │   └── ExecutionSummaryEmailer.java   ← Post-run email notification
│   │   └── utilities/
│   │       ├── ConfigManager.java             ← Centralised config with environment profiles
│   │       ├── ExcelUtilities.java            ← Excel read/write (ThreadLocal-safe)
│   │       ├── ObjReader.java                 ← Object Repository loader
│   │       ├── ObjectRepositoryValidator.java ← OR health check at framework startup
│   │       ├── RetryAnalyser.java             ← Flaky test retry mechanism
│   │       ├── ScreenshotManager.java         ← Viewport / full-page / element screenshots
│   │       │                                     and Word document image embedding
│   │       ├── SoftAssertManager.java         ← Collect-all-failures soft assertion manager
│   │       ├── TestDataFactory.java           ← Dynamic test data generation (FACTORY: prefix)
│   │       └── WaitStrategy.java             ← Named wait strategy enum
│   │
│   └── test/
│       ├── java/
│       │   ├── runner/
│       │   │   └── TestRunner.java            ← Execution entry point (Excel-driven loop)
│       │   └── stepdefinitions/
│       │       ├── Hooks.java                 ← Cucumber lifecycle hooks
│       │       ├── TC_01_Login.java           ← Login step definitions
│       │       └── TC_03.java                 ← Agent E2E journey step definitions
│       └── resources/
│           ├── config/
│           │   ├── application.properties          ← Base defaults
│           │   ├── application-dev.properties      ← DEV environment overrides
│           │   ├── application-staging.properties  ← STAGING environment overrides
│           │   └── application-prod.properties     ← PROD environment overrides
│           ├── Execution_Control_File/
│           │   └── ExecutionControl.xlsx           ← Test case execution control
│           ├── Object_Repository/
│           │   └── ObjectRepository.properties     ← All UI locators (104 entries)
│           ├── Test_Cases/
│           │   └── TC_01.feature                   ← Gherkin feature files
│           ├── Test_Data/
│           │   └── TestData.xlsx                   ← Per-test-case test data + Config sheet
│           └── logback.xml                         ← Async structured logging configuration
│
├── Test_Report/                               ← Generated at runtime (gitignored)
│   ├── html/                                  ← Management HTML report (self-contained)
│   ├── docs/                                  ← Word execution reports (.docx per TC)
│   ├── screenshots/                           ← All captured screenshots (.png)
│   ├── history/
│   │   └── results_history.csv               ← Cumulative build history (appended per build)
│   └── archive_results.py                    ← Post-build archiver (Bamboo Script task)
│
├── target/                                    ← Maven output (gitignored)
│   ├── surefire-reports/                      ← JUnit XML consumed by Bamboo JUnit Parser
│   ├── cucumber-reports/                      ← Cucumber HTML + JSON reports
│   └── logs/                                  ← Rolling execution and failure logs
│
├── pom.xml
├── .gitignore
└── README.md
```

---

## Quick Start — Running Locally

### Prerequisites

- Java 17 or later
- Maven 3.8 or later
- Chrome, Firefox, or Edge installed (Selenium Manager resolves drivers automatically)

### Run all tests — DEV environment, Chrome

```bash
mvn test -Pdev
```

### Run smoke tests on STAGING

```bash
mvn test -Psmoke,staging
```

### Run regression on STAGING with Firefox

```bash
mvn test -Pregression,staging -Dbrowser=FIREFOX
```

### Run a single test case by tag

```bash
mvn test -Penvironment=dev -Dbrowser=CHROME -Dcucumber.filter.tags="@TC_01"
```

### Run headless (for local CI simulation)

```bash
mvn test -Psmoke,staging -Dheadless=true
```

---

## Adding a New Test Case

### Step 1 — Add a row to `ExecutionControl.xlsx`

| TestCase_ID | Description | Execution | Environment | Browser | Tags | Status |
|---|---|---|---|---|---|---|
| TC_02 | Verify User Registration | Y | STAGING | CHROME | @smoke @regression @TC_02 | |

### Step 2 — Add test data to `TestData.xlsx` (sheet: Data)

| TestCase_ID | Username | Password | Email |
|---|---|---|---|
| TC_02 | testuser | P@ssword1 | FACTORY:getUniqueEmail |

### Step 3 — Add locators to `ObjectRepository.properties`

```properties
# TEXTBOX
iEmailField.xpath=//input[@name='email']

# BUTTON
iRegisterBtn.xpath=//button[@id='register']
```

### Step 4 — Create the feature file

`src/test/resources/Test_Cases/TC_02.feature`

```gherkin
@smoke @regression @registration @TC_02
Feature: TC_02 - User Registration

  Background:
    Given I navigate to the registration page

  @valid_registration
  Scenario: Register a new user successfully
    When I enter registration details
    And I submit the registration form
    Then I should see the confirmation message
```

### Step 5 — Create step definitions

`src/test/java/stepdefinitions/TC_02_Registration.java`

```java
package stepdefinitions;

import commonFunctions.CommonFunctions;
import io.cucumber.java.en.*;

public class TC_02_Registration
{
    @Given("I navigate to the registration page")
    public void navigateToRegistration()
    {
        CommonFunctions.iAction("CLICK", "XPATH", "iRegisterBtn", "");
    }
}
```

---

## ExecutionControl.xlsx — Column Reference

| Column | Required | Description |
|---|---|---|
| `TestCase_ID` | Yes | Must match the feature file name exactly. `TC_02` maps to `TC_02.feature` |
| `Description` | Yes | Human-readable description — appears in HTML report, Word report cover page |
| `Execution` | Yes | `Y` = include in this run. Any other value = skip |
| `Environment` | Yes | `DEV`, `STAGING`, or `PROD` |
| `Browser` | No | `CHROME`, `FIREFOX`, or `EDGE`. Defaults to `CHROME` if blank |
| `Tags` | No | Comma-separated Cucumber tags. Shown in HTML report. |
| `Status` | No | Written back automatically by the framework: `PASS` or `FAIL` |

---

## TestData.xlsx — Special Value Prefixes

| Prefix | Example | Behaviour |
|---|---|---|
| `TD:` | `TD:Password` | Reads another column from the same test data row |
| `FACTORY:` | `FACTORY:getUniqueEmail` | Generates a dynamic value via TestDataFactory |
| `FACTORY:` | `FACTORY:getDateOffset:-3` | Date three days before today |
| `FACTORY:` | `FACTORY:getRandomNumeric:6` | Six-digit random number |
| *(plain value)* | `testuser@biss.ie` | Used exactly as entered |

---

## Environment Configuration

Configuration files are located in `src/test/resources/config/`.

**Profile activation:** `-Penvironment=staging` (Maven profile) or `-Denv=staging` (system property)

**Resolution priority (highest wins):**

1. JVM system property passed via `-D` flag (Bamboo plan variables)
2. `application-<env>.properties`
3. `application.properties` (base defaults)

**Sensitive values** — passwords and tokens must never appear in properties files.
Pass them as Bamboo plan variables and forward via `-D` JVM arguments:

```
-Dnotify.email.password=${bamboo.email.password}
-Ddb.password=${bamboo.db.password}
```

---

## On-Demand Screenshots in Step Definitions

Call `Hooks.captureStep()` from any step definition to embed a labelled screenshot
into the Word execution report at that exact point in the test:

```java
Hooks.captureStep("BISS home page loaded — title verified");
Hooks.captureStep("Farmer dashboard opened — herd " + Hooks.RUNTIME_HERD);
Hooks.captureStep("Parcel A1190600017 added to Land Details table");
Hooks.captureStep("ACRES panel 1 — Accept warnings confirmed");
```

The screenshot is embedded in the Word report with a navy caption. Failure screenshots
are embedded automatically by `Hooks.afterScenarioExecution()` with a red caption and
do not require an explicit call.

---

## Reports Generated Per Run

| Report | Location | Purpose |
|---|---|---|
| HTML Dashboard | `Test_Report/html/*.html` | Management review — pass rate, duration charts, failure details, sortable table, lightbox screenshots |
| Word Document | `Test_Report/docs/*.docx` | Evidence and sign-off — cover page, step log table, scenario dividers, embedded screenshots |
| JUnit XML | `target/surefire-reports/*.xml` | Bamboo test parser — populates Bamboo build dashboard with pass/fail counts and durations |
| Email Summary | Sent post-run | Management notification after suite completion |
| Execution log | `target/logs/biss-execution.log` | Full structured log — all step pass/fail, DB queries, browser events |
| Failure log | `target/logs/biss-failures.log` | Failures only — used for rapid triage without scanning the full log |
| Results CSV | `Test_Report/history/results_history.csv` | Cumulative build history — one row per test case per build, appended by the post-build archiver |

---

## Bamboo Setup

### Task 1 — Source Code Checkout

Type: Source Code Checkout

### Task 2 — Maven Build

Type: Maven 3

```
test -Pregression,staging
     -Denvironment=${bamboo.env}
     -Dbrowser=${bamboo.browser}
     -Dheadless=true
     -Dretry.count=2
     -Dbamboo.buildNumber=${bamboo.buildNumber}
     -Dbamboo.buildPlanKey=${bamboo.buildPlanKey}
     -Dbamboo.agentName=${bamboo.agentName}
     -Dnotify.email.password=${bamboo.email.password}
     -Ddb.password=${bamboo.db.password}
```

JDK: JDK 17
Executable: Maven 3

> **Note:** The property is `-Denvironment` not `-Denv`. `Hooks.java` and `TestRunner.java`
> both read `System.getProperty("environment")`. Using `-Denv` will cause every test case
> to fail immediately with "Environment system property missing."

### Task 3 — JUnit Parser

Type: JUnit Parser
Results directory: `target/surefire-reports/*.xml`
Pick up test results created outside of this build: enabled

### Task 4 — Results Archiver (post-build)

Type: Script

```bash
python3 Test_Report/archive_results.py \
  --xml target/surefire-reports/BISS_Execution_Results.xml \
  --csv Test_Report/history/results_history.csv
```

This appends one row per test case to the cumulative CSV artifact.
The script is idempotent — re-running the task on a failed stage will not duplicate data.

### Artifact Definitions

| Name | Location | Copy pattern |
|---|---|---|
| HTML Execution Report | `Test_Report/html` | `*.html` |
| Word Reports | `Test_Report/docs` | `*.docx` |
| Execution Logs | `target/logs` | `*.log` |
| Failure Log | `target/logs` | `biss-failures.log` |
| Results History | `Test_Report/history` | `results_history.csv` |

### Plan Variables

| Variable | Value | Notes |
|---|---|---|
| `env` | `staging` | Forwarded as `-Denvironment` in the Maven goal |
| `browser` | `CHROME` | Forwarded as `-Dbrowser` |
| `email.password` | — | Secret variable — set in Bamboo, never in source |
| `db.password` | — | Secret variable — set in Bamboo, never in source |

---

## Runtime Herd and Username Resolution

Before any scenario runs, `Hooks.beforeAllExecution()` queries two Oracle databases to
select a valid herd and username for the test run:

1. **DATA DB** — fetches up to `herd.limit` candidate herds for the configured year,
   shuffled randomly to distribute load across reruns.
2. **INET DB** — for each candidate, looks up the corresponding username.
   The first candidate that returns a username is selected.

The resolved values are published as `TD:HerdNumber` and `TD:Username` system properties
so step definitions can consume them via `TD:` syntax without hardcoding any values.
They are also written back to `TestData.xlsx` for traceability.

**Runtime knobs (no code changes needed):**

| Property | Default | Description |
|---|---|---|
| `-Dherd.year` | `2026` | Year filter for herd selection query |
| `-Dherd.limit` | `25` | Number of candidate herds fetched per attempt |
| `-Dherd.retry` | `3` | Retry attempts if no username is found |
| `-DusernameOverride` | *(blank)* | Skip DB resolution and use this username directly |

---

## Bamboo Specs as Code

The full pipeline definition is maintained in `bamboo-specs/src/main/java/BissAutomationPlan.java`.

Changes to the CI pipeline (new stages, tasks, artifacts, notifications) are made in this
file and committed to Git — Bamboo imports the updated definition automatically on the next
build of the main branch. No manual UI configuration is required.

To publish the plan definition manually from a local machine:

```bash
cd bamboo-specs
mvn compile exec:java
```

The plan defines two stages:

- **Smoke Tests** — runs `@smoke` tagged scenarios on every commit to main. Fast path,
  critical scenarios only. Regression is blocked if smoke fails.
- **Regression Tests** — runs the full suite nightly at 22:00 Monday–Friday. Only
  executes after the Smoke stage passes.

---

## .gitignore Recommendations

The following should be excluded from version control:

```gitignore
# Maven output
target/

# Generated reports (binary and timestamped — not suitable for diffs)
Test_Report/html/
Test_Report/docs/
Test_Report/screenshots/
Test_Report/history/

# Keep the archiver and these source files tracked
!Test_Report/archive_results.py

# IDE
.idea/
*.iml
```

---

## Author

**Aniket Pathare** | aniket.pathare@goverment.ie
BISS Quality Assurance — Created 10-03-2026
