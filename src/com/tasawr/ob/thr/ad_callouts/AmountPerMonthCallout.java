package com.tasawr.ob.thr.ad_callouts;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.openbravo.dal.service.OBCriteria;
import org.openbravo.dal.service.OBDal;
import org.openbravo.erpCommon.ad_callouts.SimpleCallout;

import com.tasawr.ob.thr.data.THREmplPaycombo;
import com.tasawr.ob.thr.data.THREmplPaycomboline;
import com.tasawr.ob.thr.data.ThrEmplPayItem;
import com.tasawr.ob.thr.data.ThrEmplSalary;

public class AmountPerMonthCallout extends SimpleCallout {

  private static Logger log = Logger.getLogger(AmountPerMonthCallout.class);

  @SuppressWarnings("unchecked")
  @Override
  protected void execute(CalloutInfo info) throws ServletException {
    // TODO Auto-generated method stub

    // if isFixed === no then callout process, else continue
    // get id of pay combination
    // go to pay item combination table - thr_empl_paycombo filter with thr_empl_salary_id
    // select those lines(salary items) - thr_empl_paycomboline
    // in the previous table, get amount per month based on those salary item lines-
    // thr_empl_pay_item - salary item
    // add them and then calculate the percentage

    String strIsFixed = info.getStringParameter("inpisFixed", null);
    String strPayCombinationId = info.getStringParameter("inpthrEmplPaycomboId", null);
    String strPercentage = info.getStringParameter("inppercentage", null);
    String strEmployeeSalaryId = info.getStringParameter("inpthrEmplSalaryId", null);
    BigDecimal bdPercentage = new BigDecimal(strPercentage);

    if (strIsFixed.equals("N")) {

      THREmplPaycombo payCombination = (THREmplPaycombo) OBDal.getInstance()
          .createCriteria(THREmplPaycombo.class)
          .add(Restrictions.eq(THREmplPaycombo.PROPERTY_ID, strPayCombinationId)).list().get(0);

      OBCriteria<THREmplPaycomboline> crtPayCombinationLines = (OBCriteria<THREmplPaycomboline>) OBDal
          .getInstance().createCriteria(THREmplPaycomboline.class)
          .add(Restrictions.eq(THREmplPaycomboline.PROPERTY_PAYCOMBINATION, payCombination));

      List<THREmplPaycomboline> lstPayCombinationLines = crtPayCombinationLines.list();

      BigDecimal sum = BigDecimal.ZERO;
      OBCriteria<ThrEmplSalary> crtEmplSalary = (OBCriteria<ThrEmplSalary>) OBDal.getInstance()
          .createCriteria(ThrEmplSalary.class)
          .add(Restrictions.eq(ThrEmplSalary.PROPERTY_ID, strEmployeeSalaryId));
      ThrEmplSalary emplSalary = crtEmplSalary.list().get(0);

      for (THREmplPaycomboline pcomLine : lstPayCombinationLines) {

        OBCriteria<ThrEmplPayItem> crtPayItem = (OBCriteria<ThrEmplPayItem>) OBDal.getInstance()
            .createCriteria(ThrEmplPayItem.class)
            .add(Restrictions.eq(ThrEmplPayItem.PROPERTY_SALARYITEM, pcomLine.getCategory()))
            .add(Restrictions.eq(ThrEmplPayItem.PROPERTY_SALARYID, emplSalary));

        ThrEmplPayItem payItem = crtPayItem.list().get(0);
        sum = sum.add(payItem.getAmountPerMonth());

      }

      info.addResult("inpamtpermonth", percentage(sum, bdPercentage));
    } else {
      log.debug("amount was fixed, not calculating the amount per month");
    }

  }

  public static BigDecimal percentage(BigDecimal base, BigDecimal pct) {
    return base.multiply(pct).divide(new BigDecimal(100));
  }

}
