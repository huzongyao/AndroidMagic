AndroidMagic(使用LibMagic识别文件类型)
==================
An android project to query file info with LibMagic

[![Travis](https://img.shields.io/appveyor/ci/gruntjs/grunt.svg)](https://github.com/huzongyao/AndroidMagic/releases)
[![Travis](https://img.shields.io/badge/file-v5.37-brightgreen.svg)](https://github.com/file/file)

### Introduction
The file command is "a file type guesser", that is, a command-line tool that tells you in words
 what kind of data a file contains. Unlike most GUI systems, command-line UNIX systems - with this
 program leading the charge - don't rely on filename extentions to tell you the type of a file,
 but look at the file's actual contents. This is, of course, more reliable, but requires a bit of I/O.

### Screenshot
![screenshot](https://github.com/huzongyao/AndroidMagic/blob/master/misc/screen.gif?raw=true)

### Details
This project is for me to learn Java, NDK, and for fun.
* learn how to build ndk and sign apk with android gradle-experimental
* source code is from open source implementation of the file command
* libmagic is "a file type guesser", that tells you in words what kind of data a file contains

### 学习记录
* 使用libmagic，我们不用看文件的后缀，就可以识别出常用文件类型
* 移植LibMagic到安卓平台上, 使文件类型的识别不仅通过扩展名
* 使用cmake编译NDK
* Java与本地代码之间内存拷贝GetByteArrayRegion
* butterknife 以及 rxjava的使用

### Useful Links
* Official Site: http://www.darwinsys.com/file/
* GitHub：https://github.com/file/file

### About Me
 * GitHub: [https://huzongyao.github.io/](https://huzongyao.github.io/)
 * ITEye博客：[http://hzy3774.iteye.com/](http://hzy3774.iteye.com/)
 * 新浪微博: [http://weibo.com/hzy3774](http://weibo.com/hzy3774)

### Contact To Me
 * QQ: [377406997](http://wpa.qq.com/msgrd?v=3&uin=377406997&site=qq&menu=yes)
 * Gmail: [hzy3774@gmail.com](mailto:hzy3774@gmail.com)
 * Foxmail: [hzy3774@qq.com](mailto:hzy3774@qq.com)