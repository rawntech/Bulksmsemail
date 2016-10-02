package com.tasawr.ob.thr.ad_callouts;

import java.math.BigDecimal;

import javax.servlet.ServletException;

import org.hibernate.criterion.Restrictions;
import org.openbravo.dal.service.OBCriteria;
import org.openbravo.dal.service.OBDal;
import org.openbravo.erpCommon.ad_callouts.SimpleCallout;
import org.openbravo.erpCommon.utility.OBDateUtils;

import com.tasawr.ob.thr.data.THREmplInfo;
import com.tasawr.ob.thr.data.THREmSftInf;

public class THRShiftIdToIntimeOuttimeWorkingDays extends SimpleCallout {

  @Override
  protected void execute(CalloutInfo info) throws ServletException {
    // TODO Auto-generated method stub
    // load inpdateOfJoining
    // load inpthrEmDepartmentId
    // load inpthrEmDesignationId

    // get inpthrEmSftInfId
//    String strEmployeeId = info.getStringParameter("inpthrEmSftInfId", null);
//    OBCriteria<THREmplInfo> crtEmployee = (OBCriteria<THREmplInfo>) OBDal.getInstance()
//        .createCriteria(THREmplInfo.class)
//        .add(Restrictions.eq(THREmplInfo.PROPERTY_ID, strEmployeeId));
//    THREmplInfo employee = crtEmployee.list().get(0);

	  
	  //my	thrEmSftInfId  thrEmSftInfId  thrEmSftInfId
	  String strthrEmSftInfId = info.getStringParameter("inpthrEmSftInfId", null);
	    OBCriteria<THREmSftInf> crtEmployee = (OBCriteria<THREmSftInf>) OBDal.getInstance()
	        .createCriteria(THREmSftInf.class)
	        .add(Restrictions.eq(THREmSftInf.PROPERTY_ID, strthrEmSftInfId));
	    THREmSftInf employee = crtEmployee.list().get(0);
	    //my
	  
    //info.addResult("inpdateOfJoining", OBDateUtils.formatDate(employee.getDateOfJoining()));

    /*info.addSelect("inpthrEmDesignationId");
    info.addSelectResult(employee.getDesignation().getId(), employee.getDesignation()
        .getIdentifier(), true);
    info.endSelect();

    info.addSelect("inpthrEmDepartmentId");
    info.addSelectResult(employee.getDepartment().getId(),
        employee.getDepartment().getIdentifier(), true);
    info.endSelect();*/
	//my  inTime outTime workingDays
	    info.addSelect("inpinTime");
	    info.addSelectResult(employee.getFromTime().toString(), employee.getFromTime().toString(), true);
	    info.endSelect();
	    
	    info.addSelect("inpoutTime");
	    info.addSelectResult(employee.getToTime().toString(), employee.getToTime().toString(), true);
	    info.endSelect();

	    if (employee.getNoOfWorkingDays() != null){
	    info.addSelect("inpworkingDays");
	    info.addSelectResult(employee.getNoOfWorkingDays().toString(), employee.getNoOfWorkingDays().toString(), true);
	    info.endSelect();
	    } else {
	    	info.addSelect("inpworkingDays");
	    	employee.setNoOfWorkingDays(BigDecimal.ZERO);
	    	info.endSelect();
	    }
	//my

  }
}