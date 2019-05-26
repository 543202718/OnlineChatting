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

import java.util.Set;
import java.util.TreeSet;

public class AddressBook{
    private Set<String> friendList;//好友列表
    private Set<String> groupList;//群聊列表
    
    public AddressBook(){
        friendList=new TreeSet<>();
        groupList=new TreeSet<>();
    }
    public String[] getFriendList(){
        return (String[])friendList.toArray(new String[0]);
    }
    public String[] getGroupList(){
        return (String[])groupList.toArray(new String[0]);
    }
    public void addFriend(String user){
        friendList.add(user);
    }
    public void deleteFriend(String friend){
        friendList.remove(friend);
    }
    public void addGroup(String group){
        groupList.add(group);
    }
    public void deleteGroup(String group){
        groupList.remove(group);
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(friendList.size()).append(",").append(groupList.size()).append(",");
        for (String s:friendList){
            sb.append(s).append(",");
        }
        for (String s:groupList){
            sb.append(s).append(",");
        }      
        return sb.toString();
    }
    
    public static AddressBook toAddressBook(String s){
        System.out.println(s);
        String[] sub=s.split(",");
        AddressBook ans=new AddressBook();
        int m=Integer.parseInt(sub[0]);
        int n=Integer.parseInt(sub[1]);
        for (int i=2;i<2+m;i++){
            ans.friendList.add(sub[i]);
        }
        for (int i=2+m;i<2+m+n;i++){
            ans.groupList.add(sub[i]);
        }
        return ans;
    }
    
    
    /**
     * 查找某一组的所有好友
     * @param tag 分组标记
     * @return 所有好友
     
    public Friend[] findFriendByTag(String tag){
        ArrayList<Friend> list=new ArrayList<>();
        for (Friend f:getFriendList()){
            if ((tag==null && f.getTag()==null)||(tag!=null && tag.equals(f.getTag())) ) {
                list.add(f);
            }
        }
        return (Friend[])list.toArray();
    }*/

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


