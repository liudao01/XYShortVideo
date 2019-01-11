# XYVideo
实现短视频功能


自己做一个短视频作品


1. 添加水印,水印贴图
2. 视频剪辑(裁剪)
3. 视频合并
4. 视频拼接
5. 断点拍摄
6. 图片合成视频
7. 视频慢放、倒放(可尝试通过修改视频的时间戳实现)
8. 本地转码(avi 转 mp4 等)
9. MV 特效
10. 分屏
11. 背景音乐
12. 拼接
13. 滤镜
14. 美颜


开始:

做什么事情之前需要先想清楚做什么 规划好 后面不会感觉到凌乱.

目标1. 通过录制视频方式添加水印,视频格式mp4. 水印可以控制位置.

12.26 项目搭建 ffmpeg导入  (一点点来, 先实现在左下角添加文字水印)
    0.
    1. 通过预览方式录制视频 在视频左下角放文字水印 .
        添加水印的方式又分两种.

        1. 使用OpenGL 渲染添加水印.
        2 使用ffmpeg 添加水印
        ---

        其中ffmpeg 添加水印需要操作avframe, 而 avframe 是通过avpacket解出来的 ,然后通过FFMPEG 解码avframe.这是软解码.
        如果是硬解码只会解析到avpacket 然后把avpacket交给mediacodec 进行解码 不会有avframe.
        所以 如果是软解码可以使用ffmpeg 添加水印, 如果是硬解码 需要使用OpenGL 添加水印.
---
12.27  目前已经实现预览水印.  添加水印的方式又分两种.
        1. 录制视频的时候添加水印保存成为mp4文件,
        2. 打开本地视频添加水印.
       这两种其实实现的方式都类似. 因为都需要通过OpenGL 渲染到界面上. 那么我再渲染的时候给加上水印就好了.


---

ffmpeg 合并视频. 搜了很多命令 都是需要创建一个文本文件 然后把需要合并的视频文件名字放进去. 在这里找到答案.
ffmpeg -i "concat:input1|input2" -codec copy output
这种连接方式不适合mp4

 https://superuser.com/questions/1059245/ffmpeg-join-two-mp4-files-with-ffmpeg-on-command-line





参考了很多项目

特别感谢: 万里兄的教程   https://blog.csdn.net/ywl5320/article/details/83581509

其他github参考

https://github.com/qqchenjian318/VideoEditor-For-Android

https://github.com/ICECHN/VideoRecorderWithOpenGL
