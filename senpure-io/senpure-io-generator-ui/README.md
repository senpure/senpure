io-generator 根据对应的.io文件生成对应的源代码

#### 环境支持

windows 需要安装jdk8

#### 快速开始

java -jar xxx.jar

#### 参数说明

使用-Dsilence=true 使用静默方式启动(即没有图形界面)
以下配置在静默方式下生效

| 参数        | 说明                                                                | 备注             |
|:------------|:--------------------------------------------------------------------|:-----------------|
| -Dproject   | 使用项目名                                                          | 默认读取配置文件 |
| -Dfile      | 协议文件路径多个,隔开文件夹获文件                                   | 默认读取配置文件 |
| -Dsensitive | 使敏感设置生效(如handler覆盖,合并等)                                | 默认false        |
| -DgenJava   | 生成java代码                                                        | 默认false        |
| -DgenLua    | 生成lua代码                                                         | 默认false        |
| -DgenJs     | 生成js代码                                                          | 默认false        |
| -DjavaOut   | 生成java代码的根路径,该值存在时 含有-DgenJava=false时不生成java代码 | 默认读取配置文件 |
| -DluaOut    | 生成lua代码的根路径                                                 | 默认读取配置文件 |
| -DjsOut     | 生成js代码的根路径                                                  | 默认读取配置文件 |


