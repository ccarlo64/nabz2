package com.homeproject.nabz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class HttpLento extends AsyncTask<String, Void, String> {
	
	private String risultato=".";
	private String comando="x";
	private String token=".";

	    @Override
        protected String doInBackground(String... urls) {
 
	        String tmp="";
	        // array to string..
	        for(int i=0;i<urls.length;i++) {
	        	tmp+=urls[i];
	        }
	        //Log.d("urlrichiesto :",tmp);
	        Globals g = Globals.getInstance();
	        g.setLastURL( tmp );	// *************** DEBUG
	
			if (tmp.contains("accounts")) { this.comando ="a"; }
			if (tmp.contains("getListOfBunnies")) { this.comando ="b"; }
			if (tmp.contains("getListOfConnectedBunnies")) { this.comando ="c"; }

            return GET(urls[0]);
        }

		// onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();

        	Globals g = Globals.getInstance();
        	String server = g.getServer();
        			
            if (this.comando.equals("a")){
            	Pattern pattern = Pattern.compile("<value>(.*?)</value>");
                Matcher matcher = pattern.matcher(result);
                while (matcher.find()) {
                	String t = matcher.group(1);
                    g.setToken(t);
					//Log.d("httplento","cercatoken: " +t);
//TODO
                    String urlPlugin = "";//"http://openznab.it";//campi[2]; //server
					urlPlugin = server + "/ojn_api/bunnies/getListOfBunnies?token=" + t;
//					urlPlugin = urlPlugin + t;
					urlPlugin = urlPlugin.replace(" ", "%20");
					new HttpLento().execute(urlPlugin);
                }
            }
            if (this.comando.equals("b")){
            	
            	g.clearConigli();
            	
            	Pattern pattern = Pattern.compile("<value>(.*?)</value>");
                Matcher matcher = pattern.matcher(result);
                while (matcher.find()) {
//Log.d("httplento","numero conigli: "+matcher.group(1));
                	g.setConigliNome(matcher.group(1));
                }
            	pattern = Pattern.compile("<key>(.*?)</key>");
                matcher = pattern.matcher(result);
                while (matcher.find()) {
//Log.d("httplento","Mac conigli: "+matcher.group(1));
                	g.setConigliMac(matcher.group(1));
                }
                String t = g.getToken();
//TODO
                String urlPlugin = "";//"http://openznab.it";//campi[2]; //server
                urlPlugin = server + "/ojn_api/bunnies/getListOfConnectedBunnies?token=" + t;
//                urlPlugin = urlPlugin + t;
                urlPlugin = urlPlugin.replace(" ", "%20");
                new HttpLento().execute(urlPlugin);
            }
            if (this.comando.equals("c")){
            	
//            	Log.d("httplento","cerco numero conigli connessi");
            	//------------------------
            	ArrayList<String> conigliOnLine = new ArrayList<String>();

            	Pattern pattern = Pattern.compile("<value>(.*?)</value>");
                Matcher matcher = pattern.matcher(result);
                while (matcher.find()) {
					conigliOnLine.add(matcher.group(1));
                }
                //
                Boolean trovato;
            	ArrayList<String> elenco = g.getConigliNome();
            	for(int ii=0;ii<elenco.size();ii++) {
            		trovato=false;
            		for(int i=0;i<conigliOnLine.size();i++){
                	  if (conigliOnLine.get(i).equals(elenco.get(ii))) {
                		  //trovato
                		  g.setConigliOnLine(true);
                		  trovato = true;
                	  }
            		}
            		if (!trovato) { 
            			g.setConigliOnLine(false); 
            		}
                }
              }            
//            Log.d("la risposta :",result);
            g.setLastResponse(result); // ************DEBUG
       }
        
		public String getToken()
		{
		  return this.token;	
		}
		public String getRisultato()
		{
			  return this.risultato;	
			}
		public void setRisultato(String r) {
		  this.risultato = r;
		  return;
		}

        private static String GET(String url){
            InputStream inputStream = null;
            String result = "";
            try {
     
                // create HttpClient
                HttpClient httpclient = new DefaultHttpClient();
     
                // make GET request to the given URL
                HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
     
                // receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();
     
                // convert inputstream to string
                if(inputStream != null)
                    result = convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";
     
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
     
            return result;
        }
        
        // convert inputstream to String
        private static String convertInputStreamToString(InputStream inputStream) throws IOException{
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while((line = bufferedReader.readLine()) != null)
                result += line;
     
            inputStream.close();
            return result;
     
        }
        

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
         // Things to be done before execution of long running operation. For
         // example showing ProgessDialog
        }

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#onProgressUpdate(Progress[])
         */
        /*
        @Override
        protected void onProgressUpdate(String... text) {
         
         // Things to be done while execution of long running operation is in
         // progress. For example updating ProgessDialog
        }
        */

}
