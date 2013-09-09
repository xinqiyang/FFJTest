package FFJTest.utils;


import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlHelper {
    public static final String  XML_NAME_SPACE    = "http://www.proxure.asia/xml/2010-07-07";
    private static final String FORMAT_XML_INDENT = "4";
    private static final String STRIP_SPACE_XSL   = "conf/strip-space.xsl";

    // newDocument

    public static Document newDocument() {
        DocumentBuilder builder = newDocumentBuilder();
        return builder.newDocument();
    }

    public static Document newDocument(String xmlString) {
        DocumentBuilder builder = newDocumentBuilder();

        try {
            return builder.parse(new InputSource(new StringReader(xmlString)));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static DocumentBuilder newDocumentBuilder() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder        builder = null;

        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        return builder;
    }

    // documentAppendChild

    public static Element documentAppendChild(Document document, String childName) {
        return appendChild(document, null, childName, null);
    }

    public static Element documentAppendChild(Document document, String childName, String childTextString) {
        return appendChild(document, null, childName, childTextString);
    }

    // documentAppendChildWithAttribute

    public static Element documentAppendChildWithDefaultXmlNsAttribute(Document document, String childName) {
        return documentAppendChildWithAttribute(document, childName, null, "xmlns", XML_NAME_SPACE);
    }

    public static Element documentAppendChildWithDefaultXmlNsAttribute(Document document, String childName, String childTextString) {
        return documentAppendChildWithAttribute(document, childName, childTextString, "xmlns", XML_NAME_SPACE);
    }

    public static Element documentAppendChildWithAttribute(Document document, String childName, String childTextString, String attributeName, String attributeValue) {
        Element child = appendChild(document, null, childName, childTextString);

        child.setAttribute(attributeName, attributeValue);

        return child;
    }

    // elementAppendChild

    public static Element elementAppendChild(Document document, Element element, String childName) {
        return elementAppendChild(document, element, childName, null);
    }

    public static Element elementAppendChild(Document document, Element element, String childName, String childTextString) {
        if (element == null) {
            return null;
        }

        return appendChild(document, element, childName, childTextString);
    }

    private static Element appendChild(Document document, Element element, String childName, String childTextString) {
        Element child = document.createElement(childName);

        if (childTextString != null) {
            // append text node to child
            Text text = document.createTextNode(childTextString);
            child.appendChild(text);
        }

        if (element != null) {
            element.appendChild(child);             // append child to element
        } else {
            document.appendChild(child);            // append child to document
        }

        return child;
    }

    /*
     * XMLを文字列に変換
     */
    public static String xmlToString(Node node) {
        Source             source       = new DOMSource(node);
        StringWriter       stringWriter = new StringWriter();
        Result             result       = new StreamResult(stringWriter);
        TransformerFactory factory      = TransformerFactory.newInstance();

        try {
            Transformer transformer = factory.newTransformer();
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return stringWriter.toString();
    }

    public static String formatXmlString(String xmlString) {
        Source xmlInput = new StreamSource(new StringReader(xmlString));

        try {
            return formatXmlSource(xmlInput, true);
        } catch (TransformerException e) {
            UtilTools.getLog().warn(e);
            return xmlString;
        }
    }

    public static String formatXmlDocument(Document document) {
        Source xmlInput = new DOMSource(document);

        try {
            return formatXmlSource(xmlInput, false);
        } catch (TransformerException e) {
        	UtilTools.getLog().warn(e);
            return xmlToString(document);
        }
    }

    public static String formatXmlNode(Node node) {
        Source xmlInput = new DOMSource(node);

        try {
            return formatXmlSource(xmlInput, true);
        } catch (TransformerException e) {
        	UtilTools.getLog().warn(e);
            return xmlToString(node);
        }
    }

    private static String formatXmlSource(Source xmlInput, boolean omitXmlDeclaration)
    throws TransformerFactoryConfigurationError, TransformerException {
        StreamResult xmlOutput = new StreamResult(new StringWriter());

        Transformer transformer = TransformerFactory.newInstance().newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", FORMAT_XML_INDENT);
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, omitXmlDeclaration ? "yes" : "no");

        transformer.transform(xmlInput, xmlOutput);
        return xmlOutput.getWriter().toString();
    }

    public static String unformatXmlString(String xmlString)
    throws TransformerException {
        Source       xmlInput  = new StreamSource(new StringReader(xmlString));
        StreamResult xmlOutput = new StreamResult(new StringWriter());

        Transformer transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(STRIP_SPACE_XSL));

        transformer.transform(xmlInput, xmlOutput);
        return xmlOutput.getWriter().toString();
    }

    // getText

    public static String getText(Node node, String xPathString) {
        return getText(node, xPathString, null);
    }

    public static String getText(Node node, String xPathString, String defaultText) {
        XPath  xPath = XPathFactory.newInstance().newXPath();
        String text  = defaultText;

        try {
            // when childNode not exist, xPath.evaluate(xPathString, node) will return "",
            // so check exist first to differentiate non-exist case with true "" case
            Node childNode = (Node)xPath.evaluate(xPathString, node, XPathConstants.NODE);

            if (childNode != null) {
                text = xPath.evaluate(xPathString, node);
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return text;
    }

    public static NodeList getNodeList(Node node, String xPathString) {
        XPath    xPath    = XPathFactory.newInstance().newXPath();
        NodeList nodeList = null;

        try {
            nodeList = (NodeList)xPath.evaluate(xPathString, node, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return nodeList;
    }

    public static Node getSingleChildNode(Node node, String childTag)
    throws Exception {
        NodeList nodeList = getNodeList(node, childTag);

        if (nodeList.getLength() != 1) {
            throw new Exception("nodeList count error");
        }

        return nodeList.item(0);
    }
}
