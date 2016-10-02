package com.tasawr.ob.thr;

import javax.servlet.ServletException;

import org.joda.time.DateTime;
import org.joda.time.chrono.IslamicChronology;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Map;
import java.util.List;

import org.codehaus.jettison.json.JSONObject;
import org.openbravo.base.exception.OBException;
import org.openbravo.client.kernel.BaseActionHandler;
import org.openbravo.dal.service.OBDal;
import org.openbravo.dal.service.OBQuery;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.openbravo.base.provider.OBProvider;
import org.openbravo.dal.service.OBCriteria;


public class THRDateChangerHijriToGreg extends BaseActionHandler {

  protected JSONObject execute(Map<String, Object> parameters, String data) {
    try {

      // get the data as json
      final JSONObject jsonData = new JSONObject(data);
      final String inDate = jsonData.getString("inDate");
      String outDate;

   
      DateTime dtGreg;
      
      DateTimeFormatter fmt = DateTimeFormat.forPattern("dd-MM-yyyy");
      DateTime dt = fmt.parseDateTime(inDate);
      
     
      
     
      dtGreg = dt.withChronology(GregorianChronology.getInstance());
        outDate = fmt.print(dtGreg);
        
               
   
       
    // create the result
    JSONObject json = new JSONObject();
     json.put("outDate", outDate);
    

   // and return it
   return json;

} catch (Exception e) {
throw new OBException(e);
}
    
    
}

  
}

