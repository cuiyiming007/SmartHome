package com.gdgl.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
/***
 * uitl file for parse xml data 
 * @author hyl
 *
 */
public class SaxForXmlHandler extends DefaultHandler {
    private String[] _needTag;
    private ArrayList<Map<String,String>> _notics;
    private Map<String,String> current;
    private String preTag;
    private String _nodeTag;
    
    //构造函数
    public SaxForXmlHandler(String tag)
    {
        this._nodeTag = tag;
    }
    public SaxForXmlHandler(String[] need)
    {
        this._needTag = need;
    }
    public SaxForXmlHandler(String tag,String[] need)
    {
        this._nodeTag = tag;
        this._needTag = need;
    }
    
    //获取设置每个节点数据的标签名称
    public void setNodeTag(String tag)
    {
        this._nodeTag = tag;
    }
    public String getNodeTag()
    {
        return this._nodeTag;
    }
    
    //获取设置包含数据的标签名称数组
    public void setNeedTag(String[] need)
    {
        this._needTag = need;
    }
    public String[] getNeedTag()
    {
        return this._needTag;
    }
    
    //获得最终处理后的数据
    public ArrayList<Map<String,String>> getResult()
    {
        return this._notics;
    }
    
    //文档开始
    @Override
    public void startDocument() throws SAXException {
        this._notics = new ArrayList<Map<String,String>>();
        this.preTag = null;
        this.current = null;
        if(this._nodeTag == null){
            throw new IllegalArgumentException("节点标签名称未赋值");
        }else if(this._needTag == null){
            throw new IllegalArgumentException("数据标签数据未赋值");
        }
        super.startDocument();
    }
    
    //节点开头
    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        if(_nodeTag.equals(localName)){
            //实例化一个Map对象
            current = new HashMap<String,String>();
        }
        //将当前处理的标签名称保存至preTag中
        preTag = localName;
    }
    
    //节点中的文本
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        //提取标签中的文本
        String data = new String(ch,start,length);
        String dedata = "";
        for(String item : this._needTag)
        {
            if(item.equals(preTag))
            {
                try {
                    //将数据进行URL解码
                    dedata = URLDecoder.decode(data,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    dedata = data;
                }finally{
                    //将当前的数据放入map对象中
                    current.put(item, dedata);
                }
                return;
            }
        }
    }

    //节点结束
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if(this._nodeTag.equals(localName))
        {
            //将当前map对象放入ArrayList对象中
            this._notics.add(current);
            current = null;
        }
        //将当前标签设置为null
        preTag = null;
    }
    
    //文档结束
    @Override
    public void endDocument() throws SAXException {
        current = null;
        preTag = null;
        super.endDocument();
    }
}
