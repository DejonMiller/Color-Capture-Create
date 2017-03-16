package com.developer.andersonmiller.opencv;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;


public class RecordIntentJSONSerializer {

    private Context mContext;
    private String mFilename;

    public RecordIntentJSONSerializer(Context c, String f){
        mContext = c;
        mFilename = f;
    }


    public ArrayList<Entry> loadRecords() throws IOException, JSONException {
        ArrayList<Entry> records = new ArrayList<Entry>();
        BufferedReader reader = null;
        try {
            InputStream in = mContext.openFileInput(mFilename);
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            //builds array of crimes from jsonobjects
            for (int i = 0; i < array.length(); i++) {
                records.add(new Entry(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
        } finally {
            if (reader != null)
                reader.close();
        }
        return records;
    }

    public void saveRecords(ArrayList<Entry> records)
            throws JSONException, IOException {
        JSONArray array = new JSONArray();
        for(Entry c: records)
            array.put(c.toJSON());

        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer.write(array.toString());
        } finally {
            if (writer != null)
                writer.close();
        }
    }
}

