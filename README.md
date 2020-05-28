# session
## 简述
该项目的目的是为了统一连接方式不同而导致的差异性.
目前支持的会话类型如下
1. ssh
2. telnet
3. process
其中process是对cmd/bash操作的session,实际用途是本地执行命令
## 设计
会话具备的功能如下
1. 写入字符串
2. 读取字符串

在java中对应到的是read/write字符流
可以设置session的编码,以便包装字节流,默认是UTF-8编码

使用Predicate以及Condition进行读/写,所以可以随意组合条件以判断读写情况.
另外也提供了watch函数,可以使用独立线程来观察流中的数据,以实现异步的操作

## 功能
屏蔽了后端实际连接的差异,使用相同的字符流来进行操作.
1. ssh会话
2. telnet会话
3. 本地命令会话

