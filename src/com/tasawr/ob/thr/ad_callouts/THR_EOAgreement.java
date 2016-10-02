package com.tasawr.ob.thr.ad_callouts;

import java.text.ParseException;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.openbravo.erpCommon.ad_callouts.SimpleCallout;
import org.openbravo.erpCommon.utility.OBDateUtils;

public class THR_EOAgreement extends SimpleCallout {

  private static Logger log = Logger.getLogger(THR_EOAgreement.class);

  @SuppressWarnings("unused")
  @Override
  protected void execute(CalloutInfo info) throws ServletException {
    String strdateOfJoining = info.getStringParameter("inpdateOfJoining", null);
    String strcontractBy = info.getStringParameter("inpcontractBy", null);
    String strcontractEndDate = info.getStringParameter("inpcontractEndDate", null);

    try {
      info.addResult("inpcontractenddate", OBDateUtils.addDaysToDate(strdateOfJoining,
          Integer.valueOf(strcontractBy) * 365, OBDateUtils.getDateFormatter(info.vars)));
    } catch (NumberFormatException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }
}
