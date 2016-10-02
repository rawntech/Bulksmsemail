package com.tasawr.ob.thr.ad_callouts;

import javax.servlet.ServletException;

import org.joda.time.DateTime;
import org.joda.time.chrono.IslamicChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openbravo.erpCommon.ad_callouts.SimpleCallout;

public class HRGregToHijri extends SimpleCallout {

  private static final long serialVersionUID = 1L;

  @Override
  protected void execute(CalloutInfo info) throws ServletException {

    DateTime dtHijri;

    DateTimeFormatter fmt = DateTimeFormat.forPattern("dd-MM-yyyy");

    String strInputDateGregField = info.getStringParameter("inpLastFieldChanged", null);
    String strInputDateGreg = info.getStringParameter(strInputDateGregField, null);

    DateTime dt = fmt.parseDateTime(strInputDateGreg);
    dtHijri = dt.withChronology(IslamicChronology.getInstance());

    String StrDateHijri = fmt.print(dtHijri);
    // String StrDateHijriField = getOutputField(strInputDateGregField);

    info.addResult("inpexpireDateHijri", StrDateHijri);

  }

  public String getOutputField(String changedFieldName) {
    String outputField = "inphijri";
    String firstChar = Character.toString(changedFieldName.charAt(3));
    outputField = outputField.concat((firstChar.toUpperCase() + changedFieldName.substring(4)));
    return outputField;
  }

}