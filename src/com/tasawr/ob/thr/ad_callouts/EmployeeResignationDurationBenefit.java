package com.tasawr.ob.thr.ad_callouts;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;

import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openbravo.dal.service.OBCriteria;
import org.openbravo.dal.service.OBDal;
import org.openbravo.erpCommon.ad_callouts.SimpleCallout;

import com.tasawr.ob.thr.data.THREmplEOS;
import com.tasawr.ob.thr.data.THREmplEOSline;
import com.tasawr.ob.thr.data.THREmplInfo;

@SuppressWarnings("serial")
public class EmployeeResignationDurationBenefit extends SimpleCallout {

  @Override
  protected void execute(CalloutInfo info) throws ServletException {

    // OBDateUtils.formatDate(employee.getDateOfJoining())

    // inpbenefitAmount
    // inpdateOfJoining
    // inpisresign
    //
    // inpthrEmplInfoId
    // inpthrEmDesignationId
    // inpduration
    // inpdateOfLeaving

    DateTimeFormatter fmt = DateTimeFormat.forPattern("dd-MM-yyyy");
    BigDecimal percentage = BigDecimal.ZERO;
    BigDecimal benefit = BigDecimal.ZERO;

    // get value from resignation field
    String strResignationDate = info.getStringParameter("inpdateOfLeaving", null);
    // get value from joining date
    String strJoiningDate = info.getStringParameter("inpdateOfJoining", null);
    // get value isresign
    String strIsResigned = info.getStringParameter("inpisresign", null);

    // calculate duration

    DateTime joiningDate = fmt.parseDateTime(strJoiningDate);

    DateTime resignationDate = fmt.parseDateTime(strResignationDate);
    Interval jobDuration = new Interval(joiningDate, resignationDate);
    Period pd = new Period(joiningDate, resignationDate);
    // Long durationInDays = getDateDiff(joiningDate.toDate(), resignationDate.toDate(),
    // TimeUnit.DAYS);
    Long durationInDays = (long) Days.daysBetween(joiningDate, resignationDate).getDays();

    float durationInYears = (float) ((float) durationInDays / 365.25);

    String strDuration = "";

    strDuration += Days.daysBetween(joiningDate, resignationDate).getDays() + " days, ";

    info.addResult("inpduration",
        pd.getYears() + " Years " + pd.getMonths() + " months " + pd.getDays() + " days");
    // if duration is greater than or equal to 2 years
    // if (durationInYears >= 2) {
    // get gross salary value from employee table

    String strEmployeeId = info.getStringParameter("inpthrEmplInfoId", null);
    OBCriteria<THREmplInfo> crtEmployee = (OBCriteria<THREmplInfo>) OBDal.getInstance()
        .createCriteria(THREmplInfo.class)
        .add(Restrictions.eq(THREmplInfo.PROPERTY_ID, strEmployeeId));
    THREmplInfo employee = crtEmployee.list().get(0);

    Long grossSalary = employee.getGrossAmount();
    BigDecimal grossBig = new BigDecimal(grossSalary);

    // get percentage value from end of service 2 table according to isResign and duration

    // String strAdOrgId = info.getStringParameter("inpadOrgId", null);
    // OBCriteria<Organization> crtOrg = (OBCriteria<Organization>) OBDal.getInstance()
    // .createCriteria(Organization.class)
    // .add(Restrictions.eq(Organization.PROPERTY_ID, strAdOrgId));
    // Organization org = crtOrg.list().get(0);
    if (strIsResigned.equals("Y")) {
      if (durationInYears >= 2) {
        OBCriteria<THREmplEOS> crtEmplEOS = (OBCriteria<THREmplEOS>) OBDal.getInstance()
            .createCriteria(THREmplEOS.class)
            .add(Restrictions.eq(THREmplEOS.PROPERTY_CLAIMINGYEAR, "2"));
        THREmplEOS emplEOS = crtEmplEOS.list().get(0);
        OBCriteria<THREmplEOSline> crtEmplEOSLines = OBDal.getInstance().createCriteria(
            THREmplEOSline.class);
        crtEmplEOSLines.add(Restrictions.eq(THREmplEOSline.PROPERTY_ENDOFSERVICE, emplEOS));
        // crtEmplEOSLines.add(Restrictions.eq(THREmplEOSline.PROPERTY_ISRESGINE,
        // strIsResigned.equals("N") ? false : true));
        List<THREmplEOSline> lstEmplEOSLines = crtEmplEOSLines.list();
        Collections.sort(lstEmplEOSLines, new Comparator<THREmplEOSline>() {

          public int compare(THREmplEOSline o1, THREmplEOSline o2) {

            return o1.getYearTo().compareTo(o2.getYearTo());
          }
        });

        if (lstEmplEOSLines.size() > 0) {
          for (THREmplEOSline eosLine : lstEmplEOSLines) {
            BigDecimal tmpDurationInYears = new BigDecimal(
                (float) Math.round(durationInYears * 100000) / 100000);
            BigDecimal toYear = eosLine.getYearTo().setScale(5, RoundingMode.CEILING);
            // float toYear = Math.round(eosLine.getYearTo().longValueExact() * 100000) / 100000;

            if (tmpDurationInYears.compareTo(toYear) == -1) {
              percentage = eosLine.getPercentage();
              break;
            }
          }
        }
        // calculate benefit
        // benefit = gross * percentage according to duration and isresign
        benefit = percentage(grossBig, percentage);

        info.addResult("inpbenefitAmount", benefit);

      } else {
        info.addResult("inpbenefitAmount", "");
        System.out.println("Duration is less than two years. No benefit is available.");
      }

    } else {
      OBCriteria<THREmplEOS> crtEmplEOS = (OBCriteria<THREmplEOS>) OBDal.getInstance()
          .createCriteria(THREmplEOS.class)
          .add(Restrictions.eq(THREmplEOS.PROPERTY_CLAIMINGYEAR, "0"));
      THREmplEOS emplEOS = crtEmplEOS.list().get(0);
      OBCriteria<THREmplEOSline> crtEmplEOSLines = OBDal.getInstance().createCriteria(
          THREmplEOSline.class);
      crtEmplEOSLines.add(Restrictions.eq(THREmplEOSline.PROPERTY_ENDOFSERVICE, emplEOS));
      // crtEmplEOSLines.add(Restrictions.eq(THREmplEOSline.PROPERTY_ISRESGINE,
      // strIsResigned.equals("N") ? false : true));
      List<THREmplEOSline> lstEmplEOSLines = crtEmplEOSLines.list();
      Collections.sort(lstEmplEOSLines, new Comparator<THREmplEOSline>() {

        public int compare(THREmplEOSline o1, THREmplEOSline o2) {

          return o1.getYearTo().compareTo(o2.getYearTo());
        }
      });

      if (lstEmplEOSLines.size() > 0) {
        for (THREmplEOSline eosLine : lstEmplEOSLines) {
          BigDecimal tmpDurationInYears = new BigDecimal(
              (float) Math.round(durationInYears * 100000) / 100000);
          BigDecimal toYear = eosLine.getYearTo().setScale(5, RoundingMode.CEILING);
          // float toYear = Math.round(eosLine.getYearTo().longValueExact() * 100000) / 100000;

          if (tmpDurationInYears.compareTo(toYear) == -1) {
            percentage = eosLine.getPercentage();
            break;
          }
        }
      }
      // calculate benefit
      // benefit = gross * percentage according to duration and isresign
      benefit = percentage(grossBig, percentage);

      info.addResult("inpbenefitAmount", benefit);

    }

    // } else {
    // info.addResult("inpbenefitAmount", "");
    // System.out.println("Duration is less than two years. No benefit is available.");
    // }

  }

  public static BigDecimal percentage(BigDecimal base, BigDecimal pct) {
    return base.multiply(pct).divide(new BigDecimal(100));
  }

  public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
    long diffInMillies = date2.getTime() - date1.getTime();
    return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
  }
}
