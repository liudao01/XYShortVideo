# XYVideo
实现短视频功能


自己做一个短视频作品  本人的短视频学习项目

1. 添加水印,水印贴图(控制位置,动态贴图).预览的水印, 录制的水印, 录制完成之后播放.------- 控制位置和动态贴图没做(原理主要是坐标转换) .其他做了
2. 要做的: 分屏.三个屏.九个屏 延时. --===== 原理绘制多个纹理
3. 要做的: 滤镜.美颜 实时滤镜--分解 
4. 背景音乐.------录制时添加背景音乐 做了
5. 视频抽帧-----每秒变成图片.做了
6. 断点拍摄.------做了
7. 视频剪辑(裁剪).---1.16 -- 解决了 做了  裁剪完成后需要给个提示
8. 视频合并.----这个和断点拍摄技术点一样 略过
9. 视频拼接(图像的拼接).
10. 图片合成视频. 有了
12. 本地转码( mp4 转avi 等)

开始:

做什么事情之前需要先想清楚做什么 规划好 后面不会感觉到凌乱.

目标1. 通过录制视频方式添加水印,视频格式mp4. 水印可以控制位置.



---

ffmpeg 合并视频. 搜了很多命令 都是需要创建一个文本文件 然后把需要合并的视频文件名字放进去. 在这里找到答案.
ffmpeg -i "concat:input1|input2" -codec copy output
这种连接方式不适合mp4

 https://superuser.com/questions/1059245/ffmpeg-join-two-mp4-files-with-ffmpeg-on-command-line





参考了很多项目

特别感谢: 万里兄的教程   https://blog.csdn.net/ywl5320/article/details/83581509

https://github.com/CainKernel/CainCamera  滤镜美颜

其他github参考


https://github.com/qqchenjian318/VideoEditor-For-Android

https://github.com/ICECHN/VideoRecorderWithOpenGL
