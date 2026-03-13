package database;

import org.openqa.selenium.WebDriver;

import java.sql.*;

public class DBConnection {

    WebDriver driver;
    //ActionClass actions;
    //ApplicationPortalPage app = new ApplicationPortalPage(driver);

    public DBConnection(WebDriver driver) {
        this.driver = driver;
        actions = new ActionClass(driver);
    }

    // Clicking on the Pagination Button
    public void DevCDpsDataScript() {
        String value = "jdbc:oracle:thin:@//dbconn.agriculture.gov.ie:1532/DEVC.agriculture.gov.ie";
        String username = "DPS_DATA";
        String password = "DPS_DATA";

        try {
            Connection connection = DriverManager.getConnection(value, username, password);
            System.out.println("valuestring");

            String HerdNumber = app.GetCurrentHerdNumber();

            String sql = ("set serveroutput on\n" +
                    "\n" +
                    "conn DPS_DATA/DPS_DATA@DEVC\n" +
                    "\n" +
                    "select pkco_audit.Set_Audit('DPS','DPS') from dual;\n" +
                    "\n" +
                    " \n" +
                    "\n" +
                    "declare\n" +
                    "\n" +
                    "v_app_id number;\n" +
                    "\n" +
                    "begin\n" +
                    "\n" +
                    "dbms_output.put_line('Holding Id='||get_hold('"+HerdNumber+"'));\n" +
                    "\n" +
                    "select app_id into v_app_id from tddp_application where app_applicant_holding_id = get_hold('"+HerdNumber+"') and app_syr_code = 2022;\n" +
                    "\n" +
                    "pkdp_application.DeleteApplication(v_app_id);\n" +
                    "\n" +
                    "commit;\n" +
                    "\n" +
                    "end;");
//            String sql0 = ("set serveroutput on");
//            String sql1 = ("select pkco_audit.Set_Audit('DPS','DPS') from dual;");
//            String sql2 = ("select pkco_audit.Set_Audit('DPS','DPS') from dual;");
//            String sql3 = ("declare");
//            String sql4 = ("v_app_id number;");
//            String sql5 = ("begin");
//            String sql6 = ("dbms_output.put_line('Holding Id='||get_hold('"+HerdNumber+"'));");
//            String sql7 = ("select app_id into v_app_id from tddp_application where app_applicant_holding_id = get_hold('"+HerdNumber+"') and app_syr_code = 2022;");
//            String sql8 = ("pkdp_application.DeleteApplication(v_app_id);");
//            String sql9 = ("commit;");
//            String sql = ("end;");

            // String sql = ("Select pln_app_id, gfi_herd_no, pln_plan_id, pln_ver_num, pln_plan_year From tdga_farm_info_generic, tdga_plan, tsga_plan_type where gfi_id =pln_gfi_id And gfi_sch_code = 4 And pln_plan_type = plt_plan_type_code");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                String pln_app_id = rs.getString(1);
                System.out.println("Query is working");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
