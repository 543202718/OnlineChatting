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
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
/**
 *
 * @author Wang
 */
public class AddressBookManager {
    private static final int MAXUSERS=100;
    private static final int STARTNUM=10000;
    private static final AddressBook[] BOOKS=new AddressBook[MAXUSERS];
    static AddressBook getAddressBook(String ID){
        int index=Integer.parseInt(ID)-STARTNUM;
        return BOOKS[index];
    }
    static void addAddressBook(String ID,AddressBook addressBook){
        int index=Integer.parseInt(ID)-STARTNUM;
        BOOKS[index]=addressBook;
    }
    
}

class AddressBook{
    private Set<Friend> friendList;//好友列表
    private Set<String> groupList;//群聊列表
    public AddressBook(){
        friendList=new TreeSet<>();
        groupList=new TreeSet<>();
    }
    public Friend[] getFriendList(){
        return (Friend[])friendList.toArray();
    }
    public Group[] getGroupList(){
        return (Group[])groupList.toArray();
    }
    public void addFriend(Friend user){
        friendList.add(user);
    }
    public void addFriend(String user){
        friendList.add(new Friend(user));
    }
    public void deleteFriend(Friend friend){
        friendList.remove(friend);
    }
    public void addGroup(String group){
        groupList.add(group);
    }
    public void deleteGroup(String group){
        groupList.remove(group);
    }
    public Friend[] findFriendByTag(String tag){
        //查找某一组的所有好友
        ArrayList<Friend> list=new ArrayList<>();
        for (Friend f:getFriendList()){
            if ((tag==null && f.getTag()==null)||(tag!=null && tag.equals(f.getTag())) ) {
                list.add(f);
            }
        }
        return (Friend[])list.toArray();
    }

}

class Friend implements Comparable<Friend>{
    private final String user;//用户
    private String tag;//分组标记，内容是组名
    private String remark;//备注名
    public Friend(String user,String tag,String remark){
        this.user=user;
        this.tag=tag;
        this.remark=remark;
    }
    public Friend(String user){
        this(user,null,null);
    }
    public String getUser(){
        return user;
    }
    public String getTag(){
        return tag;
    }
    public void setTag(String tag){
        this.tag=tag;
    }
    public String getRemark(){
        return remark;
    }
    public void setRemark(String remark){
        this.remark=remark;
    }

    @Override
    public int compareTo(Friend t) {
        return UserManager.getUser(this.user).getID().compareTo(UserManager.getUser(t.user).getID());
    }
}

