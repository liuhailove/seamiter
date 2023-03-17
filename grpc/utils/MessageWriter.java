package com.shopee.seamiter.grpc.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import com.shopee.seamiter.grpc.domain.CallResults;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author honggang.liu
 */
public class MessageWriter<T extends Message> implements StreamObserver<T> {

    /**
     * LOGGER
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageWriter.class.getSimpleName());

    private final JsonFormat.Printer printer;
    private final CallResults results;

    private MessageWriter(JsonFormat.Printer printer, CallResults results) {
        this.printer = printer;
        this.results = results;
    }

    public static <T extends Message> MessageWriter<T> newInstance(JsonFormat.TypeRegistry registry, CallResults results) {
        return new MessageWriter<>(
                JsonFormat.printer().usingTypeRegistry(registry).includingDefaultValueFields(),
                results);
    }

    @Override
    public void onNext(T value) {
        try {
            results.add(printer.print(value));
        } catch (InvalidProtocolBufferException e) {
            LOGGER.error("Skipping invalid response message", e);
        }
    }

    @Override
    public void onError(Throwable t) {
        LOGGER.error("Messages write occur errors", t);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("error", t.getMessage());
        results.add(jsonObject.toJSONString());
    }

    @Override
    public void onCompleted() {
        LOGGER.debug("Messages write complete");
    }
}
