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
public class MessageAnalyzer {
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
        
    }
    /**
     * 报文种类：注册、登录、游客登录、获取用户信息、获取通讯录信息、获取群组信息、
     * 发送私聊信息、发送群聊信息、添加好友、删除好友、加入群组、退出群组、
     * 任命管理员、解除管理员、
     */
    
    
    
    
    private static void sendMessage(ServerThread th,String message){
        PrintWriter writer=null;
        try {
            Socket socket=th.clientSocket;
            writer = new PrintWriter(socket.getOutputStream());
            writer.println(message);        
        } catch (IOException ex) {
            Logger.getLogger(MessageAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            writer.close();       
        }        
    }
    
}
