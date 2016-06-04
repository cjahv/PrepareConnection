package com.sshp.model.result.impl;

import com.sshp.model.result.PrepareConnection;
import com.sshp.web.exception.InsideException;
import org.hibernate.Session;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 内容摘要 ：存储过程执行器实现
 * 创建人　 ：陈佳慧
 * 创建日期 ：2016年六月04日 20:10
 */
public class PrepareConnectionImpl implements PrepareConnection {
    private Session session;
    private String prepareName;

    private Map<Object, Object> parameter = new HashMap<>();
    private Map<Object, Integer> outParameter = new HashMap<>();

    private CallableStatement statement;
    private List<Object> list = new ArrayList<>();

    public PrepareConnectionImpl(Session session, String prepareName) {
        this.session = session;
        this.prepareName = prepareName;
    }

    @Override
    public PrepareConnection setParameter(Object... parameters) {
        return setParameter(1, parameters);
    }

    @Override
    public PrepareConnection setParameter(int start, Object... parameters) {
        int length = parameters.length + start;
        for (int i = start; i < length; i++) {
            this.parameter.put(i, parameters[i - 1]);
        }
        return this;
    }

    @Override
    public PrepareConnection setParameter(String i, Object parameter) {
        this.parameter.put(i, parameter);
        return this;
    }

    @Override
    public PrepareConnection setOutParameter(int start, int... parameters) {
        int length = parameters.length + start;
        for (int i = start; i < length; i++) {
            this.outParameter.put(i, parameters[i - 1]);
        }
        return this;
    }

    @Override
    public PrepareConnection setOutParameter(String i, int parameter) {
        this.outParameter.put(i, parameter);
        return this;
    }

    @Override
    public CallableStatement execute() {
        session.doWork(c -> {
            StringBuilder name = new StringBuilder("{call ").append(prepareName).append('(');
            for (int i = 0; i < parameter.size(); i++) {
                name.append('?').append(',');
            }
            for (int i = 0; i < outParameter.size(); i++) {
                if (!parameter.keySet().contains(outParameter.get(i))) {
                    name.append('?').append(',');
                }
            }
            if (name.charAt(name.length() - 1) == ',')
                name.deleteCharAt(name.length() - 1);
            name.append(")}");
            CallableStatement call = c.prepareCall(name.toString());
            for (Object o : parameter.keySet()) {
                Object v = parameter.get(o);
                if (o instanceof String) {
                    call.setObject((String) o, v);
                } else {
                    call.setObject((int) o, v);
                }
            }
            for (Object o : outParameter.keySet()) {
                int types = outParameter.get(o);
                if (o instanceof String) {
                    call.registerOutParameter((String) o, types);
                } else {
                    call.registerOutParameter((int) o, types);
                }
            }
            call.execute();
            statement = call.unwrap(CallableStatement.class);
        });
        return this.statement;
    }

    @Override
    public List list() {
        try {
            for (Object o : outParameter.keySet()) {
                if (o instanceof String) {
                    list.add(statement.getObject((String) o));
                } else {
                    list.add(statement.getObject((int) o));
                }
            }
        } catch (SQLException e) {
            throw new InsideException("存储过程执行器异常!", e);
        }
        return list;

    }
}
