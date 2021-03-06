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


/**
 *
 * @author Wang
 */
public class MessageManager {
    static ArrayList<Message> cacheList=new ArrayList<>();//消息缓存列表
    static ArrayList<Message> systemMessageList=new ArrayList<>();//系统消息列表
    /**
     * 增加一条消息。如果发送方尚未从服务器下载，它将被加入缓存列表；
     * 否则，加入发送方的消息列表。
     * @param message 消息
     */
    public static void addMessage(Message message) {
        switch (message.getType()) {
            case 0:
                //私聊信息
                User user = UserManager.getUser(message.getSender());
                if (user == null) {
                    cacheList.add(message);
                }
                else {
                    user.addMessage(message);                  
                }   
                break;
            case 1:
                //群聊信息
                Group group = GroupManager.getGroup(message.getReceiver());             
                if (group == null) {
                    cacheList.add(message);
                }
                else {
                    System.out.println(group.getID());
                    group.addMessage(message);                 
                }   
                break;
            default:
                analyseMessage(message);
                break;
        }
    }
    
    /**
     * 解析系统信息
     * @param message
     */
    public static void analyseMessage(Message message){
        switch (message.getType()) {
            case 2:
                User user = UserManager.getUser(message.getSender());
                if (user == null) {
                    cacheList.add(message);
                    Client.sendMessage("Get User " + message.getSender());
                } else {
                    message.setContent(user.getName() + "邀请您成为他的好友");
                    systemMessageList.add(message);
                }
                break;
            case 3:
                user = UserManager.getUser(message.getSender());
                if (user == null) {
                    cacheList.add(message);
                    Client.sendMessage("Get User " + message.getSender());
                } else {
                    message.setContent(user.getName() + "接受了您的好友申请");
                    systemMessageList.add(message);
                }
                break;
            case 4:
                user = UserManager.getUser(message.getSender());
                if (user == null) {
                    cacheList.add(message);
                    Client.sendMessage("Get User " + message.getSender());
                } else {
                    message.setContent(user.getName() + "拒绝了您的好友申请");
                    systemMessageList.add(message);
                }
                break;
            case 5:
                user = UserManager.getUser(message.getSender());
                if (user == null) {
                    cacheList.add(message);
                    Client.sendMessage("Get User " + message.getSender());
                } else {
                    message.setContent(user.getName()  + "不再是您的好友");
                    systemMessageList.add(message);
                }
                break;
            case 6:
                user = UserManager.getUser(message.getSender());
                Group group = GroupManager.getGroup(message.getContent());
                if (user == null || group == null) {
                    cacheList.add(message);
                    Client.sendMessage("Get User " + message.getSender());
                    Client.sendMessage("Get Group " + message.getContent());
                } else {
                    String content = user.getName() + "邀请您加入群聊" +group.getName();
                    message.setSender(message.getContent());
                    message.setContent(content);
                    systemMessageList.add(message);
                }
                break;
            case 7:
                user = UserManager.getUser(message.getSender());
                group = GroupManager.getGroup(message.getReceiver());
                if (user == null || group == null) {
                    cacheList.add(message);
                    Client.sendMessage("Get User " + message.getSender());
                    Client.sendMessage("Get Group " + message.getReceiver());
                } else {
                    String content = user.getName() + "加入了群聊" + group.getName();
                    message.setContent(content);
                    systemMessageList.add(message);
                }
                break;
            case 8:
                user = UserManager.getUser(message.getSender());
                if (user == null) {
                    cacheList.add(message);
                    Client.sendMessage("Get User " + message.getSender());
                } else {
                    String content = user.getName() + "拒绝了您的入群邀请";
                    message.setContent(content);
                    systemMessageList.add(message);
                }
                break;
            case 9:
                user = UserManager.getUser(message.getSender());
                group = GroupManager.getGroup(message.getReceiver());
                if (user == null || group == null) {
                    cacheList.add(message);
                    Client.sendMessage("Get User " + message.getSender());
                    Client.sendMessage("Get Group " + message.getContent());
                } else {
                    String content = user.getName() + "退出了群聊" + group.getName();
                    message.setContent(content);
                    systemMessageList.add(message);
                }
                break;
            default:
        }
    }
    /**
     * 清空缓存列表
     */
    static void clearCache(){
        while (!cacheList.isEmpty()){
            Message msg=cacheList.get(0);
            cacheList.remove(msg);
            addMessage(msg);
        }
    }
}


class Message{
    private String sender;//发送方ID
    private String receiver;//接收方ID
    private int type;//0代表私聊信息，1代表群聊信息，大于等于2表示系统消息
    private String content;//内容
    private final Date date;//发送时间
            

    public Message(int type, String sender, String receiver,Date date,String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
        this.content = content;
        this.date=date;
    }    

    public void setSender(String sender) {
        this.sender = sender;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
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
    public void setType(int type){
        this.type=type;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content){
        this.content=content;
    }

    public String showString(){      
        String s=String.format("%s %tT\n%s\n",UserManager.getUser(sender),date,content);
        return s;
    }
    
    @Override
    public String toString(){      
        return getContent();
    }
}


