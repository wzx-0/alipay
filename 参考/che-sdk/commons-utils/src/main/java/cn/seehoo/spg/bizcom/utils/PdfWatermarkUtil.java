package cn.seehoo.spg.bizcom.utils;

import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author chenzhuo
 */
public final class PdfWatermarkUtil {

    
    /**
     * 将InputStream写入本地文件
     * @param destination 写入本地目录
     * @param inputStream 输入流
     * @throws IOException
     */
    public static void writeTolocal(String destination, InputStream inputStream) throws IOException {
        int index;
        byte[] bytes = new byte[1024];
        FileOutputStream downloadFile = new FileOutputStream(destination);
        while ((index = inputStream.read(bytes))!=-1){
            downloadFile.write(bytes,0,index);
            downloadFile.flush();
        }
        downloadFile.close();
        inputStream.close();
    }

    /**
     * 给PDF添加水印
     * @param inputFile 原文件路径+名称
     * @param outputFile 添加水印后输出文件路径+名称
     * @param waterMarkName 添加睡觉的内容
     */
    public static void  PdfWatermarkLocal(String inputFile,String outputFile,String waterMarkName,float fillOpacity,float strokeOpacit){
        try {
            PdfReader reader = new PdfReader(inputFile);
            PdfStamper stamper = new PdfStamper(reader, Files.newOutputStream(Paths.get(outputFile)));
            BaseFont base = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.EMBEDDED);
            PdfGState gs = new PdfGState();
            gs.setFillOpacity(fillOpacity);//改透明度
            gs.setStrokeOpacity(strokeOpacit);
            int total = reader.getNumberOfPages()+1;
            JLabel label = new JLabel();
            label.setText(waterMarkName);
            PdfContentByte under;
            Rectangle pageRect = null;
            for (int i = 1; i < total; i++) {
                pageRect = reader.getPageSizeWithRotation(i);
                int width1 = (int) pageRect.getWidth();
                int height1 = (int) pageRect.getHeight();
                int fontSize = (width1+height1)/80;
                int interval = fontSize * 2;
                int xWidth = getStrWidth(waterMarkName,fontSize);
                int xCanNum = width1 / xWidth + 1;
                int yCanNum = height1 / fontSize + 1;
                //在内容上方加水印
                under = stamper.getOverContent(i);
                //在内容下方加水印
                //under = stamper.getUnderContent(i);
                under.saveState();
                under.setGState(gs);
                under.beginText();
                //水印字体和大小
                under.setFontAndSize(base,fontSize);
                for (int j = 0; j < yCanNum; j++) {
                    int y = fontSize * j + interval * j;
                    for (int k = 0; k < xCanNum; k++) {
                        int x = xWidth * k + interval * k;
                        under.showTextAligned(Element.ALIGN_LEFT,waterMarkName,x,y - (fontSize + interval) * k,30);
                    }
                }
                //添加水印文字
                under.endText();
            }
            stamper.close();
            reader.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 给PDF添加水印
     * @param inputStream 输入流
     * @param waterMarkName 添加水印内容
     * @return OutputStream 输出流
     */
    public static OutputStream PdfWatermarkOut(InputStream inputStream,String waterMarkName,float fillOpacity,float strokeOpacit){
        try {
            PdfReader reader = new PdfReader(inputStream);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(reader, outputStream);
            BaseFont base = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.EMBEDDED);
            PdfGState gs = new PdfGState();
            gs.setFillOpacity(fillOpacity);//改透明度
            gs.setStrokeOpacity(strokeOpacit);
            int total = reader.getNumberOfPages()+1;
            JLabel label = new JLabel();
            label.setText(waterMarkName);
            PdfContentByte under;
            Rectangle pageRect = null;
            for (int i = 1; i < total; i++) {
                pageRect = reader.getPageSizeWithRotation(i);
                int width1 = (int) pageRect.getWidth();
                int height1 = (int) pageRect.getHeight();
                int fontSize = (width1+height1)/80;
                int interval = fontSize * 2;
                int xWidth = getStrWidth(waterMarkName,fontSize);
                int xCanNum = width1 / xWidth + 1;
                int yCanNum = height1 / fontSize + 1;
                //在内容上方加水印
                under = stamper.getOverContent(i);
                //在内容下方加水印
                //under = stamper.getUnderContent(i);
                under.saveState();
                under.setGState(gs);
                under.beginText();
                //水印字体和大小
                under.setFontAndSize(base,fontSize);
                for (int j = 0; j < yCanNum; j++) {
                    int y = fontSize * j + interval * j;
                    for (int k = 0; k < xCanNum; k++) {
                        int x = xWidth * k + interval * k;
                        under.showTextAligned(Element.ALIGN_LEFT,waterMarkName,x,y - (fontSize + interval) * k,30);
                    }
                }
                //添加水印文字
                under.endText();
            }
            stamper.close();
            reader.close();
            return  outputStream;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 给PDF添加水印
     * @param inputStream 输入流
     * @param waterMarkName 添加水印内容
     * @return InputStream
     */
    public static InputStream PdfWatermarkInt(InputStream inputStream,String waterMarkName,float fillOpacity,float strokeOpacit){
        try {
            PdfReader reader = new PdfReader(inputStream);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(reader, outputStream);
            BaseFont base = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.EMBEDDED);
            PdfGState gs = new PdfGState();
            gs.setFillOpacity(fillOpacity);//改透明度
            gs.setStrokeOpacity(strokeOpacit);
            int total = reader.getNumberOfPages()+1;
            JLabel label = new JLabel();
            label.setText(waterMarkName);
            PdfContentByte under;
            Rectangle pageRect = null;
            for (int i = 1; i < total; i++) {
                pageRect = reader.getPageSizeWithRotation(i);
                int width1 = (int) pageRect.getWidth();
                int height1 = (int) pageRect.getHeight();
                int fontSize = (width1+height1)/80;
                int interval = fontSize * 2;
                int xWidth = getStrWidth(waterMarkName,fontSize);
                int xCanNum = width1 / xWidth + 1;
                int yCanNum = height1 / fontSize + 1;
                //在内容上方加水印
                under = stamper.getOverContent(i);
                //在内容下方加水印
                //under = stamper.getUnderContent(i);
                under.saveState();
                under.setGState(gs);
                under.beginText();
                //水印字体和大小
                under.setFontAndSize(base,fontSize);
                for (int j = 0; j < yCanNum; j++) {
                    int y = fontSize * j + interval * j;
                    for (int k = 0; k < xCanNum; k++) {
                        int x = xWidth * k + interval * k;
                        under.showTextAligned(Element.ALIGN_LEFT,waterMarkName,x,y - (fontSize + interval) * k,30);
                    }
                }
                //添加水印文字
                under.endText();
            }
            stamper.close();
            reader.close();
            return  new ByteArrayInputStream(outputStream.toByteArray());
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static int getStrWidth(String str,int fontSize){
        char[] chars = str.toCharArray();
        int fontSize2 = fontSize/2;
        int width = 0;
        for (char c:chars) {
            int len = String.valueOf(c).getBytes().length;
            if (len != 1){
                width +=fontSize;
            }else {
                width += fontSize2;
            }
        }
        return width ==0?1:width;
    }
    
    /**
     * 水印PDF背景图片
     * @param inputStream 输入流
     * @param waterMarkFilePath 水印图片的路径
     * @return
     */
    public static InputStream  PdfWatermarkFile(InputStream inputStream,byte[] waterMarkFilePath,float fillOpacity,float strokeOpacit){
        try {
            PdfReader reader = new PdfReader(inputStream);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(reader, outputStream);
            BaseFont base = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.EMBEDDED);
            PdfGState gs = new PdfGState();
            gs.setFillOpacity(fillOpacity);//改透明度
            gs.setStrokeOpacity(strokeOpacit);
            int total = reader.getNumberOfPages()+1;
            PdfContentByte under;
            Rectangle pageRect = null;
            Image image = Image.getInstance(waterMarkFilePath);
            image.setAbsolutePosition(0,0);
            for (int i = 1; i < total; i++) {
                pageRect = reader.getPageSizeWithRotation(i);
                int width1 = (int) pageRect.getWidth();
                int height1 = (int) pageRect.getHeight();
                //在内容上方加水印
                under = stamper.getOverContent(i);
                //在内容下方加水印
                //under = stamper.getUnderContent(i);
                under.saveState();
                under.setGState(gs);
                under.beginText();
                image.scaleAbsolute(width1,height1);
                under.addImage(image);
                //添加水印文字
                under.endText();
            }
            stamper.close();
            reader.close();
            return  new ByteArrayInputStream(outputStream.toByteArray());
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
