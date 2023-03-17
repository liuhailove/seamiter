package com.shopee.seamiter.grpc.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.protobuf.ByteString;
import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.Channel;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.StatusRuntimeException;
import io.grpc.reflection.v1alpha.ListServiceResponse;
import io.grpc.reflection.v1alpha.ServerReflectionGrpc;
import io.grpc.reflection.v1alpha.ServerReflectionRequest;
import io.grpc.reflection.v1alpha.ServerReflectionResponse;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * grpc反射服务客户端
 *
 * @author honggang.liu
 */
public class ServerReflectionClient {

    /**
     * LOGGER
     */
    private static Logger LOGGER = LoggerFactory.getLogger(ServerReflectionClient.class.getSimpleName());

    /**
     * 列举RPC服务的截至时长
     */
    private static final long LIST_RPC_DEADLINE_MS = 10000;

    /**
     * 查找RPC服务的截至时长
     */
    private static final long LOOKUP_RPC_DEADLINE_MS = 10000;

    /**
     * 列举RPC服务请求
     * setListServices("") //Not sure what this is for, appears to be ignored.
     */
    private static final ServerReflectionRequest LIST_SERVICES_REQUEST = ServerReflectionRequest.newBuilder()
            .setListServices("")
            .build();

    private final Channel channel;

    /**
     * 根据提供的channel创建RPC反射服务客户端
     *
     * @param channel RPC通道
     * @return 反射服务客户端
     */
    public static ServerReflectionClient create(Channel channel) {
        return new ServerReflectionClient(channel);
    }

    private ServerReflectionClient(Channel channel) {
        this.channel = channel;
    }

    /**
     * 列举服务
     */
    public ListenableFuture<ImmutableList<String>> listServices() {
        ListServicesHandler rpcHandler = new ListServicesHandler();
        StreamObserver<ServerReflectionRequest> requestStream = ServerReflectionGrpc.newStub(channel).withDeadlineAfter(LIST_RPC_DEADLINE_MS, TimeUnit.MILLISECONDS).serverReflectionInfo(rpcHandler);
        return rpcHandler.start(requestStream);
    }

    /**
     * Returns a {@link DescriptorProtos.FileDescriptorSet} containing all the transitive dependencies of the supplied
     * service, as provided by the remote server.
     */
    public ListenableFuture<DescriptorProtos.FileDescriptorSet> lookupService(String serviceName) {
        LookupServiceHandler rpcHandler = new LookupServiceHandler(serviceName);
        StreamObserver<ServerReflectionRequest> requestStream = ServerReflectionGrpc.newStub(channel)
                .withDeadlineAfter(LOOKUP_RPC_DEADLINE_MS, TimeUnit.MILLISECONDS)
                .serverReflectionInfo(rpcHandler);
        return rpcHandler.start(requestStream);
    }

    /**
     * 处理RPC的生命周期
     */
    private static class ListServicesHandler implements StreamObserver<ServerReflectionResponse> {

        /**
         * 结果Future
         */
        private final SettableFuture<ImmutableList<String>> resultFuture;

        /**
         * 请求Stream
         */
        private StreamObserver<ServerReflectionRequest> requestStream;

        /**
         * 构造
         */
        private ListServicesHandler() {
            resultFuture = SettableFuture.create();
        }

        /**
         * start
         *
         * @param requestStream 请求参数
         * @return 服务列表
         */
        public ListenableFuture<ImmutableList<String>> start(StreamObserver<ServerReflectionRequest> requestStream) {
            this.requestStream = requestStream;
            requestStream.onNext(LIST_SERVICES_REQUEST);
            return resultFuture;
        }

        /**
         * Receives a value from the stream.
         *
         * <p>Can be called many times but is never called after {@link #onError(Throwable)} or {@link
         * #onCompleted()} are called.
         *
         * <p>Unary calls must invoke onNext at most once.  Clients may invoke onNext at most once for
         * server streaming calls, but may receive many onNext callbacks.  Servers may invoke onNext at
         * most once for client streaming calls, but may receive many onNext callbacks.
         *
         * <p>If an exception is thrown by an implementation the caller is expected to terminate the
         * stream by calling {@link #onError(Throwable)} with the caught exception prior to
         * propagating it.
         *
         * @param serverReflectionResponse the value passed to the stream
         */
        @Override
        public void onNext(ServerReflectionResponse serverReflectionResponse) {
            ServerReflectionResponse.MessageResponseCase responseCase = serverReflectionResponse.getMessageResponseCase();
            if (responseCase == ServerReflectionResponse.MessageResponseCase.LIST_SERVICES_RESPONSE) {
                handleListServiceResponse(serverReflectionResponse.getListServicesResponse());
            } else {
                LOGGER.info("Got unknown reflection response type: {}", responseCase);
            }
        }

        /**
         * Receives a terminating error from the stream.
         *
         * <p>May only be called once and if called it must be the last method called. In particular if an
         * exception is thrown by an implementation of {@code onError} no further calls to any method are
         * allowed.
         *
         * <p>{@code t} should be a {@link StatusException} or {@link
         * StatusRuntimeException}, but other {@code Throwable} types are possible. Callers should
         * generally convert from a {@link Status} via {@link Status#asException()} or
         * {@link Status#asRuntimeException()}. Implementations should generally convert to a
         * {@code Status} via {@link Status#fromThrowable(Throwable)}.
         *
         * @param t the error occurred on the stream
         */
        @Override
        public void onError(Throwable t) {
            resultFuture.setException(new RuntimeException("Error in server reflection rpc while listing services", t));
        }

        /**
         * Receives a notification of successful stream completion.
         *
         * <p>May only be called once and if called it must be the last method called. In particular if an
         * exception is thrown by an implementation of {@code onCompleted} no further calls to any method
         * are allowed.
         */
        @Override
        public void onCompleted() {
            if (!resultFuture.isDone()) {
                LOGGER.error("Unexpected completion of server reflection rpc while listing services");
                resultFuture.setException(new RuntimeException("Unexpected end of rpc"));
            }
        }

        /**
         * 处理列举服务响应
         *
         * @param response 响应
         */
        private void handleListServiceResponse(ListServiceResponse response) {
            ImmutableList.Builder<String> serviceBuilder = ImmutableList.builder();
            response.getServiceList().forEach(service -> serviceBuilder.add(service.getName()));
            resultFuture.set(serviceBuilder.build());
            requestStream.onCompleted();
        }
    }

    /**
     * 处理RPC生命周期中的lookup操作
     */
    private static class LookupServiceHandler implements StreamObserver<ServerReflectionResponse> {

        /**
         * 结果Future
         */
        private final SettableFuture<DescriptorProtos.FileDescriptorSet> resultFuture;

        /**
         * 服务名称
         */
        private final String serviceName;

        /**
         * 请求描述
         */
        private final Set<String> requestedDescriptors;

        /**
         * 文件描述map
         */
        private final Map<String, DescriptorProtos.FileDescriptorProto> resolvedDescriptors;

        /**
         * 反射请求
         */
        private StreamObserver<ServerReflectionRequest> requestStream;

        // Used to notice when we've received all the files we've asked for and we can end the rpc.
        private int outstandingRequests;

        /**
         * 构造
         *
         * @param serviceName 服务名称
         */
        private LookupServiceHandler(String serviceName) {
            this.serviceName = serviceName;
            this.resultFuture = SettableFuture.create();
            this.requestedDescriptors = new HashSet<>();
            this.resolvedDescriptors = new HashMap<>();
            this.outstandingRequests = 0;
        }

        /**
         * 启动调用
         *
         * @param requestStream 请求流
         * @return 文件分析结果
         */
        public ListenableFuture<DescriptorProtos.FileDescriptorSet> start(StreamObserver<ServerReflectionRequest> requestStream) {
            this.requestStream = requestStream;
            requestStream.onNext(requestForSymbol(serviceName));
            ++outstandingRequests;
            return resultFuture;
        }

        /**
         * Receives a value from the stream.
         *
         * <p>Can be called many times but is never called after {@link #onError(Throwable)} or {@link
         * #onCompleted()} are called.
         *
         * <p>Unary calls must invoke onNext at most once.  Clients may invoke onNext at most once for
         * server streaming calls, but may receive many onNext callbacks.  Servers may invoke onNext at
         * most once for client streaming calls, but may receive many onNext callbacks.
         *
         * <p>If an exception is thrown by an implementation the caller is expected to terminate the
         * stream by calling {@link #onError(Throwable)} with the caught exception prior to
         * propagating it.
         *
         * @param response the value passed to the stream
         */
        @Override
        public void onNext(ServerReflectionResponse response) {
            ServerReflectionResponse.MessageResponseCase responseCase = response.getMessageResponseCase();
            if (responseCase == ServerReflectionResponse.MessageResponseCase.FILE_DESCRIPTOR_RESPONSE) {
                ImmutableSet<DescriptorProtos.FileDescriptorProto> descriptors = parseDescriptors(response.getFileDescriptorResponse().getFileDescriptorProtoList());
                descriptors.forEach(d -> resolvedDescriptors.put(d.getName(), d));
                descriptors.forEach(this::processDependencies);
            } else {
                LOGGER.warn("Got unknown reflection response type: {}", responseCase);
            }
        }

        /**
         * Receives a terminating error from the stream.
         *
         * <p>May only be called once and if called it must be the last method called. In particular if an
         * exception is thrown by an implementation of {@code onError} no further calls to any method are
         * allowed.
         *
         * <p>{@code t} should be a {@link StatusException} or {@link
         * StatusRuntimeException}, but other {@code Throwable} types are possible. Callers should
         * generally convert from a {@link Status} via {@link Status#asException()} or
         * {@link Status#asRuntimeException()}. Implementations should generally convert to a
         * {@code Status} via {@link Status#fromThrowable(Throwable)}.
         *
         * @param t the error occurred on the stream
         */
        @Override
        public void onError(Throwable t) {
            resultFuture.setException(new RuntimeException("Reflection lookup rpc failed for: " + serviceName, t));
        }

        /**
         * Receives a notification of successful stream completion.
         *
         * <p>May only be called once and if called it must be the last method called. In particular if an
         * exception is thrown by an implementation of {@code onCompleted} no further calls to any method
         * are allowed.
         */
        @Override
        public void onCompleted() {
            if (!resultFuture.isDone()) {
                LOGGER.error("Unexpected completion of the server reflection rpc");
                resultFuture.setException(new RuntimeException("Unexpected end of rpc"));
            }
        }

        /**
         * 解析文件协议描述
         *
         * @param descriptorBytes 描述字节流
         * @return 协议描述符
         */
        private ImmutableSet<DescriptorProtos.FileDescriptorProto> parseDescriptors(List<ByteString> descriptorBytes) {
            ImmutableSet.Builder<DescriptorProtos.FileDescriptorProto> resultBuilder = ImmutableSet.builder();
            for (ByteString fileDescriptorBytes : descriptorBytes) {
                try {
                    resultBuilder.add(DescriptorProtos.FileDescriptorProto.parseFrom(fileDescriptorBytes));
                } catch (InvalidProtocolBufferException e) {
                    LOGGER.warn("Failed to parse bytes as file descriptor proto");
                }
            }
            return resultBuilder.build();
        }

        /**
         * 处理依赖
         *
         * @param fileDescriptor 文件协议描述符
         */
        private void processDependencies(DescriptorProtos.FileDescriptorProto fileDescriptor) {
            LOGGER.debug("Processing deps of descriptor: {}", fileDescriptor.getName());
            fileDescriptor.getDependencyList().forEach(dep -> {
                if (!resolvedDescriptors.containsKey(dep) && !requestedDescriptors.contains(dep)) {
                    requestedDescriptors.add(dep);
                    ++outstandingRequests;
                    requestStream.onNext(requestForDescriptor(dep));
                }
            });
            --outstandingRequests;
            if (outstandingRequests == 0) {
                LOGGER.debug("Retrieved service definition for [{}] by reflection", serviceName);
                resultFuture.set(DescriptorProtos.FileDescriptorSet.newBuilder()
                        .addAllFile(resolvedDescriptors.values())
                        .build());
                requestStream.onCompleted();
            }
        }

        private static ServerReflectionRequest requestForDescriptor(String name) {
            return ServerReflectionRequest.newBuilder()
                    .setFileByFilename(name)
                    .build();
        }

        private static ServerReflectionRequest requestForSymbol(String symbol) {
            return ServerReflectionRequest.newBuilder()
                    .setFileContainingSymbol(symbol)
                    .build();
        }
    }

}
