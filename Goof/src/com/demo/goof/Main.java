package com.demo.goof;

import android.support.v7.app.ActionBarActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends ActionBarActivity {
	public static final String DATABASE_NAME = "goof.sqlite";
	public static final int MAX_BUFFER_SIZE = 1024 * 8;
	private TextView textView;
	private ListView listView;
	private GoofListViewAdapter adapter;
	private List<String>items;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        textView = (TextView)findViewById(R.id.textview_debug_output);
        listView = (ListView)findViewById(R.id.listview);
        new File(this.getDatabasePath(Main.DATABASE_NAME).getAbsolutePath()).delete();
        textView.setText("checking if database is already installed\n");
        if (!checkDataInstallation()) {
        	textView.setText(textView.getText() + "Not found! installing fresh copy of database\n");
        	performDataInstallation();
        }
        
        loadDatabaseData();
        populateData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void loadDatabaseData() {
    	items = new ArrayList<String>();
    	SQLiteOpenHelper helper = new GoofSQLiteOpenHelper(this,Main.DATABASE_NAME,null,1);
    	SQLiteDatabase db = helper.getReadableDatabase();
    	if(db != null) {
        	Cursor cursor = db.rawQuery("SELECT id,name FROM user", null);
        	if (cursor != null) {
        		cursor.moveToFirst();
        		do {
        			this.items.add(cursor.getString(1));
        		} while (cursor.moveToNext());
        	}    		
    	}
   }
    
    private void populateData() {
    	adapter = new GoofListViewAdapter(items,this);
    	listView.setAdapter(adapter);
    }
    
    private boolean checkDataInstallation() {
    	if (this.getDatabasePath(Main.DATABASE_NAME).exists()) {
        	textView.setText(textView.getText() + "Okay.. got a copy already!\n");
    		return true;
    	} else {
        	textView.setText(textView.getText() + "Nothing found?\n");
    	    return false;
    	}
    }
    
    private void performDataInstallation() {
    	FileOutputStream fileOutput = null;
    	File file = null;
    	InputStream fileInput = null;
    	byte []dataBuffer = null;
    	try {
    	    file = this.getDatabasePath(Main.DATABASE_NAME);
    	    String databasePath = this.getDatabasePath(Main.DATABASE_NAME).getAbsolutePath();
        	textView.setText(textView.getText() + databasePath + "\n");
        	databasePath = databasePath.substring(0,(databasePath.length() - Main.DATABASE_NAME.length())-1);
        	File databaseDir = new File(databasePath); 
        	if (!databaseDir.exists()) {
        		databaseDir.mkdirs();
        	}
        	databaseDir = null;
        	
        	textView.setText(textView.getText() + databasePath + "\n");        	
        	fileOutput = new FileOutputStream(file);
        	textView.setText(textView.getText() + "Reading file from assets folder\n");
			fileInput = this.getAssets().open(Main.DATABASE_NAME);
			if (fileInput != null) {
				int bytesRead = -1;
				dataBuffer = new byte[Main.MAX_BUFFER_SIZE+1];
	        	textView.setText(textView.getText() + "Writing file data\n");
				while ((bytesRead = fileInput.read(dataBuffer)) != -1) {
					fileOutput.write(dataBuffer, 0, bytesRead);
				}
				textView.setText(textView.getText() + "database installation was successful\n");
			}
    	} catch (Exception exception) {
    		exception.printStackTrace();
    	} finally {
    	    if (fileOutput != null) {
    	    	try {
    	    		fileOutput.close();
    	    	} catch(Exception exception) {
    	    	}
    	    	fileOutput = null;
    	    }
    	    if (fileInput != null) {
    	    	try {
    	    		fileInput.close();
    	    	} catch (Exception excpetion) {
    	    	}
    	    	fileInput = null;
    	    }
    	    file = null;
    	    dataBuffer = null;
    	}
    }
}
