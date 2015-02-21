package com.homeproject.nabz;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity
{
	private ListView lv;
	final Context context = this;  
 //	String urlPlugin = null;
	DatabaseHelper databaseHelper = new DatabaseHelper(this);
    TextView tvSonoConnesso;
    
    Globals g = Globals.getInstance();

    Spinner mySpinnerGenerico; 
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    
        tvSonoConnesso = (TextView) findViewById(R.id.tvSonoConnesso);
       
        lv = (ListView) findViewById(R.id.listView1);
       
        String[] pi = getResources().getStringArray(R.array.array_plugin); 
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, pi);        
        lv.setAdapter(adapter);
        
        
        if(isConnected()){
        	//tvSonoConnesso.setBackgroundColor(0xFF00CC00);
        	tvSonoConnesso.setText(R.string.net_ok);//"connessione avvenuta...");
        }
        else{
        	tvSonoConnesso.setText(R.string.net_ko);//"problemi di connessione...");
        }

 
        String[] tmp=null;
		tmp = databaseHelper.getC();
		g.setServer(tmp[2]);
		//Log.d("","");
        String urlo=null;
/*
 * CONNESSIONE
 */
        urlo = tmp[2] + "/ojn_api/accounts/auth?login=" + tmp[0] + "&pass=" + tmp[1];
        new HttpLento().execute( urlo );

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
               int position, long id) {
              
             /*
              Toast.makeText(getApplicationContext(),
                "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                .show();
                */              
    			Button dialogButton;
    			final Dialog dialog = new Dialog(context);
              
			switch (position) {
              case 0:
				// *************************************
				// ORECCHIE
				// *************************************
				
				dialog.setContentView(R.layout.plugin_ambient_ear);
				dialog.setTitle(R.string.plugin_title1);//"Ambient ears");
				dialogButton = (Button) dialog.findViewById(R.id.bntAmbientEar);

				mySpinnerGenerico = (Spinner) dialog.findViewById(R.id.spAmbientEar);
                List <ListBunnies> listLR = new LinkedList <ListBunnies>();
                for(int i=0;i<g.getConigliNome().size();i++) {
                  listLR.add(new ListBunnies(g.getConigliNome().get(i),g.getConigliMac().get(i),g.getConigliOnLine().get(i),false));
                }
                mySpinnerGenerico.setAdapter(new MyAdapterBunnies(getApplicationContext(), R.layout.custom_spinner,listLR));//spinnerValues
				
				SeekBar seekBarLeft = (SeekBar)dialog.findViewById(R.id.sbLeft); 
				final TextView seekBarValueLeft = (TextView)dialog.findViewById(R.id.textSeekBarSx); 
				
				seekBarLeft.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){ 
				   @Override 
				   public void onProgressChanged(SeekBar seekBarLeft, int progress, 
				     boolean fromUser) { 
				    // TODO Auto-generated method stub 
				    seekBarValueLeft.setText(String.valueOf(progress)); 
				   } 
				
				   @Override 
				   public void onStartTrackingTouch(SeekBar seekBar) { 
				    // TODO Auto-generated method stub 
				   } 
				
				   @Override 
				   public void onStopTrackingTouch(SeekBar seekBar) { 
				    // TODO Auto-generated method stub 
				   } 
				   });
				
				SeekBar seekBarRight = (SeekBar)dialog.findViewById(R.id.sbRight); 
				final TextView seekBarValueRight = (TextView)dialog.findViewById(R.id.textSeekBarDx); 
				seekBarRight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){ 
				   @Override 
				   public void onProgressChanged(SeekBar seekBarRight, int progress, 
				     boolean fromUser) { 
				    // TODO Auto-generated method stub 
				    seekBarValueRight.setText(String.valueOf(progress)); 
				   } 
				
				   @Override 
				   public void onStartTrackingTouch(SeekBar seekBar) { 
				    // TODO Auto-generated method stub 
				   } 
				
				   @Override 
				   public void onStopTrackingTouch(SeekBar seekBar) { 
				    // TODO Auto-generated method stub 
				   } 
				 }); 


				dialogButton.setOnClickListener(new OnClickListener() {
          		    @Override
          		    public void onClick(View v) {
          			   
					ListBunnies coniglioScelto = (ListBunnies) mySpinnerGenerico.getSelectedItem();
					
					String urlPlugin = g.getServer();//tmp[2]; //server
					urlPlugin = urlPlugin + "/ojn_api/bunny/";
					urlPlugin = urlPlugin + coniglioScelto.getMac();///tmp[3]; //mac
					urlPlugin = urlPlugin + "/packet/sendPacket?data=";
										
					TextView tv;
					tv = (TextView) dialog.findViewById(R.id.textSeekBarSx);
					String sbSx = (String) tv.getText().toString();
					tv = (TextView) dialog.findViewById(R.id.textSeekBarDx);
					String sbDx = (String) tv.getText().toString();
					sbSx = String.format( "%02X", Integer.parseInt(sbSx));
					//Integer.toHexString( Integer.parseInt(sbSx));
					sbDx = String.format( "%02X", Integer.parseInt(sbDx));
                      /*
                       * String.format("#%x", number)
                	  $pacchetto  = pack("C",0x7F);
                	  $pacchetto .= pack("C",0x04); //ambient
                	  $pacchetto .= pack("C",0x00);
                	  $pacchetto .= pack("C",0x00);
                	  $pacchetto .= pack("C",0x08);//lunghezza dati
                	  $pacchetto .= pack("C",0x7F);
                	  $pacchetto .= pack("C",0xFF);
                	  $pacchetto .= pack("C",0xFF);
                	  $pacchetto .= pack("C",0xFE);
                	  $pacchetto .= pack("C",0x04); //orecchio dx
                	  $pacchetto .= pack("C",$dx);
                	  $pacchetto .= pack("C",0x05); //orecchio sx
                	  $pacchetto .= pack("C",$sx);
                	  $pacchetto .= pack("C",0xFF);
                      */
					urlPlugin = urlPlugin + "7F040000087FFFFFFE04" + sbDx + "05" + sbSx + "FF";
					urlPlugin = urlPlugin + "&token=" + g.getToken();//token
					urlPlugin = urlPlugin.replace(" ", "%20");
					new HttpLento().execute(urlPlugin);	
					//	Toast.makeText(getApplicationContext(), "send to bunny.....", Toast.LENGTH_SHORT).show();
					dialog.dismiss();
          		}
				});
				dialog.show();              
				break;
				// *************************************** ENDORECCHIE
  
 
              case 1:
        		// *************************************
         		// TESTO
        		// *************************************
        		dialog.setContentView(R.layout.plugin_send_text);
        		dialog.setTitle(R.string.plugin_title2);
        		dialogButton = (Button) dialog.findViewById(R.id.bntSendText);

				mySpinnerGenerico = (Spinner) dialog.findViewById(R.id.spSendText);
                List <ListBunnies> list = new LinkedList <ListBunnies>();
                for(int i=0;i<g.getConigliNome().size();i++)
                {
                  list.add(new ListBunnies(g.getConigliNome().get(i),g.getConigliMac().get(i),g.getConigliOnLine().get(i),false));
                }
                mySpinnerGenerico.setAdapter(new MyAdapterBunnies(getApplicationContext(), R.layout.custom_spinner,list));//spinnerValues
        		
             	dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
				
				String urlPlugin = g.getServer();//tmp[2]; //server
				urlPlugin = urlPlugin + "/ojn_api/bunny/";
				
				ListBunnies coniglioScelto = (ListBunnies) mySpinnerGenerico.getSelectedItem();
				
				//Globals g = Globals.getInstance();
                String token=g.getToken();
				urlPlugin = urlPlugin + coniglioScelto.getMac();///tmp[3]; //mac
				urlPlugin = urlPlugin + "/tts/say?text=";
				EditText et = (EditText) dialog.findViewById(R.id.edSendText);
				String tmpx = et.getText().toString();
				urlPlugin = urlPlugin + tmpx;
                
                urlPlugin = urlPlugin + "&token=" + token;
				urlPlugin = urlPlugin.replace(" ", "%20");
				new HttpLento().execute(urlPlugin);	
//	              		        Toast.makeText(getApplicationContext(), "send to bunny.....", Toast.LENGTH_SHORT).show();
	            dialog.dismiss();
	            }
	            });
	      		dialog.show();              
        		break;
            	  // ***************************************ENDTESTO
              case 2:
        		// *************************************
        		// Led respirazione
        		// *************************************

        		dialog.setContentView(R.layout.plugin_led_resipro);
        		dialog.setTitle(R.string.plugin_title3);
        		
        		mySpinnerGenerico = (Spinner) dialog.findViewById(R.id.spLedRespiro);
                List <ListBunnies> listAE = new LinkedList <ListBunnies>();
                for(int i=0;i<g.getConigliNome().size();i++)
                {
                  listAE.add(new ListBunnies(g.getConigliNome().get(i),g.getConigliMac().get(i),g.getConigliOnLine().get(i),false));
                }
                mySpinnerGenerico.setAdapter(new MyAdapterBunnies(getApplicationContext(), R.layout.custom_spinner,listAE));//spinnerValues

        		Spinner spinner = (Spinner) dialog.findViewById(R.id.spColori);
         	    //String[] colori = getResources().getStringArray(R.array.array_colori); 
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
        		        R.array.array_colori, android.R.layout.simple_spinner_item);
        		// Specify the layout to use when the list of choices appears
        		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        		// Apply the adapter to the spinner
        		spinner.setAdapter(adapter);
        		  
        		dialogButton = (Button) dialog.findViewById(R.id.bntSendLedRespiro);

	         	dialogButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

					String urlPlugin = g.getServer();//tmp[2]; //server
					urlPlugin = urlPlugin + "/ojn_api/bunny/";
						
					ListBunnies coniglioScelto = (ListBunnies) mySpinnerGenerico.getSelectedItem();
					String token=g.getToken();
					urlPlugin = urlPlugin + coniglioScelto.getMac();///tmp[3]; //mac

					urlPlugin = urlPlugin + "/colorbreathing/setColor?name=";
										 
					Spinner spinner = (Spinner) dialog.findViewById(R.id.spColori);
					String tmpx = String.valueOf(spinner.getSelectedItem());
					
					urlPlugin = urlPlugin + tmpx;
					urlPlugin = urlPlugin + "&token=" + token;
					urlPlugin = urlPlugin.replace(" ", "%20");
									   
					new HttpLento().execute(urlPlugin);	
				   
//				    Toast.makeText(getApplicationContext(), "send to bunny.....", Toast.LENGTH_SHORT).show();
					dialog.dismiss();
				   }
				   });
    	  		  dialog.show();              
            	  break;
            	  
            	  
              case 3:
        		// *************************************
         		// NASO
        		// *************************************
        		dialog.setContentView(R.layout.plugin_naso);
        		dialog.setTitle(R.string.plugin_title4);
        		dialogButton = (Button) dialog.findViewById(R.id.bntNaso);


        		mySpinnerGenerico = (Spinner) dialog.findViewById(R.id.spSendText);
                List <ListBunnies> listNaso = new LinkedList <ListBunnies>();
                for(int i=0;i<g.getConigliNome().size();i++)
                {
                  listNaso.add(new ListBunnies(g.getConigliNome().get(i),g.getConigliMac().get(i),g.getConigliOnLine().get(i),false));
                }
                mySpinnerGenerico.setAdapter(new MyAdapterBunnies(getApplicationContext(), R.layout.custom_spinner,listNaso));//spinnerValues
        		
             	dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
        		RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radioGroupNaso);
        		int radioButtonID = rg.getCheckedRadioButtonId();
        		View radioButton = rg.findViewById(radioButtonID);
        		int numeroLampeggi = rg.indexOfChild(radioButton);
					
				String urlPlugin = g.getServer();//tmp[2]; //server
				urlPlugin = urlPlugin + "/ojn_api/bunny/";

				ListBunnies coniglioScelto = (ListBunnies) mySpinnerGenerico.getSelectedItem();
//Log.d("spinner",coniglioScelto.getMac());

                String token=g.getToken();
				urlPlugin = urlPlugin + coniglioScelto.getMac();///tmp[3]; //mac
				urlPlugin = urlPlugin + "/packet/sendPacket?data=";
									
				String valoreNaso = String.format( "%02X", numeroLampeggi);
// Clignotement du nez
// disable, clignote, double clignote
//
				urlPlugin = urlPlugin + "7F040000067FFFFFFE08" + valoreNaso + "FF";
				urlPlugin = urlPlugin + "&token=" + token;
				urlPlugin = urlPlugin.replace(" ", "%20");
				new HttpLento().execute(urlPlugin);	
   		        //Toast.makeText(getApplicationContext(), "naso.....", Toast.LENGTH_SHORT).show();
	            dialog.dismiss();
	            }
	            });
	      		dialog.show();              
        		break;
            	  // ***************************************ENDNASO
              case 4:
        		// *************************************
         		// SLEEP
        		// *************************************
          		dialog.setContentView(R.layout.plugin_sleep_wakeup);
          		dialog.setTitle(R.string.plugin_title5);
          		dialogButton = (Button) dialog.findViewById(R.id.bntSleepWakeup);


          		mySpinnerGenerico = (Spinner) dialog.findViewById(R.id.spSleepWakeup);
                  List <ListBunnies> listSleepWakeup = new LinkedList <ListBunnies>();
                  for(int i=0;i<g.getConigliNome().size();i++)
                  {
                	  listSleepWakeup.add(new ListBunnies(g.getConigliNome().get(i),g.getConigliMac().get(i),g.getConigliOnLine().get(i),false));
                  }
                  mySpinnerGenerico.setAdapter(new MyAdapterBunnies(getApplicationContext(), R.layout.custom_spinner,listSleepWakeup));
          		
               	dialogButton.setOnClickListener(new OnClickListener() {
  				@Override
  				public void onClick(View v) {
  					
          		RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radioGroupSleepWakeup);
          		int radioButtonID = rg.getCheckedRadioButtonId();
          		View radioButton = rg.findViewById(radioButtonID);
          		int sleepWakeup = rg.indexOfChild(radioButton);
  					
  				String urlPlugin = g.getServer();//tmp[2]; //server
  				urlPlugin = urlPlugin + "/ojn_api/bunny/";

  				ListBunnies coniglioScelto = (ListBunnies) mySpinnerGenerico.getSelectedItem();
  //Log.d("spinner",coniglioScelto.getMac());

                String token=g.getToken();
  				urlPlugin = urlPlugin + coniglioScelto.getMac();///tmp[3]; //mac
				if (sleepWakeup==0) {
				  	urlPlugin = urlPlugin + "/sleep/sleep?";
				}
				else {
					urlPlugin = urlPlugin + "/sleep/wakeup?";
				}
  									
  				urlPlugin = urlPlugin + "token=" + token;
  				urlPlugin = urlPlugin.replace(" ", "%20");
  				new HttpLento().execute(urlPlugin);	
     		        //Toast.makeText(getApplicationContext(), "naso.....", Toast.LENGTH_SHORT).show();
  	            dialog.dismiss();
  	            }
  	            });
  	      		dialog.show();              

            	  break;
            	  // ***************************************ENDSLEEP

        		
               default:
         			// *************************************
         			//
         			// *************************************


                    break;              
              }

            }
		});

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.debug) {

           final Dialog dialog = new Dialog(context);
           // Include dialog.xml file
           dialog.setContentView(R.layout.debug);
           // Set dialog title
           dialog.setTitle("Debug!");

           String lastURL = g.getLastURL();
           TextView text1 = (TextView) dialog.findViewById(R.id.textDebugURL);
           text1.setText(lastURL);
           String lastResponse = g.getLastResponse();
           TextView text2 = (TextView) dialog.findViewById(R.id.textDebugResponse);
           text2.setText(lastResponse);

           dialog.show();
            
           Button declineButton = (Button) dialog.findViewById(R.id.bntDebugOk);
           // if decline button is clicked, close the custom dialog
           declineButton.setOnClickListener(new OnClickListener() {
               @Override
               public void onClick(View v) {
                   // Close dialog
                   dialog.dismiss();
               }
           });        	
       	
       }
        
        
        if (id == R.id.about) {
        	 // Crea custom dialog object
            final Dialog dialog = new Dialog(context);
            // layout
            dialog.setContentView(R.layout.about);
            // Set title
            //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setTitle("About");

            TextView text = (TextView) dialog.findViewById(R.id.textDialog);
            text.setText("Nabz version 1.0 :-) by carlo64");

            dialog.show();
             
            Button declineButton = (Button) dialog.findViewById(R.id.declineButton);
            // click ...esco...
            declineButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Close dialog
                    dialog.dismiss();
                }
            });        	
        	
        }
        
        if (id == R.id.action_settings) {

        	
            //Toast.makeText(getApplicationContext(),"settaggi",Toast.LENGTH_LONG).show();  
    		// *************************************
    		//
    		// *************************************
    		final Dialog dialog = new Dialog(context);
    		dialog.setContentView(R.layout.custom);
    		dialog.setTitle(R.string.dialog_setting);

    		String[] tmp=null;
    		tmp = databaseHelper.getC();
    		
    		EditText loginUtente= (EditText) dialog.findViewById(R.id.utente);
    		loginUtente.setText(tmp[0]);
    		
    		EditText loginPassword= (EditText) dialog.findViewById(R.id.password);
    		loginPassword.setText(tmp[1]);

    		EditText loginServer= (EditText) dialog.findViewById(R.id.server);
    		loginServer.setText(tmp[2]);

    		/*EditText loginMac= (EditText) dialog.findViewById(R.id.coniglio);
    		loginMac.setText(tmp[3]);*/
    		
    		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
    		// click... esco
    		dialogButton.setOnClickListener(new OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				dialog.dismiss();
    			}
    		});

    		
    		Button dialogSalva = (Button) dialog.findViewById(R.id.salvaDati);
    		dialogSalva.setOnClickListener(new OnClickListener() {
    			
    			@Override
    			public void onClick(View arg0) {
    				// TODO Auto-generated method stub
    				String a1=null;
    				EditText loginUtente= (EditText) dialog.findViewById(R.id.utente);
    				a1 = loginUtente.getText().toString();
    				String a2=null;
    				EditText loginPassword= (EditText) dialog.findViewById(R.id.password);
    				a2 = loginPassword.getText().toString();
    				String a3=null;
    				EditText loginServer= (EditText) dialog.findViewById(R.id.server);
    				a3 = loginServer.getText().toString();
    				/*String a4=null;
    				EditText loginMac= (EditText) dialog.findViewById(R.id.coniglio);
    				a4 = loginMac.getText().toString();*/
    				databaseHelper.updateDati(a1,a2,a3);//,a4);

    				Toast.makeText(getApplicationContext(), R.string.update_ok, Toast.LENGTH_SHORT).show();						

    		        String urlo=null;
    		        urlo = a3 + "/ojn_api/accounts/auth?login=" + a1 + "&pass=" + a2;
    		        new HttpLento().execute( urlo );    				
    				dialog.dismiss();
    			}
    		});
    		dialog.show();
            
            
            
            
            
            return true;     
        }
        return super.onOptionsItemSelected(item);
    }



    
  
      // check network connection
      public boolean isConnected(){
          ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
              NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
              if (networkInfo != null && networkInfo.isConnected())
                  return true;
              else
                  return false;  
      }    

      public class MyAdapterBunnies extends ArrayAdapter<ListBunnies> {

//    	    //Globals g = Globals.getInstance();
      		public MyAdapterBunnies(Context ctx, int txtViewResourceId, List <ListBunnies> objects) {
      			super(ctx, txtViewResourceId, objects);
      		}

      		@Override
      		public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
      			return getCustomView(position, cnvtView, prnt);
      		}
      		@Override
      		public View getView(int pos, View cnvtView, ViewGroup prnt) {
      			return getCustomView(pos, cnvtView, prnt);
      		}
      		public View getCustomView(int position, View convertView,
      				ViewGroup parent) {
      			//LayoutInflater inflater = getLayoutInflater();
      			
      			
      		    View mySpinner = convertView;

      		    if (mySpinner == null) {

      		      LayoutInflater vi;
      		      vi = LayoutInflater.from(getContext());
      		      mySpinner = vi.inflate(R.layout.custom_spinner, null);

      		    }
      			
  /*    			
      			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
      			View mySpinner = inflater.inflate(R.layout.custom_spinner, parent,
      					false);
*/
      			ListBunnies ite = getItem(position);
      			TextView main_text = (TextView) mySpinner
      					.findViewById(R.id.text_main_seen);
      			//main_text.setText(spinnerValues[position]);
                String o=" ("+getResources().getString(R.string.coniglio_off)+")";
      			if(ite.getOnLine()){ o=" ("+getResources().getString(R.string.coniglio_on)+")"; }
      			//subSpinner.setBackgroundColor(Color.YELLOW);
      			main_text.setTextColor(Color.BLACK);
     			main_text.setText(ite.getNome()+o);

      			TextView subSpinner = (TextView) mySpinner
      					.findViewById(R.id.sub_text_seen);
//      			subSpinner.setText(spinnerSubs[position]);
      			subSpinner.setTextColor(Color.BLUE);
      			subSpinner.setText(ite.getMac());

      			ImageView left_icon = (ImageView) mySpinner
      					.findViewById(R.id.left_pic);
      			
      			if(ite.getOnLine()){ 
          			left_icon.setImageResource(R.drawable.ic_nano);//total_images[position]);
      			}
      			else {
          			left_icon.setImageResource(R.drawable.ic_nanoff);//total_images[position]);
      			}      			
      			
      			

      			return mySpinner;
      		}
      	}


}


