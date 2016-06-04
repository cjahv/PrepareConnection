# PrepareConnection
#####简单的存储过程执行器 v1.0
#####没有发现hibernate执行存储过程的方法就自己封装了!

# 使用方法
可以在任何能获取hibernate session的地方使用 
```Java
new PrepareConnectionImpl(session,prepareName);
```
也可以封装到service或dao层,这是我封装到dao的代码
```Java
@Override
public PrepareConnection prepareCall(String prepareName) {
    return new PrepareConnectionImpl(getSession(), prepareName);
}
```


# 属性说明   

|属性         |说明             |
|------------|------------------------------|
|statement |  储存过程的原始返回值           |
|list      |  按照输出参数返回的Object[]列队 |

# 方法说明

|  属性 |  说明           |
|------------|------------------------------|
|  setParameter |  设置输入参数           |
|  setOutParameter      |  设置输出参数 |
|  execute      |  执行存储过程 |
|  list      |  获取存储过程输出参数列队,懒加载 |

>>>吐槽下readme.md是谁抽风想出来的,非吉尔难写