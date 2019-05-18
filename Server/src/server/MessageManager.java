/*
 * The MIT License
 *
 * Copyright 2019 Wang.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Wang
 */
public class MessageManager {
    static Map<String,ArrayList<Message>> cachedMap=new HashMap<>();//维护聊天信息缓存
    static Map<String,Socket> socketMap=new HashMap<>();//维护所有在线的用户
    /**
     * 转发聊天信息。如果接收方没有在线，就将其缓存；如果在线，就直接发送。
     * @param type 聊天信息类型。0代表私聊，1代表群聊。
     * @param sender 发送方ID
     * @param receiver 接收方ID（可能是群ID，也可能是用户ID）
     * @param content 聊天信息内容
     */
    public static void forwardMessage(int type, String sender, String receiver,  String content){
        Message message=new Message(type,sender,receiver,content);
        for (String rev:message.getReceiverList()){
            Socket socket=socketMap.get(rev);
            if (socket==null){
                //接收方不在线
                ArrayList<Message> cache=cachedMap.get(rev);
                if (cache==null){
                    cache=new ArrayList<>();
                    cache.add(message);
                    cachedMap.put(rev,cache);
                }
                else {
                    cache.add(message);
                }
            }
            else {
                sendMessage(socket, message);
            }
        }        
    }
    /**
     * 通过端口发送聊天信息。
     * @param socket 端口
     * @param message 聊天信息
     */
    private static void sendMessage(Socket socket,Message message){
        PrintWriter writer;
        try {
            writer = new PrintWriter(socket.getOutputStream());
            writer.println("Message "+message);   
            writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(TCPAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    /**
     * 将用户、端口对加入哈希表
     * @param user 用户
     * @param socket 端口
     */
    public static void insertSocket(String user,Socket socket){
        socketMap.put(user, socket);
    }
    
    /**
     * 从哈希表中删除用户
     * @param user 用户
     */
    public static void deleteSocket(String user){
        socketMap.remove(user);
    }
}


class Message{
    private final String sender;//发送方ID
    private final String receiver;//接收方ID
    private final int type;//0代表私聊信息，1代表群聊信息
    private final String content;//内容
    private final Date date;//发送时间
            

    public Message(int type, String sender, String receiver,  String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
        this.content = content;
        this.date=new Date();
    }    

    public String getSender() {
        return sender;
    }


    public String getReceiver() {
        return receiver;
    }
    
    public int getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    /**
     * 获取该信息的实际接受者列表。
     * 如果是私聊信息，接受者只有一个；如果是群聊信息，接受者有多个。
     * @return 接收者列表
     */
    public String[] getReceiverList(){
        if (type==0){
            String[] s={receiver};
            return s;
        }
        else{
            return GroupManager.getGroup(receiver).getMemberList();
        }
    }
    
    @Override
    public String toString(){
        String s=String.format("%d %s %s %s %tT",type,sender,receiver,content,date);
        return s;
    }
    
    
}