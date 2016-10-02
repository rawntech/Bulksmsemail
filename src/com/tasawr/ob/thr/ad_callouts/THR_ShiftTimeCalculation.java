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

public class THR_ShiftTimeCalculation extends SimpleCallout {

	private static Logger log = Logger.getLogger(THR_ShiftTimeCalculation.class);
	@Override
	protected void execute(CalloutInfo info) throws ServletException {
		String strOffTimeDuty = info.getStringParameter("inptoTime", null);
		String strOnTimeDuty = info.getStringParameter("inpfromTime", null);
		String TIME_FORMAT = "HH:mm:ss";
		//Create object of SimpleDateFormat and pass the desired date format.
		SimpleDateFormat formatter = new SimpleDateFormat(TIME_FORMAT);
		Date OffTimeDuty;
		Date OnTimeDuty;
		long TimeDiff = 0;
		try {
			OnTimeDuty = formatter.parse(strOnTimeDuty);
			OffTimeDuty = formatter.parse(strOffTimeDuty);
			TimeDiff = (OffTimeDuty.getTime() - OnTimeDuty.getTime())/1000;
		}catch (Exception e){}
		String seconds = Integer.toString((int)(TimeDiff % 60));
		String minutes = Integer.toString((int)((TimeDiff % 3600) / 60));
		String hours = Integer.toString((int)(TimeDiff / 3600));
		String duration = hours+":"+minutes+":"+seconds;

		//if(strToDate != null && strToDate.length()>0){
			info.addResult("inpduration",duration);
			//info.addResult("MESSAGE","SUCCESS!!!!");
		//}
	}
}
