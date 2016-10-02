package com.tasawr.ob.thr.ad_callouts;

import java.math.BigDecimal;

import javax.servlet.ServletException;

import org.hibernate.criterion.Restrictions;
import org.openbravo.dal.service.OBCriteria;
import org.openbravo.dal.service.OBDal;
import org.openbravo.erpCommon.ad_callouts.SimpleCallout;

import com.tasawr.ob.thr.data.THREmplInfo;

public class PromotionIncreasedAmountCallout extends SimpleCallout {

  @Override
  protected void execute(CalloutInfo info) throws ServletException {
    // TODO Auto-generated method stub
    //
    //
    //
    BigDecimal bdPromotedAllowance = info.getBigDecimalParameter("inppromotedAllowance");

    BigDecimal bdIncreasedAmount = info.getBigDecimalParameter("inpincreasedAmount");

    String strEmployeeId = info.getStringParameter("inpthrEmplInfoId", null);
    OBCriteria<THREmplInfo> crtEmployee = (OBCriteria<THREmplInfo>) OBDal.getInstance()
        .createCriteria(THREmplInfo.class)
        .add(Restrictions.eq(THREmplInfo.PROPERTY_ID, strEmployeeId));
    THREmplInfo employee = crtEmployee.list().get(0);

    if (info.getStringParameter("inpLastFieldChanged", null).equals("inpincreasedAmount")) {

      info.addResult("inppromotedAllowance",
          BigDecimal.valueOf(employee.getGrossAmount()).add(bdIncreasedAmount));
      info.addResult("inpnewSalary",
          BigDecimal.valueOf(employee.getGrossAmount()).add(bdIncreasedAmount));

    } else if (info.getStringParameter("inpLastFieldChanged", null).equals("inppromotedAllowance")) {
      info.addResult("inpincreasedAmount",
          bdPromotedAllowance.subtract(BigDecimal.valueOf(employee.getGrossAmount())));
      info.addResult("inpnewSalary", bdPromotedAllowance);

    }

    System.out.println("inside increased amount callout");
  }
}
