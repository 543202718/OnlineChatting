# OnlineChatting
This is an online chatting system, which is the final project of Network System Experiment.
## 实现功能
+ 注册
+ 登录
+ 私聊
+ 群聊
+ 通讯录
+ 群管理
## TCP报文格式
Java会将TCP连接包装为输入输出流，使用TCP报文等价于使用流。每一份报文使用换行符分割，在读取时推荐使用Scanner类的readLine()方法，在输出时推荐使用PrintWriter类的println()方法。

### 注册
客户端发送下面的报文，注意使用方括号包裹的是一个字符串变量，空格作为分隔符。
> Register [name] [sex] [password]  

服务器接收后发送下面的报文(User的toString()方法已经重载)：
> User [user]

### 登录
客户端发送下面的报文：
> Login [ID] [password]

服务器接收后，如果密码正确，发送下面的报文：
> User [user]    

如果密码错误，发送下面的报文：
> LoginFailed

### 聊天信息
客户端发送下面的报文：
> Message [type] [sender] [receiver] [content]

服务器接收后，如果接收方不在线就将其缓存，否则进行转发：
> Message [type] [sender] [receiver] [content] [date]






## 注意事项
+ 服务器的IP地址是127.0.0.1（本机），端口号是10000
+ 在输出之后应当调用flush()方法，否则输出无法被发送到另一端
+ 除非socket被关闭，否则不要关闭输入或输出流（即调用Scanner或PrintWriter的close()方法）
+ 用户发送的聊天信息应当被限制在一行以内，可以在客户端实现（如删去信息中所有的换行符、或者将换行符用其他字符替代）


## 测试用的报文
```txt
Register Wang Male 123456
Login 10000 123456
Register Liu Male 123456
Login 10001 123456
Message 0 10000 10001 Hello,Liu 
Message 0 10001 10000 GoodBye
```