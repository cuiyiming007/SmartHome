package com.gdgl.net.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import com.gdgl.manager.CallbackManager;
import com.gdgl.util.UiUtils;
 
/**
 * Socket连接操作类
 * 
 * @author Esa
 */
public class SocketBase{
 
    private Socket mSocket;// socket连接对象
    private DataOutputStream out;
    private DataInputStream in;// 输入流
    private SocketCallback callback;// 信息回调接口
    private int timeOut = 1000 * 30;
  
    /**
     * 构造方法传入信息回调接口对象
     * 
     * @param sdi
     *            回调接口
     */
    public SocketBase(SocketCallback callback) {
        this.callback = callback;
    }
 
    /**
     * 连接网络服务器
     * 
     * @throws UnknownHostException
     * @throws IOException
     */
    public void connect(String ip, int port) throws Exception {
        mSocket = new Socket();
        SocketAddress address = new InetSocketAddress(ip, port);
        mSocket.connect(address, timeOut);// 连接指定IP和端口
        if (mSocket.isConnected()) {
            out = new DataOutputStream(mSocket.getOutputStream());// 获取网络输出流
            in = new DataInputStream(mSocket.getInputStream());// 获取网络输入流
            callback.connected();
        }
    }
 
    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }
 
    /**
     * 返回连接服是否成功
     * 
     * @return
     */
    public boolean isConnected() {
        if(mSocket != null){
            return mSocket.isConnected();
        }
        return false;
    }
 
    /**
     * 发送数据
     * 
     * @param buffer
     *            信息字节数据
     * @throws IOException
     */
    public void write(byte[] buffer) throws IOException {
        if (out != null) {
            out.write(buffer);
            out.flush();
        }
    }
 
    /**
     * 断开连接
     * 
     * @throws IOException
     */
    public void disconnect() {
        try {
            if (mSocket != null) { 
            	if (!mSocket.isInputShutdown()) {
                    mSocket.shutdownInput();
                }
                if (!mSocket.isOutputShutdown()) {
                    mSocket.shutdownOutput();
                }
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                mSocket.close();// 关闭socket
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //callback.disconnect();
            out = null;
            in = null;
            mSocket = null;// 制空socket对象
        }
    }
 
    /**
     * 读取网络数据
     * 
     * @throws IOException
     */
    public void read() throws IOException {
        if (in != null) {
            byte[] buffer = new byte[1024*1];// 缓冲区字节数组，信息不能大于此缓冲区
            byte[] tmpBuffer;// 临时缓冲区
            int len = 0;// 读取长度
            String messageData = "";
            int messageLength = 0;
            String[] messageList = null;
            while ((len = in.read(buffer)) > 0) {
//                tmpBuffer = new byte[len];// 创建临时缓冲区
//                System.arraycopy(buffer, 0, tmpBuffer, 0, len);// 将数据拷贝到临时缓冲区
//                callback.receive(tmpBuffer);// 调用回调接口传入得到的数据
//                tmpBuffer = null;
            	String message = new String(buffer, 0, len,"UTF-8");
            
            	if(message.indexOf("#data") >= 0){
//            		System.out.println("开始接收：..................");
            		messageList = message.split("#data");
            		messageLength = Integer.parseInt(messageList[0].replace("#", ""));
            		messageData += messageList[1];	
            	}else{
            		messageData += message;
            	}
            	
            	
            	if(messageLength == getStringLength(messageData)){
//            		System.out.println("接收完毕：..................");
//            		System.out.println(messageData);
        			callback.receive(messageData);// 调用回调接口传入得到的数据  
        			messageData = "";
                    messageLength = 0;
                    messageList = null;
        		} 
//            	System.out.println(messageData);
//            	System.out.println(getStringLength(messageData) + "/" + messageLength);
            }
        }
    }
    
    private int getStringLength(String value){
    	int valueLength = 0;
    	 
        String chinese = "[\u0391-\uFFE5]";
 
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
 
        for (int i = 0; i < value.length(); i++) {
 
            /* 获取一个字符 */
 
            String temp = value.substring(i, i + 1);
 
            /* 判断是否为中文字符 */
 
            if (temp.matches(chinese)) {
 
                /* 中文字符长度为2 */
 
                valueLength += 3;
 
            } else {
 
                /* 其他字符长度为1 */
 
                valueLength += 1;
 
            }
 
        }
 
        return valueLength;
 
    }

}
