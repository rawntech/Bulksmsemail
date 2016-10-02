package com.tasawr.ob.thr.ad_callouts;

import javax.servlet.ServletException;
import java.io.*;
import java.util.*;
import java.text.*;
import java.lang.*;

import org.openbravo.utils.FormatUtilities;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Expression;
import org.openbravo.erpCommon.ad_callouts.SimpleCallout;
import org.openbravo.base.secureApp.VariablesSecureApp;
import org.openbravo.dal.service.OBDal;

public class LeaveApplication1 extends SimpleCallout {

	private static Logger log = Logger.getLogger(LeaveApplication1.class);
	@Override
	protected void execute(CalloutInfo info) throws ServletException {
		String strToDate = info.getStringParameter("inptodate", null);
		String strFromDate = info.getStringParameter("inpfromdate", null);
		String DATE_FORMAT = "dd-MM-yyyy";
		//Create object of SimpleDateFormat and pass the desired date format.
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		Date FromDate;
		Date ToDate;
		long requestedDays = 0;
		try {
			FromDate = (Date)formatter.parse(strFromDate);
			ToDate = (Date)formatter.parse(strToDate);
			long diff = ToDate.getTime() - FromDate.getTime();
			requestedDays= (diff / (1000 * 60 * 60 * 24));
		}catch (Exception e){}
		int daynum=(int)requestedDays+1;

		if(strToDate != null && strToDate.length()>0){
			info.addResult("inprequestedDays",daynum);
		}
	}
}
