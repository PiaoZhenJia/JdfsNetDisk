# 朴朴个人云盘(JdfsNetDisk)

### 项目Github地址

[https://github.com/PiaoZhenJia/JdfsNetDisk](https://github.com/PiaoZhenJia/JdfsNetDisk)

### 介绍

#### 本项目由 @author 朴振嘉 倾情打造

##### 我的CSDN页

[https://blog.csdn.net/weixin_43074462](https://blog.csdn.net/weixin_43074462)

##### 我的Github页

[https://github.com/PiaoZhenJia](https://github.com/PiaoZhenJia)

#### 软件架构

###### 基于Springboot + Maven

###### 仅引用 Swagger-ui 和 lombok 为轻侵入开发插件

###### 引入两个常用apache common工具类

###### 未使用数据库 通过序列化对象代替

###### 前端使用原生JQuery编写 无任何插件引用

###### 前端背景动图来自于互联网 未使用UI库

#### 开发环境

1. jdk 1.8+
2. Maven
* 使用老版本Idea需安装Lombok插件 使用其他IDE请自行适配Lombok

#### 如何开发

1. 打开IDEA
2. 等待Maven下载依赖
3. 点击运行

* 本人对难以运行的java程序深恶痛绝 故本人项目皆可轻松启动

#### 使用说明

1. 将jar包放置于希望存放网盘文件的路径
2. 在这个文件夹运行jar包 例:linux: ```nohup java -jar ./JdfsNetDisk.jar &```
3. 首次运行会自动在当前目录创建工作空间文件夹***JdfsWorkSpace***及其中文件
* 工作路径中包括注意事项***readme.txt***


#### 参与贡献

1. 朴振嘉的爸爸妈妈提供了DNA
2. 朴振嘉的室友提供了正确的工作环境
3. 朴振嘉的同学提供了动力
4. 李晔提供了部分辅助和支撑

#### 特技

1. 使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2. Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3. 你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4. [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5. Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6. Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
