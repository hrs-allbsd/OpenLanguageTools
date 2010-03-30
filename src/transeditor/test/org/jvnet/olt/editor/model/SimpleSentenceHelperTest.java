/*
 * SimpleSentenceTest.java
 *
 * Created on October 31, 2005, 6:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package org.jvnet.olt.editor.model;

import java.util.List;
import junit.framework.TestCase;
import org.jvnet.olt.editor.format.FormatElementExtractor;
import org.jvnet.olt.editor.format.FormatElementExtractorFactory;
import org.jvnet.olt.editor.format.VariableManagerFactory;
import org.jvnet.olt.editor.util.BaseElements;
import org.jvnet.olt.format.GlobalVariableManager;

/**
 *
 * @author boris
 */
public class SimpleSentenceHelperTest extends TestCase{

    /** Creates a new instance of SimpleSentenceTest */
    public SimpleSentenceHelperTest() {
    }

    @Override
    protected void setUp() throws Exception {
	VariableManagerFactory varManagerFactory = new VariableManagerFactory();
	GlobalVariableManager gvm = varManagerFactory.createVariableManager("SGML");

	FormatElementExtractorFactory factory = new FormatElementExtractorFactory();
	FormatElementExtractor extractor = factory.createFormatExtractor("SGML", gvm);
	BaseElements.setFormatExtractor(extractor);
    }

    public void testBuildTagHolders() throws Exception {
	String s = "aaa<b>cc<i>dd</i>ee</b>ff";

	PivotText pt = new PivotText(s,true);

	List l = SimpleSentenceHelper.buildTagHolders(pt);
	assertNotNull(l);
	assertEquals(4,l.size());

        assertEquals("b",((SimpleSentenceHelper.TagHolder)l.get(0)).element.getTagName());
        assertEquals("i",((SimpleSentenceHelper.TagHolder)l.get(1)).element.getTagName());
        assertEquals("i",((SimpleSentenceHelper.TagHolder)l.get(2)).element.getTagName());

    }

    public void testTagHoldersDepths() throws Exception {
	String s = "aaa<b>cc<i>dd</i>ee</b>ff";

        PivotText pt = new PivotText(s,true);

	List l = SimpleSentenceHelper.buildTagHolders(pt);

        assertEquals(1,((SimpleSentenceHelper.TagHolder)l.get(0)).getDepth() );
        assertEquals(2,((SimpleSentenceHelper.TagHolder)l.get(1)).getDepth() );
        assertEquals(2,((SimpleSentenceHelper.TagHolder)l.get(2)).getDepth() );
        assertEquals(1,((SimpleSentenceHelper.TagHolder)l.get(3)).getDepth() );

    }

    /*
    public void testWithFormatting2() throws Exception{
	String s =        "On the login screen, click on the <guilabel>Language</guilabel>icon.";
	s =        "On the login screen, <a href='xyz.html'>click</a> on the <guilabel>Language</guilabel>icon.";
	String matchSrc = "On the login screen, click on the <guilabel>Session</guilabel> icon.";
	String matchT =   "Pulse en el icono <guilabel>Sesion</guilabel> en la pantalla de acceso.";

	String ss = SimpleSentenceHelper.formatTranslation2(s,matchSrc,matchT,true);

	System.out.println("ss:"+ss);
	assertNotNull(ss);


    }
    */

    public void testWithFormatting() throws Exception{
	String s =        "On the login screen, click on the <guilabel>Language</guilabel>icon.";
	String matchSrc = "On the login screen, click on the <guilabel>Session</guilabel> icon.";
	String matchT =   "Pulse en el icono <guilabel>Sesion</guilabel> en la pantalla de acceso.";

	String ss = SimpleSentenceHelper.formatTranslation(s,matchSrc,matchT,true);

	System.out.println("ss:"+ss);
	assertNotNull(ss);


    }

    public void testSample1() throws Exception {
	String rv = SimpleSentenceHelper.formatTranslation("The sky is <b>blue</b>","The sky is blue","Der Himmel ist blau.");
	assertEquals(rv,"Der Himmel ist blau.<b></b>");
    }
    public void testSample2() throws Exception {
	String rv = SimpleSentenceHelper.formatTranslation("The sky is blue","The sky is <b>blue</b>","Der Himmel ist <b>blau</b>.");
	System.out.println("testSample2:"+rv);
	assertEquals(rv,"Der Himmel ist blau.");
    }
    public void testSample3() throws Exception {

	String rv = SimpleSentenceHelper.formatTranslation("The sky is <b>blue</b>","The <i>sky</i> is <b>blue</b>","Der Himmel ist <b>blau</b>.");
	System.out.println("rv:"+rv);
	assertEquals(rv,"Der Himmel ist <b>blau</b>.");
    }
    public void testSample4() throws Exception {

	String rv = SimpleSentenceHelper.formatTranslation("The <i>sky</i> is <b>blue</b>","The sky is <b>blue</b>","Der Himmel ist <b>blau</b>.");
	System.out.println("rv:"+rv);
	assertEquals(rv,"Der Himmel ist <b>blau</b>.<i></i>");
    }
    public void testSample5() throws Exception {

	String rv = SimpleSentenceHelper.formatTranslation("The sky is <b>blue</b>","The <u>sky</u> is <i>blue</i>","Der <i>Himmel</i> ist <b>blau</b>.");
	System.out.println("rv:"+rv);
	assertEquals(rv,"Der Himmel ist <b>blau</b>.");
    }
    public void testSample6() throws Exception {

	String rv = SimpleSentenceHelper.formatTranslation("The sky is <b>blue</b>","The <u>sky</u> is <i>blue</i>","Der Himmel ist blau.");
	System.out.println("rv:"+rv);
	assertEquals(rv,"Der Himmel ist blau.<b></b>");
    }
    public void testSample7() throws Exception {

	String rv = SimpleSentenceHelper.formatTranslation("The sky is <b>blue</b>","The <u>sky</u> is <i>blue</i>","Der Himmel ist <b>blau</b>.");
	System.out.println("rv:"+rv);
	assertEquals(rv,"Der Himmel ist <b>blau</b>.");

    }

    public void testNoTags() throws Exception {
	String rv = SimpleSentenceHelper.formatTranslation("<?Pub UDT ?>","The <u>sky</u> is <i>blue</i>","Der Himmel ist <b>blau</b>.");

        System.out.println("rvx:"+rv);

        assertEquals("Der Himmel ist blau.",rv);
    }

    public void testError1() throws Exception{
        /*String s1 = "When a user logs in and then invokes <olink targetdocent=\"REFMAN1M\" localinfo=\"su-1m\"><citerefentry><refentrytitle>su</refentrytitle>"+
                "<manvolnum>1M</manvolnum></citerefentry></olink> to become"+
                " super-user or another user, <command>passwd</command> changes the original user's password, not the password of the super-user or the new user.";
        String s2 = "???????????<olink targetdocent=\"REFMAN1M-JA\" localinfo=\"su-1m\"><citerefentry><refentrytitle>" +
                    "su</refentrytitle><manvolnum>1M</manvolnum></citerefentry></olink> ?????????????????????????????"+
                "<command>passwd</command> ???????????????????????????????????????????????";
	*/
        String s1 = "<a x=\"1\">aa<b>cc</b></a>dddd";
        String s2 = "<a x=\"2\">aa<b>cc</b></a>dddd";

        String rv = SimpleSentenceHelper.formatTranslation(s1,s1,s2);
        System.out.println("s1:"+s1);
        System.out.println("s2:"+s2);

        System.out.println("rv:"+rv);

        assertEquals("aa<b>cc</b>dddd<a x=\"1\"></a>",rv);
    }
}
