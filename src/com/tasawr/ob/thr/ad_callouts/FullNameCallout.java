package com.tasawr.ob.thr.ad_callouts;

import javax.servlet.ServletException;

import org.openbravo.erpCommon.ad_callouts.SimpleCallout;

public class FullNameCallout extends SimpleCallout {

  @Override
  protected void execute(CalloutInfo info) throws ServletException {
    // TODO Auto-generated method stub
    info.addResult(
        "inpknownAs",
        info.getStringParameter("inpfirstname", null) + " "
            + info.getStringParameter("inpmiddlename", null) + " "
            + info.getStringParameter("inplastname", null));

  }

}
