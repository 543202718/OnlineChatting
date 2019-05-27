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
package client;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Wang
 */
public class MessageManager {
    static ArrayList<Chatter> messageList=new ArrayList<>();
    static ArrayList<Message> cacheList=new ArrayList<>();
    static Timer timer=new Timer();
       
    public static void addMessage(Message message) {
        if (message.getType() == 0) {
            //私聊信息
            User user = UserManager.getUser(message.getSender());
            if (user == null) {
                cacheList.add(message);
            } 
            else {
                user.addMessage(message);
                messageList.remove(user);
                messageList.add(user);
            }
        } 
        else if (message.getType() == 1) {
            //群聊信息
            Group group = GroupManager.getGroup(message.getReceiver());
            if (group == null) {
                cacheList.add(message);
            } 
            else {
                group.addMessage(message);
                messageList.remove(group);
                messageList.add(group);
            }
        }
    }
    
    /**
     * 解析系统信息
     * @param type
     * @param sender
     * @param receiver
     * @param content 
     */
    public static void analyseMessage(int type,String sender,String receiver,String content){
        
    }
    
}


class Message{
    private final String sender;//发送方ID
    private final String receiver;//接收方ID
    private final int type;//0代表私聊信息，1代表群聊信息，大于等于2表示系统消息
    private final String content;//内容
    private final Date date;//发送时间
            

    public Message(int type, String sender, String receiver,Date date,String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
        this.content = content;
        this.date=date;
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

    
    @Override
    public String toString(){      
        String s=String.format("%s %tT\n%s\n",UserManager.getUser(sender),date,content);
        return s;
    }
    public static Message toMessage(String s){
        return null;
    }
    
}


class MessageCacheThread implements Runnable{

    public MessageCacheThread() {
    }

    
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MessageCacheThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (!MessageManager.cacheList.isEmpty()) {
                for (Message msg : MessageManager.cacheList) {
                    MessageManager.addMessage(msg);
                }
            }
            MainFrame.getInstance().updateMessage();
        }
    }
    
}