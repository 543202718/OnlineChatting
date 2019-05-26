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
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Wang
 */
public class Client {
    private static final int SERVERPORT=10000;
    static Socket socket;
    public static AddressBook addressBook;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            socket=new Socket("127.0.0.1",SERVERPORT);
            ExecutorService threadPool = Executors.newCachedThreadPool();//使用线程池管理线程
            Thread thread=new Thread(new ListenerThread());
            threadPool.execute(thread);
            LoginFrame.getInstance().setVisible(true);
            //TestFrame.getInstance().setVisible(true);           
            
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void sendMessage(String message){
        PrintWriter writer;
        try {
            writer = new PrintWriter(socket.getOutputStream());
            writer.println(message);   
            writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public static void downloadAddressBook(){
        User user=UserManager.getClient();
        sendMessage("Get AddressBook "+user.getID());//得到通讯录        
    }
}

class TestListenerThread implements Runnable{

    public TestListenerThread() {
    }
    
    @Override
    public void run() {
        try {
            System.out.println("Start Listening.");
            Scanner sc=new Scanner(Client.socket.getInputStream());
            while (sc.hasNext()) {
                String s=sc.nextLine();
                System.out.println(s);
                TestFrame.getInstance().printToScreen(s);
            }
        } catch (IOException ex) {
            Logger.getLogger(TestListenerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
