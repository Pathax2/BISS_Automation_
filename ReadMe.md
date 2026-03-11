# BISS Automation Framework

**BDD Cucumber · Selenium WebDriver · Java 17 · Bamboo CI**

---

## 📁 Project Structure

```
Biss_Automation/
│
├── bamboo-specs/                              ← Bamboo pipeline as code
│   └── src/main/java/
│       └── BissAutomationPlan.java
│
├── src/
│   ├── main/java/
│   │   ├── commonFunctions/
│   │   │   └── CommonFunctions.java           ← Core action library (iAction, browser, reports)
│   │   ├── database/
│   │   │   └── DatabaseUtilities.java         ← JDBC DB verification layer
│   │   ├── reporting/
│   │   │   ├── ReportManager.java             ← Run-time result accumulator
│   │   │   ├── HtmlReportGenerator.java       ← Management HTML dashboard
│   │   │   ├── JUnitXmlGenerator.java         ← Bamboo JUnit XML
│   │   │   ├── ExecutionSummaryEmailer.java   ← Post-run email notification
│   │   │   └── TestStepLogger.java            ← Cucumber step-level audit trail
│   │   └── utilities/
│   │       ├── ConfigManager.java             ← Centralised config with env profiles
│   │       ├── ExcelUtilities.java            ← Excel read/write (ThreadLocal-safe)
│   │       ├── ObjReader.java                 ← Object Repository loader
│   │       ├── ObjectRepositoryValidator.java ← OR health check at startup
│   │       ├── RetryAnalyser.java             ← Flaky test retry mechanism
│   │       ├── ScreenshotManager.java         ← Viewport/full-page/element screenshots
│   │       ├── SoftAssertManager.java         ← Collect-all-failures soft assertions
│   │       ├── TestDataFactory.java           ← Dynamic test data generation
│   │       └── WaitStrategy.java              ← Named wait strategy enum
│   │
│   └── test/
│       ├── java/
│       │   ├── runner/
│       │   │   └── TestRunner.java            ← Execution entry point
│       │   └── stepdefinitions/
│       │       ├── Hooks.java                 ← Cucumber lifecycle hooks
│       │       └── TC_01_Login.java           ← Login step definitions
│       └── resources/
│           ├── config/
│           │   ├── application.properties          ← Base defaults
│           │   ├── application-dev.properties      ← DEV overrides
│           │   ├── application-staging.properties  ← STAGING overrides
│           │   └── application-prod.properties     ← PROD overrides
│           ├── Execution_Control_File/
│           │   └── ExecutionControl.xlsx           ← Test execution control
│           ├── Object_Repository/
│           │   └── ObjectRepository.properties     ← All UI locators
│           ├── Test_Cases/
│           │   └── TC_01.feature                   ← Gherkin feature files
│           ├── Test_Data/
│           │   └── TestData.xlsx                   ← Test data per test case
│           └── logback.xml                         ← Logging configuration
│
├── Test_Report/                               ← Generated at runtime (gitignored)
│   ├── html/                                  ← Management HTML report
│   ├── docs/                                  ← Word execution reports
│   └── screenshots/                           ← Failure screenshots
│
├── target/                                    ← Maven output (gitignored)
│   ├── surefire-reports/                      ← JUnit XML → Bamboo dashboard
│   ├── cucumber-reports/                      ← Cucumber HTML reports
│   └── logs/                                  ← Rolling log files
│
├── pom.xml
├── .gitignore
└── README.md
```

---

## ⚡ Quick Start — Running Locally

### Prerequisites
- Java 17+
- Maven 3.8+
- Chrome / Firefox / Edge installed (Selenium Manager handles driver download automatically)

### Run all tests (dev environment, Chrome)
```bash
mvn test -Pdev
```

### Run smoke tests on staging
```bash
mvn test -Psmoke,staging
```

### Run regression on staging with Firefox
```bash
mvn test -Pregression,staging -Dbrowser=FIREFOX
```

### Run a single test case
```bash
mvn test -Denv=dev -Dbrowser=CHROME -Dcucumber.filter.tags="@TC_01"
```

### Run headless (for local CI testing)
```bash
mvn test -Psmoke,staging -Dheadless=true
```

---

## ➕ Adding a New Test Case

Follow these 5 steps:

**Step 1 — Add a row to `ExecutionControl.xlsx`**

| TestCase_ID | Description | Execution | Environment | Browser | Tags | Status |
|---|---|---|---|---|---|---|
| TC_02 | Verify User Registration | Y | STAGING | CHROME | @smoke @regression @TC_02 | |

**Step 2 — Add test data to `TestData.xlsx`** (sheet: `Data`)

| TestCase_ID | Username | Password | Email |
|---|---|---|---|
| TC_02 | testuser | P@ssword1 | FACTORY:getUniqueEmail |

**Step 3 — Add locators to `ObjectRepository.properties`**
```properties
iRegisterBtn.xpath=//button[@id='register']
iEmailField.xpath=//input[@name='email']
```

**Step 4 — Create the feature file** `src/test/resources/Test_Cases/TC_02.feature`
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

**Step 5 — Create step definitions** `src/test/java/stepdefinitions/TC_02_Registration.java`
```java
package stepdefinitions;
// ... implement step methods using CommonFunctions.iAction(...)
```

---

## ⚙️ ExecutionControl.xlsx — Column Reference

| Column | Required | Description |
|---|---|---|
| `TestCase_ID` | ✅ | Matches feature file name (e.g. `TC_01` → `TC_01.feature`) |
| `Description` | ✅ | Human-readable test case description |
| `Execution` | ✅ | `Y` = run this test case, anything else = skip |
| `Environment` | ✅ | `DEV`, `STAGING`, or `PROD` |
| `Browser` | ⬜ | `CHROME`, `FIREFOX`, `EDGE`. Defaults to `CHROME` if blank |
| `Tags` | ⬜ | Comma-separated Cucumber tags. Used in HTML report. |
| `Status` | ⬜ | Written back by framework: `PASS` or `FAIL` |

---

## 🔢 TestData.xlsx — Special Value Prefixes

| Prefix | Example | Behaviour |
|---|---|---|
| `TD:` | `TD:Password` | Reads another column value from the same test data row |
| `FACTORY:` | `FACTORY:getUniqueEmail` | Generates dynamic value via TestDataFactory |
| `FACTORY:` | `FACTORY:getDateOffset:-3` | 3 days ago from today |
| `FACTORY:` | `FACTORY:getRandomNumeric:6` | 6-digit random number |
| *(plain value)* | `testuser@biss.ie` | Used as-is |

---

## 🌍 Environment Configuration

Config files live in `src/test/resources/config/`.

**Activation:** `-Denv=staging` or Maven profile `-Pstaging`

**Priority (highest wins):**
1. JVM `-D` system property (Bamboo plan variables)
2. `application-<env>.properties`
3. `application.properties` (base defaults)

**Sensitive values** (passwords, tokens) — **NEVER in properties files**.
Set via Bamboo plan variables and pass as `-D` JVM args:
```
-Dnotify.email.password=${bamboo.email.password}
-Ddb.password=${bamboo.db.password}
```

---

## 🏗️ Bamboo Setup

### Task 1 — Source Code Checkout
Type: `Source Code Checkout`

### Task 2 — Maven Build
Type: `Maven 3`
Goal:
```
test -Pregression,staging
     -Denv=${bamboo.env}
     -Dbrowser=${bamboo.browser}
     -Dheadless=true
     -Dretry.count=2
     -Dbamboo.buildNumber=${bamboo.buildNumber}
     -Dbamboo.buildPlanKey=${bamboo.buildPlanKey}
     -Dbamboo.agentName=${bamboo.agentName}
     -Dnotify.email.password=${bamboo.email.password}
     -Ddb.password=${bamboo.db.password}
```
JDK: `JDK 17`

### Task 3 — JUnit Parser
Type: `JUnit Parser`
Specify custom results directories: `target/surefire-reports/*.xml`
✅ Pick up test results created outside of this build

### Artifact Definitions
| Name | Location | Copy pattern |
|---|---|---|
| HTML Execution Report | `Test_Report/html` | `*.html` |
| Execution Logs | `target/logs` | `*.log` |
| Failure Log | `target/logs` | `biss-failures.log` |
| Word Reports | `Test_Report/docs` | `*.docx` |

### Plan Variables
| Variable | Value |
|---|---|
| `env` | `staging` |
| `browser` | `CHROME` |
| `email.password` | `****` (secret) |
| `db.password` | `****` (secret) |

---

## 📊 Reports Generated

| Report | Location | Audience |
|---|---|---|
| HTML Dashboard | `Test_Report/html/*.html` | Management, QA Leads |
| Word Document | `Test_Report/docs/*.docx` | Evidence / Sign-off |
| JUnit XML | `target/surefire-reports/*.xml` | Bamboo dashboard |
| Email Summary | Sent post-run | Management |
| All logs | `target/logs/biss-execution.log` | Developers |
| Failure log | `target/logs/biss-failures.log` | QA triage |

---

## 👤 Author

**Aniket Pathare** | aniket.pathare@goverment.ie
BISS Quality Assurance | Created 10-03-2026