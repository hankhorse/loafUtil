package org.net.Util;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class XmlUtils {

    /**
     * 将Map对象转换为XML字符串
     * @param map 要转换的Map对象
     * @return XML格式的字符串
     * @throws TransformerException 当转换过程出错时抛出异常
     */
    public static String mapToXml(Map<String, String> map) throws TransformerException, ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document doc = documentBuilder.newDocument();
        Element root = doc.createElement("xml");
        doc.appendChild(root);

        for (Map.Entry<String, String> entry : map.entrySet()) {
            Element node = doc.createElement(entry.getKey());
            node.appendChild(doc.createTextNode(entry.getValue()));
            root.appendChild(node);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));

        return writer.getBuffer().toString().replaceAll("\n|\r", "");
    }

    /**
     * 将XML字符串解析为Map对象
     * @param xml XML格式的字符串
     * @return 包含XML元素键值对的Map对象
     * @throws ParserConfigurationException 当初始化XML解析器时配置错误时抛出异常
     * @throws SAXException 解析XML时遇到语法错误时抛出异常
     * @throws IOException IO操作期间发生错误时抛出异常
     */
    public static Map<String, String> xmlToMap(String xml) throws ParserConfigurationException, SAXException, IOException {
        Map<String, String> result = new HashMap<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xml)));

        NodeList nodeList = doc.getElementsByTagName("*");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                result.put(element.getTagName(), element.getTextContent());
            }
        }

        return result;
    }
}
