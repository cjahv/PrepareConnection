package com.sshp.model.result;

import java.sql.CallableStatement;
import java.util.List;

/**
 * 内容摘要 ：存储过程执行器
 * 创建人　 ：陈佳慧
 * 创建日期 ：2016年六月04日 20:07
 */
public interface PrepareConnection {
    PrepareConnection setParameter(Object... parameters);

    PrepareConnection setParameter(int start, Object... parameters);

    PrepareConnection setParameter(String i, Object parameter);

    PrepareConnection setOutParameter(int start, int... parameters);

    PrepareConnection setOutParameter(String i, int parameter);

    CallableStatement execute();

    List list();
}
