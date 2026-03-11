// ===================================================================================================================================
// File          : BissAutomationPlan.java
// Description   : Bamboo Specs as Code definition for the BISS automation CI pipeline.
//                 Defines the full Bamboo plan (stages, jobs, tasks, artifacts, notifications)
//                 as version-controlled Java code. Changes to the pipeline go through Git,
//                 not through manual UI configuration.
//
// Folder        : bamboo-specs/src/main/java/BissAutomationPlan.java
//
// SETUP (one time):
//   1. Create folder: bamboo-specs/src/main/java/
//   2. Create bamboo-specs/pom.xml (see bottom of this file for required pom.xml)
//   3. Enable "Bamboo Specs" on your Bamboo plan via: Plan Settings → Bamboo Specs
//   4. On commit to main branch, Bamboo auto-imports this plan definition
//
// Bamboo Specs dependency (add to bamboo-specs/pom.xml):
//   <dependency>
//     <groupId>com.atlassian.bamboo</groupId>
//     <artifactId>bamboo-specs</artifactId>
//     <version>9.3.0</version>
//   </dependency>
//
// Author        : Aniket Pathare | aniket.pathare@goverment.ie
// Date Created  : 10-03-2026
// ===================================================================================================================================

import com.atlassian.bamboo.specs.api.BambooSpec;
import com.atlassian.bamboo.specs.api.builders.BambooKey;
import com.atlassian.bamboo.specs.api.builders.BambooOid;
import com.atlassian.bamboo.specs.api.builders.artifact.Artifact;
import com.atlassian.bamboo.specs.api.builders.artifact.ArtifactSubscription;
import com.atlassian.bamboo.specs.api.builders.notification.AnyNotificationRecipient;
import com.atlassian.bamboo.specs.api.builders.notification.Notification;
import com.atlassian.bamboo.specs.api.builders.permission.PermissionType;
import com.atlassian.bamboo.specs.api.builders.permission.Permissions;
import com.atlassian.bamboo.specs.api.builders.permission.PlanPermissions;
import com.atlassian.bamboo.specs.api.builders.plan.Job;
import com.atlassian.bamboo.specs.api.builders.plan.Plan;
import com.atlassian.bamboo.specs.api.builders.plan.PlanIdentifier;
import com.atlassian.bamboo.specs.api.builders.plan.Stage;
import com.atlassian.bamboo.specs.api.builders.plan.artifact.ArtifactDefinition;
import com.atlassian.bamboo.specs.api.builders.plan.branches.BranchCleanup;
import com.atlassian.bamboo.specs.api.builders.plan.branches.PlanBranchManagement;
import com.atlassian.bamboo.specs.api.builders.plan.configuration.AllOtherPluginsConfiguration;
import com.atlassian.bamboo.specs.api.builders.project.Project;
import com.atlassian.bamboo.specs.api.builders.repository.VcsChangeDetection;
import com.atlassian.bamboo.specs.api.builders.task.AnyTask;
import com.atlassian.bamboo.specs.api.builders.trigger.AfterSuccessfulBuildPlanTrigger;
import com.atlassian.bamboo.specs.api.builders.trigger.ScheduledTrigger;
import com.atlassian.bamboo.specs.builders.notification.PlanStatusChangedNotification;
import com.atlassian.bamboo.specs.builders.task.CheckoutTask;
import com.atlassian.bamboo.specs.builders.task.MavenTask;
import com.atlassian.bamboo.specs.builders.task.TestParserTask;
import com.atlassian.bamboo.specs.builders.task.VcsCheckoutTask;
import com.atlassian.bamboo.specs.util.BambooServer;
import com.atlassian.bamboo.specs.util.MapBuilder;

@BambooSpec
public class BissAutomationPlan
{
    // -------------------------------------------------------------------------------------------------------------------------------
    // BAMBOO SERVER — update URL to point to your Bamboo instance
    // -------------------------------------------------------------------------------------------------------------------------------
    private static final String BAMBOO_SERVER_URL = "https://bamboo.biss.ie";

    // -------------------------------------------------------------------------------------------------------------------------------
    // PROJECT + PLAN identifiers
    // -------------------------------------------------------------------------------------------------------------------------------
    private static final String PROJECT_KEY = "BISS";
    private static final String PROJECT_NAME = "BISS Automation";
    private static final String PLAN_KEY = "AUTO";
    private static final String PLAN_NAME = "BISS Automation Suite";

    // -------------------------------------------------------------------------------------------------------------------------------
    // Notification recipients — update with real team addresses
    // -------------------------------------------------------------------------------------------------------------------------------
    private static final String QA_TEAM_EMAIL = "qa-team@biss.ie";
    private static final String MANAGEMENT_EMAIL = "management@biss.ie";

    // ***************************************************************************************************************************************************************************************
    // Function Name : main
    // Description   : Entry point — publishes the plan spec to the Bamboo server.
    //                 Run this locally to push plan changes: mvn compile exec:java
    // Parameters    : pArgs (String[]) - unused
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public static void main(String[] pArgs) throws Exception
    {
        BambooServer iBambooServer = new BambooServer(BAMBOO_SERVER_URL);
        BissAutomationPlan iPlanSpec = new BissAutomationPlan();

        iBambooServer.publish(iPlanSpec.buildPlan());
        iBambooServer.publish(iPlanSpec.buildPlanPermissions());
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : buildPlan
    // Description   : Constructs the full Bamboo plan definition with all stages, jobs, tasks,
    //                 artifacts, triggers, and notifications.
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public Plan buildPlan()
    {
        return new Plan(
                new Project()
                        .key(new BambooKey(PROJECT_KEY))
                        .name(PROJECT_NAME),
                PLAN_NAME,
                new BambooKey(PLAN_KEY))

                .description("BISS BDD Cucumber Selenium automation framework. "
                        + "Executes smoke and regression suites against DEV, STAGING, PROD.")

                // ─── Triggers ───
                .triggers(
                        // Nightly regression at 22:00 weekdays on staging
                        new ScheduledTrigger()
                                .name("Nightly Regression — Staging")
                                .description("Runs full regression on staging at 22:00 Mon-Fri")
                                .cronExpression("0 0 22 ? * MON-FRI"),

                        // Smoke on every commit to main branch
                        new AfterSuccessfulBuildPlanTrigger()
                                .triggerByMasterBranch()
                )

                // ─── Stages ───
                .stages(
                        buildSmokeStage(),
                        buildRegressionStage()
                )

                // ─── Plan-level variables (Bamboo resolves these at runtime) ───
                .variables(
                        new com.atlassian.bamboo.specs.api.builders.variable.Variable("env",     "staging"),
                        new com.atlassian.bamboo.specs.api.builders.variable.Variable("browser", "CHROME")
                )

                // ─── Branch management ───
                .planBranchManagement(
                        new PlanBranchManagement()
                                .delete(new BranchCleanup())
                                .notificationForCommitters()
                )

                // ─── Notifications ───
                .notifications(
                        new Notification()
                                .type(new PlanStatusChangedNotification())
                                .recipients(
                                        new AnyNotificationRecipient(new AtlassianModule(
                                                "com.atlassian.bamboo.plugins.bamboo-slack:recipient.slack"))
                                                .recipientString("#biss-automation"),
                                        new AnyNotificationRecipient(new AtlassianModule(
                                                "com.atlassian.bamboo.plugins.bamboo-emailSender:recipient.email"))
                                                .recipientString(QA_TEAM_EMAIL + "," + MANAGEMENT_EMAIL)
                                )
                );
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : buildSmokeStage
    // Description   : Smoke test stage — fast execution of @smoke tagged scenarios on staging
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    private Stage buildSmokeStage()
    {
        return new Stage("Smoke Tests")
                .description("Fast smoke execution — critical path only")
                .jobs(
                        new Job("Smoke — Staging", new BambooKey("SMOKE"))
                                .description("Smoke suite on Staging environment")
                                .tasks(
                                        // Step 1: Checkout source code
                                        new VcsCheckoutTask()
                                                .description("Checkout BISS Automation repository")
                                                .checkoutItems(new CheckoutTask().defaultRepository()),

                                        // Step 2: Execute smoke suite via Maven
                                        new MavenTask()
                                                .description("Run smoke tests on STAGING")
                                                .goal("test -Psmoke,staging"
                                                        + " -Denv=${bamboo.env}"
                                                        + " -Dbrowser=${bamboo.browser}"
                                                        + " -Dheadless=true"
                                                        + " -Dbamboo.buildNumber=${bamboo.buildNumber}"
                                                        + " -Dbamboo.buildPlanKey=${bamboo.buildPlanKey}"
                                                        + " -Dbamboo.agentName=${bamboo.agentName}"
                                                        + " -Dnotify.email.password=${bamboo.email.password}"
                                                        + " -Ddb.password=${bamboo.db.password}")
                                                .jdk("JDK 17")
                                                .executableLabel("Maven 3")
                                                .hasTests(true)
                                                .testResultsPath("target/surefire-reports/*.xml"),

                                        // Step 3: Parse JUnit XML — populates Bamboo test dashboard
                                        new TestParserTask(TestParserTask.TestParserTaskProperties.JUNIT_PARSER)
                                                .description("Parse JUnit XML for Bamboo dashboard")
                                                .resultDirectories("target/surefire-reports/*.xml")
                                                .pickUpTestResultsCreatedOutsideOfThisBuild(true)
                                )
                                // Artifact: HTML report — downloadable from Bamboo build page
                                .artifacts(
                                        new ArtifactDefinition("HTML Execution Report")
                                                .location("Test_Report/html")
                                                .copyPatterns("*.html"),
                                        new ArtifactDefinition("Execution Logs")
                                                .location("target/logs")
                                                .copyPatterns("*.log"),
                                        new ArtifactDefinition("Failure Log")
                                                .location("target/logs")
                                                .copyPatterns("biss-failures.log")
                                )
                );
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : buildRegressionStage
    // Description   : Regression test stage — full suite execution, only runs after smoke passes
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    private Stage buildRegressionStage()
    {
        return new Stage("Regression Tests")
                .description("Full regression suite — runs only after smoke passes")
                .jobs(
                        new Job("Regression — Staging", new BambooKey("REGR"))
                                .description("Full regression on Staging")
                                .tasks(
                                        new VcsCheckoutTask()
                                                .description("Checkout")
                                                .checkoutItems(new CheckoutTask().defaultRepository()),

                                        new MavenTask()
                                                .description("Run full regression on STAGING")
                                                .goal("test -Pregression,staging"
                                                        + " -Denv=${bamboo.env}"
                                                        + " -Dbrowser=${bamboo.browser}"
                                                        + " -Dheadless=true"
                                                        + " -Dretry.count=2"
                                                        + " -Dbamboo.buildNumber=${bamboo.buildNumber}"
                                                        + " -Dbamboo.buildPlanKey=${bamboo.buildPlanKey}"
                                                        + " -Dbamboo.agentName=${bamboo.agentName}"
                                                        + " -Dnotify.email.password=${bamboo.email.password}"
                                                        + " -Ddb.password=${bamboo.db.password}")
                                                .jdk("JDK 17")
                                                .executableLabel("Maven 3")
                                                .hasTests(true)
                                                .testResultsPath("target/surefire-reports/*.xml"),

                                        new TestParserTask(TestParserTask.TestParserTaskProperties.JUNIT_PARSER)
                                                .description("Parse JUnit XML")
                                                .resultDirectories("target/surefire-reports/*.xml")
                                                .pickUpTestResultsCreatedOutsideOfThisBuild(true)
                                )
                                .artifacts(
                                        new ArtifactDefinition("HTML Execution Report")
                                                .location("Test_Report/html")
                                                .copyPatterns("*.html"),
                                        new ArtifactDefinition("Execution Logs")
                                                .location("target/logs")
                                                .copyPatterns("*.log"),
                                        new ArtifactDefinition("Word Reports")
                                                .location("Test_Report/docs")
                                                .copyPatterns("*.docx")
                                )
                );
    }

    // ***************************************************************************************************************************************************************************************
    // Function Name : buildPlanPermissions
    // Description   : Defines who can view, edit, and clone this Bamboo plan
    // Parameters    : None
    // Author        : Aniket Pathare | aniket.pathare@goverment.ie
    // Date Created  : 10-03-2026
    // ***************************************************************************************************************************************************************************************
    public PlanPermissions buildPlanPermissions()
    {
        return new PlanPermissions(new PlanIdentifier(PROJECT_KEY, PLAN_KEY))
                .permissions(new Permissions()
                        .userPermissions("aniket.pathare",
                                PermissionType.ADMIN,
                                PermissionType.CLONE,
                                PermissionType.EDIT,
                                PermissionType.VIEW,
                                PermissionType.BUILD)
                        .groupPermissions("qa-team",
                                PermissionType.VIEW,
                                PermissionType.BUILD)
                        .loggedInUserPermissions(PermissionType.VIEW)
                );
    }
}