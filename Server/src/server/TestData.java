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
 *
 * @author Wang
 */
public class TestData {
    User[] users={
        new User("10000","赵","男","123456"),
        new User("10001","钱","男","123456"),
        new User("10002","孙","男","123456"),
        new User("10003","李","男","123456"),
        new User("10004","周","女","123456"),
    };
    Group[] groups={
        new Group("10000","计算机学院","10000"),
        new Group("10001","信息学院","10003"),
    };

    public TestData() {
    }
    
    
    
    public void addTestData(){
        for (User user:users){
            UserManager.addUser(user);
            AddressBookManager.addAddressBook(user.getID(),new AddressBook());
        }
        for (Group group:groups){
            GroupManager.addGroup(group);
        }
        AddressBookManager.getAddressBook("10000").addFriend("10001");
        AddressBookManager.getAddressBook("10000").addFriend("10004");
        AddressBookManager.getAddressBook("10000").addFriend("10003");
        AddressBookManager.getAddressBook("10000").addGroup("10000");
        AddressBookManager.getAddressBook("10004").addFriend("10000");
        MessageManager.forwardMessage(0,"10001","10000","Hello");
        MessageManager.forwardMessage(1,"10000","10000","Hello");
        MessageManager.forwardMessage(2,"10002","10000","Hello");
        MessageManager.forwardMessage(6,"10003","10000","10001");
    }
}
