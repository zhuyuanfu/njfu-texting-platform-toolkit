** 关于MAS平台：
  这个平台由spring+struts2搭建，使用Cookie: JSESSIONID=xxx存储用户登录状态。为了伪造自己的用户身份，目前采用的方案是在浏览器登录一下，抓包抓到JSESSIONID，然后使用该JSESSIONID模拟用户身份登录。
  以后要改成访问登录页面-输入用户名密码验证码-登录-抓JSESSIONID的方式模拟用户身份。
** 关于电信平台：
  暂无相关功能
** 关于腾讯云平台：
  暂无相关功能。

本平台用法：创建好G:\toolkit\masPlatformJSESSIONID.txt文件，然后启动App。
