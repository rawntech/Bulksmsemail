package com.tasawr.ob.thr.ad_callouts;

import javax.servlet.ServletException;

import org.hibernate.criterion.Restrictions;
import org.openbravo.dal.service.OBCriteria;
import org.openbravo.dal.service.OBDal;
import org.openbravo.erpCommon.ad_callouts.SimpleCallout;
import org.openbravo.erpCommon.utility.OBDateUtils;

import com.tasawr.ob.thr.data.THREmplInfo;

public class EmployeeEOSDataLoad extends SimpleCallout {

  @Override
  protected void execute(CalloutInfo info) throws ServletException {
    // TODO Auto-generated method stub
    // load inpdateOfJoining
    // load inpthrEmDepartmentId
    // load inpthrEmDesignationId

    // get inpthrEmplInfoId
    String strEmployeeId = info.getStringParameter("inpthrEmplInfoId", null);
    OBCriteria<THREmplInfo> crtEmployee = (OBCriteria<THREmplInfo>) OBDal.getInstance()
        .createCriteria(THREmplInfo.class)
        .add(Restrictions.eq(THREmplInfo.PROPERTY_ID, strEmployeeId));
    THREmplInfo employee = crtEmployee.list().get(0);

    info.addResult("inpdateOfJoining", OBDateUtils.formatDate(employee.getDateOfJoining()));

    info.addSelect("inpthrEmDesignationId");
    info.addSelectResult(employee.getDesignation().getId(), employee.getDesignation()
        .getIdentifier(), true);
    info.endSelect();

    info.addSelect("inpthrEmDepartmentId");
    info.addSelectResult(employee.getDepartment().getId(),
        employee.getDepartment().getIdentifier(), true);
    info.endSelect();

  }
}
