# OnlineChatting
This is an online chatting system, which is the final project of Network System Experiment.
## 实现功能
+ 注册
+ 登录
+ 私聊
+ 群聊
+ 通讯录
+ 群管理
## 服务器与客户端的交互
Java会将TCP连接包装为输入输出流，使用TCP报文等价于使用流。每一份报文使用换行符分割，在读取时推荐使用Scanner类的readLine()方法，在输出时推荐使用PrintWriter类的println()方法。

### 注册
客户端发送下面的报文，注意使用方括号包裹的是一个字符串变量，空格作为分隔符。
> Register [name] [sex] [password]  

服务器接收后发送下面的报文(User的toString()方法已经重载)：
> NewUser [user]

### 登录
客户端发送下面的报文：
> Login [ID] [password]

服务器接收后，如果用户存在且密码正确，发送下面的报文：
> LoginSucceed [user]

否则，发送下面的报文：
> LoginFailed

### 聊天信息
客户端发送下面的报文，type=0是私聊消息，type=1是群聊消息：
> Message [type] [sender] [receiver] [content]

服务器接收后，如果接收方不在线就将其缓存，否则进行转发：
> Message [type] [sender] [receiver] [date] [content]

### 系统信息
客户端发送下面的报文：
> Message [type] [sender] [receiver] [content]

其中type大于等于2。服务器进行下面的处理：
+ 邀请好友
  + type=2
  + sender是邀请方的ID
  + receiver是被邀请方的ID
  + content无效
  + 服务器将其转发
+ 接受好友邀请
  + type=3
  + sender是被邀请方的ID
  + receiver是邀请方的ID
  + content无效
  + 服务器修改双方的通讯录，并将其转发
+ 拒绝好友邀请
  + type=4
  + sender是被邀请方的ID
  + receiver是邀请方的ID
  + content无效
  + 服务器将其转发 
+ 删除好友
  + type=5
  + sender是主动删除方的ID
  + receiver是被删除好友的ID
  + content无效
  + 服务器修改双方的通讯录，并将其转发
+ 邀请加入群聊
  + type=6
  + sender是邀请方的ID
  + receiver是被邀请方的ID
  + content是群ID
  + 服务器将其转发
+ 接受入群邀请
  + type=7
  + sender是被邀请方的ID
  + receiver是群ID
  + content无效
  + 服务器修改被邀请方的通讯录和群信息，并将其转发给所有群成员
+ 拒绝入群邀请
  + type=8
  + sender是被邀请方的ID
  + receiver是邀请方的ID
  + content是群ID
  + 服务器将其转发
+ 退群
  + type=9
  + sender是准备退群者的ID
  + receiver是群ID
  + content无效
  + 服务器修改通讯录和群信息，并将其转发给所有群管理员

### 建群
客户端发送以下报文：
> NewGroup [name]

服务器建立群聊，将它加入群主的通讯录，返回下面的报文：
> Group [group]  


### 获取数据
客户端发送以下报文：
> Get {User|Group|AddressBook} [ID]

服务器根据ID和类型返回下列报文：
> User [user]  
> Group [group]  
> AddressBook [addressbook]

三者的toString()方法均已重载。

### 退出
客户端发送下面的报文：
> Exit

## TODO
### 服务器
+ 部分系统消息（如任命、解除管理员等）
+ 群的解散
+ 群主转移
### 客户端
+ 聊天功能
+ 群功能
+ 通讯录功能

## 注意事项
+ 服务器的IP地址是127.0.0.1（本机），端口号是10000
+ 在输出之后应当调用flush()方法，否则输出无法被发送到另一端
+ 除非socket被关闭，否则不要关闭输入或输出流（即调用Scanner或PrintWriter的close()方法）
+ 用户发送的聊天信息应当被限制在一行以内，可以在客户端实现（如删去信息中所有的换行符、或者将换行符用其他字符替代）
+ JFrame窗体应当使用单例模式


## 测试用的报文
```txt
Register Wang Male 123456
Login 10000 123456
Register Liu Male 123456
Login 10001 123456
Message 0 10000 10001 Hello,Liu 
Message 0 10001 10000 GoodBye
Exit
```