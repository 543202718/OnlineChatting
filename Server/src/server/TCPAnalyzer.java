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
            sendMessage(th,"User "+user);
            //返回的报文满足格式： User [user]
        }
        else if (message.startsWith("Login")){
            //登录的报文应当满足格式： Login [ID] [password]
            String s[]=message.split(" ");
            User user=UserManager.getUser(s[1]);
            boolean correct=user.checkPassword(s[2]);
            if (correct){
                sendMessage(th,"User "+user);
                MessageManager.insertOnlineUser(s[1],th.clientSocket);//将该用户加入在线用户表
                th.user=user;
                System.out.println("用户"+user.getID()+"已经登录");
                //在密码正确的情况下，返回的报文满足格式： User [user]
            }
            else{
                sendMessage(th,"LoginFailed");
                //在密码错误的情况下，返回的报文满足格式： LoginFailed
            }
        }
        else if (message.startsWith("Message")){
            //消息报文应当满足格式： Message [type] [sender] [receiver] [content]
            String s[]=message.split(" ");
            if ("2".equals(s[1])){//处理系统消息
                MessageManager.analyseSystemMessage(s[2],s[3],s[4]);
            }
            else {//转发聊天消息
                MessageManager.forwardMessage(Integer.parseInt(s[1]),s[2],s[3],s[4]);
            }
            System.out.println("用户"+s[2]+"发送了消息");
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
    /**
     * 报文种类：注册、登录、游客登录、获取用户信息、获取通讯录信息、获取群组信息、
     * 发送私聊信息、发送群聊信息、添加好友、删除好友、加入群组、退出群组、
     * 任命管理员、解除管理员、
     */
    
    
    
    
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
