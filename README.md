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



## 注意事项
+ 服务器的IP地址是127.0.0.1（本机），端口号是10000
