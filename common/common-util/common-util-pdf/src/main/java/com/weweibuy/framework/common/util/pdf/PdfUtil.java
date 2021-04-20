package com.weweibuy.framework.common.util.pdf;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PdfUtil {
    /**
     * 字体一般就指定在项目里面避免放在 dockerFile中会有坑
     */
    private static final String FONT_PATH = "font/PingFang-SC.ttf";

    /**
     * html转化为pdf文件
     *
     * @param outputStream 输出流
     * @param html         html
     * @throws IOException exception
     * @throws DocumentException DocumentException
     */
    public static void convertHtml(ByteArrayOutputStream outputStream, String html) throws IOException, DocumentException {
        ITextRenderer renderer = new ITextRenderer();
        ITextFontResolver fontResolver = renderer.getFontResolver();
        fontResolver.addFont(FONT_PATH, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);
    }
}