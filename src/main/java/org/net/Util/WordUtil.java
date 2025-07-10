package org.net.Util;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.docx4j.Docx4J;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
*   Word转换pdf3种方法，并且可以插入图片/二维码
**/
public class WordUtil {

    public static void main(String[] args) {
        try {
            processDocument();
            System.out.println("处理完成！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void processDocument() throws Exception {

        // 1. 处理Word文档

        try {
            XWPFDocument document = new XWPFDocument(new FileInputStream("D://final.docx"));
            // 替换模板中的占位符
            replacePlaceholder(document, "companyName", "张三w话哟ss死公司");
            replacePlaceholder(document, "navicertNumber", "1R1D2X1QWXAS");
            replacePlaceholder(document, "cph", "1212D");
            replacePlaceholder(document, "cjh", "DDSVS4R3121");
            replacePlaceholder(document, "xsz", "9A8OJD9QID2");
            replacePlaceholder(document, "sy", "2025");
            replacePlaceholder(document, "sm", "11");
            replacePlaceholder(document, "sd", "11");
            replacePlaceholder(document, "en", "2026");
            replacePlaceholder(document, "em", "1");
            replacePlaceholder(document, "ed", "2");
            // 输出文件路径
            String outputPath = "d://output.docx";
            FileOutputStream fos = new FileOutputStream(outputPath);
            document.write(fos);
            // 关闭流
            document.close();
            wordToPdfDocument4J("d://output.docx", "d://outputDocument4j.pdf");

            InsertImageToPDF("d://outputDocument4j.pdf" , "d://outputImg.pdf");
            System.out.println("Word 文件导出成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void InsertImageToPDF(String pdfPath ,String outputPdfPath ) throws IOException, InvalidFormatException, WriterException {
        // 加载PDF文档
        PDDocument document = PDDocument.load(new File(pdfPath));
        // 获取第一页
        PDPage page = document.getPage(0);

        // 加载图片
        PDImageXObject image = PDImageXObject.createFromFileByExtension(getQrCode(), document);

        // 创建内容流并设置图片位置和大小
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)) {
            float x = 350; // 图片的X坐标
            float y = 170; // 图片的Y坐标
            float width = 100; // 图片宽度
            float height = 100; // 图片高度
            contentStream.drawImage(image, x, y, width, height);
        }

        // 保存修改后的PDF并关闭文档
        document.save(outputPdfPath);
        document.close();
    }
    private static void wordToPdfDocument4J(String filePath,String outFilePath){
        //源文件地址
        File inputWord = new File(filePath);
        //导出文件地址
        File outputFile = new File(outFilePath);
        InputStream doc = null;
        OutputStream outputStream = null;
        try {
            doc = new FileInputStream(inputWord);
            outputStream = new FileOutputStream(outputFile);
            IConverter converter = LocalConverter.builder().build();
            //转换docx=>pdf
            boolean flag = converter.convert(doc).as(DocumentType.DOC).to(outputStream).as(DocumentType.PDF).execute();
            if (flag) {
                converter.shutDown();
            }
            doc.close();
            outputStream.close();
            System.out.println("文件名：" + outFilePath + " 转换成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void wordToPdsItx(String inputPath,String outPath) throws Exception {
        try (FileInputStream fis = new FileInputStream(inputPath);
             XWPFDocument document = new XWPFDocument(fis);
             FileOutputStream fos = new FileOutputStream(outPath);
             PdfWriter writer = new PdfWriter(fos);
             Document pdfDoc = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer))) {


            for (XWPFParagraph para : document.getParagraphs()) {

                pdfDoc.add(new Paragraph(para.getText()));
            }

            System.out.println("Word document converted to PDF successfully!");
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
    private static File getQrCode() throws IOException, WriterException, InvalidFormatException {
        // 生成二维码
        String qrCodeData = "https://example.com/order/20250710001";
        int width = 90;
        int height = 90;

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(qrCodeData, BarcodeFormat.QR_CODE, width, height, hints);
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        // 保存为临时文件
        File tmpQrFile = File.createTempFile("qrcode-", ".png");
        ImageIO.write(bufferedImage, "PNG", tmpQrFile);

//        addImage(document, tmpQrFile);
        return tmpQrFile;
    }
    /**
     * 替换 Word 文件中的占位符
     *
     * @param document   Word 文档对象
     * @param placeholder 占位符，例如 {{name}}
     * @param replacement 替换后的值，例如 "张三"
     */
    private static void replacePlaceholder(XWPFDocument document, String placeholder, String replacement) {
        // 遍历文档中的段落
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            String paragraphText = paragraph.getText();
            // 遍历段落中的文本部分
            for (XWPFRun run : paragraph.getRuns()) {
                String text = run.getText(0);
                if (text != null && text.contains(placeholder)) {
                    // 替换占位符
                    text = text.replace(placeholder, replacement);
                    run.setText(text, 0);  // 重新设置文本内容
                }
            }
        }
    }
    public static void wordToPdf(String docFile,String pdfFile) throws Exception {

        WordprocessingMLPackage pkg = Docx4J.load(new File(docFile));
        Mapper fontMapper = new IdentityPlusMapper();
        fontMapper.put("隶书", PhysicalFonts.get("LiSu"));
        fontMapper.put("宋体", PhysicalFonts.get("SimSun"));
        fontMapper.put("微软雅黑", PhysicalFonts.get("Microsoft Yahei"));
        fontMapper.put("黑体", PhysicalFonts.get("SimHei"));
        fontMapper.put("楷体", PhysicalFonts.get("KaiTi"));
        fontMapper.put("新宋体", PhysicalFonts.get("NSimSun"));
        fontMapper.put("华文行楷", PhysicalFonts.get("STXingkai"));
        fontMapper.put("华文仿宋", PhysicalFonts.get("STFangsong"));
        fontMapper.put("仿宋", PhysicalFonts.get("FangSong"));
        fontMapper.put("幼圆", PhysicalFonts.get("YouYuan"));
        fontMapper.put("华文宋体", PhysicalFonts.get("STSong"));
        fontMapper.put("华文中宋", PhysicalFonts.get("STZhongsong"));
        fontMapper.put("等线", PhysicalFonts.get("SimSun"));
        fontMapper.put("等线 Light", PhysicalFonts.get("SimSun"));
        fontMapper.put("华文琥珀", PhysicalFonts.get("STHupo"));
        fontMapper.put("华文隶书", PhysicalFonts.get("STLiti"));
        fontMapper.put("华文新魏", PhysicalFonts.get("STXinwei"));
        fontMapper.put("华文彩云", PhysicalFonts.get("STCaiyun"));
        fontMapper.put("方正姚体", PhysicalFonts.get("FZYaoti"));
        fontMapper.put("方正舒体", PhysicalFonts.get("FZShuTi"));
        fontMapper.put("华文细黑", PhysicalFonts.get("STXihei"));
        fontMapper.put("宋体扩展", PhysicalFonts.get("simsun-extB"));
        fontMapper.put("仿宋_GB2312", PhysicalFonts.get("FangSong_GB2312"));
        pkg.setFontMapper(fontMapper);


        FileOutputStream fileOutputStream = new FileOutputStream(pdfFile);
        Docx4J.toPDF(pkg, fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
    }
    private static void addImage(XWPFDocument document, File tmpQrFile) throws IOException, InvalidFormatException {
        // 2. 读取新图片数据
        byte[] newImageData = Files.readAllBytes(tmpQrFile.toPath());
        String newImageName = tmpQrFile.getName();
// 1. 获取文档中所有图片
        List<XWPFPictureData> allPictures = document.getAllPictures();
// 3. 替换第一张图片的数据
        XWPFPictureData oldPicture = allPictures.get(0);
        // 更新所有引用此图片的运行(run)
        replacePictureDataDirectly(allPictures.get(0), newImageData);
    }
    private static void replacePictureDataDirectly(XWPFPictureData picture, byte[] newData) {
        try {
            // 使用 POI 提供的方法替换图片数据
            Method setDataMethod = XWPFPictureData.class.getDeclaredMethod("setData", byte[].class);
            setDataMethod.setAccessible(true);
            setDataMethod.invoke(picture, newData);
        } catch (Exception e) {
            throw new RuntimeException("无法替换图片数据", e);
        }
    }
}