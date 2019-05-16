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

/**
 * 该类用于管理所有的用户
 * @author Wang
 */
public class UserManager {
    private static final int MAXUSERS=100;
    private static final int STARTNUM=10000;
    private static final User[] USERS=new User[MAXUSERS];
    static User getUser(String ID){
        int index=Integer.parseInt(ID)-STARTNUM;
        return USERS[index];
    }
    static void addUser(User user){
        int index=Integer.parseInt(user.getID())-STARTNUM;
        USERS[index]=user;
    }
    private static String getValidID(){
        int i;
        for (i=0;i<MAXUSERS;i++){
            if (USERS[i]==null) break;
        }
        return Integer.toString(i+STARTNUM);
    }
    static User createUser(String name,String sex){
        return new User(getValidID(),name,sex);
    }
    
}


class User{
    private final String ID;//用户ID，唯一
    private String name;//昵称，不唯一
    private String sex;//性别，默认男性
    public User(String ID){
        this.ID=ID;
        this.name="用户"+ID;
        this.sex="男";
    }
    public User(String ID, String name, String sex) {
        this.ID = ID;
        this.name = name;
        this.sex = sex;
    } 
    public String getID(){
        return ID;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getSex(){
        return sex;
    }
    public void setSex(String sex){
        this.sex=sex;
    }
}