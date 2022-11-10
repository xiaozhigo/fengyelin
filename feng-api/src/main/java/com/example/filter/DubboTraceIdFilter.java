package com.example.filter;

import com.alibaba.dubbo.common.Constants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.slf4j.MDC;

@Activate(group = {Constants.PROVIDER, Constants.CONSUMER}, order = -9000)
public class DubboTraceIdFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext rpcContext = RpcContext.getContext();

        // before
        if (rpcContext.isProviderSide()) {
            // get traceId from dubbo consumer，and set traceId to MDC
            String traceId = rpcContext.getAttachment(TraceUtil.TRACE_ID);
            MDC.put(TraceUtil.TRACE_ID, traceId);
        }

        if (rpcContext.isConsumerSide()) {
            // get traceId from MDC, and set traceId to rpcContext
            String traceId = MDC.get(TraceUtil.TRACE_ID);
            rpcContext.setAttachment(TraceUtil.TRACE_ID, traceId);
        }

        Result result = invoker.invoke(invocation);
        // after
        if (rpcContext.isProviderSide()) {
            // clear traceId from MDC
            MDC.remove(TraceUtil.TRACE_ID);
        }
        return result;
    }
}

