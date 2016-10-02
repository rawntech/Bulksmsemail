package com.tasawr.ob.thr.ad_callouts;

import javax.servlet.ServletException;

import org.hibernate.criterion.Restrictions;
import org.openbravo.dal.service.OBCriteria;
import org.openbravo.dal.service.OBDal;
import org.openbravo.erpCommon.ad_callouts.SimpleCallout;

import com.tasawr.ob.thr.data.THREmplInfo;

public class PromotionCurrentAllowanceCallout extends SimpleCallout {

  @Override
  protected void execute(CalloutInfo info) throws ServletException {
    // TODO Auto-generated method stub
    // inpthrEmplInfoId
    // inpoldSalary
    // inppromotedAllowance
    // inpcurrentAllowances
    String strEmployeeId = info.getStringParameter("inpthrEmplInfoId", null);
    OBCriteria<THREmplInfo> crtEmployee = (OBCriteria<THREmplInfo>) OBDal.getInstance()
        .createCriteria(THREmplInfo.class)
        .add(Restrictions.eq(THREmplInfo.PROPERTY_ID, strEmployeeId));
    THREmplInfo employee = crtEmployee.list().get(0);

    info.addResult("inpcurrentAllowances", employee.getGrossAmount());
    info.addResult("inpoldSalary", employee.getGrossAmount());

    System.out.println("inside current allowance callout");

  }

}
