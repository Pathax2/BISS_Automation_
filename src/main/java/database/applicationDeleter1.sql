package com.DB;

set serveroutput on

conn BISS_DATA/BISS_DATA@CENTEST

exec pkbs_audit.pr_SetAudit('BISS','BISS');



declare

vv_HerdNo VARCHAR2(10) := 'D206040X';
v_app_id number;

begin

dbms_output.put_line('Deleting submission for herd number'||vv_HerdNo);

SELECT app_id
  INTO v_app_id
  FROM tdbs_application
 WHERE app_herd_no = vv_HerdNo
   AND app_mde_code = 4; --Inet

pkbs_application.Pr_DeleteSubmission(v_app_id);

commit;

end;
