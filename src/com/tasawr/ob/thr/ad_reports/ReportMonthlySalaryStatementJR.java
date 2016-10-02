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
 * All portions are Copyright (C) 2007-2010 Openbravo SLU 
 * All Rights Reserved.
 * Contributor(s):  ______________________________________.
 ************************************************************************
 */
package com.tasawr.ob.thr.ad_reports;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openbravo.base.filter.IsIDFilter;
import org.openbravo.base.secureApp.HttpSecureAppServlet;
import org.openbravo.base.secureApp.VariablesSecureApp;
import org.openbravo.erpCommon.businessUtility.WindowTabs;
import org.openbravo.erpCommon.utility.ComboTableData;
import org.openbravo.erpCommon.utility.DateTimeData;
import org.openbravo.erpCommon.utility.LeftTabsBar;
import org.openbravo.erpCommon.utility.NavigationBar;
import org.openbravo.erpCommon.utility.OBError;
import org.openbravo.erpCommon.utility.ToolBar;
import org.openbravo.erpCommon.utility.Utility;
import org.openbravo.xmlEngine.XmlDocument;

public class ReportMonthlySalaryStatementJR extends HttpSecureAppServlet {
  private static final long serialVersionUID = 1L;

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException,
      ServletException {
    VariablesSecureApp vars = new VariablesSecureApp(request);

    if (vars.commandIn("DEFAULT")) {

      String strOrg = vars.getGlobalVariable("inpOrganization",
          "ReportMonthlySalaryStatementJR|inpOrganization", "");
      String strYear = vars.getGlobalVariable("inpYear", "ReportMonthlySalaryStatementJR|inpYear",
          "");
      String strPeriod = vars.getGlobalVariable("inpPeriod",
          "ReportMonthlySalaryStatementJR|inpPeriod", "");
      printPageDataSheet(response, vars, strOrg, strYear, strPeriod);

    } else if (vars.commandIn("PRINT_HTML")) {
      String strDate = vars.getGlobalVariable("inpDateFrom", "ReportMonthlySalaryStatementJR|Date");
      String strProductCategory = vars.getRequestGlobalVariable("inpProductCategory",
          "ReportMonthlySalaryStatementJR|productCategory");
      String strmProductId = vars.getRequestInGlobalVariable("inpmProductId_IN",
          "ReportMonthlySalaryStatementJR|mProductId", IsIDFilter.instance);
      String strX = vars.getRequestGlobalVariable("inpX", "ReportMonthlySalaryStatementJR|X");
      String strY = vars.getRequestGlobalVariable("inpY", "ReportMonthlySalaryStatementJR|Y");
      String strZ = vars.getRequestGlobalVariable("inpZ", "ReportMonthlySalaryStatementJR|Z");
      setHistoryCommand(request, "FIND");
      printPageDataHtml(response, vars, strDate, strProductCategory, strmProductId, strX, strY,
          strZ, "html");
    } else if (vars.commandIn("PRINT_PDF")) {
      String strDate = vars.getGlobalVariable("inpDateFrom", "ReportMonthlySalaryStatementJR|Date");
      String strProductCategory = vars.getRequestGlobalVariable("inpProductCategory",
          "ReportMonthlySalaryStatementJR|productCategory");
      String strmProductId = vars.getRequestInGlobalVariable("inpmProductId_IN",
          "ReportMonthlySalaryStatementJR|mProductId", IsIDFilter.instance);
      String strX = vars.getRequestGlobalVariable("inpX", "ReportMonthlySalaryStatementJR|X");
      String strY = vars.getRequestGlobalVariable("inpY", "ReportMonthlySalaryStatementJR|Y");
      String strZ = vars.getRequestGlobalVariable("inpZ", "ReportMonthlySalaryStatementJR|Z");
      setHistoryCommand(request, "FIND");
      printPageDataHtml(response, vars, strDate, strProductCategory, strmProductId, strX, strY,
          strZ, "pdf");
    } else if (vars.commandIn("PRINT_XLS")) {
      String strDate = vars.getGlobalVariable("inpDateFrom", "ReportMonthlySalaryStatementJR|Date");
      String strProductCategory = vars.getRequestGlobalVariable("inpProductCategory",
          "ReportMonthlySalaryStatementJR|productCategory");
      String strmProductId = vars.getRequestInGlobalVariable("inpmProductId_IN",
          "ReportMonthlySalaryStatementJR|mProductId", IsIDFilter.instance);
      String strX = vars.getRequestGlobalVariable("inpX", "ReportMonthlySalaryStatementJR|X");
      String strY = vars.getRequestGlobalVariable("inpY", "ReportMonthlySalaryStatementJR|Y");
      String strZ = vars.getRequestGlobalVariable("inpZ", "ReportMonthlySalaryStatementJR|Z");
      setHistoryCommand(request, "FIND");
      printPageDataHtml(response, vars, strDate, strProductCategory, strmProductId, strX, strY,
          strZ, "xls");
    } else
      pageError(response);
  }

  private void printPageDataHtml(HttpServletResponse response, VariablesSecureApp vars,
      String strDate, String strProductCategory, String strmProductId, String strX, String strY,
      String strZ, String strOutput) throws IOException, ServletException {
    if (log4j.isDebugEnabled())
      log4j.debug("Output: dataSheet");

    ReportMonthlySalaryStatementData[] data = ReportMonthlySalaryStatementData.select(this,
        vars.getLanguage(), Utility.getContext(this, vars, "#User_Client", "ReportSalaryControl"),
        Utility.getContext(this, vars, "#AccessibleOrgTree", "ReportSalaryControl"),
        DateTimeData.nDaysAfter(this, strDate, "1"), strmProductId, strProductCategory, strX, strY,
        strZ);

    String strReportName = "@basedesign@/com/tasawr/ob/thr/ad_reports/ReportMonthlySalaryStatementJR.jrxml";

    HashMap<String, Object> parameters = new HashMap<String, Object>();
    // parameters.put("Subtitle",strSubtitle);
    renderJR(vars, response, strReportName, strOutput, parameters, data, null);

  }

  private void printPageDataSheet(HttpServletResponse response, VariablesSecureApp vars,
      String strOrg, String strYear, String strPeriod) throws IOException, ServletException {
    if (log4j.isDebugEnabled())
      log4j.debug("Output: dataSheet");
    response.setContentType("text/html; charset=UTF-8");
    PrintWriter out = response.getWriter();
    XmlDocument xmlDocument = xmlEngine.readXmlTemplate(
        "com/tasawr/ob/thr/ad_reports/ReportMonthlySalaryStatementJR").createXmlDocument();

    ToolBar toolbar = new ToolBar(this, vars.getLanguage(), "ReportMonthlySalaryStatementJR",
        false, "", "", "", false, "ad_reports", strReplaceWith, false, true);
    toolbar.prepareSimpleToolBarTemplate();
    xmlDocument.setParameter("toolbar", toolbar.toString());

    try {
      WindowTabs tabs = new WindowTabs(this, vars,
          "com.tasawr.ob.thr.ad_reports.ReportMonthlySalaryStatementJR");
      xmlDocument.setParameter("parentTabContainer", tabs.parentTabs());
      xmlDocument.setParameter("mainTabContainer", tabs.mainTabs());
      xmlDocument.setParameter("childTabContainer", tabs.childTabs());
      xmlDocument.setParameter("theme", vars.getTheme());
      NavigationBar nav = new NavigationBar(this, vars.getLanguage(),
          "ReportMonthlySalaryStatementJR.html", classInfo.id, classInfo.type, strReplaceWith,
          tabs.breadcrumb());
      xmlDocument.setParameter("navigationBar", nav.toString());
      LeftTabsBar lBar = new LeftTabsBar(this, vars.getLanguage(),
          "ReportMonthlySalaryStatementJR.html", strReplaceWith);
      xmlDocument.setParameter("leftTabs", lBar.manualTemplate());
    } catch (Exception ex) {
      throw new ServletException(ex);
    }

    {
      OBError myMessage = vars.getMessage("ReportMonthlySalaryStatementJR");
      vars.removeMessage("ReportMonthlySalaryStatementJR");
      if (myMessage != null) {
        xmlDocument.setParameter("messageType", myMessage.getType());
        xmlDocument.setParameter("messageTitle", myMessage.getTitle());
        xmlDocument.setParameter("messageMessage", myMessage.getMessage());
      }
    }

    xmlDocument.setParameter("calendar", vars.getLanguage().substring(0, 2));
    xmlDocument.setParameter("directory", "var baseDirectory = \"" + strReplaceWith + "/\";\n");
    xmlDocument.setParameter("paramLanguage", "defaultLang=\"" + vars.getLanguage() + "\";");

    xmlDocument.setParameter("mOrg", strOrg);
    xmlDocument.setParameter("mYear", strYear);
    xmlDocument.setParameter("mPeriod", strPeriod);

    try {
      ComboTableData comboTableData = new ComboTableData(vars, this, "TABLEDIR", "AD_Org_ID", "",
          "", Utility.getContext(this, vars, "#AccessibleOrgTree",
              "ReportPurchaseDimensionalAnalysesJR"), Utility.getContext(this, vars,
              "#User_Client", "ReportPurchaseDimensionalAnalysesJR"), 0);
      Utility.fillSQLParameters(this, vars, null, comboTableData,
          "ReportPurchaseDimensionalAnalysesJR", strOrg);
      xmlDocument.setData("reportM_ORG", "liststructure", comboTableData.select(false));
      comboTableData = null;

    } catch (Exception ex) {
      throw new ServletException(ex);
    }

    try {
      ComboTableData comboTableData = new ComboTableData(vars, this, "TABLEDIR", "C_Year_ID", "",
          "", Utility.getContext(this, vars, "#AccessibleOrgTree",
              "ReportPurchaseDimensionalAnalysesJR"), Utility.getContext(this, vars,
              "#User_Client", "ReportPurchaseDimensionalAnalysesJR"), 0);
      Utility.fillSQLParameters(this, vars, null, comboTableData,
          "ReportPurchaseDimensionalAnalysesJR", strYear);
      xmlDocument.setData("reportM_Year", "liststructure", comboTableData.select(false));
      comboTableData = null;

    } catch (Exception ex) {
      throw new ServletException(ex);
    }

    try {
      ComboTableData comboTableData = new ComboTableData(vars, this, "TABLEDIR", "C_Period_ID", "",
          "", Utility.getContext(this, vars, "#AccessibleOrgTree",
              "ReportPurchaseDimensionalAnalysesJR"), Utility.getContext(this, vars,
              "#User_Client", "ReportPurchaseDimensionalAnalysesJR"), 0);
      Utility.fillSQLParameters(this, vars, null, comboTableData,
          "ReportPurchaseDimensionalAnalysesJR", strPeriod);
      xmlDocument.setData("reportM_PERIOD", "liststructure", comboTableData.select(false));
      comboTableData = null;

    } catch (Exception ex) {
      throw new ServletException(ex);
    }

    out.println(xmlDocument.print());
    out.close();
  }

  /*
   * void printPageDataSheet(HttpServletResponse response, VariablesSecureApp vars, String strDate,
   * String strProductCategory, String strmProductId, String strX, String strY, String strZ) throws
   * IOException, ServletException { if (log4j.isDebugEnabled()) log4j.debug("Output: dataSheet");
   * response.setContentType("text/html; charset=UTF-8"); PrintWriter out = response.getWriter();
   * XmlDocument xmlDocument = xmlEngine.readXmlTemplate(
   * "org/openbravo/erpCommon/ad_reports/ReportMonthlySalaryStatementJR" ).createXmlDocument();
   * 
   * ToolBar toolbar = new ToolBar(this, vars.getLanguage(), "ReportMonthlySalaryStatementJR",
   * false, "", "", "",false, "ad_reports", strReplaceWith, false, true);
   * toolbar.prepareSimpleToolBarTemplate(); xmlDocument.setParameter("toolbar",
   * toolbar.toString());
   * 
   * try { WindowTabs tabs = new WindowTabs(this, vars,
   * "org.openbravo.erpCommon.ad_reports.ReportMonthlySalaryStatementJR");
   * xmlDocument.setParameter("parentTabContainer", tabs.parentTabs());
   * xmlDocument.setParameter("mainTabContainer", tabs.mainTabs());
   * xmlDocument.setParameter("childTabContainer", tabs.childTabs());
   * xmlDocument.setParameter("theme", vars.getTheme()); NavigationBar nav = new NavigationBar(this,
   * vars.getLanguage(), "ReportMonthlySalaryStatementJR.html", classInfo.id, classInfo.type,
   * strReplaceWith, tabs.breadcrumb()); xmlDocument.setParameter("navigationBar", nav.toString());
   * LeftTabsBar lBar = new LeftTabsBar(this, vars.getLanguage(),
   * "ReportMonthlySalaryStatementJR.html", strReplaceWith); xmlDocument.setParameter("leftTabs",
   * lBar.manualTemplate()); } catch (Exception ex) { throw new ServletException(ex); } { OBError
   * myMessage = vars.getMessage("ReportMonthlySalaryStatement");
   * vars.removeMessage("ReportMonthlySalaryStatement"); if (myMessage!=null) {
   * xmlDocument.setParameter("messageType", myMessage.getType());
   * xmlDocument.setParameter("messageTitle", myMessage.getTitle());
   * xmlDocument.setParameter("messageMessage", myMessage.getMessage()); } }
   * 
   * xmlDocument.setParameter("calendar", vars.getLanguage().substring(0,2));
   * xmlDocument.setParameter("directory", "var baseDirectory = \"" + strReplaceWith + "/\";\n");
   * xmlDocument.setParameter("paramLanguage", "defaultLang=\"" + vars.getLanguage() + "\";");
   * xmlDocument.setParameter("date", strDate); xmlDocument.setParameter("parameterX", strX);
   * xmlDocument.setParameter("parameterY", strY); xmlDocument.setParameter("parameterZ", strZ);
   * xmlDocument.setParameter("mProductCategoryId", strProductCategory);
   * 
   * xmlDocument.setData("reportMProductId_IN", "liststructure",
   * ReportMonthlySalaryStatementData.selectMproduct2(this, Utility.getContext(this, vars,
   * "#AccessibleOrgTree", ""), Utility.getContext(this, vars, "#User_Client", ""), strmProductId));
   * try { ComboTableData comboTableData = new ComboTableData(vars, this, "TABLEDIR",
   * "M_Product_Category_ID", "", "", Utility.getContext(this, vars, "#AccessibleOrgTree",
   * "ReportPricelist"), Utility.getContext(this, vars, "#User_Client", "ReportPricelist"), 0);
   * Utility.fillSQLParameters(this, vars, null, comboTableData, "ReportPricelist",
   * strProductCategory); xmlDocument.setData("reportM_PRODUCT_CATEGORYID","liststructure",
   * comboTableData.select(false)); comboTableData = null; } catch (Exception ex) { throw new
   * ServletException(ex); }
   * 
   * 
   * xmlDocument.setData("structure1", ReportMonthlySalaryStatementData.select(this,
   * vars.getLanguage(), Utility.getContext(this, vars, "#User_Client", "ReportSalaryControl"),
   * Utility.getContext(this, vars, "#AccessibleOrgTree", "ReportSalaryControl"),
   * DateTimeData.nDaysAfter(this, strDate,"1"), strmProductId, strProductCategory, strX, strY,
   * strZ)); out.println(xmlDocument.print()); out.close(); }
   */

  public String getServletInfo() {
    return "Servlet ReportMonthlySalaryStatementJR. This Servlet was made by Jon Alegria";
  } // end of getServletInfo() method
}
