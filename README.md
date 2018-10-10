# ReferenceDemo
try to understand all reference.
在引用与引用队列一起使用时，软引用或虚引用不管对象在finalize时能否自救都能加入到引用队列，而虚引用则不然，必须是对象自救失败并销毁了，才加入引用队列。
