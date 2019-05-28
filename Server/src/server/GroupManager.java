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
import java.util.Set;
import java.util.HashSet;
/**
 *
 * @author Wang
 */
public class GroupManager {
    private static final int MAXGROUP=100;
    private static final int STARTNUM=10000;
    private static final Group[] GROUPS=new Group[MAXGROUP];
    static Group getGroup(String ID){
        int index=Integer.parseInt(ID)-STARTNUM;
        return GROUPS[index];
    }
    static void addGroup(Group group){
        int index=Integer.parseInt(group.getID())-STARTNUM;
        GROUPS[index]=group;
    }
    private static int getValidID(){
        int i;
        for (i=0;i<MAXGROUP;i++){
            if (GROUPS[i]==null) break;
        }
        return i;
    }
    static Group createGroup(String name,String master){
        int i=getValidID();
        String ID=Integer.toString(i+STARTNUM);//得到有效ID
        Group group=new Group(ID,name,master);//新建群聊
        GROUPS[i]=group;
        AddressBookManager.getAddressBook(master).addGroup(ID);//在群主的通讯录中加入该群聊
        return group;
    }
}


class Group implements Comparable<Group>{
    private final String ID;//群ID，唯一
    private String name;//群名称，不唯一
    private final String master;//群主的ID
    private final Set<String> managerList;//管理员列表
    private final Set<String> memberList;//群成员列表
    public Group(String ID,String name,String master){
        this.ID=ID;
        this.name=name;
        this.master=master;
        this.managerList=new HashSet<>();
        this.managerList.add(master);//群主是当然的管理员
        this.memberList=new HashSet<>();
        this.memberList.add(master);//群主是当然的群成员
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
    public String getMaster(){
        return master;
    }
    public String[] getMemberList(){
        return memberList.toArray(new String[0]);
    }
    public String[] getManagerList(){
        return managerList.toArray(new String[0]);
    }
    /**
     * 任命管理员
     * @param user 管理员ID
     */
    public void appointManager(String user){
        if (memberList.contains(user)){
            managerList.add(user);
        }
    }
    /**
     * 解除管理员职务
     * @param user 管理员ID
     */
    public void dismissManager(String user){
        if (memberList.contains(user)){
            managerList.remove(user);
        }
    }
    /**
     * 增加群成员
     * @param user 群成员ID
     */
    public void addMember(String user){
        memberList.add(user);
    }  
    /**
     * 删除群成员
     * @param user 群成员ID
     */
    public void deleteMember(String user){
        managerList.remove((String)user);
    }
    @Override
    public int compareTo(Group t) {
        return this.getID().compareTo(t.getID());
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(ID).append(",").append(name).append(",");
        sb.append(master).append(",");
        sb.append(managerList.size()).append(",");
        sb.append(memberList.size()).append(",");        
        for (String s:managerList){
            sb.append(s).append(",");
        }
        for (String s:memberList){
            sb.append(s).append(",");
        }     
        return sb.toString();
    }
    
    public static Group toGroup(String s){
        String sub[]=s.split(",");
        Group group=new Group(sub[0],sub[1],sub[2]);
        int m=Integer.parseInt(sub[3]);
        int n=Integer.parseInt(sub[4]);
        for (int i=5;i<5+m;i++){
            group.managerList.add(sub[i]);
        }
        for (int i=5+m;i<5+m+n;i++){
            group.memberList.add(sub[i]);
        }
        return group;
    }
    
    
}