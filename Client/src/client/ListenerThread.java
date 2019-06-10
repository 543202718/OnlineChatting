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

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Wang
 */
public class ListenerThread implements Runnable{

    public ListenerThread() {
    }
    
    @Override
    public void run() {
        try {
            System.out.println("Start Listening.");
            Scanner sc=new Scanner(Client.socket.getInputStream());
            while (sc.hasNext()) {
                String line=sc.nextLine();
                System.out.println(line);
                analyse(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(TestListenerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * 解析服务器发送的报文
     * @param line 服务器发送的报文
     */
    public void analyse(String line){
        if (line.startsWith("LoginFailed")){
            JOptionPane.showMessageDialog(null,"您输入的账号或密码错误", "登录失败", JOptionPane.ERROR_MESSAGE);
        }
        else if (line.startsWith("LoginSucceed")) {
            String[] s = line.split(" ");
            User user = User.toUser(s[1]);
            UserManager.addUser(user);
            UserManager.setClientID(user.getID());
            LoginFrame.getInstance().setVisible(false);
            MainFrame.getInstance().setVisible(true);
            AddressBook.downloadAddressBook();
        } 
        else if (line.startsWith("NewUser")) {
            String[] s = line.split(" ");
            User user = User.toUser(s[1]);
            UserManager.addUser(user);
            UserManager.setClientID(user.getID());
            JOptionPane.showMessageDialog(null, "您的账号是" + user.getID(), "注册成功", JOptionPane.INFORMATION_MESSAGE);
            RegisterFrame.getInstance().setVisible(false);
            LoginFrame.getInstance().setVisible(false);
            MainFrame.getInstance().setVisible(true);
            AddressBook.downloadAddressBook();
        } 
        else if (line.startsWith("AddressBook")) {
            String[] s = line.split(" ");
            Client.addressBook = AddressBook.toAddressBook(s[1]);
            for (String friend : Client.addressBook.getFriendList()) {
                Client.sendMessage("Get User " + friend);
            }
            for (String group : Client.addressBook.getGroupList()) {
                Client.sendMessage("Get Group " + group);
            }
            //Client.sendMessage("Fetch " + UserManager.getClient().getID());
            MainFrame.getInstance().updateAvatar();
            MainFrame.getInstance().updateAddressBook();
        } 
        else if (line.startsWith("User")) {
            String[] s = line.split(" ");
            User user = User.toUser(s[1]);
            UserManager.addUser(user);
            MainFrame.getInstance().updateAddressBook();
        } 
        else if (line.startsWith("Group")) {
            String[] s = line.split(" ");
            Group group = Group.toGroup(s[1]);
            GroupManager.addGroup(group);
            for (String ID: group.getMemberList()){
                Client.sendMessage("Get User " + ID);
            }
            MainFrame.getInstance().updateGroup();
        }
        else if (line.startsWith("Message")){          
            try {
                String[] s = line.split(" ");
                System.out.println(line);
                int type = Integer.parseInt(s[1]);
                Date date = new SimpleDateFormat("hh:mm:ss").parse(s[4]);
                StringBuilder content = new StringBuilder(s[5]);
                for (int i = 6; i < s.length; i++) {
                    content.append(" ").append(s[i]);
                }
                System.out.println(content);
                //整个报文除去前面5部分外都是content
                Message message = new Message(type, s[2], s[3], date, content.toString());
                MessageManager.addMessage(message);
                MainFrame.getInstance().updateMessage();
                MainFrame.getInstance().updateChatPanel();
            } catch (ParseException ex) {
                Logger.getLogger(ListenerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
}
