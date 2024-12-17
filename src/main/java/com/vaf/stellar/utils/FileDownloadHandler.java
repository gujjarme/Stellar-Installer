//package com.vaf.stellar.utils;
//import com.vaf.stellar.installationSteps.ProgressDisplayController;
//import io.netty.bootstrap.Bootstrap;
//import io.netty.buffer.ByteBuf;
//import io.netty.channel.*;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.nio.NioSocketChannel;
//import io.netty.handler.codec.http.*;
//import io.netty.handler.stream.ChunkedWriteHandler;
//import javafx.application.Platform;
//
//
//import java.io.FileOutputStream;
//
//public class FileDownloadHandler extends SimpleChannelInboundHandler<HttpObject> {
//    private final String saveFilePath;
//    private final double startProgress;
//    private final double endProgress;
//    private final ProgressDisplayController controller;
//    private FileOutputStream fileOutputStream;
//    private long totalRead = 0;
//    private long contentLength = -1;
//
//    public FileDownloadHandler(String saveFilePath, double startProgress, double endProgress, ProgressDisplayController controller) {
//        this.saveFilePath = saveFilePath;
//        this.startProgress = startProgress;
//        this.endProgress = endProgress;
//        this.controller = controller;
//    }
//
//    @Override
//    public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
//        if (msg instanceof HttpResponse) {
//            HttpResponse response = (HttpResponse) msg;
//            contentLength = Long.parseLong(response.headers().get(HttpHeaderNames.CONTENT_LENGTH, "-1"));
//            fileOutputStream = new FileOutputStream(saveFilePath);
//        }
//
//        if (msg instanceof HttpContent) {
//            HttpContent content = (HttpContent) msg;
//            ByteBuf byteBuf = content.content();
//
//            byte[] buffer = new byte[byteBuf.readableBytes()];
//            byteBuf.readBytes(buffer);
//            fileOutputStream.write(buffer);
//
//            totalRead += buffer.length;
//            if (contentLength > 0) {
//                double progress = startProgress + (double) totalRead / contentLength * (endProgress - startProgress);
//                Platform.runLater(() -> controller.updateProgress(progress));
//            }
//
//            if (content instanceof LastHttpContent) {
//                fileOutputStream.close();
//                ctx.close();
//            }
//        }
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//        Platform.runLater(() -> {
//            ErrorUtils.showInfoPopup("Download failed.");
//            controller.enableResumeButton();
//        });
//        cause.printStackTrace();
//        ctx.close();
//    }
//}