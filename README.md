# MultiThreadDownload
多线程下载文件，支持断点

## 运行效果图

![image](https://upload-images.jianshu.io/upload_images/1472453-aa3d81c85cd6942c.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/320)

### 怎样去设计，思考这样一个框架？

我们可以这样理解，一个大任务或者多个大任务，利用单一的线程去下载的话，肯定会很慢，效率也不高。如果我们把一个大任务去拆分一个一个的小任务，利用多个线程去下载一个一个的小任务的话，每个小任务的进度叠加起来，这样做且不下载的速度很快了，效率也提升了。在下载的过程中，我们又需要考虑特殊的情况，用户可能需要中止下载，这时我们需要保存下下载的进度，下次进来时，读取上次的下载进度，接着下载。

哈，其实像fork/join框架
<img src="https://upload-images.jianshu.io/upload_images/1472453-7d3ec954e6a02e38.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240" width="230" hegiht="300" align=center />

### License

    
    Copyright 2018 StevenYan88
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
    http://www.apache.org/licenses/LICENSE-2.0  
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
