package com.example.nero.barcodescanner.com.nero.service;

import android.os.Bundle;
import android.os.Message;

import org.json.JSONException;

import java.io.Serializable;
import java.security.Provider;
import java.util.ArrayList;

/**
 * Created by Nero on 07/07/2017.
 */

public abstract class AbstractService implements Serializable, Runnable{
    private ArrayList<ServiceListener> listeners;
    private boolean error;

    public AbstractService(){
        listeners = new ArrayList<>();
    }

    public void addListener(ServiceListener serviceListener){
        listeners.add(serviceListener);
    }

    public void removeListener(ServiceListener serviceListener){
        listeners.remove(serviceListener);
    }

    public boolean hasError(){
        return error;
    }

    public void serviceCallComplete(boolean error){
        this.error = error;

        Message m = _handler.obtainMessage();
        Bundle b = new Bundle();
        b.putSerializable("service", this);
        m.setData(b);
        _handler.sendMessage(m);
    }

    final android.os.Handler _handler = new android.os.Handler(){
        public void handleMessage(Message msg){
            AbstractService service = (AbstractService) msg.getData().getSerializable("service");

            for(ServiceListener serviceListener : service.listeners){
                try{
                    serviceListener.serviceComplete(service);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    };

}
