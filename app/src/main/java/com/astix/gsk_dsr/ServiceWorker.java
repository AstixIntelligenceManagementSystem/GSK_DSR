package com.astix.gsk_dsr;


import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;














import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.widget.Toast;


public class ServiceWorker 
{
	private Context context;
	
	public String UrlForWebService=CommonInfo.WebServicePath.trim();
	String director;
	String movie_name;
	
	public static int flagExecutedServiceSuccesfully=0;
	public int timeout=0;
	
	public String currSysDate;
	public String SysDate;
	
	
	
	
	public ServiceWorker getAvailableAndUpdatedVersionOfAppNew(Context ctx,String uuid,int DatabaseVersionID,int ApplicationID,String ContactNo,String Password)
	{
		
		this.context = ctx;
		GskDBAdapter dbengine = new GskDBAdapter(context);
		
		//decimalFormat.applyPattern(pattern);
		
		int chkTblStoreListContainsRow=1;
		StringReader read;
		InputSource inputstream;
		final String SOAP_ACTION = "http://tempuri.org/GetIMEIVersionDetailStatusNew";
		final String METHOD_NAME = "GetIMEIVersionDetailStatusNew";
		final String NAMESPACE = "http://tempuri.org/";
		final String URL = UrlForWebService;
		
	    //Create request
		SoapObject table = null; //Contains table of dataset that returned
		// through SoapObject
		SoapObject client = null; //Its the client petition to the web service
		SoapObject tableRow = null; //Contains row of table
		SoapObject responseBody = null; //Contains XML content of dataset

		//SoapObject param
		HttpTransportSE transport = null; // That call webservice
		SoapSerializationEnvelope sse = null;
		sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);		
		sse.dotNet = true;
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,0);
		
		ServiceWorker setmovie = new ServiceWorker();
		try 
		{
			client = new SoapObject(NAMESPACE, METHOD_NAME);
			client.addProperty("uuid", uuid.toString());
			client.addProperty("DatabaseVersion", DatabaseVersionID);
			client.addProperty("ApplicationID", ApplicationID);//ContactNo
			client.addProperty("ContactNo", ContactNo);
			client.addProperty("Password", Password);
			sse.setOutputSoapObject(client);
			sse.bodyOut = client;
			androidHttpTransport.call(SOAP_ACTION, sse);

			
			responseBody = (SoapObject)sse.bodyIn;
			// This step: get file XML
			//responseBody = (SoapObject) sse.getResponse();
			int totalCount = responseBody.getPropertyCount();

			// System.out.println("Kajol 2 :"+totalCount);
	        String resultString=androidHttpTransport.responseDump;
			
	        String name=responseBody.getProperty(0).toString();
	        
	       // System.out.println("Kajol 3 :"+name);
	        
	        XMLParser xmlParser = new XMLParser();
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	            DocumentBuilder db = dbf.newDocumentBuilder();
	            InputSource is = new InputSource();
	            is.setCharacterStream(new StringReader(name));
	            Document doc = db.parse(is);
	            
	            
	            dbengine.droptblUserAuthenticationMstrTBL();
	            dbengine.createtblUserAuthenticationMstrTBL();
	            dbengine.dropAvailbUpdatedVersionTBL();
			    dbengine.createAvailbUpdatedVersionTBL();
			    
			    dbengine.deletetblUserLoginMstr();
			    
	            dbengine.open();
	          /*  "><NewDataSet> <tblUserAuthentication> <flgUserAuthenticated>1</flgUserAuthenticated> <PersonRegID>9</PersonRegID> " +
	            "<PersonApproved>1</PersonApproved> <PersonName>Abdul Satter</PersonName> </tblUserAuthentication>" +
	            " <tblAvailableVersion> <VersionID>1</VersionID> <VersionSerialNo>1.0</VersionSerialNo> " +
	            "<VersionDownloadStatus>0 </VersionDownloadStatus> <ServerDate>18-03-2016</ServerDate> </tblAvailableVersion> " +
	            "</NewDataSet></string>
*/	        
	            NodeList tblUserAuthenticationNode = doc.getElementsByTagName("tblUserAuthentication");
	            for (int i = 0; i < tblUserAuthenticationNode.getLength(); i++)
	            {
	            	String flgUserAuthenticated="0";
	            	String PersonRegID="0";
	            	String PersonApproved="0";
	            	String PersonName="0";
	            	
	            	
	                Element element = (Element) tblUserAuthenticationNode.item(i);

	                
	                NodeList flgUserAuthenticatedNode = element.getElementsByTagName("flgUserAuthenticated");
	                Element line = (Element) flgUserAuthenticatedNode.item(0);
	                flgUserAuthenticated=xmlParser.getCharacterDataFromElement(line);
	                
	             
	              
	                	
	                if(!element.getElementsByTagName("PersonRegID").equals(null))
	                 {
	                 NodeList PersonRegIDNode = element.getElementsByTagName("PersonRegID");
	                line = (Element) PersonRegIDNode.item(0);
		                if(PersonRegIDNode.getLength()>0)
		                {
		                PersonRegID=xmlParser.getCharacterDataFromElement(line);
		                }
	            	 }
	                
	                
	                NodeList PersonApprovedNode = element.getElementsByTagName("PersonApproved");
	                line = (Element) PersonApprovedNode.item(0);
	                PersonApproved=xmlParser.getCharacterDataFromElement(line);
	                
	                
	               
	                if(!element.getElementsByTagName("PersonName").equals(null))
	                {
	                NodeList PersonNameNode = element.getElementsByTagName("PersonName");
	                line = (Element) PersonNameNode.item(0);
	                if(PersonNameNode.getLength()>0)
	                {
	                PersonName=xmlParser.getCharacterDataFromElement(line);
	                }
	                }
	                LoginActivity.flgUserAlreadyExists=Integer.parseInt(flgUserAuthenticated);
	                
	                LoginActivity.flgTSIApproved=Integer.parseInt(PersonApproved);
	                
	                LoginActivity.PersonNodeID=Integer.parseInt(PersonRegID);
	                
					
	                dbengine.savetblUserAuthenticationMstr(flgUserAuthenticated);
	             }
	            
	            
	            
	          /*  <tblAvailableVersion> <VersionID>1</VersionID> <VersionSerialNo>1.0</VersionSerialNo> " +
	            "<VersionDownloadStatus>0 </VersionDownloadStatus> <ServerDate>18-03-2016</ServerDate> </tblAvailableVersion>
*/	            
	            NodeList tblAvailableVersionNode = doc.getElementsByTagName("tblAvailableVersion");
	            for (int i = 0; i < tblAvailableVersionNode.getLength(); i++)
	            {
	            	
	            	
	            	String VersionID = "0";
					String VersionSerialNo= "NA";
					String VersionDownloadStatus= "NA";
					Date pdaDate=new Date();
					SimpleDateFormat sdfPDaDate = new SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH);
					String fDatePda = sdfPDaDate.format(pdaDate).toString().trim();
					String ServerDate= fDatePda;
	            	
	                Element element = (Element) tblAvailableVersionNode.item(i);
	                NodeList SchemeIDNode = element.getElementsByTagName("VersionID");
	  	            Element line = (Element) SchemeIDNode.item(0);
	  	            VersionID=xmlParser.getCharacterDataFromElement(line);
  	               // System.out.println("Kajol tblSchemeMstr: VersionID" +VersionID );
  	                

	                NodeList SchemeNameNode = element.getElementsByTagName("VersionSerialNo");
	                line = (Element) SchemeNameNode.item(0);
	                VersionSerialNo=xmlParser.getCharacterDataFromElement(line);
	               // System.out.println("Kajol tblSchemeMstr: VersionSerialNo " +VersionSerialNo );
	                
	                
	                
	                NodeList SchemeApplicationIDNode = element.getElementsByTagName("VersionDownloadStatus");
	                line = (Element) SchemeApplicationIDNode.item(0);
	                VersionDownloadStatus=xmlParser.getCharacterDataFromElement(line);
	               // System.out.println("Kajol tblSchemeMstr: VersionDownloadStatus " +VersionDownloadStatus );
	                
	                NodeList SchemeAppliedRuleNode = element.getElementsByTagName("ServerDate");
	                line = (Element) SchemeAppliedRuleNode.item(0);
	                ServerDate=xmlParser.getCharacterDataFromElement(line);
	              //  System.out.println("Kajol tblSchemeMstr: ServerDate " +ServerDate );
	                
	                
	                dbengine.savetblAvailbUpdatedVersion(VersionID.trim(), VersionSerialNo.trim(),VersionDownloadStatus.trim(),ServerDate);
	             }
	            dbengine.close();
            setmovie.director = "1";
			return setmovie;

		} catch (Exception e) {
			
			//System.out.println("Aman Exception occur in GetIMEIVersionDetailStatusNew :"+e.toString());
			if(e.toString().contains("SocketTimeoutException") ||e.toString().contains("ConnectException")||e.toString().contains("SocketException"))
			{
				setmovie.director = "100";
			}
			else if(e.toString().contains("NullPointerException"))
			{
				setmovie.director = "200";
			}
			
			else
			{
				setmovie.director = e.toString();
			}
			
			setmovie.movie_name = e.toString();
			dbengine.close();
			
			return setmovie;
		}
	
		
		
		

	}

	
	
	

	public ServiceWorker fetchModuleData(Context ctx,String uuid,int loginId) 
	{

		
		this.context = ctx;
		GskDBAdapter dbengine = new GskDBAdapter(context);
		
		//decimalFormat.applyPattern(pattern);
		
		int chkTblStoreListContainsRow=1;
		StringReader read;
		InputSource inputstream;
		final String SOAP_ACTION = "http://tempuri.org/fnGetUserModuleCourseListForWorkShopPDA";
		final String METHOD_NAME = "fnGetUserModuleCourseListForWorkShopPDA";
		final String NAMESPACE = "http://tempuri.org/";
		final String URL = UrlForWebService;
		
	    //Create request
		SoapObject table = null; //Contains table of dataset that returned
		// through SoapObject
		SoapObject client = null; //Its the client petition to the web service
		SoapObject tableRow = null; //Contains row of table
		SoapObject responseBody = null; //Contains XML content of dataset

		//SoapObject param
		HttpTransportSE transport = null; // That call webservice
		SoapSerializationEnvelope sse = null;
		sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);		
		sse.dotNet = true;
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,0);
		
		ServiceWorker setmovie = new ServiceWorker();
		try 
		{
			client = new SoapObject(NAMESPACE, METHOD_NAME);
			client.addProperty("uuid", uuid.toString());
		
			client.addProperty("UserLoginID", loginId);
			
			sse.setOutputSoapObject(client);
			sse.bodyOut = client;
			androidHttpTransport.call(SOAP_ACTION, sse);

			
			responseBody = (SoapObject)sse.bodyIn;
			// This step: get file XML
			//responseBody = (SoapObject) sse.getResponse();
			int totalCount = responseBody.getPropertyCount();

			// System.out.println("Kajol 2 :"+totalCount);
	        String resultString=androidHttpTransport.responseDump;
			
	        String name=responseBody.getProperty(0).toString();
	        
	       // System.out.println("Kajol 3 :"+name);
	        
	        XMLParser xmlParser = new XMLParser();
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	            DocumentBuilder db = dbf.newDocumentBuilder();
	            InputSource is = new InputSource();
	            is.setCharacterStream(new StringReader(name));
	            Document doc = db.parse(is);
	            
	          //  dbengine.deleteModuleData();
	         
	            NodeList tblUserPasswordInfoNode = doc.getElementsByTagName("tblUserModuleGetCourseListForWorkShop");
	            dbengine.open();
	            for (int i = 0; i < tblUserPasswordInfoNode.getLength(); i++)
	            {
	            	
	            	String lPCourseGroupMainID="NA";
	            	String lPCourseGroupName="0";
	            	String lPCourseID="0";
	            	String lPCourseName="0";
	            	String lPCourseDescription="0";
	            	String moduleImgName="0";
	            	String moduleImgUrl="0";
	            	String status="0";
	            		
	            	String dayVal="0";
	            	String flgNewOld="0";
	            	
	            	
	            	
	            	String lpEnglishCoursePath="0";
	            	String lpHIndiCoursePath="0";
	            	String lpMalyalamCoursePath="0";
	            	String lpTamilCoursePath="0";
	            	String lpKannadaCoursePath="0";
	            	String lpMarathiCoursePath="0";
	            	String lpGujratiCoursePath="0";
	            	String lpBangaliCoursePath="0";
	            	String lpAssamiesCoursePath="0";
	            	String lpTeleguCoursePath="0";
	            	String lpOdiyaCoursePath="0";
	            	
	            	
	            	String PDAEnglishCoursePath="0";
	            	String PDAHIndiCoursePath="0";
	            	String PDAMalyalamCoursePath="0";
	            	String PDATamilCoursePath="0";
	            	String PDAKannadaCoursePath="0";
	            	String PDAMarathiCoursePath="0";
	            	String PDAGujratiCoursePath="0";
	            	String PDABangaliCoursePath="0";
	            	String PDAAssamiesCoursePath="0";
	            	String PDATeleguCoursePath="0";
	            	String PDAOdiyaCoursePath="0";
	            	String flgPDAOnline="0";
	            	String LPCourseCatID="0";
	            	
	            											
	          
	            	
	            	
	            	
	                Element element = (Element) tblUserPasswordInfoNode.item(i);

	                
	                NodeList ContactNoNode = element.getElementsByTagName("LPCourseGroupMainID");
	                Element line = (Element) ContactNoNode.item(0);
	                if(ContactNoNode.getLength()>0)
	                {
	                	lPCourseGroupMainID=xmlParser.getCharacterDataFromElement(line);
	                }
	               
	             
	              
	                	
	             
	                 NodeList LoginResultNode = element.getElementsByTagName("LPCourseGroupName");
	                line = (Element) LoginResultNode.item(0);
		                if(LoginResultNode.getLength()>0)
		                {
		                	lPCourseGroupName=xmlParser.getCharacterDataFromElement(line);
		                }
	            	
	                
	                
	                NodeList LoginIDNode = element.getElementsByTagName("LPCourseID");
	                line = (Element) LoginIDNode.item(0);
	                if(LoginIDNode.getLength()>0)
	                {
	                	lPCourseID=xmlParser.getCharacterDataFromElement(line);
	                }
	              
	                NodeList NodeIDNode = element.getElementsByTagName("LPCourseName");
	                line = (Element) NodeIDNode.item(0);
	                if(NodeIDNode.getLength()>0)
	                {
	                	lPCourseName=xmlParser.getCharacterDataFromElement(line);
	                }
	                
	                NodeList NodeTypeNode = element.getElementsByTagName("LPCourseDescription");
	                line = (Element) NodeIDNode.item(0);
	                if(NodeTypeNode.getLength()>0)
	                {
	                	lPCourseDescription=xmlParser.getCharacterDataFromElement(line);
	                }
	             
	                NodeList UserFullNameNode = element.getElementsByTagName("ModuleImgUrl");
	                line = (Element) UserFullNameNode.item(0);
	                if(UserFullNameNode.getLength()>0)
	                {
	                	moduleImgUrl=xmlParser.getCharacterDataFromElement(line);
	                	String[] moduleImgNameArray=moduleImgUrl.split(Pattern.quote("/"));
	                	int lastIndex=moduleImgNameArray.length;
	                	moduleImgName=moduleImgNameArray[lastIndex-1];
	                	
	                }
	            
	                NodeList RegionIDNode = element.getElementsByTagName("Status");
	                line = (Element) RegionIDNode.item(0);
	                if(RegionIDNode.getLength()>0)
	                {
	                	status=xmlParser.getCharacterDataFromElement(line);
	                }
	                
	                NodeList RoleForSurveyNode = element.getElementsByTagName("DayVal");
	                line = (Element) RoleForSurveyNode.item(0);
	                if(RoleForSurveyNode.getLength()>0)
	                {
	                	dayVal=xmlParser.getCharacterDataFromElement(line);
	                }
	                
	                NodeList EmailIDNode = element.getElementsByTagName("flgNewOld");
	                line = (Element) EmailIDNode.item(0);
	                if(EmailIDNode.getLength()>0)
	                {
	                	flgNewOld=xmlParser.getCharacterDataFromElement(line);
	                }
	              
	                
	                NodeList lpCoursePathNode = element.getElementsByTagName("PDAEnglishCoursePath");
	                line = (Element) lpCoursePathNode.item(0);
	                if(lpCoursePathNode.getLength()>0)
	                {
	                	lpEnglishCoursePath=xmlParser.getCharacterDataFromElement(line);
	                	String[] moduleImgNameArray=lpEnglishCoursePath.split(Pattern.quote("/"));
	                	int lastIndex=moduleImgNameArray.length;
	                	PDAEnglishCoursePath=moduleImgNameArray[lastIndex-1];
	                }
	                NodeList PDAHIndiCoursePathNode = element.getElementsByTagName("PDAHIndiCoursePath");
	                line = (Element) PDAHIndiCoursePathNode.item(0);
	                if(PDAHIndiCoursePathNode.getLength()>0)
	                {
	                	lpHIndiCoursePath=xmlParser.getCharacterDataFromElement(line);
	                	String[] moduleImgNameArray=lpHIndiCoursePath.split(Pattern.quote("/"));
	                	int lastIndex=moduleImgNameArray.length;
	                	PDAHIndiCoursePath=moduleImgNameArray[lastIndex-1];
	                }
	                
	                NodeList PDAMalyalamCoursePathNode = element.getElementsByTagName("PDAMalyalamCoursePath");
	                line = (Element) PDAMalyalamCoursePathNode.item(0);
	                if(PDAMalyalamCoursePathNode.getLength()>0)
	                {
	                	lpMalyalamCoursePath=xmlParser.getCharacterDataFromElement(line);
	                	String[] moduleImgNameArray=lpMalyalamCoursePath.split(Pattern.quote("/"));
	                	int lastIndex=moduleImgNameArray.length;
	                	PDAMalyalamCoursePath=moduleImgNameArray[lastIndex-1];
	                }
	                NodeList PDATamilCoursePathNode = element.getElementsByTagName("PDATamilCoursePath");
	                line = (Element) PDATamilCoursePathNode.item(0);
	                if(PDATamilCoursePathNode.getLength()>0)
	                {
	                	lpTamilCoursePath=xmlParser.getCharacterDataFromElement(line);
	                	String[] moduleImgNameArray=lpTamilCoursePath.split(Pattern.quote("/"));
	                	int lastIndex=moduleImgNameArray.length;
	                	PDATamilCoursePath=moduleImgNameArray[lastIndex-1];
	                }
	              
	                NodeList PDAKannadaCoursePathNode = element.getElementsByTagName("PDAKannadaCoursePath");
	                line = (Element) PDAKannadaCoursePathNode.item(0);
	                if(PDAKannadaCoursePathNode.getLength()>0)
	                {
	                	lpKannadaCoursePath=xmlParser.getCharacterDataFromElement(line);
	                	String[] moduleImgNameArray=lpKannadaCoursePath.split(Pattern.quote("/"));
	                	int lastIndex=moduleImgNameArray.length;
	                	PDAKannadaCoursePath=moduleImgNameArray[lastIndex-1];
	                }
	                
	                NodeList PDAMarathiCoursePathNode = element.getElementsByTagName("PDAMarathiCoursePath");
	                line = (Element) PDAMarathiCoursePathNode.item(0);
	                if(PDAMarathiCoursePathNode.getLength()>0)
	                {
	                	lpMarathiCoursePath=xmlParser.getCharacterDataFromElement(line);
	                	String[] moduleImgNameArray=lpMarathiCoursePath.split(Pattern.quote("/"));
	                	int lastIndex=moduleImgNameArray.length;
	                	PDAMarathiCoursePath=moduleImgNameArray[lastIndex-1];
	                }
	            	
	                NodeList PDAGujratiCoursePathNode = element.getElementsByTagName("PDAGujratiCoursePath");
	                line = (Element) PDAGujratiCoursePathNode.item(0);
	                if(PDAGujratiCoursePathNode.getLength()>0)
	                {
	                	lpGujratiCoursePath=xmlParser.getCharacterDataFromElement(line);
	                	String[] moduleImgNameArray=lpGujratiCoursePath.split(Pattern.quote("/"));
	                	int lastIndex=moduleImgNameArray.length;
	                	PDAGujratiCoursePath=moduleImgNameArray[lastIndex-1];
	                }
	                NodeList PDABangaliCoursePathNode = element.getElementsByTagName("PDABangaliCoursePath");
	                line = (Element) PDABangaliCoursePathNode.item(0);
	                if(PDABangaliCoursePathNode.getLength()>0)
	                {
	                	lpBangaliCoursePath=xmlParser.getCharacterDataFromElement(line);
	                	String[] moduleImgNameArray=lpBangaliCoursePath.split(Pattern.quote("/"));
	                	int lastIndex=moduleImgNameArray.length;
	                	PDABangaliCoursePath=moduleImgNameArray[lastIndex-1];
	                }
	            	
	                NodeList PDAAssamiesCoursePathNode = element.getElementsByTagName("PDAAssamiesCoursePath");
	                line = (Element) PDAAssamiesCoursePathNode.item(0);
	                if(PDAAssamiesCoursePathNode.getLength()>0)
	                {
	                	lpAssamiesCoursePath=xmlParser.getCharacterDataFromElement(line);
	                	String[] moduleImgNameArray=lpAssamiesCoursePath.split(Pattern.quote("/"));
	                	int lastIndex=moduleImgNameArray.length;
	                	PDAAssamiesCoursePath=moduleImgNameArray[lastIndex-1];
	                }
	                
	                NodeList PDATeleguCoursePathNode = element.getElementsByTagName("PDATeleguCoursePath");
	                line = (Element) PDATeleguCoursePathNode.item(0);
	                if(PDATeleguCoursePathNode.getLength()>0)
	                {
	                	lpTeleguCoursePath=xmlParser.getCharacterDataFromElement(line);
	                	String[] moduleImgNameArray=lpTeleguCoursePath.split(Pattern.quote("/"));
	                	int lastIndex=moduleImgNameArray.length;
	                	PDATeleguCoursePath=moduleImgNameArray[lastIndex-1];
	                }
	            	
	                NodeList PDAOdiyaCoursePathNode = element.getElementsByTagName("PDAOdiyaCoursePath");
	                line = (Element) PDAOdiyaCoursePathNode.item(0);
	                if(PDAOdiyaCoursePathNode.getLength()>0)
	                {
	                	lpOdiyaCoursePath=xmlParser.getCharacterDataFromElement(line);
	                	String[] moduleImgNameArray=lpOdiyaCoursePath.split(Pattern.quote("/"));
	                	int lastIndex=moduleImgNameArray.length;
	                	PDAOdiyaCoursePath=moduleImgNameArray[lastIndex-1];
	                }
	            	
	            	
	                NodeList flgPDAOnlineNode = element.getElementsByTagName("flgPDAOnline");
	                line = (Element) flgPDAOnlineNode.item(0);
	                if(flgPDAOnlineNode.getLength()>0)
	                {
	                	flgPDAOnline=xmlParser.getCharacterDataFromElement(line);
	                }
	            	
	                NodeList LPCourseCatIDNode = element.getElementsByTagName("LPCourseCatID");
	                line = (Element) LPCourseCatIDNode.item(0);
	                if(LPCourseCatIDNode.getLength()>0)
	                {
	                	LPCourseCatID=xmlParser.getCharacterDataFromElement(line);
	                }
	            	
	              //LPCourseCatID
	               
	                dbengine.savetblUserModuleMstr( lPCourseGroupMainID, lPCourseGroupName,lPCourseID,
	                		lPCourseName, lPCourseDescription, moduleImgName, moduleImgUrl, status, 
	                		dayVal, flgNewOld,lpEnglishCoursePath,lpHIndiCoursePath,lpMalyalamCoursePath,lpTamilCoursePath,lpKannadaCoursePath,lpMarathiCoursePath,lpGujratiCoursePath,lpBangaliCoursePath,lpAssamiesCoursePath,lpTeleguCoursePath,lpOdiyaCoursePath,PDAEnglishCoursePath,PDAHIndiCoursePath,
	                		PDAMalyalamCoursePath,PDATamilCoursePath,PDAKannadaCoursePath,PDAMarathiCoursePath,
	                		PDAGujratiCoursePath,PDABangaliCoursePath,PDAAssamiesCoursePath,PDATeleguCoursePath,PDAOdiyaCoursePath,flgPDAOnline,LPCourseCatID);
	               
	             }
	        //    tblModuleWiseSlideDetails(LPCourceMainID text null, LPCourseName text null, SlideNo int null,SlideName text null
	           
	            NodeList tblModuleWiseSlideDetailsSlide = doc.getElementsByTagName("tblModuleWiseSlideDetails");
	          
	            for (int i = 0; i < tblModuleWiseSlideDetailsSlide.getLength(); i++)
	            {
	            	
	            	String lpCourceMainID="NA";
	            	
	            	
	            	String lPCourseName="0";
	            	
	            	String slideNo="0";
	            	String slideName="0";
	            	String flgLastSlide="0";
	          
	            	String LanguageID="0";
	            	
	            	
	                Element element = (Element) tblModuleWiseSlideDetailsSlide.item(i);

	                
	                NodeList ContactNoNode = element.getElementsByTagName("LPCourceMainID");
	                Element line = (Element) ContactNoNode.item(0);
	                if(ContactNoNode.getLength()>0)
	                {
	                	lpCourceMainID=xmlParser.getCharacterDataFromElement(line);
	                }
	               
	             
	              
	                	
	             
	                 NodeList LoginResultNode = element.getElementsByTagName("LPCourseName");
	                line = (Element) LoginResultNode.item(0);
		                if(LoginResultNode.getLength()>0)
		                {
		                	lPCourseName=xmlParser.getCharacterDataFromElement(line);
		                }
	            	
	                
	                
	                NodeList LoginIDNode = element.getElementsByTagName("SqNo");
	                line = (Element) LoginIDNode.item(0);
	                if(LoginIDNode.getLength()>0)
	                {
	                	slideNo=xmlParser.getCharacterDataFromElement(line);
	                }
	              
	                NodeList NodeIDNode = element.getElementsByTagName("SlideName");
	                line = (Element) NodeIDNode.item(0);
	                if(NodeIDNode.getLength()>0)
	                {
	                	slideName=xmlParser.getCharacterDataFromElement(line);
	                }
	                
	                NodeList FlgLastSlideNode = element.getElementsByTagName("flgLastSlide");
	                line = (Element) FlgLastSlideNode.item(0);
	                if(FlgLastSlideNode.getLength()>0)
	                {
	                	
	                	flgLastSlide=xmlParser.getCharacterDataFromElement(line);
	                }
	                NodeList LanguageIDNode = element.getElementsByTagName("LanguageID");
	                line = (Element) LanguageIDNode.item(0);
	                if(LanguageIDNode.getLength()>0)
	                {
	                	
	                	LanguageID=xmlParser.getCharacterDataFromElement(line);
	                }
	               
	                
	                dbengine.savetblModuleWiseSlideDetails( lpCourceMainID, lPCourseName, slideNo, slideName,flgLastSlide,LanguageID);
	               
	             }
	            
	            
	            NodeList tblModuleLanguageMstrDetails = doc.getElementsByTagName("tblModuleLanguageMstr");
	            dbengine.deletetblModuleLanguageMstr();
	            for (int i = 0; i < tblModuleLanguageMstrDetails.getLength(); i++)
	            {
	            	
	            	String LPCourseID="0";
	            	String LanguageID="0";
	            	String RegionID="0";
	            	String flgPDAOnline="0";
	            	
	                Element element = (Element) tblModuleLanguageMstrDetails.item(i);

	                
	                NodeList ContactNoNode = element.getElementsByTagName("LPCourseID");
	                Element line = (Element) ContactNoNode.item(0);
	                if(ContactNoNode.getLength()>0)
	                {
	                	LPCourseID=xmlParser.getCharacterDataFromElement(line);
	                }
	               
	               NodeList LoginResultNode = element.getElementsByTagName("LanguageID");
	                line = (Element) LoginResultNode.item(0);
		                if(LoginResultNode.getLength()>0)
		                {
		                	LanguageID=xmlParser.getCharacterDataFromElement(line);
		                }
	            	
	                 NodeList LoginIDNode = element.getElementsByTagName("RegionID");
	                line = (Element) LoginIDNode.item(0);
	                if(LoginIDNode.getLength()>0)
	                {
	                	RegionID=xmlParser.getCharacterDataFromElement(line);
	                }
	              
	                NodeList flgPDAOnlineNode = element.getElementsByTagName("flgPDAOnline");
	                line = (Element) flgPDAOnlineNode.item(0);
	                if(flgPDAOnlineNode.getLength()>0)
	                {
	                	flgPDAOnline=xmlParser.getCharacterDataFromElement(line);
	                }
	                dbengine.savetblModuleLanguageMstr( LPCourseID, LanguageID, RegionID,flgPDAOnline);
	               
	             }
	            
	            
	            NodeList tblLanguageMstrDetails = doc.getElementsByTagName("tblLanguageMstr");
		          
	            for (int i = 0; i < tblLanguageMstrDetails.getLength(); i++)
	            {
	            	
	            	String LanguageID="0";
	            	String Language="0";
	            	
	            	
	            	
	                Element element = (Element) tblLanguageMstrDetails.item(i);

	                
	                NodeList ContactNoNode = element.getElementsByTagName("LanguageID");
	                Element line = (Element) ContactNoNode.item(0);
	                if(ContactNoNode.getLength()>0)
	                {
	                	LanguageID=xmlParser.getCharacterDataFromElement(line);
	                }
	               
	               NodeList LoginResultNode = element.getElementsByTagName("Language");
	                line = (Element) LoginResultNode.item(0);
		                if(LoginResultNode.getLength()>0)
		                {
		                	Language=xmlParser.getCharacterDataFromElement(line);
		                }
	            	
	                
	               
	                dbengine.savetblLanguageMstr(LanguageID, Language);
	               
	             }
	            
	            dbengine.close();
	            
	            
	          
	            
            setmovie.director = "1";
			return setmovie;

		} catch (Exception e) {
			
			//System.out.println("Aman Exception occur in GetIMEIVersionDetailStatusNew :"+e.toString());
			if(e.toString().contains("SocketTimeoutException") ||e.toString().contains("ConnectException")||e.toString().contains("SocketException"))
			{
				setmovie.director = "100";
			}
			else if(e.toString().contains("NullPointerException"))
			{
				setmovie.director = "200";
			}
			
			else
			{
				setmovie.director = e.toString();
			}
			
			setmovie.movie_name = e.toString();
			dbengine.close();
			
			return setmovie;
		}
	
		
		
		

	
	}
	
	public ServiceWorker getAuthenticate(Context ctx,String uuid,String userName,String password,String PhoneModel,String AndroidVersion)
	{
		
		this.context = ctx;
		GskDBAdapter dbengine = new GskDBAdapter(context);
		
		//decimalFormat.applyPattern(pattern);
		
		int chkTblStoreListContainsRow=1;
		StringReader read;
		InputSource inputstream;
		//
		final String SOAP_ACTION = "http://tempuri.org/fnGSKMatchLoginCredentials";
		final String METHOD_NAME = "fnGSKMatchLoginCredentials";
		final String NAMESPACE = "http://tempuri.org/";
		final String URL = UrlForWebService;
		
	    //Create request
		SoapObject table = null; //Contains table of dataset that returned
		// through SoapObject
		SoapObject client = null; //Its the client petition to the web service
		SoapObject tableRow = null; //Contains row of table
		SoapObject responseBody = null; //Contains XML content of dataset

		//SoapObject param
		HttpTransportSE transport = null; // That call webservice
		SoapSerializationEnvelope sse = null;
		sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);		
		sse.dotNet = true;
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,0);
		
		ServiceWorker setmovie = new ServiceWorker();
		try 
		{
			int VersionID1=CommonInfo.DATABASE_VERSION;
	        int ApplicationType=CommonInfo.Application_TypeID;
	        
	      //  uuid="357327070587767";   alok sir phone imei
	      //  uuid="357642050131398";
	        
	      //  uuid="355490069871410";     //Sunil phone imei ,alok sir need a demo apk with already set Username and Password
			
	       // uuid="357642050131398";

			uuid="315971071180381"; //by avinash sir
	        
			client = new SoapObject(NAMESPACE, METHOD_NAME);
			client.addProperty("uuid", uuid.toString());
		   client.addProperty("UserLoginName", userName);
			client.addProperty("UserLoginPassword", password);
			client.addProperty("VersionID", VersionID1);
			client.addProperty("ApplicationType", ApplicationType);
			client.addProperty("PhoneModel", PhoneModel);
			client.addProperty("AndroidVersion", AndroidVersion);
			
			
			
			sse.setOutputSoapObject(client);
			sse.bodyOut = client;
			androidHttpTransport.call(SOAP_ACTION, sse);

			
			responseBody = (SoapObject)sse.bodyIn;
			// This step: get file XML
			//responseBody = (SoapObject) sse.getResponse();
			int totalCount = responseBody.getPropertyCount();

			// System.out.println("Kajol 2 :"+totalCount);
	        String resultString=androidHttpTransport.responseDump;
			
	        String name=responseBody.getProperty(0).toString();
	        
	       // System.out.println("Kajol 3 :"+name);
	        
	        XMLParser xmlParser = new XMLParser();
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	            DocumentBuilder db = dbf.newDocumentBuilder();
	            InputSource is = new InputSource();
	            is.setCharacterStream(new StringReader(name));
	            Document doc = db.parse(is);
	            
	            
	            dbengine.dropAvailbUpdatedVersionTBL();
			    dbengine.createAvailbUpdatedVersionTBL();
	            
	            dbengine.deletetblUserLoginMstr();
	           
	   
	            NodeList tblUserPasswordInfoNode = doc.getElementsByTagName("tblUserLoginCredentials");
	            dbengine.open();
	            for (int i = 0; i < tblUserPasswordInfoNode.getLength(); i++)
	            {
	            	String userId="NA";
	            	String loginResult="0";
	            	String loginID="0";
	            	String nodeID="0";
	            	String nodeType="0";
	            	String roleId="0";
	            	String userFullName="0";
	            	String regionID="0";
	            	String roleForSurvey="0";
	            	String emailID="0";
	            	String flgIndMangerAvail="0";
	            	String mobNo="0";
	            	String flgUserValidPhoneReg="0";
	            	
	            	
	            	
	                Element element = (Element) tblUserPasswordInfoNode.item(i);

	                
	               
	                
	                NodeList ContactNoNode = element.getElementsByTagName("UserId");
	                Element line = (Element) ContactNoNode.item(0);
	                if(ContactNoNode.getLength()>0)
	                {
	                	userId=xmlParser.getCharacterDataFromElement(line);
	                }
	                
	                if(userId.equals("0"))
	                {
	                	CommonInfo.userId=0;
	                	  setmovie.director = "0";
	                	  dbengine.close();
	                	  return setmovie;
	                }
	                
	                NodeList flgUserValidPhoneRegNode = element.getElementsByTagName("flgUserValidPhoneReg");
	                line = (Element) flgUserValidPhoneRegNode.item(0);
	                if(flgUserValidPhoneRegNode.getLength()>0)
	                {
	                	flgUserValidPhoneReg=xmlParser.getCharacterDataFromElement(line);
	                }
	                
	                if(flgUserValidPhoneReg.equals("0"))
	                {
	                	
	                	  setmovie.director = "10";
	                	  dbengine.close();
	                	  return setmovie;
	                }
	             
	              
	                	
	             
	                 NodeList LoginResultNode = element.getElementsByTagName("LoginResult");
	                line = (Element) LoginResultNode.item(0);
		                if(LoginResultNode.getLength()>0)
		                {
		                	loginResult=xmlParser.getCharacterDataFromElement(line);
		                }
	            	
	                
	                
	                NodeList LoginIDNode = element.getElementsByTagName("LoginID");
	                line = (Element) LoginIDNode.item(0);
	                if(LoginIDNode.getLength()>0)
	                {
	                	  loginID=xmlParser.getCharacterDataFromElement(line);
	                }
	              
	                NodeList NodeIDNode = element.getElementsByTagName("NodeID");
	                line = (Element) NodeIDNode.item(0);
	                if(NodeIDNode.getLength()>0)
	                {
	                	nodeID=xmlParser.getCharacterDataFromElement(line);
	                }
	                
	                NodeList NodeTypeNode = element.getElementsByTagName("NodeType");
	                line = (Element) NodeIDNode.item(0);
	                if(NodeTypeNode.getLength()>0)
	                {
	                	nodeType=xmlParser.getCharacterDataFromElement(line);
	                }
	                NodeList RoleIdNode = element.getElementsByTagName("RoleId");
	                line = (Element) NodeIDNode.item(0);
	                if(RoleIdNode.getLength()>0)
	                {
	                	roleId=xmlParser.getCharacterDataFromElement(line);
	                }
	                
	                NodeList UserFullNameNode = element.getElementsByTagName("UserFullName");
	                line = (Element) UserFullNameNode.item(0);
	                if(UserFullNameNode.getLength()>0)
	                {
	                	userFullName=xmlParser.getCharacterDataFromElement(line);
	                }
	                
	                NodeList RegionIDNode = element.getElementsByTagName("RegionID");
	                line = (Element) RegionIDNode.item(0);
	                if(RegionIDNode.getLength()>0)
	                {
	                	regionID=xmlParser.getCharacterDataFromElement(line);
	                }
	                
	                NodeList RoleForSurveyNode = element.getElementsByTagName("RoleForSurvey");
	                line = (Element) RoleForSurveyNode.item(0);
	                if(RoleForSurveyNode.getLength()>0)
	                {
	                	roleForSurvey=xmlParser.getCharacterDataFromElement(line);
	                }
	                
	                NodeList EmailIDNode = element.getElementsByTagName("EmailID");
	                line = (Element) EmailIDNode.item(0);
	                if(EmailIDNode.getLength()>0)
	                {
	                	emailID=xmlParser.getCharacterDataFromElement(line);
	                }
	              
	                NodeList flgIndMangerAvailNode = element.getElementsByTagName("flgIndMangerAvail");
	                line = (Element) flgIndMangerAvailNode.item(0);
	                if(flgIndMangerAvailNode.getLength()>0)
	                {
	                	flgIndMangerAvail=xmlParser.getCharacterDataFromElement(line);
	                }
	                
	                NodeList MobNoNode = element.getElementsByTagName("MobNo");
	                line = (Element)MobNoNode.item(0);
	                if(MobNoNode.getLength()>0)
	                {
	                	mobNo=xmlParser.getCharacterDataFromElement(line);
	                }
	               
	                CommonInfo.userId=Integer.parseInt(userId);
	                CommonInfo.loginId=Integer.parseInt(loginID);
	                
	                dbengine.savetblUserLoginMstr( userId, loginID, nodeID, nodeType, roleId, userFullName, regionID, roleForSurvey, emailID, flgIndMangerAvail, mobNo);
	               
	             }
	            
	            
	            
	            NodeList tblSchemeMstrNode = doc.getElementsByTagName("tblAvailableVersion");
	            for (int i = 0; i < tblSchemeMstrNode.getLength(); i++)
	            {
	            	//VersionID	VersionSerialNo	VersionDownloadStatus	ServerDate
	            	
	            	String VersionID = "0";
					String VersionSerialNo= "NA";
					String VersionDownloadStatus= "NA";
					Date pdaDate=new Date();
					SimpleDateFormat sdfPDaDate = new SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH);
					String fDatePda = sdfPDaDate.format(pdaDate).toString().trim();
					String ServerDate= fDatePda;
	            	
	                Element element = (Element) tblSchemeMstrNode.item(i);
	                NodeList SchemeIDNode = element.getElementsByTagName("VersionID");
	  	            Element line = (Element) SchemeIDNode.item(0);
	  	            VersionID=xmlParser.getCharacterDataFromElement(line);
  	                // System.out.println("Kajol tblSchemeMstr: VersionID" +VersionID );
  	                

	                NodeList SchemeNameNode = element.getElementsByTagName("VersionSerialNo");
	                line = (Element) SchemeNameNode.item(0);
	                VersionSerialNo=xmlParser.getCharacterDataFromElement(line);
	                // System.out.println("Kajol tblSchemeMstr: VersionSerialNo " +VersionSerialNo );
	                
	                
	                
	                NodeList SchemeApplicationIDNode = element.getElementsByTagName("VersionDownloadStatus");
	                line = (Element) SchemeApplicationIDNode.item(0);
	                VersionDownloadStatus=xmlParser.getCharacterDataFromElement(line);
	                // System.out.println("Kajol tblSchemeMstr: VersionDownloadStatus " +VersionDownloadStatus );
	                
	                NodeList SchemeAppliedRuleNode = element.getElementsByTagName("ServerDate");
	                line = (Element) SchemeAppliedRuleNode.item(0);
	                ServerDate=xmlParser.getCharacterDataFromElement(line);
	                // System.out.println("Kajol tblSchemeMstr: ServerDate " +ServerDate );
	                
	                
	                dbengine.savetblAvailbUpdatedVersion(VersionID.trim(), VersionSerialNo.trim(),VersionDownloadStatus.trim(),ServerDate);
					
	                
	                
					
					
	                
	             }
	            dbengine.close();
	            
	            
	          
	            dbengine.close();
            setmovie.director = "1";
			return setmovie;

		} catch (Exception e) {
			
			//System.out.println("Aman Exception occur in GetIMEIVersionDetailStatusNew :"+e.toString());
			if(e.toString().contains("SocketTimeoutException") ||e.toString().contains("ConnectException")||e.toString().contains("SocketException"))
			{
				setmovie.director = "100";
			}
			else if(e.toString().contains("NullPointerException"))
			{
				setmovie.director = "200";
			}
			
			else
			{
				setmovie.director = e.toString();
			}
			
			setmovie.movie_name = e.toString();
			dbengine.close();
			
			return setmovie;
		}
	
		
		
		

	}
	
	
	
	
	public ServiceWorker sendPasswordToRegisteredUser(Context ctx,String uuid,int DatabaseVersionID,int ApplicationID,String UserLoginName)
	{
		
		this.context = ctx;
		GskDBAdapter dbengine = new GskDBAdapter(context);
		
		//decimalFormat.applyPattern(pattern);
		
		int chkTblStoreListContainsRow=1;
		StringReader read;
		InputSource inputstream;
		final String SOAP_ACTION = "http://tempuri.org/fnGSKRetrivePassword";
		final String METHOD_NAME = "fnGSKRetrivePassword";
		final String NAMESPACE = "http://tempuri.org/";
		final String URL = UrlForWebService;
		
	    //Create request
		SoapObject table = null; //Contains table of dataset that returned
		// through SoapObject
		SoapObject client = null; //Its the client petition to the web service
		SoapObject tableRow = null; //Contains row of table
		SoapObject responseBody = null; //Contains XML content of dataset

		//SoapObject param
		HttpTransportSE transport = null; // That call webservice
		SoapSerializationEnvelope sse = null;
		sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);		
		sse.dotNet = true;
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,0);
		
		ServiceWorker setmovie = new ServiceWorker();
		try 
		{
			client = new SoapObject(NAMESPACE, METHOD_NAME);
			client.addProperty("uuid", uuid.toString());
			//client.addProperty("DatabaseVersion", DatabaseVersionID);
			//client.addProperty("ApplicationType", ApplicationID);//ContactNo
			client.addProperty("UserLoginName", UserLoginName);
			
			sse.setOutputSoapObject(client);
			sse.bodyOut = client;
			androidHttpTransport.call(SOAP_ACTION, sse);

			
			responseBody = (SoapObject)sse.bodyIn;
			// This step: get file XML
			//responseBody = (SoapObject) sse.getResponse();
			int totalCount = responseBody.getPropertyCount();

			
	        String resultString=androidHttpTransport.responseDump;
			
	        String name=responseBody.getProperty(0).toString();
	        
	      
	        
	        XMLParser xmlParser = new XMLParser();
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	            DocumentBuilder db = dbf.newDocumentBuilder();
	            InputSource is = new InputSource();
	            is.setCharacterStream(new StringReader(name));
	            Document doc = db.parse(is);
	            
	            
	         
	           
	            dbengine.open();
	            
	          //  <tblRetrivePassWord> <Password>asm00</Password> </tblRetrivePassWord>
	          
	            NodeList tblUserPasswordInfoNode = doc.getElementsByTagName("tblRetrivePassWord");
	            for (int i = 0; i < tblUserPasswordInfoNode.getLength(); i++)
	            {
	            	String Password="NA";
	            	
	            	
	            	
	                Element element = (Element) tblUserPasswordInfoNode.item(i);

	                
	               
	                	
	                if(!element.getElementsByTagName("Password").equals(null))
	                 {
	                 NodeList PasswordNode = element.getElementsByTagName("Password");
	                 Element line = (Element) PasswordNode.item(0);
		                if(PasswordNode.getLength()>0)
		                {
		                	Password=xmlParser.getCharacterDataFromElement(line);
		                }
	            	 }
	                
	                
	                NodeList msgNode = element.getElementsByTagName("msg");
	               
	                LoginActivity.msg=Password;
	               
	                
	             
	               
	             }
	            
	            
	            
	          
	            dbengine.close();
            setmovie.director = "1";
			return setmovie;

		} catch (Exception e) {
			
			//System.out.println("Aman Exception occur in GetIMEIVersionDetailStatusNew :"+e.toString());
			if(e.toString().contains("SocketTimeoutException") ||e.toString().contains("ConnectException")||e.toString().contains("SocketException"))
			{
				setmovie.director = "100";
			}
			else if(e.toString().contains("NullPointerException"))
			{
				setmovie.director = "200";
			}
			
			else
			{
				setmovie.director = e.toString();
			}
			
			setmovie.movie_name = e.toString();
			dbengine.close();
			
			return setmovie;
		}
	
		
		
		

	}
	
	
	public ServiceWorker fetchUserModuleQuestionAndAnswer(Context ctx,String uuid,int UserID) 
	{

		
		this.context = ctx;
		GskDBAdapter dbengine = new GskDBAdapter(context);
		
		//decimalFormat.applyPattern(pattern);
		
		int chkTblStoreListContainsRow=1;
		StringReader read;
		InputSource inputstream;
		final String SOAP_ACTION = "http://tempuri.org/fnGetUserModuleQuestionAndAnswer";
		final String METHOD_NAME = "fnGetUserModuleQuestionAndAnswer";
		final String NAMESPACE = "http://tempuri.org/";
		final String URL = UrlForWebService;
		
	    //Create request
		SoapObject table = null; //Contains table of dataset that returned
		// through SoapObject
		SoapObject client = null; //Its the client petition to the web service
		SoapObject tableRow = null; //Contains row of table
		SoapObject responseBody = null; //Contains XML content of dataset

		//SoapObject param
		HttpTransportSE transport = null; // That call webservice
		SoapSerializationEnvelope sse = null;
		sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);		
		sse.dotNet = true;
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,0);
		
		ServiceWorker setmovie = new ServiceWorker();
		try 
		{
			client = new SoapObject(NAMESPACE, METHOD_NAME);
			
			client.addProperty("uuid", uuid.toString());
		
			client.addProperty("UserID", UserID);
			
			sse.setOutputSoapObject(client);
			sse.bodyOut = client;
			androidHttpTransport.call(SOAP_ACTION, sse);

			
			responseBody = (SoapObject)sse.bodyIn;
			// This step: get file XML
			//responseBody = (SoapObject) sse.getResponse();
			int totalCount = responseBody.getPropertyCount();

			// System.out.println("Kajol 2 :"+totalCount);
	        String resultString=androidHttpTransport.responseDump;
			
	        String name=responseBody.getProperty(0).toString();
	        
	       // System.out.println("Kajol 3 :"+name);
	        
	        XMLParser xmlParser = new XMLParser();
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	            DocumentBuilder db = dbf.newDocumentBuilder();
	            InputSource is = new InputSource();
	            is.setCharacterStream(new StringReader(name));
	            Document doc = db.parse(is);
	            
	            dbengine.deleteUserModuleQuestionAndAnswer();
	            
	         /*   
	            private static final String DATABASE_CREATE_TABLE_11 = "create table tblModuleQuestionAnswerOne (TestID text null," +
	            		"TestInstanceID text null,TestInsUserMapID text null,CourseMainID text null,TotalQstn text null,PassingQstn" +
	            		" text null,AllocatedtimeInSecPerQstn text null,Descr text null,RSPID text null,PGNmbr text null," +
	            		"AssessmentStatus text null,TimeElapsedInSec text null,QstNo text null,SubmitType text null," +
	            		"NoCorrectAns text null,flgPassStatus text null,StartTime text null,EndTime text null);";
	         */
	            NodeList tblModuleQuestionAnswerOneNode = doc.getElementsByTagName("tblModuleQuestionAnswerOne");
	            dbengine.open();
	            for (int i = 0; i < tblModuleQuestionAnswerOneNode.getLength(); i++)
	            {
	            	
	            	int TestID=0;
	            	int TestInstanceID=0;
	            	int TestInsUserMapID=0;
	            	int CourseMainID=0;
	            	int TotalQstn=0;
	            	int PassingQstn=0;
	            	int AllocatedtimeInSecPerQstn=0;
	            	String Descr="0";
	            	int RSPID=0;
	            	int PGNmbr=0;
	            	
	            	
	            	int AssessmentStatus=0;
	            	int TimeElapsedInSec=0;
	            	int QstNo=0;
	            	int SubmitType=0;
	            	int NoCorrectAns=0;
	            	int flgPassStatus=0;
	            	String StartTime="0";
	            	String EndTime="0";
	            	int flgAssessmentAvailable=0;
	          
	            	
	            	
	            	
	                Element element = (Element) tblModuleQuestionAnswerOneNode.item(i);

	                
	                NodeList TestIDNode = element.getElementsByTagName("TestID");
	                Element line = (Element) TestIDNode.item(0);
	                if(TestIDNode.getLength()>0)
	                {
	                	TestID=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
	                }
	               
	                NodeList TestInstanceIDNode = element.getElementsByTagName("TestInstanceID");
	                line = (Element) TestInstanceIDNode.item(0);
		                if(TestInstanceIDNode.getLength()>0)
		                {
		                	TestInstanceID=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
		                }
	            	
	                NodeList TestInsUserMapIDNode = element.getElementsByTagName("TestInsUserMapID");
	                line = (Element) TestInsUserMapIDNode.item(0);
	                if(TestInsUserMapIDNode.getLength()>0)
	                {
	                	TestInsUserMapID=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
	                }
	              
	                NodeList CourseMainIDNode = element.getElementsByTagName("CourseMainID");
	                line = (Element) CourseMainIDNode.item(0);
	                if(CourseMainIDNode.getLength()>0)
	                {
	                	CourseMainID=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
	                }
	                
	                NodeList TotalQstnNode = element.getElementsByTagName("TotalQstn");
	                line = (Element) TotalQstnNode.item(0);
	                if(TotalQstnNode.getLength()>0)
	                {
	                	TotalQstn=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
	                }
	             
	                NodeList PassingQstnNode = element.getElementsByTagName("PassingQstn");
	                line = (Element) PassingQstnNode.item(0);
	                if(PassingQstnNode.getLength()>0)
	                {
	                	PassingQstn=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
	                	
	                }
	            
	                NodeList AllocatedtimeInSecPerQstnNode = element.getElementsByTagName("AllocatedtimeInSecPerQstn");
	                line = (Element) AllocatedtimeInSecPerQstnNode.item(0);
	                if(AllocatedtimeInSecPerQstnNode.getLength()>0)
	                {
	                	AllocatedtimeInSecPerQstn=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
	                }
	                
	                NodeList DescrNode = element.getElementsByTagName("Descr");
	                line = (Element) DescrNode.item(0);
	                if(DescrNode.getLength()>0)
	                {
	                	Descr=xmlParser.getCharacterDataFromElement(line);
	                }
	                
	                NodeList RSPIDNode = element.getElementsByTagName("RSPID");
	                line = (Element) RSPIDNode.item(0);
	                if(RSPIDNode.getLength()>0)
	                {
	                	RSPID=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
	                }
	              
	                
	                NodeList PGNmbrNode = element.getElementsByTagName("PGNmbr");
	                line = (Element) PGNmbrNode.item(0);
	                if(PGNmbrNode.getLength()>0)
	                {
	                	PGNmbr=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
	                	
	                }
	                
	                NodeList AssessmentStatusNode = element.getElementsByTagName("AssessmentStatus");
	                line = (Element) AssessmentStatusNode.item(0);
	                if(AssessmentStatusNode.getLength()>0)
	                {
	                	AssessmentStatus=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
	                	
	                }
	                
	                NodeList TimeElapsedInSecNode = element.getElementsByTagName("TimeElapsedInSec");
	                line = (Element) TimeElapsedInSecNode.item(0);
	                if(TimeElapsedInSecNode.getLength()>0)
	                {
	                	TimeElapsedInSec=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
	                }
	                
	                NodeList QstNoNode = element.getElementsByTagName("QstNo");
	                line = (Element) QstNoNode.item(0);
	                if(QstNoNode.getLength()>0)
	                {
	                	QstNo=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
	                }
	                
	                NodeList SubmitTypeNode = element.getElementsByTagName("SubmitType");
	                line = (Element) SubmitTypeNode.item(0);
	                if(SubmitTypeNode.getLength()>0)
	                {
	                	SubmitType=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
	                }
	                
	              
	                NodeList NoCorrectAnsNode = element.getElementsByTagName("NoCorrectAns");
	                line = (Element) NoCorrectAnsNode.item(0);
	                if(NoCorrectAnsNode.getLength()>0)
	                {
	                	NoCorrectAns=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
	                }
	                
	                NodeList flgPassStatusNode = element.getElementsByTagName("flgPassStatus");
	                line = (Element) flgPassStatusNode.item(0);
	                if(flgPassStatusNode.getLength()>0)
	                {
	                	flgPassStatus=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
	                }
	                
	                NodeList StartTimeNode = element.getElementsByTagName("StartTime");
	                line = (Element) StartTimeNode.item(0);
	                if(StartTimeNode.getLength()>0)
	                {
	                	StartTime=xmlParser.getCharacterDataFromElement(line);
	                }
	                
	                NodeList EndTimeNode = element.getElementsByTagName("EndTime");
	                line = (Element) EndTimeNode.item(0);
	                if(EndTimeNode.getLength()>0)
	                {
	                	EndTime=xmlParser.getCharacterDataFromElement(line);
	                }
	                
	                NodeList flgAssessmentAvailableNode = element.getElementsByTagName("flgAssessmentAvailable");
	                line = (Element) flgAssessmentAvailableNode.item(0);
	                if(flgAssessmentAvailableNode.getLength()>0)
	                {
	                	flgAssessmentAvailable=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
	                }
		            
	             
	                
	                
	              
	                dbengine.savetblModuleQuestionAnswerOne(TestID,TestInstanceID,TestInsUserMapID,CourseMainID,
	    					TotalQstn,PassingQstn,AllocatedtimeInSecPerQstn,Descr,RSPID,PGNmbr,
	    					AssessmentStatus,TimeElapsedInSec,QstNo,SubmitType,NoCorrectAns,
	    					flgPassStatus,StartTime,EndTime,flgAssessmentAvailable);
	               
	               
	             }
	           
	            
	           /* private static final String DATABASE_CREATE_TABLE_12 = "create table tblModuleQuestionAnswerTwo (TestID text null," +
	            		"QstID text null,QstpartTxt text null,SRNmbr text null,QstnTypeID text null);";//, AutoIdOutlet int null
	        	*/
	            
	            NodeList tblModuleQuestionAnswerTwoNode = doc.getElementsByTagName("tblModuleQuestionAnswerTwo");
	          
	            for (int i = 0; i < tblModuleQuestionAnswerTwoNode.getLength(); i++)
	            {
	            	
	            	int TestID=0;
	            	int QstID=0;
	            	String QstpartTxt="0";
	            	int SRNmbr=0;
	            	int QstnTypeID=0;
	          
	            	
	            	
	            	
	                Element element = (Element) tblModuleQuestionAnswerTwoNode.item(i);

	                
	                NodeList TestIDNode = element.getElementsByTagName("TestID");
	                Element line = (Element) TestIDNode.item(0);
	                if(TestIDNode.getLength()>0)
	                {
	                	TestID=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
	                }
	               
	             
	                NodeList QstIDNode = element.getElementsByTagName("QstID");
	                line = (Element) QstIDNode.item(0);
		                if(QstIDNode.getLength()>0)
		                {
		                	QstID=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
		                }
	            	
	                
	                
	                NodeList QstpartTxtNode = element.getElementsByTagName("QstpartTxt");
	                line = (Element) QstpartTxtNode.item(0);
	                if(QstpartTxtNode.getLength()>0)
	                {
	                	QstpartTxt=xmlParser.getCharacterDataFromElement(line);
	                }
	              
	                NodeList SRNmbrNode = element.getElementsByTagName("SRNmbr");
	                line = (Element) SRNmbrNode.item(0);
	                if(SRNmbrNode.getLength()>0)
	                {
	                	SRNmbr=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
	                }
	                
	                NodeList QstnTypeIDNode = element.getElementsByTagName("QstnTypeID");
	                line = (Element) QstnTypeIDNode.item(0);
	                if(QstnTypeIDNode.getLength()>0)
	                {
	                	
	                	QstnTypeID=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
	                }
	                
	               
	                
	                dbengine.savetblModuleQuestionAnswerTwo(TestID,QstID,QstpartTxt,SRNmbr,QstnTypeID);
	               
	             }
	            
	            
	           /* private static final String DATABASE_CREATE_TABLE_13 = "create table tblModuleQuestionAnswerThree (TestID text null," +
	            		"TestInsUserMapID text null,QstID text null,QstnStatID text null,StatementDescr text null,CorrectAns text" +
	            		" null,OptionSeq text null,RspDetID text null,RspID text null,RsltQstnStatID text null," +
	            		"flgCorrectAns text null);";*/
	            
	            NodeList tblModuleQuestionAnswerThreeNode = doc.getElementsByTagName("tblModuleQuestionAnswerThree");
		          
	            for (int i = 0; i < tblModuleQuestionAnswerThreeNode.getLength(); i++)
	            {
	            	
	            	int TestID=0;
	            	int TestInsUserMapID=0;
	            	int QstID=0;
	            	int QstnStatID=0;
	            	String StatementDescr="0";
	            	int CorrectAns=0;
	            	
	            	int OptionSeq=0;
	            	int RspDetID=0;
	            	int RspID=0;
	            	int RsltQstnStatID=0;
	            	int flgCorrectAns=0;
	          
	            	
	            	
	            	
	                Element element = (Element) tblModuleQuestionAnswerThreeNode.item(i);

	                
	                NodeList TestIDNode = element.getElementsByTagName("TestID");
	                Element line = (Element) TestIDNode.item(0);
	                if(TestIDNode.getLength()>0)
	                {
	                	TestID=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
	                }
	               
	                NodeList TestInsUserMapIDNode = element.getElementsByTagName("TestInsUserMapID");
	                line = (Element) TestInsUserMapIDNode.item(0);
		                if(TestInsUserMapIDNode.getLength()>0)
		                {
		                	TestInsUserMapID=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
		                }
	            	
	             
	                NodeList QstIDNode = element.getElementsByTagName("QstID");
	                line = (Element) QstIDNode.item(0);
		                if(QstIDNode.getLength()>0)
		                {
		                	QstID=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
		                }
	            	
	                
	                
	                NodeList QstnStatIDNode = element.getElementsByTagName("QstnStatID");
	                line = (Element) QstnStatIDNode.item(0);
	                if(QstnStatIDNode.getLength()>0)
	                {
	                	QstnStatID=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
	                }
	              
	                NodeList StatementDescrNode = element.getElementsByTagName("StatementDescr");
	                line = (Element) StatementDescrNode.item(0);
	                if(StatementDescrNode.getLength()>0)
	                {
	                	StatementDescr=xmlParser.getCharacterDataFromElement(line);
	                }
	                
	                NodeList CorrectAnsNode = element.getElementsByTagName("CorrectAns");
	                line = (Element) CorrectAnsNode.item(0);
	                if(CorrectAnsNode.getLength()>0)
	                {
	                	CorrectAns=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
	                }
	                
	                NodeList OptionSeqNode = element.getElementsByTagName("OptionSeq");
	                line = (Element) OptionSeqNode.item(0);
	                if(OptionSeqNode.getLength()>0)
	                {
	                	OptionSeq=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
	                }
	                
	                NodeList RspDetIDNode = element.getElementsByTagName("RspDetID");
	                line = (Element) RspDetIDNode.item(0);
	                if(RspDetIDNode.getLength()>0)
	                {
	                	RspDetID=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
	                }
	                NodeList RspIDNode = element.getElementsByTagName("RspID");
	                line = (Element) RspIDNode.item(0);
	                if(RspIDNode.getLength()>0)
	                {
	                	RspID=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
	                }
	                
	                NodeList RsltQstnStatIDNode = element.getElementsByTagName("RsltQstnStatID");
	                line = (Element) RsltQstnStatIDNode.item(0);
	                if(RsltQstnStatIDNode.getLength()>0)
	                {
	                	RsltQstnStatID=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
	                }
	                
	                NodeList flgCorrectAnsNode = element.getElementsByTagName("flgCorrectAns");
	                line = (Element) flgCorrectAnsNode.item(0);
	                if(flgCorrectAnsNode.getLength()>0)
	                {
	                	flgCorrectAns=Integer.parseInt(xmlParser.getCharacterDataFromElement(line));
	                }
	               
	          
	                dbengine.savetblModuleQuestionAnswerThree(TestID,TestInsUserMapID,QstID,QstnStatID,
	    					StatementDescr,CorrectAns,OptionSeq,RspDetID,RspID,RsltQstnStatID,
	    					flgCorrectAns);
	               
	             }
	            
	            
	            dbengine.close();
	            
	            
	          
	            
            setmovie.director = "1";
			return setmovie;

		} catch (Exception e) {
			
			//System.out.println("Aman Exception occur in GetIMEIVersionDetailStatusNew :"+e.toString());
			if(e.toString().contains("SocketTimeoutException") ||e.toString().contains("ConnectException")||e.toString().contains("SocketException"))
			{
				setmovie.director = "100";
			}
			else if(e.toString().contains("NullPointerException"))
			{
				setmovie.director = "200";
			}
			
			else
			{
				setmovie.director = e.toString();
			}
			
			setmovie.movie_name = e.toString();
			dbengine.close();
			
			return setmovie;
		}
	
		
		
		

	
	}
	
	public ServiceWorker fnCallAllQuestionWebservice(Context ctx) 
	{
		this.context = ctx;
		GskDBAdapter dbengine = new GskDBAdapter(context);
		
		int chkTblStoreListContainsRow=1;
		StringReader read;
		InputSource inputstream;
		final String SOAP_ACTION = "http://tempuri.org/fnGetDistributorAssesment";
		final String METHOD_NAME = "fnGetDistributorAssesment";
		final String NAMESPACE = "http://tempuri.org/";
		final String URL = UrlForWebService;
		
	    //Create request
		SoapObject table = null; //Contains table of dataset that returned
		// through SoapObject
		SoapObject client = null; //Its the client petition to the web service
		SoapObject tableRow = null; //Contains row of table
		SoapObject responseBody = null; //Contains XML content of dataset

		//SoapObject param
		HttpTransportSE transport = null; // That call webservice
		SoapSerializationEnvelope sse = null;
		sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);		
		sse.dotNet = true;
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,0);
		
		ServiceWorker setmovie = new ServiceWorker();
		try {
			client = new SoapObject(NAMESPACE, METHOD_NAME);
			/*client.addProperty("ApplicationID", ApplicationID);
			client.addProperty("uuid", uuid.toString());
			*/
			sse.setOutputSoapObject(client);
			sse.bodyOut = client;
			androidHttpTransport.call(SOAP_ACTION, sse);
			responseBody = (SoapObject)sse.bodyIn;
			// This step: get file XML
			//responseBody = (SoapObject) sse.getResponse();
			int totalCount = responseBody.getPropertyCount();

			// System.out.println("Kajol 2 :"+totalCount);
	        String resultString=androidHttpTransport.responseDump;
			
	        String name=responseBody.getProperty(0).toString();
	        
	       // System.out.println("Kajol 3 :"+name);
	        
	        XMLParser xmlParser = new XMLParser();
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	            DocumentBuilder db = dbf.newDocumentBuilder();
	            InputSource is = new InputSource();
	            is.setCharacterStream(new StringReader(name));
	            Document doc = db.parse(is);
	            
	           
	            
	          dbengine.deleteAllQuestionTable();
	           dbengine.open();
	            
	            NodeList tblSPGetDistributorDetailsNode = doc.getElementsByTagName("tblDistributorQstnGrpMstr");
	            for (int i = 0; i < tblSPGetDistributorDetailsNode.getLength(); i++)
	            {
	            	String QstnGrpID="0";
	            	String QstnGrpDescr="0";
	            	
	            	
	                Element element = (Element) tblSPGetDistributorDetailsNode.item(i);
	                
	                

	                
	               /* NodeList NodeIDNode = element.getElementsByTagName("QuestID");
	                Element line = (Element) NodeIDNode.item(0);
	                QuestID=xmlParser.getCharacterDataFromElement(line);*/
	                
	                if(!element.getElementsByTagName("QstnGrpID").equals(null))
	                 {
	                 NodeList QuestIDNode = element.getElementsByTagName("QstnGrpID");
	                 Element     line = (Element) QuestIDNode.item(0);
		                if(QuestIDNode.getLength()>0)
		                {
		                	QstnGrpID=xmlParser.getCharacterDataFromElement(line);
		                }
	            	 }
	              /*  
	                NodeList DescrNode = element.getElementsByTagName("QuestCode");
	                line = (Element) DescrNode.item(0);
	                QuestCode=xmlParser.getCharacterDataFromElement(line);
	                */
	                  if(!element.getElementsByTagName("QstnGrpDescr").equals(null))
	                 {
	                 NodeList QuestCodeNode = element.getElementsByTagName("QstnGrpDescr");
	                 Element     line = (Element) QuestCodeNode.item(0);
		                if(QuestCodeNode.getLength()>0)
		                {
		                	QstnGrpDescr=xmlParser.getCharacterDataFromElement(line);
		                }
	            	 }
	                
	             
	                
	               dbengine.savetblDistributorQstnGrpMstr(QstnGrpID, QstnGrpDescr);
	               
	               // System.out.println("Shiv tblDistributorQstnGrpMstr"+"QstnGrpID :"+QstnGrpID+"QstnGrpDescr:"+QstnGrpDescr );
	                
	            }
	            
	            NodeList tblGetPDAQuestOptionMstrNode = doc.getElementsByTagName("tblDistributorQstnMstr");
	            for (int i = 0; i < tblGetPDAQuestOptionMstrNode.getLength(); i++)
	            {
	            	
	            	String QstnID="0";
	            	String QstnTxtname ="0";
	            	String QstnDescr="0";
	            	String Weightage="0";
	            	String QstnGrpID ="0";
	            	
	            	
	            	
	            	
	            	
	            	
	            	  Element element = (Element) tblGetPDAQuestOptionMstrNode.item(i);
	            	 
	                if(!element.getElementsByTagName("QstnID").equals(null))
	                 {
	                 NodeList OptIDNode = element.getElementsByTagName("QstnID");
	                 Element      line = (Element) OptIDNode.item(0);
		                if(OptIDNode.getLength()>0)
		                {
		                	QstnID=xmlParser.getCharacterDataFromElement(line);
		                }
	            	 }
	                
	                if(!element.getElementsByTagName("StnOfEx").equals(null))
	                  {
	                  NodeList QuestIDNode = element.getElementsByTagName("StnOfEx");
	                  Element      line = (Element) QuestIDNode.item(0);
	                  if(QuestIDNode.getLength()>0)
	                  {
	                   QstnTxtname=xmlParser.getCharacterDataFromElement(line);
	                  }
	               }
	                
	                if(!element.getElementsByTagName("QstnDescr").equals(null))
	                 {
	                 NodeList OptionNoDNode = element.getElementsByTagName("QstnDescr");
	                 Element      line = (Element) OptionNoDNode.item(0);
		                if(OptionNoDNode.getLength()>0)
		                {
		                	QstnDescr=xmlParser.getCharacterDataFromElement(line);
		                }
	            	 }
	                
	                if(!element.getElementsByTagName("Weightage").equals(null))
	                 {
	                 NodeList OptionDescrNode = element.getElementsByTagName("Weightage");
	                 Element      line = (Element) OptionDescrNode.item(0);
		                if(OptionDescrNode.getLength()>0)
		                {
		                	Weightage=xmlParser.getCharacterDataFromElement(line);
		                }
	            	 }
	                
	                if(!element.getElementsByTagName("QstnGrpID").equals(null))
	                 {
	                 NodeList SequenceNode = element.getElementsByTagName("QstnGrpID");
	                 Element      line = (Element) SequenceNode.item(0);
		                if(SequenceNode.getLength()>0)
		                {
		                	
		                	QstnGrpID=xmlParser.getCharacterDataFromElement(line);
		                	
		                }
	            	 }
	                dbengine.savetblDistributorQstnMstr(QstnID, QstnTxtname, QstnDescr, Weightage, QstnGrpID);
	            	//System.out.println("Shiv tblDistributorQstnMstr"+"QstnID:"+QstnID+"QstnTxtname:"+QstnTxtname+"QstnDescr:"+QstnDescr+"Weightage:"+Weightage  +"QstnGrpID:"+QstnGrpID);
	            	
	             }
	            

	            NodeList tblGetPDAQuestionDependentMstrNode = doc.getElementsByTagName("tblDistributorQstnOptionMapping");
	                        for (int i = 0; i < tblGetPDAQuestionDependentMstrNode.getLength(); i++)
	                        {
	                         
	                         
	                         String QstnId="0";
	                         String OptionId="0";
	                         String Score ="0";
	                         String OptionDesc="0";
	                         
	                         
	                         
	                         
	                         
	                         
	                           Element element = (Element) tblGetPDAQuestionDependentMstrNode.item(i);
	                          
	                           
	                            
	                            if(!element.getElementsByTagName("QstnId").equals(null))
	                             {
	                             NodeList QuestionIDNode = element.getElementsByTagName("QstnId");
	                             Element      line = (Element) QuestionIDNode.item(0);
	                             if(QuestionIDNode.getLength()>0)
	                             {
	                            	 QstnId=xmlParser.getCharacterDataFromElement(line);
	                             }
	                          }
	                            
	                            if(!element.getElementsByTagName("OptionId").equals(null))
	                             {
	                             NodeList OptionIDNode = element.getElementsByTagName("OptionId");
	                             Element      line = (Element) OptionIDNode.item(0);
	                             if(OptionIDNode.getLength()>0)
	                             {
	                            	 OptionId=xmlParser.getCharacterDataFromElement(line);
	                             }
	                          }
	                            
	                            if(!element.getElementsByTagName("OptionDescr").equals(null))
	                             {
	                             NodeList OptionDescNode = element.getElementsByTagName("OptionDescr");
	                             Element      line = (Element) OptionDescNode.item(0);
	                             if(OptionDescNode.getLength()>0)
	                             {
	                            	 OptionDesc=xmlParser.getCharacterDataFromElement(line);
	                             }
	                          }
	                            
	                            if(!element.getElementsByTagName("Score").equals(null))
	                             {
	                             NodeList DependentQuestionIDNode = element.getElementsByTagName("Score");
	                             Element      line = (Element) DependentQuestionIDNode.item(0);
	                             if(DependentQuestionIDNode.getLength()>0)
	                             {
	                            	 Score=xmlParser.getCharacterDataFromElement(line);
	                             }
	                          }
	                          // dbengine.savetblQuestionDependentMstr(QuestionID, OptionID, DependentQuestionID);
	                           
	                        //  dbengine.savetblOptionMstr(OptID, QuestID, OptionNo, OptionDescr, Sequence);
	                            dbengine.savetblDistributer_Question_Option_Mstr(QstnId, OptionId,OptionDesc, Score);
	                         //System.out.println("Shiv tblDistributorQstnOptionMapping"+"QstnId:"+QstnId+"OptionId:"+OptionId+"Score:"+Score);
	                        
	                         }
	                        NodeList tblGetPDAtblDistributorProfileMstrNode = doc.getElementsByTagName("tblDistributorProfileMstr");
	                        for (int i = 0; i < tblGetPDAtblDistributorProfileMstrNode.getLength(); i++)
	                        {
	                         
	                         
	                         String ProfileId="0";
	                         String Descr="0";
	                         String Profile="0";
	                         String MinScore="0";
	                         String MaxScore="0";
	                         String RecommendedAction="0";
	                         String Spirit="0";
	                         
	                         Element element = (Element) tblGetPDAtblDistributorProfileMstrNode.item(i);
	                          
	                           
	                            
	                            if(!element.getElementsByTagName("ProfileId").equals(null))
	                             {
	                             NodeList ProfileIdNode = element.getElementsByTagName("ProfileId");
	                             Element      line = (Element) ProfileIdNode.item(0);
	                             if(ProfileIdNode.getLength()>0)
	                             {
	                            	 ProfileId=xmlParser.getCharacterDataFromElement(line);
	                             }
	                          }
	                            
	                            if(!element.getElementsByTagName("Descr").equals(null))
	                             {
	                             NodeList DescrNode = element.getElementsByTagName("Descr");
	                             Element      line = (Element) DescrNode.item(0);
	                             if(DescrNode.getLength()>0)
	                             {
	                            	 Descr=xmlParser.getCharacterDataFromElement(line);
	                             }
	                          }
	                            
	                            if(!element.getElementsByTagName("Profile").equals(null))
	                             {
	                             NodeList ProfileNode = element.getElementsByTagName("Profile");
	                             Element      line = (Element) ProfileNode.item(0);
	                             if(ProfileNode.getLength()>0)
	                             {
	                            	 Profile=xmlParser.getCharacterDataFromElement(line);
	                             }
	                          }
	                            if(!element.getElementsByTagName("MinScore").equals(null))
	                             {
	                             NodeList MinScoreNode = element.getElementsByTagName("MinScore");
	                             Element      line = (Element) MinScoreNode.item(0);
	                             if(MinScoreNode.getLength()>0)
	                             {
	                            	 MinScore=xmlParser.getCharacterDataFromElement(line);
	                             }
	                          }
	                            
	                            if(!element.getElementsByTagName("MaxScore").equals(null))
	                             {
	                             NodeList MaxScoreNode = element.getElementsByTagName("MaxScore");
	                             Element      line = (Element) MaxScoreNode.item(0);
	                             if(MaxScoreNode.getLength()>0)
	                             {
	                            	 MaxScore=xmlParser.getCharacterDataFromElement(line);
	                             }
	                          }
	                            
	                            if(!element.getElementsByTagName("RecommendedAction").equals(null))
	                             {
	                             NodeList RecommendedActionNode = element.getElementsByTagName("RecommendedAction");
	                             Element      line = (Element) RecommendedActionNode.item(0);
	                             if(RecommendedActionNode.getLength()>0)
	                             {
	                            	 RecommendedAction=xmlParser.getCharacterDataFromElement(line);
	                             }
	                          }
	                            
	                            if(!element.getElementsByTagName("Spirit").equals(null))
	                             {
	                             NodeList SpiritNode = element.getElementsByTagName("Spirit");
	                             Element      line = (Element) SpiritNode.item(0);
	                             if(SpiritNode.getLength()>0)
	                             {
	                            	 Spirit=xmlParser.getCharacterDataFromElement(line);
	                             }
	                          }
	                           //dbengine.savetblQuestionDependentMstr(QuestionID, OptionID, DependentQuestionID);
	                           
	                        //  dbengine.savetblOptionMstr(OptID, QuestID, OptionNo, OptionDescr, Sequence);
	                            dbengine.savetblDistributorProfileMstr(ProfileId, Descr, Profile, MinScore, MaxScore, RecommendedAction, Spirit);
	                        // System.out.println("Shiv tblDistributorProfileMstr"+"ProfileId:"+ProfileId+"Descr:"+Descr+"Profile :"+Profile+"MinScore:"+MinScore+"MaxScore:"+MaxScore+"RecommendedAction :"+RecommendedAction+"Spirit :"+Spirit);
	                        
	                         }
	            
	            
            setmovie.director = "1";
           
			return setmovie;

		} catch (Exception e) 
		{
			 dbengine.close();
			System.out.println("Aman Exception occur in fnSingleCallAllWebService :"+e.toString());
			setmovie.director = e.toString();
			setmovie.movie_name = e.toString();
			
			
			return setmovie;
		}
	}
	
	public ServiceWorker getGSKModuleStatusOnline(Context ctx,String UserId,String LoginId,String ModuleId)
	{

		this.context = ctx;
		
		
		int chkTblStoreListContainsRow=1;
		StringReader read;
		InputSource inputstream;
		final String SOAP_ACTION = "http://tempuri.org/fnGSKModuleStatusOnline";
		final String METHOD_NAME = "fnGSKModuleStatusOnline";
		final String NAMESPACE = "http://tempuri.org/";
		final String URL = UrlForWebService;
		
	    //Create request
		SoapObject table = null; //Contains table of dataset that returned
		// through SoapObject
		SoapObject client = null; //Its the client petition to the web service
		SoapObject tableRow = null; //Contains row of table
		SoapObject responseBody = null; //Contains XML content of dataset

		GskDBAdapter dbengine = new GskDBAdapter(context);
		//SoapObject param
		HttpTransportSE transport = null; // That call webservice
		SoapSerializationEnvelope sse = null;
		sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);		
		sse.dotNet = true;
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,0);
		
		ServiceWorker setmovie = new ServiceWorker();
		try {
			client = new SoapObject(NAMESPACE, METHOD_NAME);
			
			client.addProperty("UserId", UserId);
			client.addProperty("LoginId", LoginId);
			client.addProperty("ModuleId", ModuleId);
		
			sse.setOutputSoapObject(client);
			sse.bodyOut = client;
			androidHttpTransport.call(SOAP_ACTION, sse);
			responseBody = (SoapObject)sse.bodyIn;
			// This step: get file XML
			//responseBody = (SoapObject) sse.getResponse();
			int totalCount = responseBody.getPropertyCount();

			// System.out.println("Kajol 2 :"+totalCount);
	        String resultString=androidHttpTransport.responseDump;
			
	        String name=responseBody.getProperty(0).toString();
	        
	       // System.out.println("Kajol 3 :"+name);
	        
	        XMLParser xmlParser = new XMLParser();
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	            DocumentBuilder db = dbf.newDocumentBuilder();
	            InputSource is = new InputSource();
	            is.setCharacterStream(new StringReader(name));
	            Document doc = db.parse(is);
	            
	           
	            
	            
	           String LPStatus="0";
	           String LPEndDate="0";

	            NodeList tblGetValidateUserLogin = doc.getElementsByTagName("tblSpGetModuleStatus");
	            {
	                       Element element = (Element) tblGetValidateUserLogin.item(0);
	                        
	                       if(!element.getElementsByTagName("LPStatus").equals(null))
	                             {
		                             NodeList QuestionIDNode = element.getElementsByTagName("LPStatus");
		                             Element      line = (Element) QuestionIDNode.item(0);
		                             if(QuestionIDNode.getLength()>0)
		                             {
		                            	 LPStatus=xmlParser.getCharacterDataFromElement(line);
		                             }
	                             }
	                       
	                       if(!element.getElementsByTagName("LPEndDate").equals(null))
                           {
	                             NodeList QuestionIDNode = element.getElementsByTagName("LPEndDate");
	                             Element      line = (Element) QuestionIDNode.item(0);
	                             if(QuestionIDNode.getLength()>0)
	                             {
	                            	 LPEndDate=xmlParser.getCharacterDataFromElement(line);
	                             }
                           }
	                            
	                     
	                       AssesmentModuleActivity.ModulesStatus=LPStatus;
	                       AssesmentModuleActivity.LPEndDate=LPEndDate;
	                       
	                       
	                       dbengine.savetblCheckOnlineModuleStatus(UserId,LoginId,ModuleId,LPStatus.trim(),LPEndDate);
	            }
	            String  RSPID="0";
	            String Status="0";
	            String flgPassStatus="0";
	            String flgTestMap="0";
	            NodeList tblSpGetAssesmentStatus = doc.getElementsByTagName("tblSpGetAssesmentStatus");
	            {
	                       Element element = (Element) tblSpGetAssesmentStatus.item(0);
	                        
	                       if(!element.getElementsByTagName("RSPID").equals(null))
	                             {
		                             NodeList RSPIDNode = element.getElementsByTagName("RSPID");
		                             Element      line = (Element) RSPIDNode.item(0);
		                             if(RSPIDNode.getLength()>0)
		                             {
		                            	 RSPID=xmlParser.getCharacterDataFromElement(line);
		                             }
	                             }
	                       if(!element.getElementsByTagName("Status").equals(null))
                           {
	                             NodeList RSPIDNode = element.getElementsByTagName("Status");
	                             Element      line = (Element) RSPIDNode.item(0);
	                             if(RSPIDNode.getLength()>0)
	                             {
	                            	 Status=xmlParser.getCharacterDataFromElement(line);
	                             }
                           }
	                       if(!element.getElementsByTagName("flgPassStatus").equals(null))
                           {
	                             NodeList RSPIDNode = element.getElementsByTagName("flgPassStatus");
	                             Element      line = (Element) RSPIDNode.item(0);
	                             if(RSPIDNode.getLength()>0)
	                             {
	                            	 flgPassStatus=xmlParser.getCharacterDataFromElement(line);
	                             }
                           }
	                       if(!element.getElementsByTagName("flgTestMap").equals(null))
                           {
	                             NodeList RSPIDNode = element.getElementsByTagName("flgTestMap");
	                             Element      line = (Element) RSPIDNode.item(0);
	                             if(RSPIDNode.getLength()>0)
	                             {
	                            	 flgTestMap=xmlParser.getCharacterDataFromElement(line);
	                             }
                           }
	                            
	                     
	                       dbengine.savetblCheckOnlineAssessmentStatus(UserId,LoginId,ModuleId,RSPID,Status,flgPassStatus,flgTestMap);
	            }
	                      
	                        	setmovie.director = "1";	
	                      
	            
           
           
			return setmovie;

		} catch (Exception e) 
		{
			
			 if(e.toString().contains("SocketTimeoutException") ||e.toString().contains("ConnectException")||e.toString().contains("SocketException"))
				{
					setmovie.director = "100";
				}
				else if(e.toString().contains("NullPointerException"))
				{
					setmovie.director = "200";
				}
				
				else
				{
					setmovie.director = e.toString();
				}
			
			
			return setmovie;
		}
	
	}

}
