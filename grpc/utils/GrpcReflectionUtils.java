package com.shopee.seamiter.grpc.utils;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.*;
import com.google.protobuf.util.JsonFormat;
import com.shopee.seamiter.grpc.domain.GrpcMethodDefinition;
import io.grpc.Channel;
import io.grpc.MethodDescriptor;
import io.grpc.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * grpc 反射工具类
 *
 * @author honggang.liu
 */
public class GrpcReflectionUtils {

    /**
     * LOGGER
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GrpcReflectionUtils.class.getSimpleName());

    /**
     * 服务解析
     *
     * @param channel 通信通道
     * @return 文件描述符
     */
    public static List<DescriptorProtos.FileDescriptorSet> resolveServices(Channel channel) {
        ServerReflectionClient serverReflectionClient = ServerReflectionClient.create(channel);
        try {
            List<String> services = serverReflectionClient.listServices().get().stream().filter(serviceName -> !GrpcServiceUtils.blockServiceSet.contains(serviceName.toLowerCase())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(services)) {
                LOGGER.info("Can't find services by channel {}", channel);
                return Collections.emptyList();
            }
            return services.stream().map(serviceName -> {
                ListenableFuture<DescriptorProtos.FileDescriptorSet> future = serverReflectionClient.lookupService(serviceName);
                try {
                    return future.get();
                } catch (InterruptedException | ExecutionException e) {
                    LOGGER.error("Get {} fileDescriptor occurs error", serviceName, e);
                    return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toList());
        } catch (Exception t) {
            LOGGER.error("Exception resolve service", t);
            throw new RuntimeException(t);
        }
    }

    public static DescriptorProtos.FileDescriptorSet resolveService(Channel channel, String serviceName) {
        ServerReflectionClient reflectionClient = ServerReflectionClient.create(channel);
        try {
            List<String> serviceNames = reflectionClient.listServices().get();
            if (!serviceNames.contains(serviceName)) {
                throw Status.NOT_FOUND.withDescription(
                                String.format("Remote server does not have service %s. Services: %s", serviceName, serviceNames))
                        .asRuntimeException();
            }
            return reflectionClient.lookupService(serviceName).get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("Resolve services get error", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 抓取完整的方法名字
     *
     * @param methodDescriptor 方法描述
     * @return 完整的方法名字
     */
    public static String fetchFullMethodName(Descriptors.MethodDescriptor methodDescriptor) {
        String serviceName = methodDescriptor.getService().getFullName();
        String methodName = methodDescriptor.getName();
        return generateFullMethodName(serviceName, methodName);
    }

    /**
     * 获取方法类型
     *
     * @param methodDescriptor 方法描述符
     * @return 方法类型
     */
    public static MethodDescriptor.MethodType fetchMethodType(Descriptors.MethodDescriptor methodDescriptor) {
        boolean clientStreaming = methodDescriptor.toProto().getClientStreaming();
        boolean serverStreaming = methodDescriptor.toProto().getServerStreaming();
        if (clientStreaming && serverStreaming) {
            return MethodDescriptor.MethodType.BIDI_STREAMING;
        } else if (!clientStreaming && !serverStreaming) {
            return MethodDescriptor.MethodType.UNARY;
        } else if (!clientStreaming) {
            return MethodDescriptor.MethodType.SERVER_STREAMING;
        } else {
            return MethodDescriptor.MethodType.SERVER_STREAMING;
        }
    }

    /**
     * 解析为消息体
     *
     * @param registry   注册的类型信息
     * @param descriptor 描述信息
     * @param jsonTexts  消息json格式
     * @return 动态消息体
     */
    public static List<DynamicMessage> parseToMessage(JsonFormat.TypeRegistry registry, Descriptors.Descriptor descriptor, List<String> jsonTexts) {
        JsonFormat.Parser parser = JsonFormat.parser().usingTypeRegistry(registry);
        List<DynamicMessage> messages = new ArrayList<>();
        try {
            for (String jsonText : jsonTexts) {
                DynamicMessage.Builder messageBuilder = DynamicMessage.newBuilder(descriptor);
                parser.merge(jsonText, messageBuilder);
                messages.add(messageBuilder.build());
            }
            return messages;
        } catch (InvalidProtocolBufferException e) {
            throw new IllegalArgumentException("Unable to parse json text", e);
        }
    }

    /**
     * 把原始方法转换为GRPC方法定义
     *
     * @param rawMethodName 原始方法名字
     * @return GRPC方法定义
     */
    public static GrpcMethodDefinition parseToMethodDefinition(String rawMethodName) {
        checkArgument(StringUtils.hasLength(rawMethodName), "Raw method name can't be empty.");
        int methodSplitPosition = rawMethodName.lastIndexOf(".");
        checkArgument(methodSplitPosition != -1, "No package name and service name found.");
        String methodName = rawMethodName.substring(methodSplitPosition + 1);
        checkArgument(StringUtils.hasLength(methodName), "Method name can't be empty.");
        String fullServiceName = rawMethodName.substring(0, methodSplitPosition);
        int serviceSplitPosition = fullServiceName.lastIndexOf(".");
        String serviceName = fullServiceName.substring(serviceSplitPosition + 1);
        String packageName = "";
        if (serviceSplitPosition != -1) {
            packageName = fullServiceName.substring(0, serviceSplitPosition);
        }
        checkArgument(StringUtils.hasLength(serviceName), "Service name can't be empty.");
        return new GrpcMethodDefinition(packageName, serviceName, methodName);
    }
}
