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
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * 该类用于管理所有的用户
 * @author Wang
 */
public class UserManager {
    private static final int MAXUSERS=100;
    private static final int STARTNUM=10000;
    private static final User[] USERS=new User[MAXUSERS];
    /**
     * 得到指定的用户
     * @param ID 用户ID
     * @return 用户
     */
    static User getUser(String ID){
        int index=Integer.parseInt(ID)-STARTNUM;
        if (index<0 || index>MAXUSERS)  return null;
        else return USERS[index];
    }
    /**
     * 向列表中新增一个用户
     * @param user 用户
     */
    static void addUser(User user){
        int index=Integer.parseInt(user.getID())-STARTNUM;
        USERS[index]=user;
    }
    /**
     * 得到有效的未使用的用户ID
     * @return 未使用的用户ID
     */
    private static int getValidID(){
        int i;
        for (i=0;i<MAXUSERS;i++){
            if (USERS[i]==null) break;
        }
        return i;
    }
    /**
     * 创建一个用户，并将其加入列表
     * @param name 昵称
     * @param sex 性别
     * @param password 密码
     * @return 创建的用户
     */
    static User createUser(String name,String sex,String password){
        int i=getValidID();
        String ID=Integer.toString(i+STARTNUM);
        USERS[i]=new User(ID,name,sex,password);
        AddressBookManager.addAddressBook(ID,new AddressBook());
        System.out.println("用户"+ID+"创建完成");
        return USERS[i];
    }
    
}


class User{
    private final String ID;//用户ID，唯一
    private String name;//昵称，不唯一
    private String sex;//性别，默认男性
    private final String password;//密码
    private static String salt="&%5123***&&%%$$#@";//盐
    //所有字符串都不允许包含标点符号
    public User(String ID,String password){
        this.ID=ID;
        this.name="用户"+ID;
        this.sex="男";
        this.password=encrypt(password);
    }
    public User(String ID, String name, String sex,String password) {
        this.ID = ID;
        this.name = name;
        this.sex = sex;
        this.password=encrypt(password);
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
    public boolean checkPassword(String s){
        return password.equals(encrypt(s));
    }
    @Override
    public String toString(){
        return "{"+ID+","+name+","+sex+"}";
    }
    
    /**
     * 对密码加盐后计算MD5
     * @param dataStr 原始数据
     * @return 加盐后的MD5
     */
    private static String encrypt(String dataStr) {
        try {
            dataStr = dataStr + salt;
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(dataStr.getBytes("UTF8"));
            byte s[] = m.digest();
            String result = "";
            for (int i = 0; i < s.length; i++) {
		result += Integer.toHexString((0x000000FF & s[i]) | 0xFFFFFF00).substring(6);
            }
            return result;
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
        }
        return null;
    }
    
}