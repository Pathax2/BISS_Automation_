-- Q_STAFF_1  submitted -> APP_HERD_NO
SELECT app_herd_no FROM (
  SELECT app_herd_no
  FROM   vwbs_application_herd
  WHERE  mde_abbrev = 'I'
  AND    app_year   = 2024
  AND    SUBSTR(app_herd_no, 1, 1) > 'A'
  AND    NOT EXISTS (
             SELECT 1 FROM tdbs_application_land
             WHERE  apl_year = 2024 AND apl_app_id = aph_app_id )
  ORDER  BY app_herd_no
) WHERE ROWNUM <= 25;

-- Q_STAFF_2  penalty -> APP_HERD_NO
SELECT app_herd_no FROM (
  SELECT app_herd_no
  FROM   vwbs_application_penalty
  WHERE  penalty_type = 'APPLICATION'
  AND    app_year     = 2024
  AND    SUBSTR(app_herd_no, 1, 1) > 'A'
  ORDER  BY app_herd_no
) WHERE ROWNUM <= 25;

-- Q_STAFF_3  noamend -> APP_HERD_NO
SELECT app_herd_no FROM (
  SELECT app_herd_no
  FROM   tdbs_application
  WHERE  app_year      = 2024
  AND    app_mde_code  = 4
  AND    app_dct_code != 7
  AND    SUBSTR(app_herd_no, 1, 1) > 'A'
  ORDER  BY app_herd_no
) WHERE ROWNUM <= 25;

-- Q_STAFF_4  admincheck -> APP_HERD_NO
SELECT app_herd_no FROM (
  SELECT ta.app_herd_no
  FROM   tdbs_application ta
  LEFT   JOIN tdbs_admin_check tac ON tac.adc_app_id = ta.app_id
  WHERE  ta.app_mde_code = 4
  AND    ta.app_year     = 2024
  AND    tac.adc_app_id IS NULL
  AND    SUBSTR(ta.app_herd_no, 1, 1) > 'A'
  ORDER  BY ta.app_herd_no
) WHERE ROWNUM <= 25;

-- Q_STAFF_5  queryletter -> APP_HERD_NO
SELECT app_herd_no FROM (
  SELECT DISTINCT app_year, app_herd_no
  FROM   tdbs_system_note, tdbs_application
  WHERE  snt_app_id = app_id
  AND    app_year   = 2024
  AND    snt_action_subtype != 'QUERY_LETTER_SUBMITTED'
  AND    SUBSTR(app_herd_no, 1, 1) > 'A'
  ORDER  BY app_herd_no
) WHERE ROWNUM <= 25;

-- Q_STAFF_6  simopted -> APP_HERD_NO
SELECT app_herd_no FROM (
  SELECT app_herd_no
  FROM   vwbs_application_herd
  WHERE  mde_abbrev = 'I'
  AND    app_year   = 2024
  AND    SUBSTR(app_herd_no, 1, 1) > 'A'
  AND    aph_app_id IN (
             SELECT apf_app_id FROM tdbs_application_flag
             WHERE  apf_flag_value = 'Y' AND apf_flg_code = 7 )
  ORDER  BY app_herd_no
) WHERE ROWNUM <= 25;

-- Q_STAFF_7  payments -> PHD_HERD_NO
SELECT phd_herd_no FROM (
  SELECT ta.app_herd_no AS phd_herd_no
  FROM   tdbs_application ta
  LEFT   JOIN tdbs_payment_header tph ON tph.phd_app_id = ta.app_id
  WHERE  ta.app_year      = 2024
  AND    tph.phd_ptp_code = 9
  ORDER  BY ta.app_herd_no
) WHERE ROWNUM <= 25;

-- Q_STAFF_8  parcels -> LPS_PARCEL_LABEL  (no year in legacy)
SELECT lps_parcel_label FROM (
  SELECT DISTINCT twp.lps_parcel_label
  FROM   vwlp_parcel twp
  JOIN   tddp_scheme_land_unit tslu ON tslu.slu_lnu_id = twp.lps_parcel_id
) WHERE ROWNUM <= 14;

-- Q_STAFF_9  refs -> APP_HERD_NO  (TRN: substr < 'A')
SELECT app_herd_no FROM (
  SELECT app_herd_no
  FROM   vwbs_application_herd
  WHERE  mde_abbrev = 'I'
  AND    app_year   = 2024
  AND    SUBSTR(app_herd_no, 1, 1) < 'A'
  ORDER  BY app_herd_no
) WHERE ROWNUM <= 5;
