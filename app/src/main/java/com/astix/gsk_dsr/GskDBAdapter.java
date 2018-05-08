package com.astix.gsk_dsr;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

public class GskDBAdapter 
{
	
	 public static String DATABASE_NAME = CommonInfo.DATABASE_NAME;
	 
	 static int DATABASE_VERSION = CommonInfo.DATABASE_VERSION;
	 static String AppVersionID = CommonInfo.AppVersionID.trim();
	
	 static int Application_TypeID = CommonInfo.Application_TypeID; //1=Parag Store Mapping,2=Parag SFA Indirect,3=Parag SFA Direct
	
	 private static final String DATABASE_TABLE_StartEndTime = "tblStartEndTime";
	 private static final String DATABASE_CREATE_TableStartEndTime = "create table tblStartEndTime(moduleID text null,StartTime text null,LpEndDate text null,LanguageID text null);";
	 
	 private static final String DATABASE_TABLE_MAIN1_Version = "tblAvailableVersionMstr";
	 private static final String DATABASE_TABLE_MAIN2_UserAuth = "tblUserAuthenticationMstr";
	
	 
	 private static final String DATABASE_TABLE_MAIN3_ChainMstr = "tblChainMstr";
	 private static final String DATABASE_TABLE_MAIN4_StoreMstr = "tblStoreList";
	 
	 private static final String DATABASE_TABLE_MAIN5_DisplayMstr = "tblDisplayMstr";
	 
	
	 
	 
	 private static final String TABLE_UserLoginMstr = "tblUserLoginMstr";
	 private static final String TABLE_ModuleWiseSlideDetails ="tblModuleWiseSlideDetails";
	 private static final String TABLE_UserModuleMstr = "tblUserModuleMstr";
	 private static final String TABLE_SmallIconMstr = "tblSmallIconMstr";
	 private static final String TABLE_ModuleContent = "tblModuleContent";
	 private static final String DATABASE_TABLE_Main6 = "tblStoreDisplayIDPhotoDetail";
	 
	 private static final String DATABASE_TABLE_MODULE_SAVED = "tblModuleSaved";
	 
	 private static final String DATABASE_TABLE_Main11 = "tblModuleQuestionAnswerOne";
	 private static final String DATABASE_TABLE_Main12 = "tblModuleQuestionAnswerTwo";
	 private static final String DATABASE_TABLE_Main13 = "tblModuleQuestionAnswerThree";
	 
	 
	 private static final String DATABASE_TABLE_Main14 = "tblTestsAnswerForModule";
	 
	 private static final String TABLE_CheckOnlineModuleStatus = "tblCheckOnlineModuleStatus";
	 private static final String DATABASE_CREATE_TABLE_CheckOnlineModuleStatus = "create table tblCheckOnlineModuleStatus(UserId text null, LoginID text null, ModuleID text null, Status text null,LPEndDate text null);";
			
	 private static final String TABLE_CheckOnlineAssessmentStatus = "tblCheckOnlineAssessmentStatus";
	 private static final String DATABASE_CREATE_TABLE_CheckOnlineAssessmentStatus = "create table tblCheckOnlineAssessmentStatus(UserId text null, LoginID text null, ModuleID text null,RSPID text null,Status text null,flgPassStatus text null,flgTestMap text null);";
	
	 
	 private static final String TABLE_ModuleLanguageMstr ="tblModuleLanguageMstr";
	 private static final String DATABASE_CREATE_TABLE_ModuleLanguageMstr = "create table tblModuleLanguageMstr (LPCourseID text null, LanguageID text null, RegionID text null,flgPDAOnline text null);";
	 private static final String TABLE_LanguageMstr ="tblLanguageMstr";
	 private static final String DATABASE_CREATE_TABLE_LanguageMstr = "create table tblLanguageMstr (LanguageID text null, Language text null);";
	 
	 
	  private static final String DATABASE_TABLE_Distributer_QuestionGrpMstr = "tblDistributorQstnGrpMstr";
	  private static final String DATABASE_TABLE_Distributer_QuestionMstr = "tblDistributor_QstnMstr";
	  private static final String DATABASE_TABLE_Distributer_Question_Option_Mstr = "tblDistributorQstnOptionMapping";
	  private static final String DATABASE_TABLE_Distributer_Profile_Mstr = "tblDistributorProfileMstr";
	  private static final String TABLE_Distributor_Ques_Ans = "tblDstrbtrQuesAns";
	  private static final String TABLE_Distributor_Assesment = "tblDstrbtrAssmnt";
	  
	  private static final String DATABASE_CREATE_TABLE_DistributorQstnGrpMstr = "create table tblDistributorQstnGrpMstr (QstnGrpID text null,QstnGrpDescr text null);";//, AutoIdOutlet int null
	  private static final String DATABASE_CREATE_TABLE_Distributor_QstnMstr = "create table tblDistributor_QstnMstr (QstnID text null,QstnTxtname text null,QstnDescr text null,Weightage text null,QstnGrpID text null);";//, AutoIdOutlet int null
	  private static final String DATABASE_CREATE_TABLE_tblDistributorQstnOptionMapping = "create table tblDistributorQstnOptionMapping (QstnId text null,OptionId text null,OptionDesc text null,Score text null);";//, AutoIdOutlet int null
	  private static final String DATABASE_CREATE_TABLE_tblDistributorProfileMstr = "create table tblDistributorProfileMstr (ProfileId text null,Descr text null,Profile text null,MinScore text null,MaxScore text null,RecommendedAction text null,Spirit text null);";//, AutoIdOutlet int null
	  
	  private static final String DATABASE_CREATE_TABLE_tblDistributorQstnAnsVal = "create table tblDstrbtrQuesAns (tempId text null,QstnGrpId text null,QstnId text null,OptionId text null,Score text null,Weightage text null,Sstat integer null);";
	 
		
	  
	 private static final String DATABASE_CREATE_TABLE_1 = "create table tblAvailableVersionMstr (VersionID text null,VersionSerialNo text null,VersionDownloadStatus text null,ServerDate text null);";//, AutoIdOutlet int null
	 private static final String DATABASE_CREATE_TABLE_2 = "create table tblUserAuthenticationMstr (flgUserAuthenticated text null);";
	 
	 private static final String DATABASE_CREATE_TABLE_3 = "create table tblChainMstr (ChainID text not null,ChainName text null);";
		
	 
	
	 
	 private static final String DATABASE_CREATE_TABLE_4 = "create table tblStoreList (StoreID text not null, StoreType text null," +
	 		" StoreName string not null, StoreLatitude text  null, StoreLongitude text  null, LastVisitDate text  null," +
	 		" Sstat integer not null, ActualLatitude text null, ActualLongitude text null, VisitStartTS text null, VisitEndTS text null," +
	 		"AutoIdStore int null, LocProvider text null, Accuracy text null, BateryLeftStatus text null," +
	 		"ChainID text null,DisplayTarget text null,ActualDisplayPlace text null,AppVersion int null,imei text null,NoOfCheckOutCounters text null,StoreCity text null);";
		
	 private static final String DATABASE_CREATE_TABLE_5 = "create table tblDisplayMstr (DisplayLocationID text not null,DisplayLocation text null);";
		
	 
	 
	 private static final String DATABASE_CREATE_TABLE_UserLoginMstr = "create table tblUserLoginMstr(UserId text null, LoginResult text null,LoginID text null, NodeID text null, NodeType text null, RoleId text null, UserFullName text null, RegionID text null, RoleForSurvey text null, EmailID text null, flgIndMangerAvail text null,MobNo text null,Sstat integer null);";
	 private static final String DATABASE_CREATE_TABLE_ModuleWiseSlideDetails = "create table tblModuleWiseSlideDetails(LPCourceMainID text null, LPCourseName text null, SlideNo int null,SlideName text null,flgLastSlide text null,LanguageID text null);";
	
	 
	
	
	
		
	
	
	
		
		
		
		
	 private static final String DATABASE_CREATE_TABLE_UserModuleMstr = "create table tblUserModuleMstr(LPCourseGroupMainID text null, LPCourseGroupName text null, LPCourseID text null, LPCourseName text null, LPCourseDescription text null,ModuleImgName text null, ModuleImgUrl text null, Status text null,DayVal text null, flgNewOld text null,PDACoursePath text null,PDAEnglishCoursePath text null,PDAHIndiCoursePath text null,PDAMalyalamCoursePath text null,PDATamilCoursePath text null,PDAKannadaCoursePath text null,PDAMarathiCoursePath text null,PDAGujratiCoursePath text null,PDABangaliCoursePath text null,PDAAssamiesCoursePath text null,PDATeleguCoursePath text null,PDAOdiyaCoursePath text null,flgPDAOnline text null,LPCourseCatID text null);"; 
	 private static final String DATABASE_CREATE_TABLE_6 = "create table tblStoreDisplayIDPhotoDetail (ChainID text null,StoreID text null,DisplayID text null,ClickedDateTime text null,PhotoName text null,SelectedLocationID text null,SelectedLocation text null,PhotoValidation text null,PDAPhotoPath text null,Sstat integer null);";
	 private static final String DATABASE_CREATE_TABLE_SmallIconMstr = "create table tblSmallIconMstr(LPCourseID text null,ModuleImgName text null,PathsmallIcon text null);";
	 private static final String DATABASE_CREATE_TABLE_ModuleContent = "create table tblModuleContent(LPCourseID text null,ModuleContentName text null,PathModuleContnt text null,isContentDownloaded integer null,LanguageID text null,flgPDAOnline text null);";
	 
	 private static final String DATABASE_CREATE_TABLE_MODULE_SAVED = "create table tblModuleSaved(moduleID text null,ModuleContentName text null,moduleSlideSequenceNo text null,isLastSlide text null,imeiNum text null,AssessmentComplete integer null,Sstat integer null,StartTime text null,LpEndDate text null,loginId text null,CourseCatID text null,LanguageID text null);";
	   
	 
	 
	 private static final String DATABASE_CREATE_TABLE_11 = "create table tblModuleQuestionAnswerOne (TestID integer null,TestInstanceID integer null,TestInsUserMapID integer null,CourseMainID integer null,TotalQstn integer null,PassingQstn integer null,AllocatedtimeInSecPerQstn integer null,Descr text null,RSPID integer null,PGNmbr integer null,AssessmentStatus integer null,TimeElapsedInSec integer null,QstNo integer null,SubmitType integer null,NoCorrectAns int null,flgPassStatus integer null,StartTime text null,EndTime text null,flgAssessmentAvailable integer null);";
	
	 private static final String DATABASE_CREATE_TABLE_12 = "create table tblModuleQuestionAnswerTwo (TestID integer null,QstID integer null,QstpartTxt text null,SRNmbr integer null,QstnTypeID integer null);";//, AutoIdOutlet int null
		
	 private static final String DATABASE_CREATE_TABLE_13 = "create table tblModuleQuestionAnswerThree (TestID integer null,TestInsUserMapID integer null,QstID integer null,QstnStatID integer null,StatementDescr text null,CorrectAns integer null,OptionSeq integer null,RspDetID integer null,RspID integer null,RsltQstnStatID integer null,flgCorrectAns integer null);";
		
	 
	 private static final String DATABASE_CREATE_TABLE_14 = "create table tblTestsAnswerForModule (ModuleId integer null,TestID integer null,TestInstanceID integer null,TestInsUserMapID integer null,QstID integer null,AnswerDescr text null,QstnTypeID integer null,ModuleCompleteDate text null,Sstat integer null);";
	
	 private static final String DATABASE_CREATE_DISTRIBUTOR_ASSESMENT = "create table tblDstrbtrAssmnt (tempId text null,distributor text null,assessorName1 text null,assessorName2 text null,zone text null,territory text null,town text null,VisitStartTS text null,Sstat integer null);";
		
	 
	 
	   Context context;
	  private SQLiteDatabase db;
		private DatabaseHelper DBHelper;
	  
	  public GskDBAdapter(Context ctx)
	  {
		this.context=ctx;
		DBHelper=new DatabaseHelper(context);
	 }

	  	public static class  DatabaseHelper extends SQLiteOpenHelper
	  	{

			public DatabaseHelper(Context ctx) {
				super(ctx, DATABASE_NAME, null, CommonInfo.DATABASE_VERSION);
				// TODO Auto-generated constructor stub
			}

			@Override
			public void onCreate(SQLiteDatabase db) 
			{
				db.execSQL(DATABASE_CREATE_TABLE_1);
				db.execSQL(DATABASE_CREATE_TABLE_2); 
				db.execSQL(DATABASE_CREATE_TABLE_3);
				db.execSQL(DATABASE_CREATE_TABLE_4); 
				db.execSQL(DATABASE_CREATE_TABLE_5); 
				
				
				 db.execSQL(DATABASE_CREATE_TABLE_UserLoginMstr);
				 db.execSQL(DATABASE_CREATE_TABLE_UserModuleMstr);
				 db.execSQL(DATABASE_CREATE_TABLE_SmallIconMstr);
				 
				 db.execSQL(DATABASE_CREATE_TABLE_6); 
				 db.execSQL(DATABASE_CREATE_TABLE_ModuleWiseSlideDetails); 
				 db.execSQL(DATABASE_CREATE_TABLE_ModuleContent); 
				 
				 db.execSQL(DATABASE_CREATE_TABLE_MODULE_SAVED);
				 
				 db.execSQL(DATABASE_CREATE_TABLE_11); 
				 db.execSQL(DATABASE_CREATE_TABLE_12); 
				 db.execSQL(DATABASE_CREATE_TABLE_13); 
				 db.execSQL(DATABASE_CREATE_TABLE_14); 
			
				 db.execSQL(DATABASE_CREATE_TableStartEndTime);
				 db.execSQL(DATABASE_CREATE_TABLE_DistributorQstnGrpMstr);
			     db.execSQL(DATABASE_CREATE_TABLE_Distributor_QstnMstr);
			     db.execSQL(DATABASE_CREATE_TABLE_tblDistributorQstnOptionMapping);
			     db.execSQL(DATABASE_CREATE_TABLE_tblDistributorProfileMstr);
			     db.execSQL(DATABASE_CREATE_TABLE_tblDistributorQstnAnsVal);
			     db.execSQL(DATABASE_CREATE_DISTRIBUTOR_ASSESMENT);
			     
			     db.execSQL(DATABASE_CREATE_TABLE_ModuleLanguageMstr);
			     db.execSQL(DATABASE_CREATE_TABLE_LanguageMstr);
			     db.execSQL(DATABASE_CREATE_TABLE_CheckOnlineModuleStatus);
			     db.execSQL(DATABASE_CREATE_TABLE_CheckOnlineAssessmentStatus);
			     
				
			}
			
			
			
			public void onUpgrade(SQLiteDatabase db) 
			{
				db.execSQL("DELETE FROM tblCheckOnlineAssessmentStatus");
				db.execSQL("DELETE FROM tblCheckOnlineModuleStatus");
				db.execSQL("DELETE FROM tblAvailableVersionMstr");
				db.execSQL("DELETE FROM tblUserAuthenticationMstr"); 
				db.execSQL("DELETE FROM tblChainMstr");
				db.execSQL("DELETE FROM tblStoreList"); 
				db.execSQL("DELETE FROM tblDisplayMstr"); 
				
				
				 db.execSQL("DELETE FROM "+DATABASE_TABLE_Main14);
				 db.execSQL("DELETE FROM "+DATABASE_TABLE_Main13);
				 db.execSQL("DELETE FROM "+DATABASE_TABLE_Main12);
				 
				 db.execSQL("DELETE FROM "+DATABASE_TABLE_Main11); 
				 db.execSQL("DELETE FROM "+DATABASE_TABLE_MODULE_SAVED); 
				 
				 
				

				 
				 
				 db.execSQL("DELETE FROM "+DATABASE_TABLE_Main6); 
				 
				 db.execSQL("DELETE FROM "+TABLE_ModuleContent);
				 
				 db.execSQL("DELETE FROM "+TABLE_SmallIconMstr); 
				 db.execSQL("DELETE FROM "+TABLE_UserModuleMstr); 
				 db.execSQL("DELETE FROM "+TABLE_ModuleWiseSlideDetails); 
				 db.execSQL("DELETE FROM "+TABLE_UserLoginMstr); 
			
				 db.execSQL("DELETE FROM "+DATABASE_TABLE_MAIN5_DisplayMstr);
				 db.execSQL("DELETE FROM "+DATABASE_TABLE_MAIN4_StoreMstr);
			     db.execSQL("DELETE FROM "+DATABASE_TABLE_MAIN3_ChainMstr);
			     db.execSQL("DELETE FROM "+DATABASE_TABLE_MAIN2_UserAuth);
			     db.execSQL("DELETE FROM "+DATABASE_TABLE_MAIN1_Version);
			     db.execSQL("DELETE FROM "+DATABASE_CREATE_TableStartEndTime);
			     db.execSQL("DELETE FROM "+DATABASE_TABLE_StartEndTime);
			
				 
								 

			}

			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion) 
			{
				//onUpgrade(db);
				
				onCreate(db);
				
			}
	  		
	  	}
	  	
	  	
	  	/*private static final String DATABASE_CREATE_TABLE_UserLoginMstr = "create table tblUserLoginMstr" +
	  			"(UserID text null,UserName text null,UserRegisterNumber text null,flgUserAlreadyExists int null," +
	  			"TSIApproved int null,Sstat integer null,IMEINo text null);";*/
		
	  	
	  	public void fnUpdatetblUserLoginMstr(int Sstat) 
		{
			
			db.execSQL("Update tblUserLoginMstr Set  Sstat='"+Sstat+"'");
		}
	  	
	  	
	  	
	  	/* private static final String DATABASE_CREATE_TABLE_3 = "create table tblChainMstr " +
	  	 		"(ChainID text not null,ChainName text null);";*/
	  	
	  	
	  /*	public String getChainNameBasedOnChainID(String ChainID) 
		{
			int LoncolumnIndex = 0;
			open();
		
			
				 Cursor cursor2 = db.rawQuery("SELECT DISTINCT ChainName FROM tblChainMstr WHERE ChainID='"+ChainID+"'", null); //order by AutoIdOutlet Desc
			
			try {
				String UniqueProductShortName="0";
				if (cursor2.moveToFirst()) 
				{

					for (int i = 0; i < cursor2.getCount(); i++)
					{
						
						if (!cursor2.isNull(LoncolumnIndex))
						{
							UniqueProductShortName =cursor2.getString(LoncolumnIndex).toString();
							cursor2.moveToNext();
						}
						
					}

				}
				return UniqueProductShortName;
			} finally {
				close();

			}
		}*/
	  	

	  		public boolean isModuleCompleted(String moduleId,String LanguageID)
	  		{
	  			open();
	  			try {
					
				
	  			Cursor cur=db.rawQuery("Select moduleID from tblModuleSaved where moduleID='"+moduleId+"'And isLastSlide=1", null);
	  			if(cur.getCount()>0)
	  			{
	  				return true;
	  			}
	  			else 
	  			{
	  				return false;
	  			}
	  			} catch (Exception e) {
	  				return false;
				}
	  		}
	  	public void saveOrUpdateModuleData(String _moduleId,String _moduleContentName,String _moduleSlideSequenceNo,String _isLastSlide,String _imeiNum,String StartTime,String LpEndDate,String loginId,String CourseCatID,String LanguageId)
	  	{
	  		 //tblModuleSaved(moduleID text null,ModuleContentName text null,moduleSlideSequenceNo text null,isLastSlide text null,imeiNum text null);";
	  	open();
	  try {
	  	Cursor cur=db.rawQuery("Select isLastSlide from tblModuleSaved where moduleID='"+_moduleId+"'And isLastSlide=0", null);
	  	ContentValues content=new ContentValues();
	  	content.put("moduleSlideSequenceNo", _moduleSlideSequenceNo);
	  	content.put("isLastSlide", _isLastSlide);
	  	content.put("LanguageID", LanguageId);
	  	
	  	//LanguageID
	  	if(cur.getCount()>0)
	  	{
	  		cur.close();
	  	System.out.println("tblModuleSaved Update with moduleSlideSequenceNo= "+_moduleSlideSequenceNo+" isLastSlide= "+_isLastSlide);	
	  		db.update(DATABASE_TABLE_MODULE_SAVED, content, "moduleID=? and LanguageID=?", new String[] {_moduleId,LanguageId});
	  	}
	  	else
	  	{
	  		Cursor cursor=db.rawQuery("Select moduleID from tblModuleSaved where moduleID='"+_moduleId+"'", null);
	  		if(cursor.getCount()>0)
	  		{
	  			
	  		}
	  		else
	  		{
	  		 	System.out.println("tblModuleSaved Inserted with moduleSlideSequenceNo= "+_moduleSlideSequenceNo+" isLastSlide= "+_isLastSlide+" moduleID= "+ _moduleId+"ModuleContentName= "+_moduleContentName);
	  			content.put("moduleID", _moduleId);
	  			content.put("ModuleContentName", _moduleContentName);
	  			content.put("imeiNum", _imeiNum);
	  			content.put("StartTime", StartTime);
	  			content.put("LpEndDate", LpEndDate);
	  			content.put("loginId", loginId);
	  			content.put("CourseCatID", CourseCatID);
	  			content.put("AssessmentComplete", 0);
	  			content.put("Sstat", 3);
	  			content.put("LanguageID", LanguageId);
	  			db.insert(DATABASE_TABLE_MODULE_SAVED, null, content);
	  			
	  		}
	  		
	  	}
	  } catch (Exception e) {
			// TODO: handle exception
		}
	  finally
	  {
		  close();
	  }
	  	}
	  	/*public String getChainIDBasedOnStoreID(String StoreID) 
		{
			int LoncolumnIndex = 0;
			open();
		
			
				 Cursor cursor2 = db.rawQuery("SELECT DISTINCT ChainID FROM tblStoreList WHERE StoreID='"+StoreID+"'", null); //order by AutoIdOutlet Desc
			
			try {
				String UniqueProductShortName="0";
				if (cursor2.moveToFirst()) 
				{

					for (int i = 0; i < cursor2.getCount(); i++)
					{
						
						if (!cursor2.isNull(LoncolumnIndex))
						{
							UniqueProductShortName =cursor2.getString(LoncolumnIndex).toString();
							cursor2.moveToNext();
						}
						
					}

				}
				return UniqueProductShortName;
			} finally {
				close();

			}
		}*/
	  	
	  	public void insertDistributor(String _tempId,String _distributor,String _assessorName1,String _assessorName2,String _zone,String _territory,String _town,String visitStartTime)
	  	{
	  		open();
	  		try {
				
			
	  		//assessorName2
	  		//   //  "create table tblDstrbtrAssmnt (tempId text null,distributor text null,assessorName1 text null,zone text null,territory text null,town text null,assessorName2,Sstat integer null);";
	  		ContentValues content=new ContentValues();
	  		content.put("tempId", _tempId);
	  		content.put("distributor", _distributor);
	  		content.put("assessorName1", _assessorName1);
	  		content.put("assessorName2", _assessorName2);
	  		content.put("zone", _zone);
	  		content.put("territory", _territory);
	  		content.put("town", _town);
	  		content.put("VisitStartTS", visitStartTime);
	  		content.put("Sstat", 3);
	  		
	  		
	  		db.insert(TABLE_Distributor_Assesment, null, content);
	  		} catch (Exception e) {
				
			}
	  		finally
	  		{
	  			close();
	  		}
	  	}
	  /*	public void UpdateChainIDIntblStoreDisplayIDPhotoDetail(String sID, String ChainID)
		{
	  		open();
			try
			 {

				final ContentValues values = new ContentValues();
				values.put("ChainID", ChainID);
	           
				int affected1 = db.update("tblStoreDisplayIDPhotoDetail", values, "StoreID=?",new String[] { sID });
				
			 }
			catch (Exception ex)
			{
				
			}
			finally
			{
				close();
			}
				

		}*/
	  	
		/* private static final String DATABASE_CREATE_TABLE_4 = "create table tblStoreList (StoreID text not null, StoreType text null," +
			 		" StoreName string not null, StoreLatitude text  null, StoreLongitude text  null, LastVisitDate text  null," +
			 		" Sstat integer not null, ActualLatitude text null, ActualLongitude text null, VisitStartTS text null, VisitEndTS text null," +
			 		"AutoIdStore int null, LocProvider text null, Accuracy text null, BateryLeftStatus text null," +
			 		"ChainID text null,DisplayTarget text null,ActualDisplayPlace text null,AppVersion int null,imei text null,NoOfCheckOutCounters text null);";
			*/
		/*public void UpdateActualDisplayPlace(String StoreID, String ActualDisplayPlace)
		{
	  		open();
			try
			 {

				final ContentValues values = new ContentValues();
				values.put("ActualDisplayPlace", ActualDisplayPlace);
	           
				int affected1 = db.update("tblStoreList", values, "StoreID=?",new String[] { StoreID });
				
			 }
			catch (Exception ex)
			{
				
			}
			finally
			{
				close();
			}
				

		}*/
		
		/*public void UpdatStoreCity(String StoreID, String StoreCity)
		{
	  		open();
			try
			 {

				final ContentValues values = new ContentValues();
				values.put("StoreCity", StoreCity);
	           
				int affected1 = db.update("tblStoreList", values, "StoreID=?",new String[] { StoreID });
				
			 }
			catch (Exception ex)
			{
				
			}
			finally
			{
				close();
			}
				

		}*/
		
		 public void saveEndTime(String moduleID,String LpEndDate,String LanguageId ) {
		  		db.execSQL("UPDATE tblStartEndTime SET LpEndDate='"+LpEndDate+"' WHERE moduleID='"+moduleID+"'");
				

			}
		public long saveStartTime(String moduleID,String StartTime,String LpEndDate,String LanguageId )
		{

			ContentValues initialValues = new ContentValues();

			System.out.println("moduleID=" + moduleID);
			initialValues.put("moduleID", moduleID.trim());
			initialValues.put("StartTime", StartTime.trim());
			initialValues.put("LanguageID", LanguageId.trim());
			
			/* Calendar calendar = Calendar.getInstance();
			 calendar.setTimeInMillis(System.currentTimeMillis()+5*60*1000);
			 SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				String strDate = sdf.format(calendar.getTime());*/
		      System.out.println("Date"+LpEndDate);
		       
			initialValues.put("LpEndDate", LpEndDate);


			return db.insert(DATABASE_TABLE_StartEndTime, null, initialValues);
		}
		public long saveEndtTime(String moduleID,String LpEndDate,int LanguageId )
		{

			ContentValues initialValues = new ContentValues();

			System.out.println("moduleID=" + moduleID);
			initialValues.put("moduleID", moduleID.trim());
		
			initialValues.put("LpEndDate", LpEndDate.trim());


			return db.insert(DATABASE_TABLE_StartEndTime, null, initialValues);
		}
		
		public String  fngetTableStartEndTime(String ModuleId,String LanguageId)
		{
			open();
			Cursor cursor = db.rawQuery("SELECT StartTime,LpEndDate from tblStartEndTime where moduleID = '"+ModuleId +"' and LanguageID='"+LanguageId+"'", null);
			String flag="0";
			try {
				if(cursor.getCount()>0){
					if (cursor.moveToFirst()){
						flag=	cursor.getString(0).toString()+"^"+cursor.getString(1).toString();
					}

				}
				return flag;
			}

			finally
			{
				cursor.close();
				close();
			}
		}
	  	
	  	
/*	  	public void UpdateStoreFlag(String sID, int flag2set)
		{
	  		open();
			try
			 {

				final ContentValues values = new ContentValues();
				values.put("Sstat", flag2set);
	           
				int affected1 = db.update("tblStoreList", values, "StoreID=?",new String[] { sID });
				
				int affected2 = db.update("tblStoreDisplayIDPhotoDetail", values, "StoreID=?",new String[] { sID });
				
				

			 }
			catch (Exception ex)
			{
				
			}
			finally
			{
				close();
			}
				

		}*/
	  	
	  	/* public boolean validateToLogout()
		   {
		    boolean isDataPending=false;
		    open();
		        Cursor cur=db.rawQuery("Select StoreID from tblStoreList where Sstat ='"+3+"'",null );
		        
		        if(cur.getCount()>0)
		        {
		         isDataPending=true;  
		        }
		        else
		        {
		        cur.close();
		         isDataPending=validatePhotDataPresnt();
		        }
		        
		        
		        close();
		   return isDataPending;
		   }*/
	  	 
	  	/* public boolean validatePhotDataPresnt()
		   {
		    boolean isPhotoDataPresent=false;
		    Cursor cur=db.rawQuery("Select PhotoName from tblStoreDisplayIDPhotoDetail where Sstat ='"+3+"'",null );
		    
		     if(cur.getCount()>0)
		        {
		      isPhotoDataPresent=true;  
		        }
		        else
		        {
		        
		         isPhotoDataPresent=false;
		        }
		   return isPhotoDataPresent;
		        
		   }
	  	*/
	  	
	  	/* private static final String DATABASE_CREATE_TABLE_6 = "create table tblStoreDisplayIDPhotoDetail" +
	  	 		" (RouteID text null,StoreID text null,DisplayID text null,ClickedDateTime text null,PhotoName text null," +
	  	 		"SelectedLocation text null,PhotoValidation text null,PDAPhotoPath text null,Sstat integer null);";*/
	 	
	  	public void updateImageRecordsSyncd(String PhotoName) 
		{
          
			try 
			{
				open();
				System.out.println("Sunil Doing Testing Response after sending Image inside BD" + PhotoName);
				final ContentValues values = new ContentValues();
				values.put("Sstat", 4);
				
				int affected3 = db.update("tblStoreDisplayIDPhotoDetail", values, "PhotoName=?",
						new String[] { PhotoName });
				
				
				/*int affected = db.update("tblOutletPhotoDetail", values, "PhotoName=? AND OutletID=?",
						new String[] { imageNameToUpdate,storeId });*/
				
			} 
			catch (Exception ex) 
			{
				
			}
			finally
			 {
				 close(); 
			 }

		}
	  	
	  	
	  	public void updateRecordsSyncd() 
		{
          
			try 
			{
				

				final ContentValues values = new ContentValues();
				values.put("Sstat", "4");
				int affected = db.update("tblUserLoginMstr", values, "Sstat=?",
						new String[] { "3" });
				int affected2 = db.update("tblStoreList", values, "Sstat=?",
						new String[] { "3" });
				int affected3 = db.update("tblStoreDisplayIDPhotoDetail", values, "Sstat=?",
						new String[] { "3" });
				
			} 
			catch (Exception ex) 
			{
				//Log.e(TAG, ex.toString());
			}

		}
	  	
	  	public void updateXMLRecordsSyncd() 
		{
          
			try 
			{
				

				final ContentValues values = new ContentValues();
				values.put("Sstat", "4");
				/*int affected = db.update("tblUserLoginMstr", values, "Sstat=?",
						new String[] { "3" });*/
				/*int affected2 = db.update("tblModuleSaved", values, "Sstat=?",
						new String[] { "3" });*/
				int affected3 = db.update("tblTestsAnswerForModule", values, "Sstat=?",
						new String[] { "3" });
				int affected4 = db.update("tblDstrbtrQuesAns", values, "Sstat=?",
						new String[] { "3" });
				int affected5 = db.update("tblDstrbtrAssmnt", values, "Sstat=?",
						new String[] { "3" });
				
				
			} 
			catch (Exception ex) 
			{
				//Log.e(TAG, ex.toString());
			}

		}
	 	
	  	
	  	
	  	
	  	/*public String getSelectedLocationBasedOnDisplayID(String DisplayID,String StoreID) 
		{
			int LoncolumnIndex = 0;
			open();
		
			 Cursor cursor2 = db.rawQuery("SELECT DISTINCT SelectedLocation FROM tblStoreDisplayIDPhotoDetail WHERE DisplayID='"+DisplayID+"'and StoreID='"+StoreID+"' and Sstat = 1", null); //order by AutoIdOutlet Desc
			
			try {
				String UniqueProductShortName="0";
				if (cursor2.moveToFirst()) 
				{

					for (int i = 0; i < cursor2.getCount(); i++)
					{
						
						if (!cursor2.isNull(LoncolumnIndex))
						{
							UniqueProductShortName =cursor2.getString(LoncolumnIndex).toString();
							cursor2.moveToNext();
						}
						
					}

				}
				return UniqueProductShortName;
			} finally {
				close();

			}
		}*/
	  	
	  	
		/*public String getSelectedLocationBasedOnDisplayIDSummary(String DisplayID,String StoreID) 
		{
			int LoncolumnIndex = 0;
			open();
		
			 Cursor cursor2 = db.rawQuery("SELECT DISTINCT SelectedLocation FROM tblStoreDisplayIDPhotoDetail WHERE DisplayID='"+DisplayID+"'and StoreID='"+StoreID+"' and Sstat = 3", null); //order by AutoIdOutlet Desc
			
			try {
				String UniqueProductShortName="0";
				if (cursor2.moveToFirst()) 
				{

					for (int i = 0; i < cursor2.getCount(); i++)
					{
						
						if (!cursor2.isNull(LoncolumnIndex))
						{
							UniqueProductShortName =cursor2.getString(LoncolumnIndex).toString();
							cursor2.moveToNext();
						}
						
					}

				}
				return UniqueProductShortName;
			} finally {
				close();

			}
		}
	  	*/
	  	
	  	/*public void fnDeletePhoto(String imageNameToUpdate)
		{
	  		 open();
	      try
	        {
	           db.execSQL("Delete from tblStoreDisplayIDPhotoDetail where PhotoName='"+imageNameToUpdate+"'");
	           db.execSQL("Delete from tblNewStoreEntries where Sstat=4" );
	        //   // System.out.println("Anil Dangi SET flgTodayRoute= 0 :");
			}
	      catch (Exception ex)
	        {
				//Log.e(TAG, ex.toString());
			}
	      finally
	      {
	    	  close();
	      }

		}*/
	  	
	  	
	  	 /*public void updatePhotoValidation(String validation,String imageNameToUpdate)
	  	 {

	  			 open();
	  				final ContentValues values = new ContentValues();
	  				values.put("PhotoValidation", validation);
	  				int affected = db.update("tblStoreDisplayIDPhotoDetail", values, "PhotoName=?",
	  						new String[] { imageNameToUpdate });
	  				close();
	  			
	  			}
	  	*/
	  	
	  	
	  /*	public boolean getTakenAtleastOnePhotoEachDisplay(String DisplayID,String StoreID) 
		{
			int LoncolumnIndex = 0;
			
			boolean DisplayIDPhoto=false;
			open();
		
			 Cursor cursor2 = db.rawQuery("SELECT DISTINCT PhotoValidation FROM tblStoreDisplayIDPhotoDetail WHERE DisplayID='"+DisplayID+"'and StoreID='"+StoreID+"' ", null); //order by AutoIdOutlet Desc
			
			try {
				String UniqueProductShortName="0";
				if (cursor2.moveToFirst()) 
				{

					for (int i = 0; i < cursor2.getCount(); i++)
					{
						
						if (!cursor2.isNull(LoncolumnIndex))
						{
							
							UniqueProductShortName =cursor2.getString(LoncolumnIndex).toString();
							if(UniqueProductShortName.equals("1"))
							{
								DisplayIDPhoto=true;
							}
							else
							{
								DisplayIDPhoto=false;
							}
							cursor2.moveToNext();
						}
						
					}

				}
				return DisplayIDPhoto;
			} finally {
				close();

			}
		}
	  		
	  	
	  	*/
	  	
	  	
	  	
	  	
	  	
	  	
	  	
	  	
	  	
	  	
	  	
	  	
	  	
	  	
	  	/*public String getDisplayLocationIDBasedOnDisplayLocation(String DisplayLocation) 
		{
			int LoncolumnIndex = 0;
			open();
		
			 Cursor cursor2 = db.rawQuery("SELECT DISTINCT DisplayLocationID FROM tblDisplayMstr WHERE DisplayLocation='"+DisplayLocation+"'", null); //order by AutoIdOutlet Desc
			
			try {
				String UniqueProductShortName="0";
				if (cursor2.moveToFirst()) 
				{

					for (int i = 0; i < cursor2.getCount(); i++)
					{
						
						if (!cursor2.isNull(LoncolumnIndex))
						{
							UniqueProductShortName =cursor2.getString(LoncolumnIndex).toString();
							cursor2.moveToNext();
						}
						
					}

				}
				return UniqueProductShortName;
			} finally {
				close();

			}
		}
	  	*/
	  	
	  	
	  	
	  	
	  	//private static final String DATABASE_CREATE_TABLE_5 = "create table tblDisplayMstr (DisplayLocationID text not null,DisplayLocation text null);";
		
	  	
	  	
		/*public LinkedHashMap<String, String> fetch_Display_List()
		{
			
			open();
			LinkedHashMap<String, String> hmapCatgry = new LinkedHashMap<String, String>();
			Cursor cursor = db.rawQuery("SELECT DisplayLocationID,DisplayLocation FROM tblDisplayMstr",null);
			try 
			{
				if(cursor.getCount()>0)
				{
					if (cursor.moveToFirst()) 
					{
						
						for (int i = 0; i <= (cursor.getCount() - 1); i++)
						{
							hmapCatgry.put(cursor.getString(1).toString(),cursor.getString(0).toString());
							cursor.moveToNext();
						}
					}
					
				}
				
				else
				{
					hmapCatgry.put("No Chain", "0");
				}
				return hmapCatgry;
			}
			finally
			{
				cursor.close();
				close();
			}
		}
		*/
	  	
	  	/* private static final String DATABASE_CREATE_TABLE_3 = "create table tblChainMstr " +
	  	 		"(ChainID text not null,ChainName text null);";*/
	 	
	  	
	  	/*public LinkedHashMap<String, String> fetch_Chain_List()
		{
	  		open();
			LinkedHashMap<String, String> hmapCatgry = new LinkedHashMap<String, String>();
			Cursor cursor = db.rawQuery("SELECT tblStoreList.ChainID,tblChainMstr.ChainName FROM tblStoreList inner join tblChainMstr on tblChainMstr.ChainID=tblStoreList.ChainID",null);
			try 
			{
				if(cursor.getCount()>0)
				{
					if (cursor.moveToFirst()) 
					{
						
						for (int i = 0; i <= (cursor.getCount() - 1); i++)
						{
							hmapCatgry.put(cursor.getString(1).toString(),cursor.getString(0).toString());
							cursor.moveToNext();
						}
					}
					
				}
				
				else
				{
					hmapCatgry.put("No Chain", "0");
				}
				return hmapCatgry;
			}
			finally
			{
				cursor.close();
				close();
			}
		}
		*/
	  	
	  	
	  	/*public String[] getStoreNameNid4LV(String ChainID) 
		{
			int LoncolumnIndex = 0;
			int LoncolumnIndex1 = 1;
			
			
			
			 open();
		
			 Cursor cursor2;
			 if(ChainID.equals("0"))
			 {
				cursor2 = db.rawQuery("SELECT DISTINCT StoreID, StoreName FROM tblStoreList WHERE Sstat = 0", null); //order by AutoIdOutlet Desc
					 
			 }
			 else
			 {
			cursor2 = db.rawQuery("SELECT DISTINCT StoreID, StoreName FROM tblStoreList WHERE Sstat = 0 and ChainID='"+ChainID+"'", null); //order by AutoIdOutlet Desc
			 }
			try {
				String UniqueProductShortName[] = new String[cursor2.getCount()];
				if (cursor2.moveToFirst()) 
				{

					for (int i = 0; i < cursor2.getCount(); i++)
					{
						
						if (!cursor2.isNull(LoncolumnIndex))
						{
							UniqueProductShortName[i] =cursor2.getString(LoncolumnIndex).toString() + "^" + cursor2.getString(LoncolumnIndex1).toString();
							cursor2.moveToNext();
						}
						
					}

				}
				return UniqueProductShortName;
			} finally {
				close();

			}
		}*/
	  	
	  	
	  	/*public String getNoOfCheckOutCountersBasedOnStoreID(String StoreID) 
		{
	  		
			int LoncolumnIndex = 0;
			open();
		
			
				 Cursor cursor2 = db.rawQuery("SELECT DISTINCT NoOfCheckOutCounters FROM tblStoreList WHERE StoreID='"+StoreID+"'", null); //order by AutoIdOutlet Desc
			
			try {
				String UniqueProductShortName="0";
				if (cursor2.moveToFirst()) 
				{

					for (int i = 0; i < cursor2.getCount(); i++)
					{
						
						if (!cursor2.isNull(LoncolumnIndex))
						{
							UniqueProductShortName =cursor2.getString(LoncolumnIndex).toString();
							cursor2.moveToNext();
						}
						
					}

				}
				return UniqueProductShortName;
			} finally {
				close();

			}
		}
	  	*/
	  	
		/*public String getDisplayTargetBasedOnStoreID(String StoreID) 
		{
			int LoncolumnIndex = 0;
			open();
		
			
				 Cursor cursor2 = db.rawQuery("SELECT DISTINCT DisplayTarget FROM tblStoreList WHERE StoreID='"+StoreID+"'", null); //order by AutoIdOutlet Desc
			
			try {
				String UniqueProductShortName="0";
				if (cursor2.moveToFirst()) 
				{

					for (int i = 0; i < cursor2.getCount(); i++)
					{
						
						if (!cursor2.isNull(LoncolumnIndex))
						{
							UniqueProductShortName =cursor2.getString(LoncolumnIndex).toString();
							cursor2.moveToNext();
						}
						
					}

				}
				return UniqueProductShortName;
			} finally {
				close();

			}
		}*/
	  	
	  	/* private static final String DATABASE_CREATE_TABLE_4 = "create table tblStoreList (StoreID text not null, StoreType string not null," +
	 	 		" StoreName string not null, StoreLatitude real not null, StoreLongitude real not null, LastVisitDate string not null," +
	 	 		" Sstat integer not null, ActualLatitude text null, ActualLongitude text null, VisitStartTS text null, VisitEndTS text null," +
	 	 		"AutoIdStore int null, LocProvider text null, Accuracy text null, BateryLeftStatus text null," +
	 	 		"ChainID text null,DisplayTarget text null,ActualDisplayPlace text null);";*/
	  	
	  	
	  	/*public LinkedHashMap<String, String> fetch_Store_List(String ChainID)
		{
	  		
	  		open();
			LinkedHashMap<String, String> hmapCatgry = new LinkedHashMap<String, String>();
			
			Cursor cursor;
			if(ChainID.equals("0"))
			 {
				cursor = db.rawQuery("SELECT DISTINCT StoreID, StoreName FROM tblStoreList WHERE Sstat = 5", null); //order by AutoIdOutlet Desc
					 
			 }
			 else
			 {
			cursor = db.rawQuery("SELECT DISTINCT StoreID, StoreName FROM tblStoreList WHERE Sstat = 0 and ChainID='"+ChainID+"'", null); //order by AutoIdOutlet Desc
			 }
			
			
			//Cursor cursor = db.rawQuery("SELECT StoreID,StoreName FROM tblStoreList",null);
			try 
			{
				if(cursor.getCount()>0)
				{
					if (cursor.moveToFirst()) 
					{
						
						for (int i = 0; i <= (cursor.getCount() - 1); i++)
						{
							hmapCatgry.put(cursor.getString(1).toString(),cursor.getString(0).toString());
							cursor.moveToNext();
						}
					}
					
				}
				
				else
				{
					//hmapCatgry.put("No Store", "0");
				}
				return hmapCatgry;
			}
			finally
			{
				cursor.close();
				close();
			}
		}*/
		
	  	
	  	/* public void deleteAllSingleCallWebServiceTable() 
		   {
	  		 
	  		
		    open();
		    db.execSQL("DELETE FROM tblDisplayMstr");
		    db.execSQL("DELETE FROM tblChainMstr"); 
		    db.execSQL("DELETE FROM tblStoreList");
		    db.execSQL("DELETE FROM tblStoreDisplayIDPhotoDetail");
		   
		    close(); 
		   }*/
	  	 
	  	 public void deleteModuleData() 
		   {
	  		 open();
			 //   db.execSQL("DELETE FROM tblUserModuleMstr");
			  close();
			   
		   }
	  	public void deleteLoginData() 
		   {
	  		 open();
			    db.execSQL("DELETE FROM tblUserAuthenticationMstr");
			  
			    close();
		   }
	  	 
	  	
	  	 
	  /*	 private static final String DATABASE_CREATE_TABLE_5 = "create table tblDisplayMstr " +
	  	 		"(DisplayLocationID text not null,DisplayLocation text null);";*/
	 	
	  	 
	  	/*public long savetblDisplayMstrr(String DisplayLocationID,String DisplayLocation)
		{
							
							ContentValues initialValues = new ContentValues();
							
							initialValues.put("DisplayLocationID", DisplayLocationID.trim()); 
							initialValues.put("DisplayLocation", DisplayLocation.trim()); 
							
							
							return db.insert(DATABASE_TABLE_MAIN5_DisplayMstr, null, initialValues);	
		}
	  	
	  	// private static final String DATABASE_CREATE_TABLE_3 = "create table tblChainMstr " +
	 	//	"(ChainID text not null,ChainDescr text null,ShowSequence int null,ChainOrdr int null);";
	 
public long savetblChainMstr(String ChainID, String ChainName) 
	{
		
		ContentValues initialValues = new ContentValues();
		initialValues.put("ChainID", ChainID.trim());
		initialValues.put("ChainName", ChainName.trim());
		//initialValues.put("ShowSequence", ShowSequence);
		//initialValues.put("ChainOrdr", ChainOrdr);
		
		return db.insert(DATABASE_TABLE_MAIN3_ChainMstr, null, initialValues);
	}*/
	
	  	
	  	
	  	/* private static final String DATABASE_CREATE_TABLE_6 = "create table tblStoreDisplayIDPhotoDetail" +
	  	 		" (RouteID text null,StoreID text null,DisplayID text null,ClickedDateTime text null,PhotoName text null," +
	  	 		"SelectedLocation text null,PhotoValidation text null,PDAPhotoPath text null,Sstat integer null);";*/
	 	
	  	
	  	 /*public long insertPhotoDetail(String storeId,String DisplayID,String clickedDate,String photoName,String SelectedLocationID,
	  			 String SelectedLocation,String photoValidation,String pdaPhotoPath,int Sstat)
	  		 {
	  			
	  			 open();
	  		   ContentValues initialValues = new ContentValues();
	  		   // StoreID ,ProductID ,ClickedDateTime ,PhotoName ,PhotoValidation ,PDAPhotoPath ,outstat 
	  		   initialValues.put("StoreID", storeId.trim()); 
	  		   initialValues.put("DisplayID", DisplayID.trim());
	  		   initialValues.put("ClickedDateTime", clickedDate.trim());
	  		   initialValues.put("PhotoName", photoName.trim()); 
	  		   initialValues.put("SelectedLocationID", SelectedLocationID.trim());
	  		   initialValues.put("SelectedLocation", SelectedLocation.trim()); 
	  		  
	  		   initialValues.put("PhotoValidation", photoValidation.trim());
	  		   initialValues.put("PDAPhotoPath", pdaPhotoPath.trim());
	  		   initialValues.put("Sstat", Sstat);
	  		   
	  		  long inserted=db.insert(DATABASE_TABLE_Main6, null, initialValues); 
	  		  close();
	  		   return inserted;
	  		 }*/
	  	
	  	
	  	/* public HashMap<String, String> getProductPicInfoMaterial(File[] fileImageName,String storeId)
		 {
			 HashMap<String, String> pathForPhotoInfo=new HashMap<String, String>();
			 open();
			 for(int position=0;position<fileImageName.length;position++)
			 {
				 Cursor cur=db.rawQuery("Select PhotoName,DisplayID from tblStoreDisplayIDPhotoDetail where PhotoName = '"+fileImageName[position].getName().toString()+"' and StoreID = '"+storeId+"'", null);
				 if(cur.getCount()>0)
				 {
					 if(cur.moveToFirst())
					 {
						 pathForPhotoInfo.put(cur.getString(0), cur.getString(1));
						 
					 }
					
				 } 
			 }
			
			 close();
			 return pathForPhotoInfo;
		 }*/


	  	
	  	public GskDBAdapter open() throws SQLException 
	  	{
	  	  db = DBHelper.getWritableDatabase();
	  	  return this;
	  	 }
	  	 // ---closes the database---
	  	 public void close()
	  	 {
	  		DBHelper.close();
	  	 }
	  	 
	  	 
	  	 
	  	 
	  	public void dropAvailbUpdatedVersionTBL() 
		{
	  		open();
	       db.execSQL("DROP TABLE IF EXISTS tblAvailableVersionMstr");
	       close();
			
		}
		
		public void createAvailbUpdatedVersionTBL()
		{open();
			try
			  {
	           db.execSQL(DATABASE_CREATE_TABLE_1);
			  }
			catch (Exception e)
			  {
				
			  }
			finally
	 		{
	 		close();	
	 		}


		}
		
		public long savetblUserAuthenticationMstr(String flgUserAuthenticated)
		{
							
							ContentValues initialValues = new ContentValues();
							
							initialValues.put("flgUserAuthenticated", flgUserAuthenticated.trim()); 
							
							
							return db.insert(DATABASE_TABLE_MAIN2_UserAuth, null, initialValues);	
		}
		
		
		public long savetblAvailbUpdatedVersion(String VersionID, String VersionSerialNo,String VersionDownloadStatus,String ServerDate)
		{
							
							ContentValues initialValues = new ContentValues();
							
							initialValues.put("VersionID", VersionID.trim()); 
							initialValues.put("VersionSerialNo", VersionSerialNo.trim());
							initialValues.put("VersionDownloadStatus", VersionDownloadStatus.trim());
							initialValues.put("ServerDate", ServerDate.trim());
							
							return db.insert(DATABASE_TABLE_MAIN1_Version, null, initialValues);	
		}
		
		
		/* public void Delete_tblChainMstr()
		 {
			 db.execSQL("DELETE FROM tblChainMstr");
		 }*/
	  	 
		 
		 public int FetchflgUserAuthenticated()
			{
			 open();
				int SnamecolumnIndex1 = 0;
				int CatId=0;

				Cursor cursor = db.rawQuery("SELECT flgUserAuthenticated from tblUserAuthenticationMstr", null);
				try {
					
					if (cursor.moveToFirst())
					{
						
						for (int i = 0; i <= (cursor.getCount() - 1); i++) 
						{
							
							String abc =(String) cursor.getString(SnamecolumnIndex1).toString();
							CatId=Integer.parseInt(abc);
							cursor.moveToNext();
						}

					}
					return CatId;
				} finally {
					close();
				}
				
			}
		 
			public long savetblUserLoginMstr(String UserID,String _loginID,String _nodeID,String _nodeType,String _roleId,String _userFullName,String _regionID,String _roleForSurvey,String _emailID,String _flgIndMangerAvail,String _mobNo)
			{					//UserId, LoginResult ,LoginID, NodeID, NodeType, RoleId, UserFullName, RegionID, RoleForSurvey, EmailID, flgIndMangerAvail ,MobNo
								ContentValues initialValues = new ContentValues();
								initialValues.put("UserId", UserID.trim()); 
								initialValues.put("LoginID", _loginID.trim()); 
								initialValues.put("NodeID", _nodeID.trim());
								initialValues.put("NodeType", _nodeType);
								initialValues.put("RoleId", _roleId);
								initialValues.put("UserFullName", _userFullName);
								initialValues.put("RegionID", _regionID);
								initialValues.put("RoleForSurvey", _roleForSurvey);
								initialValues.put("EmailID", _emailID);
								initialValues.put("flgIndMangerAvail", _flgIndMangerAvail);
								initialValues.put("MobNo", _mobNo);
								initialValues.put("Sstat", 3);
							System.out.println("GSK LoginCredentials = "+UserID+","+_loginID+","+_nodeID+","+_nodeType+","+_roleId+","+_userFullName+","+_regionID+","+_roleForSurvey+","+_emailID+","+_flgIndMangerAvail+","+_mobNo);
								return db.insert(TABLE_UserLoginMstr, null, initialValues);	
			}
			
			public long savetblUserModuleMstr(String lPCourseGroupMainID, String lPCourseGroupName, String lPCourseID, String lPCourseName,String lPCourseDescription,String moduleImgName,String moduleImgUrl,String status,String dayVal,String flgNewOld,String lpEnglishCoursePath,
	            	String lpHIndiCoursePath,
	            	String lpMalyalamCoursePath,
	            	String lpTamilCoursePath,
	            	String lpKannadaCoursePath,
	            	String lpMarathiCoursePath,
	            	String lpGujratiCoursePath,
	            	String lpBangaliCoursePath,
	            	String lpAssamiesCoursePath,
	            	String lpTeleguCoursePath,
	            	String lpOdiyaCoursePath,String PDAEnglishCoursePath,String PDAHIndiCoursePath,
					String PDAMalyalamCoursePath,String PDATamilCoursePath,String PDAKannadaCoursePath,String PDAMarathiCoursePath,
					String PDAGujratiCoursePath,String PDABangaliCoursePath,String PDAAssamiesCoursePath,String PDATeleguCoursePath,String PDAOdiyaCoursePath,String flgPDAOnline,String LPCourseCatID)
			{				
				Cursor cur=db.rawQuery("Select LPCourseID from tblUserModuleMstr where LPCourseID='"+lPCourseID+"'", null);
				
				if(cur.getCount()>0)
				{
					return 0;
				}
				else
				{

					ContentValues initialValues = new ContentValues();
					
					initialValues.put("LPCourseGroupMainID", lPCourseGroupMainID.trim()); 
					initialValues.put("LPCourseGroupName", lPCourseGroupName.trim()); 
					initialValues.put("LPCourseID", lPCourseID.trim());
					initialValues.put("LPCourseName", lPCourseName);
					initialValues.put("LPCourseDescription",lPCourseDescription);
					initialValues.put("ModuleImgName", moduleImgName.trim());
					initialValues.put("ModuleImgUrl", moduleImgUrl);
					initialValues.put("Status", status);
					initialValues.put("DayVal", dayVal);
					initialValues.put("flgNewOld", flgNewOld);
					
					
					initialValues.put("PDAEnglishCoursePath", lpEnglishCoursePath);
					initialValues.put("PDAHIndiCoursePath", lpHIndiCoursePath);
					initialValues.put("PDAMalyalamCoursePath", lpMalyalamCoursePath);
					initialValues.put("PDATamilCoursePath", lpTamilCoursePath);
					initialValues.put("PDAKannadaCoursePath", lpKannadaCoursePath);
					initialValues.put("PDAMarathiCoursePath", lpMarathiCoursePath);
					initialValues.put("PDAGujratiCoursePath", lpGujratiCoursePath);
					initialValues.put("PDABangaliCoursePath", lpBangaliCoursePath);
					initialValues.put("PDAAssamiesCoursePath", lpAssamiesCoursePath);
					initialValues.put("PDATeleguCoursePath", lpTeleguCoursePath);
					initialValues.put("PDAOdiyaCoursePath", lpOdiyaCoursePath);
					initialValues.put("flgPDAOnline", flgPDAOnline);
					initialValues.put("LPCourseCatID", LPCourseCatID);
					//LPCourseID
					savetblSmallIconMstr(lPCourseID.trim(), moduleImgName.trim());
					/*if(Integer.parseInt(flgPDAOnline)==0)
					{*/
						LinkedHashMap<String, String> hmapLanguageIDColumnNameMap123=new LinkedHashMap<String, String>();
						if(!PDAEnglishCoursePath.equals("0"))
						{
							hmapLanguageIDColumnNameMap123.put(PDAEnglishCoursePath, "1");
						}
						if(!PDAHIndiCoursePath.equals("0"))
						{
							hmapLanguageIDColumnNameMap123.put(PDAHIndiCoursePath, "2");
						}
						if(!PDATeleguCoursePath.equals("0"))
						{
							hmapLanguageIDColumnNameMap123.put(PDATeleguCoursePath, "3");
						}
						if(!PDATamilCoursePath.equals("0"))
						{
							hmapLanguageIDColumnNameMap123.put(PDATamilCoursePath, "4");
						}
						if(!PDAMalyalamCoursePath.equals("0"))
						{
							hmapLanguageIDColumnNameMap123.put(PDAMalyalamCoursePath, "5");
						}
						if(!PDAKannadaCoursePath.equals("0"))
						{
							hmapLanguageIDColumnNameMap123.put(PDAKannadaCoursePath, "6");
						}
						if(!PDAMarathiCoursePath.equals("0"))
						{
							hmapLanguageIDColumnNameMap123.put(PDAMarathiCoursePath, "7");
						}
						if(!PDAGujratiCoursePath.equals("0"))
						{
							hmapLanguageIDColumnNameMap123.put(PDAGujratiCoursePath, "8");
						}
						if(!PDABangaliCoursePath.equals("0"))
						{
							hmapLanguageIDColumnNameMap123.put(PDABangaliCoursePath, "9");
						}
						if(!PDAAssamiesCoursePath.equals("0"))
						{
							hmapLanguageIDColumnNameMap123.put(PDAAssamiesCoursePath, "10");
						}
						if(!PDAOdiyaCoursePath.equals("0"))
						{
							hmapLanguageIDColumnNameMap123.put(PDAOdiyaCoursePath, "11");
						}
						
				
					//}
					savetblModuleContent(lPCourseID.trim(), hmapLanguageIDColumnNameMap123,flgPDAOnline);
							
					
					
					
				
					return db.insert(TABLE_UserModuleMstr, null, initialValues);
				}	
			}
			
			
			public long savetblModuleWiseSlideDetails(String lpCourceMainID, String lPCourseName, String slideNo,
					String slideName,String flgLastSlide,String LanguageID)
			{					//UserId, LoginResult ,LoginID, NodeID, NodeType, RoleId, UserFullName, RegionID, RoleForSurvey, EmailID, flgIndMangerAvail ,MobNo
				Cursor cur=db.rawQuery("Select LPCourceMainID from tblModuleWiseSlideDetails where LPCourceMainID='"+lpCourceMainID+"' And SlideName='"+slideName+"'", null);
				//TABLE_ModuleWiseSlideDetails ="tblModuleWiseSlideDetails"
				if(cur.getCount()>0)
				{
					return 0;
				}
				else
				{

					ContentValues initialValues = new ContentValues();
					
					initialValues.put("LPCourceMainID", lpCourceMainID.trim()); 
					
					initialValues.put("LPCourseName", lPCourseName);
					initialValues.put("SlideNo",Integer.valueOf(slideNo));
					initialValues.put("SlideName", slideName.trim());
					
					initialValues.put("flgLastSlide", flgLastSlide.trim());
					initialValues.put("LanguageID", LanguageID.trim());
				//System.out.println("GSK ModuleCredentials = "+lPCourseGroupMainID+","+lPCourseGroupName+","+lPCourseID+","+lPCourseName+","+lPCourseDescription+","+moduleImgName+","+moduleImgUrl+","+status+","+dayVal+","+flgNewOld);
					return db.insert(TABLE_ModuleWiseSlideDetails, null, initialValues);
				}	
			}
			
			public long savetblSmallIconMstr(String lPCourseID,String moduleImgName)
			{				
				Cursor cur=db.rawQuery("Select LPCourseID from tblSmallIconMstr where LPCourseID='"+lPCourseID+"'and ModuleImgName='"+moduleImgName+"'", null);
				if(cur.getCount()>0)
				{
					return 0;
				}
				else
				{
					ContentValues initialValues = new ContentValues();
					
					initialValues.put("LPCourseID", lPCourseID.trim());
				
					initialValues.put("ModuleImgName", moduleImgName);
			
				
			
					return db.insert(TABLE_SmallIconMstr, null, initialValues);	
				}
							
			}
			
			public String fetchModuleDownloaded(String moduleId,String LanguageId)
			{
				String moduleFolderName=null;
				open();
				try
				{
					Cursor cur=db.rawQuery("Select ModuleContentName from tblModuleContent where LPCourseID='"+moduleId+"' and LanguageID='"+LanguageId+"'", null);
					if(cur.getCount()>0)
					{
						if(cur.moveToFirst())
						{
							moduleFolderName=cur.getString(0);
						}
						
						return moduleFolderName;
					}
					else
					{
						
						return moduleFolderName="";
					}	
				} catch(Exception e)
				{
					return moduleFolderName="error";
				}
				finally
				{
					close();
				}
				
			}
			
			public String getLastNameSlide(String moduleId,String LanguageId)
			{

				//tblModuleWiseSlideDetails(LPCourceMainID text null, LPCourseName text null, SlideNo int null,SlideName text null,flgLastSlide);";
				String lastSlideNameId=null;
				open();
				try
				{
					Cursor cur=db.rawQuery("Select SlideName from tblModuleWiseSlideDetails where LPCourceMainID='"+moduleId+"' And flgLastSlide='"+1+"' and LanguageID='"+LanguageId+"'", null);
					if(cur.getCount()>0)
					{
						if(cur.moveToFirst())
						{
							lastSlideNameId=cur.getString(0);
						}
						
						return lastSlideNameId;
					}
					else
					{
						
						return lastSlideNameId="";
					}	
				} catch(Exception e)
				{
					return lastSlideNameId="error";
				}
				finally
				{
					close();
				}
				
			
			}
			
			public LinkedHashMap<String, String> getAllModuleIDName()
			{

					LinkedHashMap<String, String> hmapAllModules=new LinkedHashMap<String, String>();
				//tblUserModuleMstr(LPCourseGroupMainID text null, LPCourseGroupName text null, LPCourseID text null, LPCourseName text null, LPCourseDescription text null,ModuleImgName text null, ModuleImgUrl text null, Status text null,DayVal text null, flgNewOld text null,PDACoursePath text null);";
			
				open();
				try
				{
					//Cursor cur=db.rawQuery("Select LPCourseID,LPCourseName from tblUserModuleMstr", null);
					Cursor cur=db.rawQuery("Select LPCourseID,LPCourseName,LanguageID from tblUserModuleMstr", null);
					if(cur.getCount()>0)
					{
						if(cur.moveToFirst())
						{
							for(int i=0;i<cur.getCount();i++)
							{
								hmapAllModules.put(cur.getString(0)+"_"+cur.getString(2), cur.getString(1));
								cur.moveToNext();
							}
							
						}
						
						return hmapAllModules;
					}
					else
					{
						
						return hmapAllModules;
					}	
				} catch(Exception e)
				{
					return hmapAllModules;
				}
				finally
				{
					close();
				}
				
			
			
			}
			
			public String fetchModuleNameId(String moduleId,String LanguageId)
			{
				//tblModuleWiseSlideDetails(LPCourceMainID text null, LPCourseName text null, SlideNo int null,SlideName text null);";
				String moduleNameId=null;
				open();
				try
				{
					//tblModuleWiseSlideDetails(LPCourceMainID text null, LPCourseName text null, SlideNo int null,SlideName text null,flgLastSlide text null,LanguageID text null);";
					Cursor cur=db.rawQuery("Select LPCourseName from tblModuleWiseSlideDetails where LPCourceMainID='"+moduleId+"' and LanguageID='"+LanguageId+"' limit 1", null);
					if(cur.getCount()>0)
					{
						if(cur.moveToFirst())
						{
							moduleNameId=cur.getString(0);
						}
						
						return moduleNameId;
					}
					else
					{
						
						return moduleNameId="";
					}	
				} catch(Exception e)
				{
					return moduleNameId="error";
				}
				finally
				{
					close();
				}
				
			}
			
			
			public LinkedHashMap<String, String> fetchSlideInf(String moduleId,String LanguageId)
			{
				//tblModuleWiseSlideDetails(LPCourceMainID text null, LPCourseName text null, SlideNo int null,SlideName text null);";
				LinkedHashMap<String, String> hmapModuleSlideName=new LinkedHashMap<String, String>();
				open();
				try
				{
					Cursor cur=db.rawQuery("Select SlideName,SlideNo from tblModuleWiseSlideDetails where LPCourceMainID='"+moduleId+"' and LanguageID='"+LanguageId+"'", null);
					if(cur.getCount()>0)
					{
						if(cur.moveToFirst())
						{
							for(int i=0;i<cur.getCount();i++)
							{
								hmapModuleSlideName.put(cur.getString(0), String.valueOf(cur.getInt(1)));	
							cur.moveToNext();
							}
							
						}
						
						return hmapModuleSlideName;
					}
					else
					{
						
						return hmapModuleSlideName;
					}	
				} catch(Exception e)
				{
					return hmapModuleSlideName;
				}
				finally
				{
					close();
				}
				
			}
			
			/*public LinkedHashMap<String,String> fetchAllProjectName_ID()
			{
				//tblModuleWiseSlideDetails(LPCourceMainID text null, LPCourseName text null, SlideNo int null,SlideName text null);";
				 LinkedHashMap<String,String> listModuleSlideName=new  LinkedHashMap<String,String>();
				open();
				try
				{
					Cursor cur=db.rawQuery("Select Distinct LPCourseName,LPCourceMainID from tblModuleWiseSlideDetails ", null);
					if(cur.getCount()>0)
					{
						if(cur.moveToFirst())
						{
							for(int i=0;i<cur.getCount();i++)
							{
								listModuleSlideName.put(cur.getString(0), cur.getString(1));	
							cur.moveToNext();
							}
							
						}
						
						return listModuleSlideName;
					}
					else
					{
						
						return listModuleSlideName;
					}	
				} catch(Exception e)
				{
					
					return listModuleSlideName;
				}
				finally
				{
					close();
				}
				
			}*/
			
			
			public long savetblModuleContent(String lPCourseID,LinkedHashMap<String, String> hmapLanguageIDColumnNameMap123,String flgPDAOnline)
			{				
				
				for(Map.Entry<String, String> entry:hmapLanguageIDColumnNameMap123.entrySet())
				{
					String keyModuleLink=entry.getKey();
					String valueModuleLanguageID=entry.getValue();
					Cursor cur=db.rawQuery("Select LPCourseID from tblModuleContent where LPCourseID='"+lPCourseID+"'and ModuleContentName='"+keyModuleLink+"' and LanguageID='"+valueModuleLanguageID+"' and flgPDAOnline='"+flgPDAOnline+"'", null);
					if(cur.getCount()>0)
					{
						return 0;
					}
					else
					{
						//LPCourseID text null,ModuleContentName text null,PathModuleContnt text null);";
						//ModuleContentName text null,PathModuleContnt text null
						ContentValues initialValues = new ContentValues();
						
						initialValues.put("LPCourseID", lPCourseID.trim());
					
						initialValues.put("ModuleContentName", keyModuleLink);
						initialValues.put("isContentDownloaded", 0);
						initialValues.put("LanguageID", valueModuleLanguageID);
						initialValues.put("flgPDAOnline", flgPDAOnline);
						//flgPDAOnline
				
						//tblModuleContent(LPCourseID text null,ModuleContentName text null,PathModuleContnt text null,isContentDownloaded integer null,LanguageID text null);";
				
						long num= db.insert(TABLE_ModuleContent, null, initialValues);	
					}
					
				}
				return 0;
				
				
							
			}
			
			
			
			//Use Distinct in cursor
			public LinkedHashMap<String, String> getAllModules()
			{
				LinkedHashMap<String, String> linkedHasmap=new LinkedHashMap<String, String>();
				open();
				Cursor cur=db.rawQuery("Select  tblUserModuleMstr.LPCourseGroupMainID , tblUserModuleMstr.LPCourseGroupName , tblUserModuleMstr.LPCourseID , tblUserModuleMstr.LPCourseName , tblUserModuleMstr.LPCourseDescription ,tblUserModuleMstr.ModuleImgName , tblUserModuleMstr.ModuleImgUrl , tblUserModuleMstr.Status ,tblUserModuleMstr.DayVal , tblUserModuleMstr.flgNewOld,tblSmallIconMstr.PathsmallIcon from tblUserModuleMstr Inner JOIN tblSmallIconMstr ON tblUserModuleMstr.LPCourseID=tblSmallIconMstr.LPCourseID", null);
				if(cur.getCount()>0)
				{
					if (cur.moveToFirst())
					{
						for(int i=0;i<cur.getCount();i++)
						{
							linkedHasmap.put(cur.getString(2), cur.getString(0)+"^"+cur.getString(1)+"^"+cur.getString(2)+"^"+cur.getString(3)+"^"+cur.getString(4)+"^"+cur.getString(5)+"^"+cur.getString(6)+"^"+cur.getString(7)+"^"+cur.getString(8)+"^"+cur.getString(9)+"^"+cur.getString(10));
						cur.moveToNext();
						}
						
					}
				}
				
				close();
				return linkedHasmap;
			}
			//Use Distinct in Cursor
			public LinkedHashMap<String, String> getSingleModulesData(String module_Id)
			{
				LinkedHashMap<String, String> linkedHasmap=new LinkedHashMap<String, String>();
				open();
				Cursor cur=db.rawQuery("Select tblUserModuleMstr.LPCourseGroupMainID , tblUserModuleMstr.LPCourseGroupName , tblUserModuleMstr.LPCourseID , tblUserModuleMstr.LPCourseName , tblUserModuleMstr.LPCourseDescription ,tblUserModuleMstr.ModuleImgName , tblUserModuleMstr.ModuleImgUrl , tblUserModuleMstr.Status ,tblUserModuleMstr.DayVal , tblUserModuleMstr.flgNewOld,tblSmallIconMstr.PathsmallIcon from tblUserModuleMstr Inner JOIN tblSmallIconMstr ON tblUserModuleMstr.LPCourseID=tblSmallIconMstr.LPCourseID where tblUserModuleMstr.LPCourseID ='"+module_Id+"'", null);
				if(cur.getCount()>0)
				{
					if (cur.moveToFirst())
					{
						for(int i=0;i<cur.getCount();i++)
						{
							linkedHasmap.put(cur.getString(2), cur.getString(0)+"^"+cur.getString(1)+"^"+cur.getString(2)+"^"+cur.getString(3)+"^"+cur.getString(4)+"^"+cur.getString(5)+"^"+cur.getString(6)+"^"+cur.getString(7)+"^"+cur.getString(8)+"^"+cur.getString(9)+"^"+cur.getString(10));
						cur.moveToNext();
						}
						
					}
				}
				
				close();
				return linkedHasmap;
			}
			
			
			
			public LinkedHashMap<String, String> returnImageName()
			{
				// tblModuleContent(LPCourseID text null,ModuleContentName text null,PathModuleContnt text null,isContentDownloaded integer null);";
				LinkedHashMap<String, String> dataImage=new LinkedHashMap<String, String>();
				open();
				//ModuleImgName text null, ModuleImgUrl
				Cursor c=db.rawQuery("Select tblUserModuleMstr.ModuleImgName,tblUserModuleMstr.ModuleImgUrl from tblUserModuleMstr inner join tblModuleContent ON tblUserModuleMstr.LPCourseID=tblModuleContent.LPCourseID where tblModuleContent.isContentDownloaded=0",null);
				if(c.getCount()>0)
				{
					if(c.moveToFirst())
					{
						for(int i=0;i<c.getCount();i++)
						{
							dataImage.put(c.getString(0),c.getString(0)+"^"+c.getString(1));
							
						c.moveToNext();
						}
					}	
				}
				
				close();
				return dataImage;
			}
			
			public ArrayList<String> returnZipFileDetails()
			{
				String zipContentData=null;
				ArrayList<String> arrayZipData=new ArrayList<String>();
				////tblModuleContent(LPCourseID text null,ModuleContentName text null,PathModuleContnt text null);";
				
				open();
				Cursor c=db.rawQuery("Select ModuleContentName,PathModuleContnt,LanguageID from tblModuleContent where isContentDownloaded=0 and flgPDAOnline=0",null);
				if(c.moveToFirst())
				{
					for(int i=0;i<c.getCount();i++)
					{
						zipContentData=c.getString(0)+"^"+c.getString(1)+"^"+c.getString(2);
						arrayZipData.add(zipContentData);
					c.moveToNext();
					}
				}
				close();
				return arrayZipData;
			}
			
			public void dsdasdasd()
			{
				
				//tblModuleLanguageMstr (LPCourseID text null, LanguageID text null, RegionID text null);";
				/*LinkedHashMap<String, String> hmapLanguageIDColumnNameMap=new LinkedHashMap<String, String>();
				hmapLanguageIDColumnNameMap.put("1", "PDAEnglishCoursePath");
				hmapLanguageIDColumnNameMap.put("2", "PDAHIndiCoursePath");
				hmapLanguageIDColumnNameMap.put("3", "PDATeleguCoursePath");
				hmapLanguageIDColumnNameMap.put("4", "PDATamilCoursePath");
				hmapLanguageIDColumnNameMap.put("5", "PDAMalyalamCoursePath");
				hmapLanguageIDColumnNameMap.put("6", "PDAKannadaCoursePath");
				hmapLanguageIDColumnNameMap.put("7", "PDAMarathiCoursePath");
				hmapLanguageIDColumnNameMap.put("8", "PDAGujratiCoursePath");
				hmapLanguageIDColumnNameMap.put("9", "PDABangaliCoursePath");
				hmapLanguageIDColumnNameMap.put("10", "PDAAssamiesCoursePath");
				hmapLanguageIDColumnNameMap.put("11", "PDAOdiyaCoursePath");*/
				
				
			}
			
			public LinkedHashMap<String, ArrayList<String>> getModuleLanguage()
			{
				LinkedHashMap<String, ArrayList<String>> hmapListLanguageId=new LinkedHashMap<String, ArrayList<String>>();
				ArrayList<String> listLanguage=new ArrayList<String>();
				try {
					
			//	tblModuleContent(LPCourseID text null,ModuleContentName text null,PathModuleContnt text null,isContentDownloaded integer null,LanguageID text null);";
				Cursor cur=db.rawQuery("Select LPCourseID ,LanguageID,ModuleContentName from tblModuleContent where isContentDownloaded=0 and flgPDAOnline=0 order by LPCourseID ", null);
				if(cur.getCount()>0)
				{
					if(cur.moveToFirst())
					{
						String prvsModuleId = null,currentModuleId,langId,moduleContentName;
						for(int i=0;i<cur.getCount();i++)
						{
							currentModuleId=cur.getString(0);
							langId=cur.getString(1);
							moduleContentName=cur.getString(2);
							if(i==0)
							{
								prvsModuleId=cur.getString(0);
								listLanguage.add(langId+"^"+moduleContentName);
							}
							else 
							{
								if(prvsModuleId.equals(currentModuleId))
								{
									listLanguage.add(langId+"^"+moduleContentName);
								}
								else
								{
									hmapListLanguageId.put(prvsModuleId, listLanguage);
									listLanguage=new ArrayList<String>();
									prvsModuleId=cur.getString(0);
									listLanguage.add(langId+"^"+moduleContentName);
								}
							}
							
							if(i==(cur.getCount()-1))
							{
								hmapListLanguageId.put(prvsModuleId, listLanguage);
							}
							
							cur.moveToNext();
						}
					}
				}	
				} catch (Exception e) {
					// TODO: handle exception
				}
				finally
				{
					return hmapListLanguageId;
				}
				
				
				
			}
			
			//Use LanguageID in Cursor
			public LinkedHashMap<String, String> returnModulContent()//String moduleId1,String module2,String module3
			{//tblModuleContent(LPCourseID text null,ModuleContentName text null,PathModuleContnt text null,isContentDownloaded integer null);";
				LinkedHashMap<String, String> dataImage=new LinkedHashMap<String, String>();
				open();
				
				LinkedHashMap<String, ArrayList<String>> hmapListLanguageId=new LinkedHashMap<String, ArrayList<String>>();
				
				hmapListLanguageId=getModuleLanguage();
				
				LinkedHashMap<String, String> hmapLanguageIDColumnNameMap=new LinkedHashMap<String, String>();
				hmapLanguageIDColumnNameMap.put("1", "PDAEnglishCoursePath");
				hmapLanguageIDColumnNameMap.put("2", "PDAHIndiCoursePath");
				hmapLanguageIDColumnNameMap.put("3", "PDATeleguCoursePath");
				hmapLanguageIDColumnNameMap.put("4", "PDATamilCoursePath");
				hmapLanguageIDColumnNameMap.put("5", "PDAMalyalamCoursePath");
				hmapLanguageIDColumnNameMap.put("6", "PDAKannadaCoursePath");
				hmapLanguageIDColumnNameMap.put("7", "PDAMarathiCoursePath");
				hmapLanguageIDColumnNameMap.put("8", "PDAGujratiCoursePath");
				hmapLanguageIDColumnNameMap.put("9", "PDABangaliCoursePath");
				hmapLanguageIDColumnNameMap.put("10", "PDAAssamiesCoursePath");
				hmapLanguageIDColumnNameMap.put("11", "PDAOdiyaCoursePath");
				
				for(Map.Entry<String, ArrayList<String>> entrySetVal:hmapListLanguageId.entrySet())
				{
					String myMDID=entrySetVal.getKey();
					ArrayList<String> arrmyMDNameLangID=entrySetVal.getValue();
					String ColumnsNameToRead="";
					for(String ModuleNameLanID:arrmyMDNameLangID)
					{
						String LangID=ModuleNameLanID.split(Pattern.quote("^"))[0];
						if(hmapLanguageIDColumnNameMap.containsKey(LangID))
						{
							if(ColumnsNameToRead.equals(""))
							{
								ColumnsNameToRead="tblUserModuleMstr."+hmapLanguageIDColumnNameMap.get(LangID);
								
							}
							else
							{
								ColumnsNameToRead= ColumnsNameToRead+"," +"tblUserModuleMstr."+hmapLanguageIDColumnNameMap.get(LangID);
							}
						}
					}
					if(!ColumnsNameToRead.equals("") && !TextUtils.isEmpty(ColumnsNameToRead))
					{
					Cursor c=db.rawQuery("Select "+ColumnsNameToRead+" from tblUserModuleMstr where LPCourseID='"+myMDID+"'",null);
					if(c.getCount()>0)
					{
						if(c.moveToFirst())
						{
							for(int j=0;j<c.getColumnCount();j++)
								{
									String modulNameLngId=arrmyMDNameLangID.get(j);
									String LangID=modulNameLngId.split(Pattern.quote("^"))[0];
									String cntntName=modulNameLngId.split(Pattern.quote("^"))[1];
									dataImage.put(myMDID+"^"+LangID,cntntName+"^"+c.getString(c.getColumnIndex(c.getColumnName(j))));
								}
							//c.moveToNext();
						}	
					}
					}
				}
				
				
				/*
				//LPCourseID
				//Cursor c=db.rawQuery("Select tblUserModuleMstr.PDACoursePath,tblModuleContent.ModuleContentName from tblUserModuleMstr inner join tblModuleContent on tblUserModuleMstr.LPCourseID=tblModuleContent.LPCourseID where tblUserModuleMstr.LPCourseID= '"+moduleId1+"' OR tblUserModuleMstr.LPCourseID= '"+module2+"' OR tblUserModuleMstr.LPCourseID= '"+module3+"'",null);
				
				//"Select LPCourseID from tblModuleContent where LPCourseID='"+lPCourseID+"'and ModuleContentName='"+keyModuleLink+"' and LanguageID='"+valueModuleLanguageID+"'", null);
				
				//tblModuleContent(LPCourseID text null,ModuleContentName text null,PathModuleContnt text null,isContentDownloaded integer null,LanguageID text null);";
				//Cursor c=db.rawQuery("Select PathModuleContnt,ModuleContentName,LanguageID from tblModuleContent where isContentDownloaded=0",null);
				Cursor c=db.rawQuery("Select PathModuleContnt,tblModuleContent.ModuleContentName,tblModuleContent.LanguageID from tblModuleContent where isContentDownloaded=0",null);
				if(c.getCount()>0)
				{
					if(c.moveToFirst())
					{
						for(int i=0;i<c.getCount();i++)
						{
							dataImage.put(c.getString(1),c.getString(1)+"^"+c.getString(0));
							
						c.moveToNext();
						}
					}	
				}
				*/
				close();
				return dataImage;
			}
			
			
		 public int FetchVersionDownloadStatus()
			{
			 
			 open();
				int SnamecolumnIndex1 = 0;
				int CatId=0;

				Cursor cursor = db.rawQuery("SELECT VersionDownloadStatus from tblAvailableVersionMstr", null);
				try {
					//String OldDateInfo[] = new String[cursor.getCount() ];
					if (cursor.moveToFirst())
					{
						
						for (int i = 0; i <= (cursor.getCount() - 1); i++) 
						{
							//CatId = cursor.getString(SnamecolumnIndex1).toString();
							String abc =(String) cursor.getString(SnamecolumnIndex1).toString();
							CatId=Integer.parseInt(abc);
							cursor.moveToNext();
						}

					}
					return CatId;
				} finally {
					close();
				}
				
			}
	  	 
	  	 
	  	
	   public void droptblUserAuthenticationMstrTBL() 
	 	{
		 open();
	        db.execSQL("DROP TABLE IF EXISTS DATABASE_CREATE_TABLE_2");
	 		close();
	 	}
	 	
	 	public void createtblUserAuthenticationMstrTBL()
	 	{
	 		open();
	 		try
	 		  {
	            db.execSQL(DATABASE_TABLE_MAIN2_UserAuth);
	 		  }
	 		catch (Exception e)
	 		  {
	 			
	 		  }
	 		finally
	 		{
	 		close();	
	 		}

	 	}
	 	
	 	public void deletetblUserLoginMstr()
		{
			open();
			db.execSQL("DELETE FROM tblUserLoginMstr"); 
			close();
	    }
		
	  	 
	  	 
	   public void Delete_tblStoreList()
		  {
		   db.execSQL("DELETE FROM tblStoreList");
		  }
	  	 
	   
	   
	   public void UpdateStoreEndVisit(String StoreID, String VisitEndTS)
		 { open();
				final ContentValues values = new ContentValues();
				
				values.put("VisitEndTS", VisitEndTS);

				int affected = db.update("tblStoreList", values,"StoreID=? and VisitStartTS is not null",
						new String[] { StoreID });
				 close();
				
			}
		 
	   
	/*   private static final String DATABASE_CREATE_TABLE_4 = "create table tblStoreList (StoreID text not null, StoreType string not null," +
		 		" StoreName string not null, StoreLatitude real not null, StoreLongitude real not null, LastVisitDate string not null," +
		 		" Sstat integer not null, ActualLatitude text null, ActualLongitude text null, VisitStartTS text null, VisitEndTS text null," +
		 		"AutoIdStore int null, LocProvider text null, Accuracy text null, BateryLeftStatus text null," +
		 		"ChainID text null,DisplayTarget text null,ActualDisplayPlace text null,AppVersion int null);";*/
	   
	  /* public void inserttblOutletMstr(String StoreID,String VisitStartTS,String ActualLatitude,
				 String ActualLongitude,String Accuracy,String LocProvider,String BateryLeftStatus,String StoreName,
				 String imei,int ISNewStore,int Sstat,int AppVersion)
			 {

		   
		   
             open();
             
            // final ContentValues values = new ContentValues();
				
				//values.put("VisitEndTS", VisitEndTS);

				
			   ContentValues initialValues = new ContentValues();
			  
			   initialValues.put("StoreID", StoreID.trim()); 
			   initialValues.put("VisitStartTS", VisitStartTS.trim());
			  
			   initialValues.put("ActualLatitude", ActualLatitude.trim()); 
			   initialValues.put("ActualLongitude", ActualLongitude.trim()); 
			  
			  
			   initialValues.put("Accuracy", Accuracy.trim());
			   initialValues.put("LocProvider", LocProvider.trim());
			   initialValues.put("BateryLeftStatus", BateryLeftStatus.trim());
			  // initialValues.put("StoreName", StoreName.trim());
			   initialValues.put("imei", imei.trim());
			   
			  
			   initialValues.put("Sstat", Sstat);
			   initialValues.put("AppVersion", AppVersion);
			   
			   System.out.println("Data insert in OutletMstr");
			   
			   int affected = db.update("tblStoreList", initialValues,"StoreID=?",
						new String[] { StoreID });
            
			   
			  //long inserted=db.insert(DATABASE_TABLE_MAIN4_StoreMstr, null, initialValues); 
			  close();
			 
			 }*/
		 
	   
	  /* public long savetblStoreListMstr(String StoreID, String StoreName, String StoreType,
			   String StoreLatitude, String StoreLongitude, String LastVisitDate,
				 int AutoIdStore, int Sstat,String ChainID,String DisplayTarget,String ActualDisplayPlace,String NoOfCheckOutCounters)
		{
			
			ContentValues initialValues = new ContentValues();

			initialValues.put("StoreID", StoreID.trim());
			initialValues.put("StoreType", StoreType.trim());
			initialValues.put("StoreName", StoreName.trim());
			
			initialValues.put("StoreLatitude", StoreLatitude);
			initialValues.put("StoreLongitude", StoreLongitude);
			initialValues.put("LastVisitDate", LastVisitDate.trim());
			initialValues.put("Sstat", Sstat);
			
			initialValues.put("ActualLatitude", "0");
			initialValues.put("ActualLongitude", "0");
			initialValues.put("VisitStartTS", "0");
			initialValues.put("VisitEndTS", "0");
			initialValues.put("AutoIdStore", AutoIdStore);
			initialValues.put("LocProvider", "0");
			initialValues.put("Accuracy", "0");
			initialValues.put("BateryLeftStatus", "0");
			initialValues.put("AppVersion", "0");
			
			initialValues.put("ChainID", ChainID.trim());
			initialValues.put("DisplayTarget", DisplayTarget.trim());
			initialValues.put("ActualDisplayPlace", ActualDisplayPlace);
			initialValues.put("NoOfCheckOutCounters", NoOfCheckOutCounters);
			
			
			
			 
				
			
			////System.out.println("inserting records in StoreList table..");

			return db.insert(DATABASE_TABLE_MAIN4_StoreMstr, null, initialValues);
		}
		
	  	 
	  	 */
	  	 
	  	 
	  	 
	  /* public void updatexmlSyncdData() 
		{
		 try 
			{

            final ContentValues values = new ContentValues();
				values.put("Sstat", "4");
				int affected = db.update("tblOutletMstr", values, "Sstat=?",
						new String[] { "3" });
				int affected2 = db.update("tblOutletQuestAnsMstr", values, "Sstat=?",
						new String[] { "3" });
				
			} 
			catch (Exception ex) 
			{
				
			}

		}
	 */
	   
	   
	  /* private static final String DATABASE_CREATE_TABLE_6 = "create table tblStoreDisplayIDPhotoDetail" +
	   		" (RouteID text null,StoreID text null,DisplayID text null,ClickedDateTime text null,PhotoName text null," +
	   		"SelectedLocation text null,PhotoValidation text null,PDAPhotoPath text null,Sstat integer null);";*/
		
	   
	   public String[] getStoreIDTblSelectedStoreIDinChangeRouteCase() 
	   {

			int SnamecolumnIndex1 = 0;

				Cursor cursor = db.rawQuery("SELECT DISTINCT(StoreID) FROM tblStoreDisplayIDPhotoDetail where Sstat=3", null);
			try {

				String StoreName[] = new String[cursor.getCount()];

				if (cursor.moveToFirst()) {

					for (int i = 0; i <= (cursor.getCount() - 1); i++) {

						StoreName[i] = (String) cursor.getString(SnamecolumnIndex1).toString();
						System.out.println("STORE ID 4 Pic: "+cursor.getString(SnamecolumnIndex1).toString());

						cursor.moveToNext();
					}
				}
				return StoreName;
			} finally {
				cursor.close();
			}

		}
	   
	   
	   /* private static final String DATABASE_CREATE_TABLE_6 = "create table tblStoreDisplayIDPhotoDetail" +
  		" (RouteID text null,StoreID text null,DisplayID text null,ClickedDateTime text null,PhotoName text null," +
  		"SelectedLocation text null,PhotoValidation text null,PDAPhotoPath text null,Sstat integer null);";*/
	
	   
	   public int getExistingPicNos(String OutId) {

			int ScodecolumnIndex = 0;
        open();
			Cursor cursor = db.rawQuery("SELECT Count(StoreID) FROM tblStoreDisplayIDPhotoDetail where StoreID='" + OutId + "' and Sstat=3", null);
			try {
				
				int strProdStockQty = 0;
				if (cursor.moveToFirst()) {

					for (int i = 0; i <= (cursor.getCount() - 1); i++) 
					{
						if (!cursor.isNull(ScodecolumnIndex))
						{
							strProdStockQty = Integer.parseInt(cursor.getString(ScodecolumnIndex).toString());
							cursor.moveToNext();
						}

					}
				}
				return strProdStockQty;
			} finally {
				cursor.close();
				 close(); 
			}
		}
	 
	   
	   /* private static final String DATABASE_CREATE_TABLE_6 = "create table tblStoreDisplayIDPhotoDetail" +
 		" (RouteID text null,StoreID text null,DisplayID text null,ClickedDateTime text null,PhotoName text null," +
 		"SelectedLocation text null,PhotoValidation text null,PDAPhotoPath text null,Sstat integer null);";*/
	
	   
	 public String[] getImgsPath(String outID)
	 {

			int SnamecolumnIndex1 = 0;
         open();
			Cursor cursor = db.rawQuery("SELECT PhotoName FROM tblStoreDisplayIDPhotoDetail WHERE StoreID ='"+ outID + "' and Sstat=3", null);
			try {

				String StoreName[] = new String[cursor.getCount()];
				
				if (cursor.moveToFirst()) {

					for (int i = 0; i <= (cursor.getCount() - 1); i++) {

						StoreName[i] = (String) cursor.getString(SnamecolumnIndex1)
								.toString();

						cursor.moveToNext();
					}
				}
				return StoreName;
			} finally {
				cursor.close();
				close();
			}

		}
	 
	 public void updatePhotoSyncdData() 
		{
		 try 
			{

       final ContentValues values = new ContentValues();
				values.put("Sstat", "4");
				int affected3 = db.update("tblOutletPhotoDetail", values, "Sstat=?",
						new String[] { "3" });
				
			} 
			catch (Exception ex) 
			{
				
			}

		}
	 
	 
	 public String[] deletFromSDcCardPhotoValidationBasedSstat(String Sstat) 
	 {

		 String[] imageNameToBeDeleted = null;
		 open();
		
		 Cursor cursor = db.rawQuery("SELECT  PhotoName from tblStoreDisplayIDPhotoDetail where Sstat='"+Sstat+"'", null);
		 try{
				if(cursor.getCount()>0)
				{
					imageNameToBeDeleted=new String[cursor.getCount()];
					if(cursor.moveToFirst())
					{
						for(int i=0;i<cursor.getCount();i++)
						{
							imageNameToBeDeleted[i]=cursor.getString(0);
							cursor.moveToNext();
						}
					}
				}
				else
				{
					imageNameToBeDeleted=new String[1];
					imageNameToBeDeleted[0]="No Data";
				}
		 }finally
		 {
			 cursor.close();
			 close(); 
		 }

			
		//	Log.w(TAG, "affected records: " + affected);

			//Log.w(TAG, "UpdateStoreActualLatLongi added..");
		 return imageNameToBeDeleted;
		}
	 
	 
	/* public void deleteAllSubmitDataToServer(int Sstat) 
     {
      open();
      
      db.execSQL("DELETE FROM tblOutletMstr WHERE Sstat ="+ Sstat);
      db.execSQL("DELETE FROM tblOutletQuestAnsMstr WHERE Sstat ="+ Sstat);
      db.execSQL("DELETE FROM tblOutletPhotoDetail WHERE Sstat ="+ Sstat);
     
   
      
      close(); 
     }
  	 */
	 
	/* private static final String DATABASE_CREATE_TABLE_UserLoginMstr = "create table tblUserLoginMstr" +
	 		"(UserId text null, LoginResult text null,LoginID text null, NodeID text null, NodeType text null, " +
	 		"RoleId text null, UserFullName text null, RegionID text null, RoleForSurvey text null," +
	 		" EmailID text null, flgIndMangerAvail text null,MobNo text null,Sstat integer null);";*/
		
	  	 
	 public String fnGetUserNameContact()
	    {
	    	String UserNameandContact="NA";
	    	
			 open();
	//OutletID,StoreCode,OutletDescr,OutletClassID,RetalierName,RetailerNo,HQTownID,TotSellinPeriodID,TotFacing,SellinValue,ZoneDisplayID,OutletNew, ActualLatitude, ActualLongitude, VisitStartTS, VisitEndTS, LocProvider, Accuracy, BateryLeftStatus,Sstat		 
			 Cursor cursor=db.rawQuery("Select UserFullName,MobNo,LoginID,UserId from tblUserLoginMstr", null);
			 
			 if(cursor.getCount()>0)
			 {
				if(cursor.moveToFirst()) 
				{
					UserNameandContact=cursor.getString(0)+"^"+cursor.getString(1)+"^"+cursor.getString(2)+"^"+cursor.getString(3);
				}
			 }
			 close();
			 return UserNameandContact;
	    } 
	  	
	/*  
	 private static final String DATABASE_CREATE_TABLE_4 = "create table tblStoreList (StoreID text not null, StoreType text null," +
		 		" StoreName string not null, StoreLatitude text  null, StoreLongitude text  null, LastVisitDate text  null," +
		 		" Sstat integer not null, ActualLatitude text null, ActualLongitude text null, VisitStartTS text null, VisitEndTS text null," +
		 		"AutoIdStore int null, LocProvider text null, Accuracy text null, BateryLeftStatus text null," +
		 		"ChainID text null,DisplayTarget text null,ActualDisplayPlace text null,AppVersion int null,imei text null,NoOfCheckOutCounters text null);";
			
	  	
	  */	 
	  	 
	/*	public String[] fnfetchStoreNameAndStoreID(int Sstat)
	 	{

            int ScodecolumnIndex = 0;
			Cursor cursor = db.rawQuery("SELECT StoreID,StoreName,ChainID FROM tblStoreList where Sstat='"+ Sstat+"'", null);
			try
			{
				String ProductComboIds[] = new String[cursor.getCount()];
				
				if (cursor.moveToFirst())
				{
                    for (int i = 0; i <= (cursor.getCount() - 1); i++) 
                    {
						ProductComboIds[i] = (String) cursor.getString(0).toString()+"^"+(String) cursor.getString(1).toString()+"^"+(String) cursor.getString(2).toString();
						cursor.moveToNext();
						System.out.println("fnGetOtherProductIdOfCombo : Product  Id  that Returns"+ ProductComboIds[i].trim());
					}
				}
				return ProductComboIds;
			}
			finally 
			{
				cursor.close();
			}
		}  
		*/
		
		/* private static final String DATABASE_CREATE_TABLE_6 = "create table tblStoreDisplayIDPhotoDetail " +
		 		"(ChainID text null,StoreID text null,DisplayID text null,ClickedDateTime text null,PhotoName text null," +
		 		"SelectedLocationID text null,SelectedLocation text null,PhotoValidation text null,PDAPhotoPath text null," +
		 		"Sstat integer null);";*/
			
	  	  
	  	  
		/*public String[] fnfetchAllStoreinfoBasedOnStoreId(String StoreID)
	 	{

            int ScodecolumnIndex = 0;
            open();
            
        	//Cursor cursor = db.rawQuery("SELECT tblStoreList.ChainID,tblChainMstr.ChainName FROM tblStoreList inner join tblChainMstr on tblChainMstr.ChainID=tblStoreList.ChainID",null);
			
           // Cursor cursor = db.rawQuery("SELECT tblStoreList.NoOfCheckOutCounters,tblStoreList.DisplayTarget,tblStoreList.ActualDisplayPlace,(SELECT COUNT(tblStoreDisplayIDPhotoDetail.StoreID)) FROM tblStoreList inner join tblStoreDisplayIDPhotoDetail on tblStoreList.StoreID=tblStoreDisplayIDPhotoDetail.StoreID", null);
			
			//Cursor cursor = db.rawQuery("SELECT NoOfCheckOutCounters,DisplayTarget,ActualDisplayPlace,AppVersion FROM tblStoreList where StoreID='"+ StoreID+"'", null);
			
            
            Cursor cursor = db.rawQuery("SELECT tblStoreList.NoOfCheckOutCounters,tblStoreList.DisplayTarget,tblStoreList.ActualDisplayPlace,(SELECT COUNT(tblStoreDisplayIDPhotoDetail.PhotoName) from tblStoreDisplayIDPhotoDetail where tblStoreDisplayIDPhotoDetail.StoreID=tblStoreList.StoreID) as photocnt FROM tblStoreList where StoreID='"+ StoreID+"'", null);
			
            
            try
			{
				String[] ProductComboIds = new String[4];
				
				if (cursor.moveToFirst())
				{
                    for (int i = 0; i < (cursor.getCount()); i++) 
                    {
                    	if(i==0)
                    	{
                    		ProductComboIds[i] = "Total Checkout Counters"+"^"+(String) cursor.getString(0).toString();//+"^"+(String) cursor.getString(1).toString()+"^"+(String) cursor.getString(2).toString();
                    		ProductComboIds[i+1] = "Total Target Display"+"^"+(String) cursor.getString(1).toString();
                    		ProductComboIds[i+2] = "Total Display Placed"+"^"+(String) cursor.getString(2).toString();
                    		ProductComboIds[i+3] = "Total Display Photo Added"+"^"+(String) cursor.getString(3).toString();
                        	
                    	}
                    	else if(i==0)
                    	{
                    		//Total Checkout Counters
                    		//Total Target Display
                    		//Total Display Placed
                    		//Total Display Photo Added
                    		ProductComboIds[i] = "Total Target Display"+"^"+(String) cursor.getString(0).toString()+"^"+(String) cursor.getString(1).toString()+"^"+(String) cursor.getString(2).toString();
                        	
                    	}
                    	
                    	else
                    	{
						ProductComboIds[i] = (String) cursor.getString(0).toString()+"^"+(String) cursor.getString(1).toString()+"^"+(String) cursor.getString(2).toString()+"^"+(String) cursor.getString(3).toString();
                    	
						cursor.moveToNext();
					    }
                    }
				}
				return ProductComboIds;
			}
			finally 
			{
				cursor.close();
				close();
			}
		}
		*/
		
		/*
		public String fnfetchActualDisplayPlaceBasedOnStoreId(String StoreID)
	 	{

            int ScodecolumnIndex = 0;
            open();
			Cursor cursor = db.rawQuery("SELECT ActualDisplayPlace FROM tblStoreList where StoreID='"+ StoreID+"'", null);
			try
			{
				String ProductComboIds="0";
				
				if (cursor.moveToFirst())
				{
                    for (int i = 0; i < (cursor.getCount()); i++) 
                    {
                    	
                    		ProductComboIds = (String) cursor.getString(0).toString();
                    		
                    	
                    }
				}
				return ProductComboIds;
			}
			finally 
			{
				cursor.close();
				close();
			}
		}
		*/
			public void updatePathsmallIcon(String imagePath,String smallIconName)
			{

		  		open();
				try
				 {

					final ContentValues values = new ContentValues();
					values.put("PathsmallIcon", imagePath);
		           
					int affected1 = db.update(TABLE_SmallIconMstr, values, "ModuleImgName=?",new String[] { smallIconName });
					
					

				 }
				catch (Exception ex)
				{
					
				}
				finally
				{
					close();
				}
					

			
			}
		
			public void updateModuleContent(String contentPath,String contentName,String LanguageId)
			{

		  		open();
				try
				 {
					////tblModuleContent(LPCourseID text null,ModuleContentName text null,PathModuleContnt text null,isContentDownloaded integer null,LanguageID text null);";
					final ContentValues values = new ContentValues();
					values.put("PathModuleContnt", contentPath);
						int affected1 = db.update(TABLE_ModuleContent, values, "ModuleContentName=? and LanguageID=?",new String[] { contentName,LanguageId });
					
					

				 }
				catch (Exception ex)
				{
					
				}
				finally
				{
					close();
				}
					

			
			}
			
			public void updateModuleContentDownloaded(String contentPath,String LanguageId)
			{

		  		open();
				try
				 {
					
					final ContentValues values = new ContentValues();
					
					values.put("isContentDownloaded", 1);
					
		           
					int affected1 = db.update(TABLE_ModuleContent, values, "PathModuleContnt=? and LanguageID=?",new String[] {contentPath,LanguageId});
					
					

				 }
				catch (Exception ex)
				{
					
				}
				finally
				{
					close();
				}
					

			
			}
			public String getimagePath(String imageName)
			{

				String imagePath = null;
				open();
				Cursor cur=db.rawQuery("Select PathsmallIcon from tblSmallIconMstr where ModuleImgName = '"+imageName+"'", null);
				if(cur.getCount()>0)
				{
					if (cur.moveToFirst())
					{
						
							imagePath=cur.getString(0);
						
						
					}
				}
				
				close();
				return imagePath;
			
				
			}
			
			/* private static final String DATABASE_CREATE_TABLE_11 = "create table tblModuleQuestionAnswerOne (TestID text null," +
			 		"TestInstanceID text null,TestInsUserMapID text null,CourseMainID text null,TotalQstn text null," +
			 		"PassingQstn text null,AllocatedtimeInSecPerQstn text null,Descr text null,RSPID text null," +
			 		"PGNmbr text null,AssessmentStatus text null,TimeElapsedInSec text null,QstNo text null,SubmitType text null," +
			 		"NoCorrectAns text null,flgPassStatus text null,StartTime text null,EndTime text null);";
			*/
			
			public long savetblModuleQuestionAnswerOne(int TestID,int TestInstanceID,int TestInsUserMapID,int CourseMainID,
					int TotalQstn,int PassingQstn,int AllocatedtimeInSecPerQstn,String Descr,int RSPID,int PGNmbr,
					int AssessmentStatus,int TimeElapsedInSec,int QstNo,int SubmitType,int NoCorrectAns,
					int flgPassStatus,String StartTime,String EndTime,int flgAssessmentAvailable)
			{
								
								ContentValues initialValues = new ContentValues();
								
								initialValues.put("TestID", TestID); 
								initialValues.put("TestInstanceID", TestInstanceID); 
								initialValues.put("TestInsUserMapID", TestInsUserMapID); 
								initialValues.put("CourseMainID", CourseMainID); 
								initialValues.put("TotalQstn", TotalQstn); 
								initialValues.put("PassingQstn", PassingQstn); 
								initialValues.put("AllocatedtimeInSecPerQstn", AllocatedtimeInSecPerQstn); 
								initialValues.put("Descr", Descr.trim()); 
								initialValues.put("RSPID", RSPID); 
								initialValues.put("PGNmbr", PGNmbr); 
								initialValues.put("AssessmentStatus", AssessmentStatus); 
								initialValues.put("TimeElapsedInSec", TimeElapsedInSec); 
								initialValues.put("QstNo", QstNo); 
								initialValues.put("SubmitType", SubmitType); 
								initialValues.put("NoCorrectAns", NoCorrectAns); 
								initialValues.put("flgPassStatus", flgPassStatus); 
								initialValues.put("StartTime", StartTime.trim()); 
								initialValues.put("EndTime", EndTime.trim()); 
								initialValues.put("flgAssessmentAvailable", flgAssessmentAvailable); 
								
								
								return db.insert(DATABASE_TABLE_Main11, null, initialValues);	
			}
			
	
			//Use Distinct in Cursor
			 public String fnfetchTestIDAndDescrBasedOnCourseMainID(int CourseMainID)
			    {
			    	String UserNameandContact="0^NA";
			    	
					 open();

					 Cursor cursor = db.rawQuery("SELECT TestID,Descr,TestInstanceID,TestInsUserMapID,TotalQstn,PassingQstn,flgAssessmentAvailable FROM tblModuleQuestionAnswerOne where CourseMainID="+CourseMainID, null);
						
					 if(cursor.getCount()>0)
					 {
						if(cursor.moveToFirst()) 
						{
							UserNameandContact=cursor.getString(0)+"^"+cursor.getString(1)+"^"+cursor.getString(2)+"^"+cursor.getString(3)+"^"+cursor.getString(4)+"^"+cursor.getString(5)+"^"+cursor.getString(6);
						}
					 }
					 close();
					 return UserNameandContact;
			    }
			 
			 
			
			
			/*
			 private static final String DATABASE_CREATE_TABLE_12 = "create table tblModuleQuestionAnswerTwo (TestID text null," +
			 		"QstID text null,QstpartTxt text null,SRNmbr text null,QstnTypeID text null);";//, AutoIdOutlet int null
			*/
			
			public long savetblModuleQuestionAnswerTwo(int TestID,int QstID,String QstpartTxt,int SRNmbr,int QstnTypeID)
			{
								
								ContentValues initialValues = new ContentValues();
								
								initialValues.put("TestID", TestID); 
								initialValues.put("QstID", QstID); 
								initialValues.put("QstpartTxt", QstpartTxt.trim()); 
								initialValues.put("SRNmbr", SRNmbr); 
								initialValues.put("QstnTypeID", QstnTypeID); 
								
								return db.insert(DATABASE_TABLE_Main12, null, initialValues);	
			}
			
			
			 public int getTotalNoOfQstID(int TestID) {

					int ScodecolumnIndex = 0;
		        open();
					Cursor cursor = db.rawQuery("SELECT Count(QstID) FROM tblModuleQuestionAnswerTwo where TestID="+TestID, null);
					try {
						
						int strProdStockQty = 0;
						if (cursor.moveToFirst()) {

							for (int i = 0; i <= (cursor.getCount() - 1); i++) 
							{
								if (!cursor.isNull(ScodecolumnIndex))
								{
									strProdStockQty = Integer.parseInt(cursor.getString(ScodecolumnIndex).toString());
									cursor.moveToNext();
								}

							}
						}
						return strProdStockQty;
					} finally {
						cursor.close();
						 close(); 
					}
				}
			 
			
			
			 public int getTotalNoOfQuesution(int TestID) {

					int ScodecolumnIndex = 0;
		        open();
					Cursor cursor = db.rawQuery("SELECT Count(TestID) FROM tblModuleQuestionAnswerTwo where TestID="+TestID, null);
					try {
						
						int strProdStockQty = 0;
						if (cursor.moveToFirst()) {

							for (int i = 0; i <= (cursor.getCount() - 1); i++) 
							{
								if (!cursor.isNull(ScodecolumnIndex))
								{
									strProdStockQty = Integer.parseInt(cursor.getString(ScodecolumnIndex).toString());
									cursor.moveToNext();
								}

							}
						}
						return strProdStockQty;
					} finally {
						cursor.close();
						 close(); 
					}
				}
			 
			
			 
			 
			 public String fnfetchUserFullNameFromtblUserLoginMstr()
			    {
			    	String UserNameandContact="0";
			    	
					 open();
                  try
                  {
                	  
                  
					 Cursor cursor = db.rawQuery("SELECT UserFullName FROM tblUserLoginMstr", null);
						
					 if(cursor.getCount()>0)
					 {
						if(cursor.moveToFirst()) 
						{
							UserNameandContact=cursor.getString(0);
						}
					 }
                  }
                  catch(Exception e)
                  {
                	  
                  }
                  finally
                  {
                	  close();  
                  }
					 
					 return UserNameandContact;
			    }
			 
			 public String fnfetchUserIdFromtblUserLoginMstr()
			    {
			    	String UserNameandContact="0";
			    	
					 open();
               try
               {
             	  
               
					 Cursor cursor = db.rawQuery("SELECT UserId FROM tblUserLoginMstr", null);
						
					 if(cursor.getCount()>0)
					 {
						if(cursor.moveToFirst()) 
						{
							UserNameandContact=cursor.getString(0);
						}
					 }
               }
               catch(Exception e)
               {
             	  
               }
               finally
               {
             	  close();  
               }
					 
					 return UserNameandContact;
			    }
			 
			
			 
			/* private static final String DATABASE_CREATE_TABLE_MODULE_SAVED = "create table tblModuleSaved" +
			 		"(moduleID text null,ModuleContentName text null,moduleSlideSequenceNo text null,isLastSlide text null," +
			 		"imeiNum text null,Sstat integer null);";
			*/	
			 
			 //Use Distict in the Corsor
			 public String fnfetchModuleContentNameBasedmoduleID(int moduleID)
			    {
			    	String UserNameandContact="0^NA";
			    	
					 open();

					 Cursor cursor = db.rawQuery("SELECT ModuleContentName FROM tblModuleSaved where moduleID="+moduleID, null);
						
					 if(cursor.getCount()>0)
					 {
						if(cursor.moveToFirst()) 
						{
							UserNameandContact=cursor.getString(0);
						}
					 }
					 close();
					 return UserNameandContact;
			    }
			 
			 
		//	 private static final String DATABASE_CREATE_TABLE_UserModuleMstr = "create table tblUserModuleMstr" +
			// 		"(LPCourseGroupMainID text null, LPCourseGroupName text null, LPCourseID text null," +
			 //		"LPCourseName text null, LPCourseDescription text null,ModuleImgName text null, ModuleImgUrl text null, Status text null,DayVal text null, flgNewOld text null,PDACoursePath text null,PDAEnglishCoursePath text null,PDAHIndiCoursePath text null,PDAMalyalamCoursePath text null,PDATamilCoursePath text null,PDAKannadaCoursePath text null,PDAMarathiCoursePath text null,PDAGujratiCoursePath text null,PDABangaliCoursePath text null,PDAAssamiesCoursePath text null,PDATeleguCoursePath text null,PDAOdiyaCoursePath text null,flgPDAOnline text null,LPCourseCatID text null);"; 
				
			 public String fnfetchModuleNametblUserModuleMstr(int LPCourseID)
			    {
			    	String UserNameandContact="0^NA";
			    	
					 open();

					 Cursor cursor = db.rawQuery("SELECT LPCourseName FROM tblUserModuleMstr where LPCourseID="+LPCourseID, null);
						
					 if(cursor.getCount()>0)
					 {
						if(cursor.moveToFirst()) 
						{
							UserNameandContact=cursor.getString(0);
						}
					 }
					 close();
					 return UserNameandContact;
			    }
			 
			 public String fnfetchQstnTypeIDBasedQstID(int QstID)
			    {
			    	String UserNameandContact="0^NA";
			    	
					 open();

					 Cursor cursor = db.rawQuery("SELECT QstnTypeID FROM tblModuleQuestionAnswerTwo where QstID="+QstID, null);
						
					 if(cursor.getCount()>0)
					 {
						if(cursor.moveToFirst()) 
						{
							UserNameandContact=cursor.getString(0);
						}
					 }
					 close();
					 return UserNameandContact;
			    }
			
			
			 public LinkedHashMap<String, String> fetchQstIDAndQstpartTxtbasedbasedTestID(int TestID)
				{
					LinkedHashMap<String, String> lnkdHmapAllQuesSavedForOutlet=new LinkedHashMap<String, String>();
								
					open();
					try
					{
					Cursor cur=db.rawQuery("Select QstID,QstpartTxt from tblModuleQuestionAnswerTwo where TestID ="+TestID, null);
							if(cur.getCount()>0)
							{
	
								if(cur.moveToFirst())
								{
									for(int i=0;i<cur.getCount();i++)
									{
										lnkdHmapAllQuesSavedForOutlet.put(""+(i+1),cur.getString(0)+"^"+ cur.getString(1));
										cur.moveToNext();
									}
								}
							}
				}
			 catch(Exception e)
			 {
			 }
			finally
			{
				close();
			}
							
					return lnkdHmapAllQuesSavedForOutlet;
				}
				
			
			
			
			
		/*	
			private static final String DATABASE_CREATE_TABLE_13 = "create table tblModuleQuestionAnswerThree (TestID text null," +
					"TestInsUserMapID text null,QstID text null,QstnStatID text null,StatementDescr text null,CorrectAns text null," +
					"OptionSeq text null,RspDetID text null,RspID text null,RsltQstnStatID text null,flgCorrectAns text null);";
			
			*/ 
			
			public long savetblModuleQuestionAnswerThree(int TestID,int TestInsUserMapID,int QstID,int QstnStatID,
					String StatementDescr,int CorrectAns,int OptionSeq,int RspDetID,int RspID,int RsltQstnStatID,int flgCorrectAns)
			{
								
								ContentValues initialValues = new ContentValues();
								
								initialValues.put("TestID", TestID); 
								initialValues.put("TestInsUserMapID", TestInsUserMapID); 
								initialValues.put("QstID", QstID); 
								initialValues.put("QstnStatID", QstnStatID); 
								initialValues.put("StatementDescr", StatementDescr.trim()); 
								
								initialValues.put("CorrectAns", CorrectAns); 
								initialValues.put("OptionSeq", OptionSeq); 
								initialValues.put("RspDetID", RspDetID); 
								initialValues.put("RspID", RspID); 
								initialValues.put("RsltQstnStatID", RsltQstnStatID); 
								initialValues.put("flgCorrectAns", flgCorrectAns); 
								
								return db.insert(DATABASE_TABLE_Main13, null, initialValues);	
			}
			
			
			
			
			
			 public LinkedHashMap<String, String> fetchQstnStatIDAndCorrectAnsbasedbasedTestIDQstID(int TestID,int QstID)
				{
					LinkedHashMap<String, String> lnkdHmapAllQuesSavedForOutlet=new LinkedHashMap<String, String>();
								
					open();
					try
					{
					Cursor cur=db.rawQuery("Select QstnStatID,CorrectAns from tblModuleQuestionAnswerThree where QstID ="+QstID +" and TestID="+TestID +" and CorrectAns="+1, null);
							if(cur.getCount()>0)
							{
	
								if(cur.moveToFirst())
								{
									for(int i=0;i<cur.getCount();i++)
									{
										lnkdHmapAllQuesSavedForOutlet.put(cur.getString(0),cur.getString(1));
										cur.moveToNext();
									}
								}
							}
				}
			 catch(Exception e)
			 {
			 }
			finally
			{
				close();
			}
							
					return lnkdHmapAllQuesSavedForOutlet;
				}
			
			   
			 public int fnfetchCorrectAnsBasedQstIDQstnStatID(int QstID,int QstnStatID)
			    {
			    	int CorrectAns=0;
			    	
					 open();
            try
            {
            	// Cursor cursor = db.rawQuery("SELECT  QstnStatID,StatementDescr from tblModuleQuestionAnswerThree  Where QstID="+QstID +" Order By OptionSeq ASC" , null);// Where PNodeID='"+TSIID+"'
    			 
					 Cursor cursor = db.rawQuery("SELECT CorrectAns FROM tblModuleQuestionAnswerThree where QstID="+QstID +" and QstnStatID="+QstnStatID, null);
						
					 if(cursor.getCount()>0)
					 {
						if(cursor.moveToFirst()) 
						{
							CorrectAns=Integer.parseInt(cursor.getString(0).toString().trim());
						}
					 }
            }
            catch(Exception e)
            {
            	
            }
            finally
            {
					 close();
            }
					 return CorrectAns;
			    }
			
			 public LinkedHashMap<String, String> fetchQstnStatIDAndStatementDescrbasedbasedTestIDQstID(int TestID,int QstID)
				{
					LinkedHashMap<String, String> lnkdHmapAllQuesSavedForOutlet=new LinkedHashMap<String, String>();
								
					open();
					try
					{
					Cursor cur=db.rawQuery("Select QstnStatID,StatementDescr from tblModuleQuestionAnswerThree where QstID ="+QstID +" and TestID="+TestID, null);
							if(cur.getCount()>0)
							{
	
								if(cur.moveToFirst())
								{
									for(int i=0;i<cur.getCount();i++)
									{
										lnkdHmapAllQuesSavedForOutlet.put(cur.getString(0),cur.getString(1));
										cur.moveToNext();
									}
								}
							}
				}
			 catch(Exception e)
			 {
			 }
			finally
			{
				close();
			}
							
					return lnkdHmapAllQuesSavedForOutlet;
				}
			 
				public ArrayList<String> fnGetOptionMstr(int QstID)
			      {
				     
				      ArrayList<String> listOptionMstr = new ArrayList<String>();
				      String idd="0";
				  
				       open();
				       Cursor cursor = db.rawQuery("SELECT  QstnStatID,StatementDescr from tblModuleQuestionAnswerThree  Where QstID="+QstID +" Order By OptionSeq ASC" , null);// Where PNodeID='"+TSIID+"'
				    
				       try {
				        if(cursor.getCount()>0)
				         
				       {
				         if (cursor.moveToFirst())
				        {
				         
				          for (int i = 0; i <= (cursor.getCount() - 1); i++) {
				         
				           listOptionMstr.add(i,(String) cursor.getString(0).toString()+"^"+(String) cursor.getString(1).toString());
				             cursor.moveToNext();
				          }
				       
				        }
				       }
				       return listOptionMstr;
				       } 
				       finally 
				       {
				       cursor.close();
				       close();
				      }
				      }
				
		 
			public void deletetblModuleLanguageMstr() 
			   {
				try
				{
		  		
				    db.execSQL("DELETE FROM tblModuleLanguageMstr");
				  
				  
				}
				catch(Exception e)
				{
					
				}
				   
			   }
			 
			public void deleteUserModuleQuestionAndAnswer() 
			   {
		  		 open();
				    db.execSQL("DELETE FROM tblModuleQuestionAnswerOne");
				    db.execSQL("DELETE FROM tblModuleQuestionAnswerTwo");
				    db.execSQL("DELETE FROM tblModuleQuestionAnswerThree");
				  close();
				   
			   }
			
			
			
			/* private static final String DATABASE_CREATE_TABLE_14 = "create table tblTestsAnswerForModule (ModuleId integer null," +
	 		"TestID integer null,TestInstanceID integer null,TestInsUserMapID integer null,QstID integer null," +
	 		"AnswerDescr text null,QstnTypeID integer null,Sstat integer null);";
	*/	
			
			 public String fnfetchModuleCompleteDate(int moduleID)
		        {
		         String UserNameandContact="0^NA";
		         try
		         {
		       open();

		       Cursor cursor = db.rawQuery("SELECT ModuleCompleteDate FROM tblTestsAnswerForModule where moduleID="+moduleID, null);
		       
		       if(cursor.getCount()>0)
		       {
		       if(cursor.moveToFirst()) 
		       {
		        UserNameandContact=cursor.getString(0);
		       }
		       }
		         }
		         catch(Exception e)
		         {
		          
		         }
		         finally
		         {
		          close();
		         }
		       
		       return UserNameandContact;
		        }
			
			 public void savetblTestsAnswerForModule(int ModuleId,int TestID,int TestInstanceID,int TestInsUserMapID,int QstID,String  AnswerDescr,
						int QstnTypeID,String ModuleCompleteDate,int Sstat)
				{
					Cursor cur=null;
					try {
						
					
					
					cur=db.rawQuery("Select ModuleId from tblTestsAnswerForModule where ModuleId="+ModuleId+" And TestID="+TestID+" And QstID="+QstID, null);
					ContentValues initialValues = new ContentValues();
					if(cur.getCount()>0)
					{
						initialValues.put("AnswerDescr", AnswerDescr.trim()); 
						db.update(DATABASE_TABLE_Main14, initialValues, "ModuleId=? And TestID=? And QstID=? And AnswerDescr=?", new String[]{""+ModuleId,""+TestID,""+QstID,AnswerDescr});
					}
					else
					{
						
						initialValues.put("ModuleId", ModuleId); 
						initialValues.put("TestID", TestID); 
						initialValues.put("TestInstanceID", TestInstanceID); 
						initialValues.put("TestInsUserMapID", TestInsUserMapID); 
						initialValues.put("QstID", QstID); 
						
						initialValues.put("AnswerDescr", AnswerDescr.trim()); 
						initialValues.put("QstnTypeID", QstnTypeID); 
						initialValues.put("ModuleCompleteDate", ModuleCompleteDate); 
						initialValues.put("Sstat", Sstat); 
						
						db.insert(DATABASE_TABLE_Main14, null, initialValues);	
						
					}
					} catch (Exception e) {
						// TODO: handle exception
					}
				finally
				{
					cur.close();
				}
								
				}
			
			
			 public int getTotalNoOfQstIDUserAttend(int TestID) {

					int ScodecolumnIndex = 0;
		        open();
					Cursor cursor = db.rawQuery("SELECT Count(QstID) FROM tblTestsAnswerForModule where TestID="+TestID, null);
					try {
						
						int strProdStockQty = 0;
						if (cursor.moveToFirst()) {

							for (int i = 0; i <= (cursor.getCount() - 1); i++) 
							{
								if (!cursor.isNull(ScodecolumnIndex))
								{
									strProdStockQty = Integer.parseInt(cursor.getString(ScodecolumnIndex).toString());
									cursor.moveToNext();
								}

							}
						}
						return strProdStockQty;
					} finally {
						cursor.close();
						 close(); 
					}
				}
			 
			
			
			public void fnDeletefailsRecords(int ModuleId)
			{
		  		 open();
		      try
		        {
		         
		           db.execSQL("Delete from tblTestsAnswerForModule where ModuleId="+ModuleId );
		       
				}
		      catch (Exception ex)
		        {
					
				}
		      finally
		      {
		    	  close();
		      }

			}
			
			
			 public void deleteAllQuestionTable() 
			   {
		  		 
		  		
			    open();
			    db.execSQL("DELETE FROM tblDistributorQstnGrpMstr");
			    db.execSQL("DELETE FROM tblDistributor_QstnMstr");
			    db.execSQL("DELETE FROM tblDistributorQstnOptionMapping");
			    db.execSQL("DELETE FROM tblDistributorProfileMstr");
			   
			   
			    close(); 
			   }
			 public long savetblDistributorQstnGrpMstr(String QstnGrpID,String QstnGrpDescr)
				{
									
									ContentValues initialValues = new ContentValues();
									
									initialValues.put("QstnGrpID", QstnGrpID.trim()); 
									initialValues.put("QstnGrpDescr", QstnGrpDescr.trim()); 
									
									
									return db.insert(DATABASE_TABLE_Distributer_QuestionGrpMstr, null, initialValues);
									
				}
			 
			 
			 public long savetblDistributorQstnMstr(String QstnID,String QstnTxtname,String QstnDescr,String Weightage,String QstnGrpID)
				{
									
									ContentValues initialValues = new ContentValues();
									
									initialValues.put("QstnID", QstnID.trim()); 
									initialValues.put("QstnTxtname", QstnTxtname.trim()); 

									initialValues.put("QstnDescr", QstnDescr.trim()); 
									initialValues.put("Weightage", Weightage.trim()); 

									initialValues.put("QstnGrpID", QstnGrpID.trim()); 
									
									
									
									return db.insert(DATABASE_TABLE_Distributer_QuestionMstr, null, initialValues);	
				}
				public long savetblDistributer_Question_Option_Mstr(String QstnId,String OptionId,String optionDesc,String Score)
				{
									
									ContentValues initialValues = new ContentValues();
									
									initialValues.put("QstnId", QstnId.trim()); 
									initialValues.put("OptionId", OptionId.trim()); 
									initialValues.put("OptionDesc", optionDesc.trim()); 

									initialValues.put("Score", Score.trim()); 
									
									
									
									
									return db.insert(DATABASE_TABLE_Distributer_Question_Option_Mstr, null, initialValues);	
				}
				public long savetblDistributorProfileMstr(String ProfileId,String Descr,String Profile,String MinScore,String MaxScore,String RecommendedAction,String Spirit)
				{
									
									ContentValues initialValues = new ContentValues();
									
									initialValues.put("ProfileId", ProfileId.trim()); 
									initialValues.put("Descr", Descr.trim()); 

									initialValues.put("Profile", Profile.trim()); 
									initialValues.put("MinScore", MinScore.trim()); 

									initialValues.put("MaxScore", MaxScore.trim());
									initialValues.put("RecommendedAction", RecommendedAction.trim());
									initialValues.put("Spirit", Spirit.trim());
									
									
									
									return db.insert(DATABASE_TABLE_Distributer_Profile_Mstr, null, initialValues);	
				}
				
				public LinkedHashMap<String, String> fetchQuestInfo()
				{
					LinkedHashMap<String, String> hmapQuestInfo=new LinkedHashMap<String, String>();
					open();
					
					//tblDistributorQstnGrpMstr (QstnGrpID text null,QstnGrpDescr text null);";//, AutoIdOutlet int null
					 //tblDistributor_QstnMstr (QstnID text null,QstnTxtname text null,QstnDescr text null,Weightage text null,QstnGrpID text null);";//, AutoIdOutlet int null
					//tblDistributorQstnOptionMapping (QstnId text null,OptionId text null,Score text null);";//, AutoIdOutlet int null
					
					 try
					 {
						 
					 
					Cursor cur=db.rawQuery("Select tblDistributorQstnGrpMstr.QstnGrpID,tblDistributorQstnGrpMstr.QstnGrpDescr,tblDistributor_QstnMstr.QstnID,tblDistributor_QstnMstr.QstnDescr,tblDistributor_QstnMstr.Weightage from  tblDistributorQstnGrpMstr Inner Join tblDistributor_QstnMstr On tblDistributorQstnGrpMstr.QstnGrpID=tblDistributor_QstnMstr.QstnGrpID", null);
					
					if(cur.getCount()>0)
					{
						if(cur.moveToFirst())
						{
							String qstnGrpID,qstnGrpDescr,qstnID,qstnDescr,weightage;
							for(int i=0;i<cur.getCount();i++)
							{
								qstnGrpID=cur.getString(0);
								qstnGrpDescr=cur.getString(1);
								qstnID=cur.getString(2);
								qstnDescr=cur.getString(3);
								weightage=cur.getString(4);
								hmapQuestInfo.put(qstnGrpID+"~"+qstnID, qstnGrpDescr+"~"+qstnDescr+"~"+weightage);
								cur.moveToNext();
							}
						}
					}
					 }catch(Exception e)
					 {
						return hmapQuestInfo; 
					 }
					 finally
					 {
						 close();
						 return hmapQuestInfo; 
					 }
				}
				
				public LinkedHashMap<String, String> fetchQuestOptionInfo()
				{
					LinkedHashMap<String, String> hmapQuestOptionInfo=new LinkedHashMap<String, String>();
					open();
					
					//tblDistributorQstnGrpMstr (QstnGrpID text null,QstnGrpDescr text null);";//, AutoIdOutlet int null
					 //tblDistributor_QstnMstr (QstnID text null,QstnTxtname text null,QstnDescr text null,Weightage text null,QstnGrpID text null);";//, AutoIdOutlet int null
					//tblDistributorQstnOptionMapping (QstnId text null,OptionId text null,OptionDesc,Score text null);";//, AutoIdOutlet int null
					
					 try
					 {
						 
					 
					Cursor cur=db.rawQuery("Select tblDistributor_QstnMstr.QstnID,tblDistributor_QstnMstr.QstnGrpID,tblDistributorQstnOptionMapping.OptionId,tblDistributorQstnOptionMapping.OptionDesc,tblDistributorQstnOptionMapping.Score from  tblDistributor_QstnMstr Inner Join tblDistributorQstnOptionMapping On tblDistributorQstnOptionMapping.QstnID=tblDistributor_QstnMstr.QstnID", null);
					
					if(cur.getCount()>0)
					{
						if(cur.moveToFirst())
						{
							String qstnGrpID,optionId,qstnID,score,_optionDesc;
							for(int i=0;i<cur.getCount();i++)
							{
								qstnID=cur.getString(0);
								qstnGrpID=cur.getString(1);
								optionId=cur.getString(2);
								_optionDesc=cur.getString(3);
								score=cur.getString(4);
								
								hmapQuestOptionInfo.put(qstnGrpID+"^"+qstnID+"^"+optionId, _optionDesc+"^"+score);
								cur.moveToNext();
							}
						}
					}
					 }catch(Exception e)
					 {
						return hmapQuestOptionInfo; 
					 }
					 finally
					 {
						 close();
						 return hmapQuestOptionInfo; 
					 }
				}
				
				public LinkedHashMap<String, ArrayList<String>> fetchQuestGrpIdQuestId()
				{
					LinkedHashMap<String, ArrayList<String>> hmapQuestGrpIdWdQuestId=new LinkedHashMap<String, ArrayList<String>>();
					ArrayList<String> listQuestId=new ArrayList<String>();
					String keyQuestGrpId = null;
					open();
					
					//tblDistributorQstnGrpMstr (QstnGrpID text null,QstnGrpDescr text null);";//, AutoIdOutlet int null
					 //tblDistributor_QstnMstr (QstnID text null,QstnTxtname text null,QstnDescr text null,Weightage text null,QstnGrpID text null);";//, AutoIdOutlet int null
					//tblDistributorQstnOptionMapping (QstnId text null,OptionId text null,Score text null);";//, AutoIdOutlet int null
					
					 try
					 {
						 
					 
					Cursor cur=db.rawQuery("Select tblDistributorQstnGrpMstr.QstnGrpID,tblDistributor_QstnMstr.QstnID from  tblDistributorQstnGrpMstr Inner Join tblDistributor_QstnMstr On tblDistributorQstnGrpMstr.QstnGrpID=tblDistributor_QstnMstr.QstnGrpID", null);
					
					if(cur.getCount()>0)
					{
						if(cur.moveToFirst())
						{
							String qstnGrpID,optionId,qstnID,score;
							for(int i=0;i<cur.getCount();i++)
							{
								qstnID=cur.getString(1);
								qstnGrpID=cur.getString(0);
								if(i==0)
								{
									keyQuestGrpId=qstnGrpID;
									listQuestId.add(qstnID);
								}
								else
								{
									if(cur.getCount()!=i+1)
									{
										if(!keyQuestGrpId.equals(qstnGrpID))
										{
											hmapQuestGrpIdWdQuestId.put(keyQuestGrpId, listQuestId);
											keyQuestGrpId=qstnGrpID;
											listQuestId=new ArrayList<String>();
											listQuestId.add(qstnID);
											
										}
										else
										{
											listQuestId.add(qstnID);
										}
									}
									else
									{
										hmapQuestGrpIdWdQuestId.put(keyQuestGrpId, listQuestId);
									}
									
								}
								
							
								cur.moveToNext();
							}
						}
					}
					 }catch(Exception e)
					 {
						return hmapQuestGrpIdWdQuestId; 
					 }
					 finally
					 {
						 close();
						 return hmapQuestGrpIdWdQuestId; 
					 }
				}
				
				public LinkedHashMap<String, ArrayList<String>> fetchQuestIdOptnId()
				{
					LinkedHashMap<String, ArrayList<String>> hmapQuestIdWdOptId=new LinkedHashMap<String, ArrayList<String>>();
					ArrayList<String> listOptId=new ArrayList<String>();
					String keyQuestId = null;
					open();
					
					//tblDistributorQstnGrpMstr (QstnGrpID text null,QstnGrpDescr text null);";//, AutoIdOutlet int null
					 //tblDistributor_QstnMstr (QstnID text null,QstnTxtname text null,QstnDescr text null,Weightage text null,QstnGrpID text null);";//, AutoIdOutlet int null
					//tblDistributorQstnOptionMapping (QstnId text null,OptionId text null,Score text null);";//, AutoIdOutlet int null
					
					 try
					 {
						 
					 
					Cursor cur=db.rawQuery("Select tblDistributor_QstnMstr.QstnID,tblDistributorQstnOptionMapping.OptionId from  tblDistributor_QstnMstr Inner Join tblDistributorQstnOptionMapping On tblDistributor_QstnMstr.QstnID=tblDistributorQstnOptionMapping.QstnID", null);
					
					if(cur.getCount()>0)
					{
						if(cur.moveToFirst())
						{
							String qstnID,optionId;
							for(int i=0;i<cur.getCount();i++)
							{
								qstnID=cur.getString(0);
								optionId=cur.getString(1);
								if(i==0)
								{
									keyQuestId=qstnID;
									listOptId.add(optionId);
								}
								else
								{
									if(!keyQuestId.equals(qstnID))
									{
										hmapQuestIdWdOptId.put(keyQuestId, listOptId);
										keyQuestId=qstnID;
										listOptId=new ArrayList<String>();
										listOptId.add(optionId);
										
									}
									else
									{
										listOptId.add(optionId);
									}
								}
								
							
								cur.moveToNext();
							}
						}
					}
					 }catch(Exception e)
					 {
						return hmapQuestIdWdOptId; 
					 }
					 finally
					 {
						 close();
						 return hmapQuestIdWdOptId; 
					 }
				}
				
				public String[] getQuestGrpIds()
				{
					
					 String[] quesGrpIds = null;
					
					
					//tblDistributorQstnGrpMstr (QstnGrpID text null,QstnGrpDescr text null);";//, AutoIdOutlet int null
					 //tblDistributor_QstnMstr (QstnID text null,QstnTxtname text null,QstnDescr text null,Weightage text null,QstnGrpID text null);";//, AutoIdOutlet int null
					//tblDistributorQstnOptionMapping (QstnId text null,OptionId text null,Score text null);";//, AutoIdOutlet int null
					
					 try
					 {
						 
						 open();
					Cursor cur=db.rawQuery("Select DISTINCT QstnGrpID from tblDistributorQstnGrpMstr", null);
					
					if(cur.getCount()>0)
					{
						quesGrpIds=new String[cur.getCount()];
						if(cur.moveToFirst())
						{
							String qstnGrpID,optionId,qstnID,score;
							for(int i=0;i<cur.getCount();i++)
							{
								quesGrpIds[i]=cur.getString(0);
								cur.moveToNext();
							}
						}
					}
					 }catch(Exception e)
					 {
						 
						return quesGrpIds; 
					 }
					 finally
					 {
						 close();
						 return quesGrpIds; 
					 }
				}
				
				public void inserDstrbtrQuesAnsVal(String tempId,LinkedHashMap<String, String> hmapQuesAnsVal)
			    {
			     String questGrpId,questId,OptionId,score,weightage;
			      // private static final String TABLE_Distributor_Ques_Ans = "tblDstrbtrQuesAns";
			    //"create table tblDstrbtrQuesAns (QstnGrpIdtext null,QstnId text null,OptionId text null,Score text null);";
			    open();
			    try {
			    
			   
			    for(Map.Entry<String, String> entry:hmapQuesAnsVal.entrySet())
			    {
			     questGrpId=entry.getKey().split(Pattern.quote("^"))[0].trim();
			     questId=entry.getKey().split(Pattern.quote("^"))[1].trim();
			     OptionId=entry.getKey().split(Pattern.quote("^"))[2].trim();
			     score=entry.getValue().split(Pattern.quote("^"))[0];
			     weightage=entry.getValue().split(Pattern.quote("^"))[1];
			     ContentValues values=new ContentValues();
			     values.put("tempId", tempId);
			     values.put("QstnGrpId", questGrpId);
			     values.put("QstnId", questId);
			     values.put("OptionId", OptionId);
			     values.put("Score", score);
			     values.put("Weightage", weightage);
			     values.put("Sstat", 3);
			    
			     System.out.println("TABLE_Distributor_Ques_Ans inserted : QstnGrpId = "+questGrpId+" QstnId ="+questId+" OptionId= "+OptionId+" scoreBoard= "+score);
			     db.insert(TABLE_Distributor_Ques_Ans, null, values);
			     
			    }
			    } catch (Exception e) {
			     // TODO: handle exception
			    }
			    finally
			    {
			     close();
			    }
			   
			    }
				
				 public LinkedHashMap<String, String> getStnOfEx()
				    {
				     LinkedHashMap<String, String> hmapQstnTxtName=new LinkedHashMap<String, String>();
				     //tblDistributor_QstnMstr (QstnID text null,QstnTxtname text null,QstnDescr text null,Weightage text null,QstnGrpID
				     open();
				     try {
				      
				     
				     Cursor cur=db.rawQuery("Select QstnID,QstnTxtname,QstnGrpID from tblDistributor_QstnMstr where QstnTxtname<>0 AND QstnTxtname<>''", null);
				     
				     if(cur.getCount()>0)
				     {
				      if(cur.moveToFirst())
				      {
				       for(int i=0;i<cur.getCount();i++)
				       {
				        hmapQstnTxtName.put(cur.getString(2)+"^"+cur.getString(0), cur.getString(1));
				        cur.moveToNext();
				       }
				      }
				     }
				     } catch (Exception e) {
				     
				    	 System.out.println(e);
				     }
				     finally
				     {
				    	 close();
				      return hmapQstnTxtName;
				     }
				     
				    }
				 
				 public LinkedHashMap<String, String> fetchProfileLevel()
				 {
					 LinkedHashMap<String,String> hmapProfileLevel=new LinkedHashMap<String, String>();
					 //tblDistributorProfileMstr (ProfileId text null,Descr text null,Profile text null,MinScore text null,MaxScore text null,RecommendedAction text null,Spirit text null);";//, AutoIdOutlet int null
				open();
				try {
					
				
				Cursor cur=db.rawQuery("Select MinScore,MaxScore,Descr,Profile,RecommendedAction,Spirit from tblDistributorProfileMstr", null);
				
				if(cur.getCount()>0)
				{
					if(cur.moveToFirst())
					{
						String minScore,maxScore,descr,profile,rcmnddAct,spirit;
						for(int i=0;i<cur.getCount();i++)
						{
							minScore=cur.getString(0);
							maxScore=cur.getString(1);
							descr=cur.getString(2);
							profile=cur.getString(3);
							rcmnddAct=cur.getString(4);
							spirit=cur.getString(5);
							hmapProfileLevel.put(minScore+"-"+maxScore, descr+"~"+profile+"~"+rcmnddAct+"~"+spirit);
						cur.moveToNext();
						}
					}
				}
				} catch (Exception e) {
					// TODO: handle exception
				}
				finally
				{
					close();
					return hmapProfileLevel;
					
				}
				 }
				 
				 public void UpdateAssessmentCompleteBasedOnModuleID(String moduleID)
					{
				  		open();
						try
						 {

							final ContentValues values = new ContentValues();
							values.put("AssessmentComplete", 1);
				           
							int affected1 = db.update("tblModuleSaved", values, "moduleID=?",new String[] { moduleID });
							
						 }
						catch (Exception ex)
						{
							
						}
						finally
						{
							close();
						}
							

					} 
				 
					public long savetblModuleLanguageMstr(String LPCourseID,String LanguageID, String RegionID,String flgPDAOnline)
					{					
						

							ContentValues initialValues = new ContentValues();
							
							initialValues.put("LPCourseID", LPCourseID.trim()); 
							
							initialValues.put("LanguageID", LanguageID);
							initialValues.put("RegionID",RegionID);
							initialValues.put("flgPDAOnline",flgPDAOnline);
							//flgPDAOnline text null
							return db.insert(TABLE_ModuleLanguageMstr, null, initialValues);
							
					}
					
					public long savetblLanguageMstr(String LanguageID, String Language)
					{					
						

							ContentValues initialValues = new ContentValues();
							
							initialValues.put("LanguageID", LanguageID);
							initialValues.put("Language",Language);
							
							return db.insert(TABLE_LanguageMstr, null, initialValues);
							
					}
					
					public LinkedHashMap<String, String> fetchtblModuleLanguageMstr(String LPCourseID)
					{
						LinkedHashMap<String, String> hmapModuleSlideName=new LinkedHashMap<String, String>();
						open();
						try
						{
							Cursor cur=db.rawQuery("Select LanguageID,LPCourseID from tblModuleLanguageMstr where LPCourseID='"+LPCourseID+"'", null);
							if(cur.getCount()>0)
							{
								if(cur.moveToFirst())
								{
									for(int i=0;i<cur.getCount();i++)
									{
										hmapModuleSlideName.put(cur.getString(0), cur.getString(1));	
									    cur.moveToNext();
									}
								}
								
								return hmapModuleSlideName;
							}
							else
							{
								
								return hmapModuleSlideName;
							}	
						}
						catch(Exception e)
						{
							return hmapModuleSlideName;
						}
						finally
						{
							close();
						}
						
					}
					
					public LinkedHashMap<String, String> fetchtblLanguageMstr()
					{
						LinkedHashMap<String, String> hmapModuleSlideName=new LinkedHashMap<String, String>();
						open();
						try
						{
							Cursor cur=db.rawQuery("Select LanguageID,Language from tblLanguageMstr", null);
							if(cur.getCount()>0)
							{
								if(cur.moveToFirst())
								{
									for(int i=0;i<cur.getCount();i++)
									{
										hmapModuleSlideName.put(cur.getString(0), cur.getString(1));	
									    cur.moveToNext();
									}
								}
								
								return hmapModuleSlideName;
							}
							else
							{
								
								return hmapModuleSlideName;
							}	
						}
						catch(Exception e)
						{
							return hmapModuleSlideName;
						}
						finally
						{
							close();
						}
						
					}
					
					
					public String fetchModuleDownloadedFromtblUserModuleMstr(String moduleId,int LanguageId)
					{
						String moduleFolderName=null;
						open();
						try
						{
							Cursor cur=db.rawQuery("Select ModuleContentName from tblUserModuleMstr where LPCourseID='"+moduleId+"'", null);
							if(cur.getCount()>0)
							{
								if(cur.moveToFirst())
								{
									moduleFolderName=cur.getString(0);
								}
								
								return moduleFolderName;
							}
							else
							{
								
								return moduleFolderName="";
							}	
						} catch(Exception e)
						{
							return moduleFolderName="error";
						}
						finally
						{
							close();
						}
						
					}
					
					
					public LinkedHashMap<String, String> fetchtblModuleOnlineOfflineMstr(String LPCourseID)
					{
						LinkedHashMap<String, String> hmapModuleSlideName=new LinkedHashMap<String, String>();
						open();
						try
						{
							//tblUserModuleMstr(LPCourseGroupMainID text null, LPCourseGroupName text null, LPCourseID text null, LPCourseName text null, LPCourseDescription text null,ModuleImgName text null, ModuleImgUrl text null, Status text null,DayVal text null, flgNewOld text null,PDACoursePath text null,PDAEnglishCoursePath text null,PDAHIndiCoursePath text null,PDAMalyalamCoursePath text null,PDATamilCoursePath text null,PDAKannadaCoursePath text null,PDAMarathiCoursePath text null,PDAGujratiCoursePath text null,PDABangaliCoursePath text null,PDAAssamiesCoursePath text null,PDATeleguCoursePath text null,PDAOdiyaCoursePath text null,flgPDAOnline text null);";
							Cursor cur=db.rawQuery("Select Distinct LPCourseID,flgPDAOnline from tblModuleLanguageMstr where LPCourseID='"+LPCourseID+"'", null);
							if(cur.getCount()>0)
							{
								if(cur.moveToFirst())
								{
									for(int i=0;i<cur.getCount();i++)
									{
										hmapModuleSlideName.put(cur.getString(0), cur.getString(1));	
									    cur.moveToNext();
									}
								}
								
								return hmapModuleSlideName;
							}
							else
							{
								
								return hmapModuleSlideName;
							}	
						}
						catch(Exception e)
						{
							return hmapModuleSlideName;
						}
						finally
						{
							close();
						}
						
					}
					
					
					public String fngetUrlLinkToOpen(String moduleId,String LanguageId)
					{
						
						LinkedHashMap<String, String> hmapLanguageIDColumnNameMap=new LinkedHashMap<String, String>();
						hmapLanguageIDColumnNameMap.put("1", "PDAEnglishCoursePath");
						hmapLanguageIDColumnNameMap.put("2", "PDAHIndiCoursePath");
						hmapLanguageIDColumnNameMap.put("3", "PDATeleguCoursePath");
						hmapLanguageIDColumnNameMap.put("4", "PDATamilCoursePath");
						hmapLanguageIDColumnNameMap.put("5", "PDAMalyalamCoursePath");
						hmapLanguageIDColumnNameMap.put("6", "PDAKannadaCoursePath");
						hmapLanguageIDColumnNameMap.put("7", "PDAMarathiCoursePath");
						hmapLanguageIDColumnNameMap.put("8", "PDAGujratiCoursePath");
						hmapLanguageIDColumnNameMap.put("9", "PDABangaliCoursePath");
						hmapLanguageIDColumnNameMap.put("10", "PDAAssamiesCoursePath");
						hmapLanguageIDColumnNameMap.put("11", "PDAOdiyaCoursePath");
						String strUrlColumnName=hmapLanguageIDColumnNameMap.get(LanguageId);
						String strUrlLink=null;
						open();
						try
						{
							Cursor cur=db.rawQuery("Select "+strUrlColumnName+" from tblUserModuleMstr where LPCourseID='"+moduleId+"' and flgPDAOnline=1", null);
							if(cur.getCount()>0)
							{
								if(cur.moveToFirst())
								{
									strUrlLink=cur.getString(0);
								}
								
								return strUrlLink;
							}
							else
							{
								
								return strUrlLink="";
							}	
						} catch(Exception e)
						{
							return strUrlLink="error";
						}
						finally
						{
							close();
						}
						
					}
					
					
					public String fnGetLpCourseCatID(String moduleId)
					{
						String LPCourseCatID=null;
						open();
						try
						{
							Cursor cur=db.rawQuery("Select LPCourseCatID from tblUserModuleMstr where LPCourseID='"+moduleId+"'", null);
							if(cur.getCount()>0)
							{
								if(cur.moveToFirst())
								{
									LPCourseCatID=cur.getString(0);
								}
								
								return LPCourseCatID;
							}
							else
							{
								
								return LPCourseCatID="";
							}	
						} catch(Exception e)
						{
							return LPCourseCatID="error";
						}
						finally
						{
							close();
						}
						
					}
					
					/* private static final String DATABASE_CREATE_TABLE_CheckOnlineModuleStatus = "create table " +
					 		"tblCheckOnlineModuleStatus(UserId text null, LoginID text null, ModuleID text null, " +
					 		"Status text null);";*/
					
					public long savetblCheckOnlineModuleStatus(String UserId,String LoginID,String ModuleID,String Status,String LPEndDate)
					{
						try
						{
							open();
						 db.execSQL("Delete from tblCheckOnlineModuleStatus where UserId='"+UserId+"' and LoginID='"+LoginID+"' and ModuleID='"+ModuleID+"'");

						ContentValues initialValues = new ContentValues();

						
						initialValues.put("UserId", UserId.trim());
						initialValues.put("LoginID", LoginID.trim());
						initialValues.put("ModuleID", ModuleID.trim());
						initialValues.put("Status", Status.trim());
						initialValues.put("LPEndDate", LPEndDate.trim());
						
						
						

						return db.insert(TABLE_CheckOnlineModuleStatus, null, initialValues);
						}
						catch(Exception e)
						{
							
						}
						finally
						{
							close();
						}
						return 0;
					}
					
					public String fetchOnlineStatus(String UserId,String LoginID,String ModuleID)
					{
						String Status=null;
						open();
						try
						{
							Cursor cur=db.rawQuery("Select Status from tblCheckOnlineModuleStatus where UserId='"+UserId+"' and LoginID='"+LoginID+"' and ModuleID='"+ModuleID+"'", null);
							if(cur.getCount()>0)
							{
								if(cur.moveToFirst())
								{
									Status=cur.getString(0);
								}
								
								return Status;
							}
							else
							{
								
								return Status="";
							}	
						} catch(Exception e)
						{
							return Status="error";
						}
						finally
						{
							close();
						}
						
					}
					
					public String fetchOnlineLPEndDate(String UserId,String LoginID,String ModuleID)
					{
						String Status=null;
						open();
						try
						{
							Cursor cur=db.rawQuery("Select LPEndDate from tblCheckOnlineModuleStatus where UserId='"+UserId+"' and LoginID='"+LoginID+"' and ModuleID='"+ModuleID+"'", null);
							if(cur.getCount()>0)
							{
								if(cur.moveToFirst())
								{
									Status=cur.getString(0);
								}
								
								return Status;
							}
							else
							{
								
								return Status="";
							}	
						} catch(Exception e)
						{
							return Status="error";
						}
						finally
						{
							close();
						}
						
					}
					
					public void updateStatusIntblCheckOnlineModuleStatus(String UserId,String LoginID,String ModuleID,String Status)
				  	 {
						 try
						 {
				  			 open();
				  				final ContentValues values = new ContentValues();
				  				values.put("Status", Status);
				  				int affected = db.update("tblCheckOnlineModuleStatus", values, "PhotoName=?,PhotoName=?,PhotoName=?",new String[] { UserId.trim(),LoginID.trim(),ModuleID.trim() });
				  				
						 }
						 catch(Exception e)
						 {
							 
						 }
						 finally
						 {
							 close(); 
						 }
				  			
				  	}
					
					
					/* private static final String DATABASE_CREATE_TABLE_UserModuleMstr = "create table " +
					 		"tblUserModuleMstr(LPCourseGroupMainID text null, LPCourseGroupName text null, " +
					 		"LPCourseID text null, LPCourseName text null, LPCourseDescription text null," +
					 		"ModuleImgName text null, ModuleImgUrl text null, Status text null,DayVal text null, " +
					 		"flgNewOld text null,PDACoursePath text null,PDAEnglishCoursePath text null" +
					 		",PDAHIndiCoursePath text null,PDAMalyalamCoursePath text null,PDATamilCoursePath text null," +
					 		"PDAKannadaCoursePath text null,PDAMarathiCoursePath text null," +
					 		"PDAGujratiCoursePath text null,PDABangaliCoursePath text null," +
					 		"PDAAssamiesCoursePath text null,PDATeleguCoursePath text null," +
					 		"PDAOdiyaCoursePath text null,flgPDAOnline text null,LPCourseCatID text null);"; */
						
					public String fetchModuleDownloaded(String LPCourseID)
					{
						String moduleFolderName=null;
						open();
						try
						{
							Cursor cur=db.rawQuery("Select flgPDAOnline from tblUserModuleMstr where LPCourseID='"+LPCourseID+"'", null);
							if(cur.getCount()>0)
							{
								if(cur.moveToFirst())
								{
									moduleFolderName=cur.getString(0);
								}
								
								return moduleFolderName;
							}
							else
							{
								
								return moduleFolderName="";
							}	
						} catch(Exception e)
						{
							return moduleFolderName="error";
						}
						finally
						{
							close();
						}
						
					}
					
					// private static final String TABLE_CheckOnlineAssessmentStatus = "tblCheckOnlineAssessmentStatus";
					// private static final String DATABASE_CREATE_TABLE_CheckOnlineAssessmentStatus = "create table 
					// tblCheckOnlineAssessmentStatus(UserId text null, LoginID text null, ModuleID text null,
						//	RSPID text null,Status text null,flgPassStatus text null,flgTestMap text null);";
					
					public long savetblCheckOnlineAssessmentStatus(String UserId,String LoginID,String ModuleID,String RSPID,String Status,String flgPassStatus,String flgTestMap)
					{
						try
						{
							open();
						 db.execSQL("Delete from tblCheckOnlineAssessmentStatus where UserId='"+UserId+"' and LoginID='"+LoginID+"' and ModuleID='"+ModuleID+"'");

						ContentValues initialValues = new ContentValues();

						
						initialValues.put("UserId", UserId.trim());
						initialValues.put("LoginID", LoginID.trim());
						initialValues.put("ModuleID", ModuleID.trim());
						initialValues.put("RSPID", RSPID.trim());
						initialValues.put("Status", Status.trim());
						initialValues.put("flgPassStatus", flgPassStatus.trim());
						initialValues.put("flgTestMap", flgTestMap.trim());
						
						


						return db.insert(TABLE_CheckOnlineAssessmentStatus, null, initialValues);
						}
						catch(Exception e)
						{
							
						}
						finally
						{
							close();
						}
						return 0;
					}
					
					public String fetchflgTestMapFromtblCheckOnlineAssessmentStatus(String UserId,String LoginID,String ModuleID)
					{
						String Status=null;
						open();
						try
						{
							Cursor cur=db.rawQuery("Select flgTestMap from tblCheckOnlineAssessmentStatus where UserId='"+UserId+"' and LoginID='"+LoginID+"' and ModuleID='"+ModuleID+"'", null);
							if(cur.getCount()>0)
							{
								if(cur.moveToFirst())
								{
									Status=cur.getString(0);
								}
								
								return Status;
							}
							else
							{
								
								return Status="";
							}	
						} catch(Exception e)
						{
							return Status="error";
						}
						finally
						{
							close();
						}
						
					}
					
					public String fetchStatusFromtblCheckOnlineAssessmentStatus(String UserId,String LoginID,String ModuleID)
					{
						String Status=null;
						open();
						try
						{
							Cursor cur=db.rawQuery("Select Status from tblCheckOnlineAssessmentStatus where UserId='"+UserId+"' and LoginID='"+LoginID+"' and ModuleID='"+ModuleID+"'", null);
							if(cur.getCount()>0)
							{
								if(cur.moveToFirst())
								{
									Status=cur.getString(0);
								}
								
								return Status;
							}
							else
							{
								
								return Status="";
							}	
						} catch(Exception e)
						{
							return Status="error";
						}
						finally
						{
							close();
						}
						
					}
					
					
}


