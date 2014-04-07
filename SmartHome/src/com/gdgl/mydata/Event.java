package com.gdgl.mydata;

/**
 * Event used to notify the UI.
 * 
 * 
 */
public class Event
{
    /**
     * Event type.
     */
    private EventType type;
    
    /**
     * If the task success.
     */
    private boolean isSuccess = false;
    
    /**
     * Additional data.
     */
    private Object data;
    
    /**
     * Constructor.
     * 
     * @param type
     *            type of the event.
     * @param isSuccess
     *            event is success or not.
     */
    public Event(EventType type, boolean isSuccess)
    {
        this.type = type;
        this.isSuccess = isSuccess;
    }
    
    /**
     * Constructor.
     * 
     * @param type
     *            type of the event.
     */
    public Event(EventType type)
    {
        this.type = type;
        this.isSuccess = false;
    }
    
    public EventType getType()
    {
        return type;
    }
    
    /**
     * Set success.
     * 
     * @param succ
     *            success or not.
     * @return this object.
     */
    public Event setSuccess(boolean succ)
    {
        isSuccess = succ;
        return this;
    }
    
    public boolean isSuccess()
    {
        return isSuccess;
    }
    
    public Object getData()
    {
        return data;
    }
    
    public void setData(Object obj)
    {
        this.data = obj;
    }
}

