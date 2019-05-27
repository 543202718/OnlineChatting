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
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * 该类用于解析TCP报文
 * @author Wang
 */
public class TCPAnalyzer {
    public static void analyse(String message,ServerThread th){
        if (message.startsWith("Register")){
            //注册的报文应当满足格式： Register [name] [sex] [password]
            String s[]=message.split(" ");
            User user=UserManager.createUser(s[1],s[2],s[3]);
            th.user=user;
            System.out.println(user.getID()+"注册成功");
            sendMessage(th,"NewUser "+user);
            //返回的报文满足格式： NewUser [user]
        }
        else if (message.startsWith("Login")){
            //登录的报文应当满足格式： Login [ID] [password]
            String s[]=message.split(" ");
            User user=UserManager.getUser(s[1]);
            if (user==null || !user.checkPassword(s[2])){
                sendMessage(th,"LoginFailed");
                //在账户不存在或密码错误的情况下，返回的报文满足格式： LoginFailed
            }
            else {
                sendMessage(th,"LoginSucceed "+user);
                th.user=user;
                MessageManager.insertOnlineUser(s[1],th.clientSocket);//将该用户加入在线用户表               
                //在密码正确的情况下，返回的报文满足格式： LoginSucceed
            }
        }
        else if (message.startsWith("Fetch")){
            String s[]=message.split(" ");
            ArrayList<Message> list=MessageManager.cachedMap.get(s[1]);
            Socket socket=MessageManager.onlineMap.get(s[1]);
            if (list!=null){
                for (Message msg:list){
                    MessageManager.sendMessage(socket,msg);
                }
                MessageManager.cachedMap.remove(s[1]);
            }
        }
        else if (message.startsWith("Message")){
            //消息报文应当满足格式： Message [type] [sender] [receiver] [content]
            String s[]=message.split(" ");
            int type=Integer.parseInt(s[1]);
            int index=message.indexOf(s[4]);
            String content=message.substring(index);//整个报文除去前面4部分外都是content
            MessageManager.analyseMessage(type,s[2],s[3],content);
            System.out.println("用户"+s[2]+"发送了消息");
        }
        else if (message.startsWith("Get")){
            //获取数据的报文应当满足格式： Get [User|Group|AddressBook] [ID]
            String s[]=message.split(" ");
            switch(s[1]){
                case "User":    User user=UserManager.getUser(s[2]);
                                sendMessage(th,"User "+user);
                                System.out.println("发送了用户"+s[2]+"的数据");
                                break;
                case "Group":   Group group=GroupManager.getGroup(s[2]);
                                sendMessage(th,"Group "+group);
                                System.out.println("发送了群组"+s[2]+"的数据");
                                break;                  
                case "AddressBook":
                                AddressBook ab=AddressBookManager.getAddressBook(s[2]);
                                sendMessage(th,"AddressBook "+ab);
                                System.out.println("发送了通讯录"+s[2]+"的数据");
                                break;
            }
        }
        else if (message.startsWith("NewGroup")){
            //建群报文应当满足格式： NewGroup [name]
            String s[]=message.split(" ");
            Group group=GroupManager.createGroup(s[1],th.user.getID());
            sendMessage(th,"Group "+group);
            System.out.println("群聊"+group.getID()+"创建成功");
        }
        else if (message.startsWith("Exit")){
            //退出报文应当满足格式： Exit
            try {
                th.clientSocket.close();//关闭接口
                if (th.user!=null){//从在线用户表中删去该用户
                    MessageManager.deleteSocket(th.user.getID());
                }
                th.interrupt();
            } catch (IOException ex) {
                Logger.getLogger(TCPAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
            }           
        }        
    }
   
    private static void sendMessage(ServerThread th,String message){
        PrintWriter writer;
        try {
            Socket socket=th.clientSocket;
            writer = new PrintWriter(socket.getOutputStream());
            writer.println(message);   
            writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(TCPAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
}
