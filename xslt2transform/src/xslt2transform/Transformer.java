package xslt2transform;

import net.sf.saxon.s9api.*;

import javax.xml.transform.stream.StreamSource;
import java.io.*;


interface ITransformer{
	public void compileStylesheet(StreamSource source) throws SaxonApiException;
	public void transformXml(StreamSource xml, OutputStream out, Boolean indent) throws SaxonApiException;
}

public class Transformer implements ITransformer {
	
	public static void main(String[] args) throws SaxonApiException, FileNotFoundException {
		// TODO: accept input
		ITransformer tr = new Transformer();
		tr.compileStylesheet(new StreamSource(new File("src\\xslt2transform\\books2genres.xslt")));
		tr.transformXml(new StreamSource(new File("src\\xslt2transform\\books.xml")), new FileOutputStream(new File("src\\xslt2transform\\genres.xml")), true );
		
		String xslt =
                "<xsl:transform version='2.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>" +
                        	"<xsl:template match='/'>" +
                        		"<genres>" +
                        			"<!-- ommited for brevity -->" +
                        		"</genres>" +
                        	"</xsl:template>" +
                        "</xsl:transform>";
		tr.transformXml(new StreamSource(new StringReader(xslt)), new FileOutputStream(new File("src\\xslt2transform\\genres2.xml")), false);
	}

	private Processor proc = new Processor(false);
    private XsltCompiler comp = proc.newXsltCompiler();
    private XsltExecutable exp = null;
	
	@Override
	public void compileStylesheet(StreamSource source) throws SaxonApiException{
		exp = comp.compile(source);
	}
	
	@Override
	public void transformXml(StreamSource xmlStream, OutputStream outStream, Boolean indent) throws SaxonApiException {
		XdmNode source = proc.newDocumentBuilder().build(xmlStream);
		Serializer out = proc.newSerializer(outStream);
        out.setOutputProperty(Serializer.Property.METHOD, "xml");
        out.setOutputProperty(Serializer.Property.INDENT, indent?"yes":"no");
        
        XsltTransformer trans = exp.load();
        trans.setInitialContextNode(source);
        trans.setDestination(out);
        trans.transform();
    }
}
