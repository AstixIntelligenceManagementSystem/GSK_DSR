package com.astix.gsk_dsr;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.http.client.HttpClient;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.util.Log;

public class UploadXMLService extends IntentService
{

	 int serverResponseCode = 0;
	
	    public static final int STATUS_RUNNING = 0;
	    public static final int STATUS_FINISHED = 1;
	    public static final int STATUS_ERROR = 2;

	    public  File dir;
	    GskDBAdapter dbengine = new GskDBAdapter(this); 
	    
	    
	    public String[] xmlForWeb = new String[1];
	
	public UploadXMLService(String name)
	{
		//super(name);
		// TODO Auto-generated constructor stub
		super(UploadXMLService.class.getName());
        setIntentRedelivery(true);
	}
	
	 public UploadXMLService()
	 {
		    super(UploadXMLService.class.getName());
	 }
	 
	 public static String[] checkNumberOfFiles(File dir) 
	  {
		 int NoOfFiles=0;
		 String [] Totalfiles = null;
		
	        if (dir.isDirectory()) 
	        {
	            String[] children = dir.list();
	            NoOfFiles=children.length;
	            Totalfiles=new String[children.length];
	            
	            for (int i=0; i<children.length; i++) 
	            {
	            	Totalfiles[i]=children[i];
	            }
	        }
	        return Totalfiles;
	    }

	@Override
	protected void onHandleIntent(Intent intent)
	{
		// TODO Auto-generated method stub
		
		 File GSKXMLFolder = new File(Environment.getExternalStorageDirectory(), "GSKXML");
			
		 if (!GSKXMLFolder.exists()) 
			{
			 GSKXMLFolder.mkdirs();
			} 
    	 
    	 File del = new File(Environment.getExternalStorageDirectory(), "GSKXML");
    	
    	// check number of files in folder
    	 String [] AllFilesName= checkNumberOfFiles(del);
    	 
    	 
    			/*File file = new File(delPath);
    		    file.delete();
    		    File file1 = new File(delPath.toString().replace(".xml", ".zip"));
    		     file1.delete();*/
    		
    	 
    	 if(AllFilesName.length>0)
    	 {
    		 SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE); 
    		
    		 
    		 for(int vdo=0;vdo<AllFilesName.length;vdo++)
				{
					String fileUri=  AllFilesName[vdo];
					

					System.out.println("Sunil Again each file Name :" +fileUri);
					
					if(fileUri.contains(".zip"))
					{
						File file = new File(fileUri);
		    		    file.delete();
					}
					else
					{
					String f1=Environment.getExternalStorageDirectory().getPath()+"/GSKXML/"+fileUri;
					System.out.println("Sunil Again each file full path"+f1);
					try
					{
					upLoad2Server(f1,fileUri);
					}
					catch (Exception e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
    		
    	 }
    	 else
    	 {
    		 
    	 }
		
		
		
		
	}
	
	
	
	
	
	
	public static void zip(String[] files, String zipFile) throws IOException
	{
	    BufferedInputStream origin = null;
	    final int BUFFER_SIZE = 2048;

	    ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
	    try { 
	        byte data[] = new byte[BUFFER_SIZE];

	        for (int i = 0; i < files.length; i++) {
	            FileInputStream fi = new FileInputStream(files[i]);    
	            origin = new BufferedInputStream(fi, BUFFER_SIZE);
	            try {
	                ZipEntry entry = new ZipEntry(files[i].substring(files[i].lastIndexOf("/") + 1));
	                out.putNextEntry(entry);
	                int count;
	                while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
	                    out.write(data, 0, count);
	                }
	            }
	            finally {
	                origin.close();
	            }
	        }
	    }
	    
	    finally {
	        out.close();
	    }
	}
	
	public  int upLoad2Server(String sourceFileUri,String fileUri) 
	{
		 
		fileUri=fileUri.replace(".xml", "");
		
		     String fileName = fileUri;
		     String zipFileName=fileUri;
		     
		     String newzipfile = Environment.getExternalStorageDirectory() + "/GSKXML/" + fileName + ".zip";
				///storage/sdcard0/PrabhatDirectSFAXml/359648069495987.2.21.04.2016.12.44.02.zip
		     
		     sourceFileUri=newzipfile;
		    	
		     xmlForWeb[0] = Environment.getExternalStorageDirectory() + "/GSKXML/" + fileName + ".xml";
		     //[/storage/sdcard0/PrabhatDirectSFAXml/359648069495987.2.21.04.2016.12.44.02.xml]
			 	
			   try 
				 {
					zip(xmlForWeb,newzipfile);
				 }
				 catch (Exception e1) 
				 {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					//java.io.FileNotFoundException: /359648069495987.2.21.04.2016.12.44.02: open failed: EROFS (Read-only file system)
				}
			   
			   
			      HttpURLConnection conn = null;
		          DataOutputStream dos = null;  
		          String lineEnd = "\r\n";
		          String twoHyphens = "--";
		          String boundary = "*****";
		          int bytesRead, bytesAvailable, bufferSize;
		          byte[] buffer;
		          int maxBufferSize = 1 * 1024 * 1024; 
				 
				 
			        File file2send = new File(newzipfile);
			        
			        String urlString = CommonInfo.OrderSyncPath.trim()+"?CLIENTFILENAME=" + zipFileName;
			        
			        try { 
			        	   
		            	 // open a URL connection to the Servlet
		               FileInputStream fileInputStream = new FileInputStream(file2send);
		               URL url = new URL(urlString);
		               
		               // Open a HTTP  connection to  the URL
		               conn = (HttpURLConnection) url.openConnection(); 
		               conn.setDoInput(true); // Allow Inputs
		               conn.setDoOutput(true); // Allow Outputs
		               conn.setUseCaches(false); // Don't use a Cached Copy
		               conn.setRequestMethod("POST");
		               conn.setRequestProperty("Connection", "Keep-Alive");
		               conn.setRequestProperty("ENCTYPE", "multipart/form-data");
		               conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
		               conn.setRequestProperty("zipFileName", zipFileName); 
		               
		               dos = new DataOutputStream(conn.getOutputStream());
		     
		               dos.writeBytes(twoHyphens + boundary + lineEnd); 
		               dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
		            		                     + zipFileName + "\"" + lineEnd);
		               
		               dos.writeBytes(lineEnd);
		     
		               // create a buffer of  maximum size
		               bytesAvailable = fileInputStream.available(); 
		     
		               bufferSize = Math.min(bytesAvailable, maxBufferSize);
		               buffer = new byte[bufferSize];
		     
		               // read file and write it into form...
		               bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
		                 
		               while (bytesRead > 0) 
		               {
		            	 dos.write(buffer, 0, bufferSize);
		                 bytesAvailable = fileInputStream.available();
		                 bufferSize = Math.min(bytesAvailable, maxBufferSize);
		                 bytesRead = fileInputStream.read(buffer, 0, bufferSize);   
		               }
		     
		               // send multipart form data necesssary after file data...
		               dos.writeBytes(lineEnd);
		               dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
		     
		               // Responses from the server (code and message)
		               serverResponseCode = conn.getResponseCode();
		               String serverResponseMessage = conn.getResponseMessage();
		                
		               Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
		               
		               if(serverResponseCode == 200)
		               {
						  /* dbengine.open();
						   dbengine.upDateXMLFileFlag(fileUri, 4);
						   dbengine.close();*/
						   
						   		//new File(dir, fileUri).delete();
		            	   
		            	   SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE); 
		            	   Editor editor = pref.edit();
		            	  // editor.remove(xmlForWeb[0]);
		            	   editor.putString(fileUri, ""+4); 
		            	   editor.commit();
		            	   
		            	   String FileSyncFlag=pref.getString(fileUri, ""+1); 
		            	   
						   		delXML(xmlForWeb[0].toString());
						   		/*dbengine.open();
					            dbengine.deleteXMLFileRow(fileUri);
					            dbengine.close();*/
					           
					   }    
		               
		               //close the streams //
		               fileInputStream.close();
		               dos.flush();
		               dos.close();
		                
		          } catch (MalformedURLException ex) 
		          {
		        	  ex.printStackTrace();
		          } catch (Exception e)
		          {
		        	 e.printStackTrace();
		           }
						
			   
		
		 
			  return serverResponseCode;  

			 }
	
	public void delXML(String delPath)
	{
		File file = new File(delPath);
	    file.delete();
	    File file1 = new File(delPath.toString().replace(".xml", ".zip"));
	    file1.delete();
	}

}
