/*
 *************************************************************************
 * The contents of this file are subject to the Openbravo  Public  License
 * Version  1.1  (the  "License"),  being   the  Mozilla   Public  License
 * Version 1.1  with a permitted attribution clause; you may not  use this
 * file except in compliance with the License. You  may  obtain  a copy of
 * the License at http://www.openbravo.com/legal/license.html
 * Software distributed under the License  is  distributed  on  an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific  language  governing  rights  and  limitations
 * under the License.
 * The Original Code is Openbravo ERP.
 * The Initial Developer of the Original Code is Openbravo SLU
 * All portions are Copyright (C) 2009 Openbravo SLU
 * All Rights Reserved.
 * Contributor(s):  ______________________________________.
 ************************************************************************
 */
package com.tasawr.ob.thr.ad_callouts;

import javax.servlet.ServletException;
import java.util.*;
import java.lang.*;

import org.hibernate.criterion.Expression;

import org.openbravo.utils.FormatUtilities;
import org.openbravo.erpCommon.ad_callouts.SimpleCallout;
import org.openbravo.base.secureApp.VariablesSecureApp;

import org.openbravo.dal.service.OBDal;
import org.openbravo.utils.FormatUtilities;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;

public class LeaveApplicationNumOfDaysCalculation extends SimpleCallout {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(LeaveApplicationNumOfDaysCalculation.class);

	@Override
	protected void execute(CalloutInfo info) throws ServletException {
		 String Changed = info.getStringParameter("inpLastFieldChanged",null);
		 String strFromDate = info.getStringParameter("inpFromdate", null);
		 String strToDate = info.getStringParameter("inpTodate", null);
		 info.addResult("inpDayes", FormatUtilities.replaceJS(getNumberOfDays(info.vars, strFromDate, strToDate)));
		 log.info("From Date:"+strFromDate);
		 log.info("From Date:"+strToDate);
		 log.info("Last Field Changed:"+Changed);
	}

	protected String getNumberOfDays(VariablesSecureApp vars, String strFromDate, String strToDate) {
		DateFormat formatter = new SimpleDateFormat(vars.getSessionValue("#AD_JavaDateFormat")); // Session variable deriving from a setting in Openbravo.properties
		Date FromDate;
		Date ToDate;
		long days = 0;
		try {
			FromDate = (Date)formatter.parse(strFromDate);
			ToDate = (Date)formatter.parse(strToDate);
			long diff = ToDate.getTime() - FromDate.getTime();
			days = (diff / (1000 * 60 * 60 * 24));
		}catch (Exception e){}
		String strDays = String.valueOf(days);
		// return strDays
		return strDays;
	}

}



