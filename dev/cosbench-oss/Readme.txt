阿里云计算开放服务软件开发工具包Java版
Aliyun OSS SDK for Java

版权所有 （C）阿里云计算有限公司

Copyright (C) Alibaba Cloud Computing
All rights reserved.

http://www.aliyun.com

环境要求：
- J2SE Development Kit (JDK) 6.0或以上版
- Apache Commons （Codec、HTTP Client和Logging）、JDOM等第三方软件包（已包含在SDK的下载压缩包中）。
- 必须注册有Aliyun.com用户账户，并开通OSS服务。

2.5.0更新日志：
- 添加：支持get bucket stat
- 添加：CName支持IsPurgeCdnCache
- 添加：强制关闭OSSObject的功能
- 修复：断点续传下载不成功不修改本地同名文件
- 修复：BucketInfo添加解析ExtranetEndpoint/IntranetEndpoint
- 修复：修复key为""时抛IndexOutOfBoundException的异常
- 修复：initiateMultipartUpload特定情况下抛NullPointerException的异常
- 优化：AccessControlList中添加权限直接描述
- 优化：统一UDF/上传回调后返回的Response
- 优化：InvalidResponse异常的描述信息中添加上下文信息
