package com.developer.andersonmiller.opencv;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;


public class Entry {

    //RECORD
    private UUID mId;
    private String mTitle;
    //private Date mDate;
    //private String mBody;
    private boolean mSolved;
    private static final String JSON_ID= "id";
    private static final String JSON_TITLE="title";
    private static final String JSON_DATE ="date";
    private static final String JSON_SOlVED ="solved";

    public Entry(JSONObject json) throws JSONException {
        mId = UUID.fromString(json.getString(JSON_ID));
        if(json.has(JSON_TITLE)) {
            mTitle = json.getString(JSON_TITLE);
        }
        // mSolved = json.getBoolean(JSON_SOlVED);
     //   mDate = new Date(json.getLong(JSON_DATE));
    }


    public Entry(){
        mId = UUID.randomUUID();
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_TITLE, mTitle);
        json.put(JSON_ID, mId);
       // json.put(JSON_DATE, mDate);
        // json.put(JSON_SOlVED, mSolved);
        return json;
    }

    @Override
    public String toString(){
        return mTitle;
    }

    public UUID getId(){
        return mId;
    }

    public String getTitle(){
        return mTitle;
    }

    public void setTitle(String title){
        mTitle=title;
    }
/*
    public String getBody(){
        return mBody;
    }

    public void setBody (String body){
        mBody = body;
    }

    public Date getDate(){
        return mDate;
    }

    public void setDate(Date date){
        mDate = date;
    }

    public boolean isSolved(){
        return mSolved;
    }
*/
    public void setSolved(boolean solved){
        mSolved = solved;
    }



}
