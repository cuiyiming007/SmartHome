package com.gdgl.manager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import android.util.Log;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.gdgl.app.ApplicationController;
import com.gdgl.mydata.Event;
import com.gdgl.mydata.EventType;
import com.gdgl.mydata.SimpleResponseData;
import com.gdgl.network.CustomRequest;


public class Manger {
	
    private boolean changed = false;
    
    private Vector observers;
    
    /**
     * observers
     */
    public Manger()
    {
        observers = new Vector();
    }
    public synchronized void addObserver(UIListener o)
    {
        if (o == null)
        {
            throw new NullPointerException();
        }
        if (!observers.contains(o))
        {
            observers.addElement(o);
        }
    }
    
    public synchronized void deleteObserver(UIListener o)
    {
        observers.removeElement(o);
    }
    
    public void notifyObservers()
    {
        notifyObservers(null);
    }
    
    /**
     * notify all the changes observer
     * 
     * @param data
     *            event
     */
    public void notifyObservers(Object data)
    {
        
//        synchronized (this)
//        {
//            
//            if (!changed)
//            {
//                return;
//            }
//            clearChanged();
//        }
        
        for (int i = 0; i < observers.size(); i++)
        {
            try
            {
                ((UIListener) observers.elementAt(i)).update(this,data);
            }
            catch (Exception e)
            {
            }
        }
    }
    
    /**
     * delete all the observers
     */
    public synchronized void deleteObservers()
    {
        observers.removeAllElements();
    }
    
    /**
     * set the all the observers changed
     */
    public synchronized void setChanged()
    {
        changed = true;
    }
    
    /**
     * set the all the observers not changed
     */
    protected synchronized void clearChanged()
    {
        changed = false;
    }
    
    /**
     * the
     * 
     * @return true is changed ,false not changed
     */
    public synchronized boolean hasChanged()
    {
        return changed;
    }
    
    /**
     * how many Observers which the object have
     * 
     * @return how many Observers which the object have
     */
    public synchronized int countObservers()
    {
        return observers.size();
    }
    public void simpleVolleyRequset(String url,final EventType type) {
    	
		Listener<SimpleResponseData> respondListener = new Listener<SimpleResponseData>() {
			@Override
			public void onResponse(SimpleResponseData arg0) {
				SimpleResponseData data = arg0;
				Log.i("onResponse", data.toString());
				Event event = new Event(type, true);
				event.setData(data);
				notifyObservers(event);
			}
			
		};
		ErrorListener errorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("Error: ", error.getMessage());
				Event event = new Event(type, false);
				event.setData(error);
				notifyObservers(event);
			}
		};
		
		
		Log.i("request url", url);
		CustomRequest<SimpleResponseData> request = new CustomRequest<SimpleResponseData>(
				url, "response_params", SimpleResponseData.class,
				respondListener, errorListener);
		ApplicationController.getInstance().addToRequestQueue(request);
	}
    public String hashMap2ParamString(HashMap<String, String> map)
	{
		if (map==null||map.isEmpty()) {
			return "";
		}
		StringBuilder para = new StringBuilder();
		Iterator<Entry<String, String>> iterator= map.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<java.lang.String, java.lang.String> entry = (Map.Entry<java.lang.String, java.lang.String>) iterator
					.next();
			para.append(entry.getKey());
			para.append("=");
			para.append(entry.getValue());
			para.append("&");
		}
		para.deleteCharAt(para.length()-1);
		return para.toString();
	}
    
}
