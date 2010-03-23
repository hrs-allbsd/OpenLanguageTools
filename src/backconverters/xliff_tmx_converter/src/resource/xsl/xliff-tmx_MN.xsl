<?xml version='1.0' ?>

<!-- SunTrans 2 XLIFF To TMX XSLT

     Author: Brian Kidney
     Date: 2002/08/22
     
     Revision History: 
     First Draft:  2002/08/22 Brian Kidney
     v1.3:         2004/08/02 John Corrigan 
     v1.4:         2004/08/09 John Corrigan
-->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output doctype-system="tmx13.dtd" method="xml" encoding="utf-8" 
indent="no"/>

<xsl:param name="GlobalSrc">
    <xsl:value-of select="/xliff/file[position()=1]/@source-language"/>
</xsl:param>
<xsl:param name="GlobalTrg">
    <xsl:value-of select="/xliff/file[position()=1]/@target-language"/>
</xsl:param>

<!-- explicitly don't match text --> 
<xsl:template match="text()"/>

<xsl:template match="xliff">
    <tmx version="1.3"> 
      <xsl:apply-templates/>
<xsl:text>&#10;</xsl:text>

    </tmx>
</xsl:template>

<!-- Do nothing if we are not in the first file. -->
<xsl:template match="file[position()!=1]"/>


<!-- The "[position()=1]" test ensures that only the first file element in
     a XLIFF file is transformed to the TMX file. There should only be one 
     file element in a XLIFF file, as the Java SAX application pre parses 
     the XLIFF files and breaks XLIFF files with multplie file elements into
     smaller seperate XLIFF files for each file element. However the 
     "[position()=1]" test is included here just in case. -->
<xsl:template match="file[position()=1]">
<xsl:text>&#10;</xsl:text>

    <header     
        creationtool="SunTrans 2 XLIFF To TMX Converter" 
        creationtoolversion="Pre FCS" 
        segtype="sentence"
        o-tmf="xliff" 
        srclang="{$GlobalSrc}"
        adminlang="{$GlobalSrc}">
        <!-- This test is included because the datatype value of 
             C/C++ style text is different in XLIFF (i.e. "cpp") to TMX 
             (i.e. "cstyle") and needs to be converted accordingly. -->
        <xsl:choose>
            <xsl:when test="@datatype='cpp'">
              <xsl:attribute name="datatype">cstyle</xsl:attribute>
            </xsl:when>
            <xsl:otherwise>
                <xsl:attribute name="datatype">
                  <xsl:value-of select="@datatype"/>
                </xsl:attribute>
            </xsl:otherwise>
        </xsl:choose>
        <!-- This will need to be modified for software files -->
<xsl:text>&#10;</xsl:text>
        <prop type="SunTrans::DocFile">
            <xsl:value-of select="@original"/>
        </prop>
<xsl:text>&#10;</xsl:text>

    </header>
    <xsl:apply-templates/>    
</xsl:template>

<xsl:template match="body">
<xsl:text>&#10;</xsl:text>

    <body>
        <xsl:apply-templates/>
<xsl:text>&#10;</xsl:text>

    </body>
</xsl:template>

<xsl:template match="body/trans-unit">
<xsl:text>&#10;</xsl:text>

    <tu tuid="{@id}">
        <!-- 
            The three apply-template tags below are included to ensure that
            the prop elements appear in the correct place. context-group
            elements appear at the end of XLIFF trans-unit elements, but the 
            information they contain has to appear at the start of the tu element.
         -->
        <xsl:apply-templates select="note"/>
        <xsl:apply-templates select="context-group"/>
        <xsl:apply-templates select="source"/>
        <xsl:apply-templates select="target"/>
<xsl:text>&#10;</xsl:text>

    </tu>
</xsl:template>
  
<xsl:template match="body/trans-unit/source">
    <!-- This test is to set the correct xml:lang of a source text.
         By default the xml:lang is set in the header of the XLIFF file and
         this value is used for each source segment in the XLIFF file.
         However if a source segment in an XLIFF file specifies its on 
         xml:lang, then the value specified in the source element overrides 
         the default set in the header. -->
    <xsl:choose>
        <xsl:when test="@xml:lang">
<xsl:text>&#10;</xsl:text>
 <tuv xml:lang="{@xml:lang}">
<xsl:text>&#10;</xsl:text>
     <seg>
                    <xsl:apply-templates/>
                </seg>
<xsl:text>&#10;</xsl:text>

            </tuv>
        </xsl:when>
        <xsl:otherwise>
<xsl:text>&#10;</xsl:text>
  <tuv xml:lang="{$GlobalSrc}">
<xsl:text>&#10;</xsl:text>
     <seg>
                    <xsl:apply-templates/>
                </seg>
<xsl:text>&#10;</xsl:text>

            </tuv> 
        </xsl:otherwise>
    </xsl:choose>
</xsl:template>  

<xsl:template match="body/trans-unit/target">
    <!-- This test is to set the correct xml:lang of a target text.
         By default the xml:lang is set in the header of the XLIFF file and
         this value is used for each target segment in the XLIFF file.
         However if a target segment in an XLIFF file specifies its on 
         xml:lang, then the value specified in the target element overrides 
         the default set in the header. -->
    <xsl:choose>
        <xsl:when test="@xml:lang">
<xsl:text>&#10;</xsl:text>
 <tuv xml:lang="{@xml:lang}">
<xsl:text>&#10;</xsl:text>
    <seg>
                    <xsl:apply-templates/>
                </seg>
<xsl:text>&#10;</xsl:text>

            </tuv>
        </xsl:when>
        <xsl:otherwise>
<xsl:text>&#10;</xsl:text>
 <tuv xml:lang="{$GlobalTrg}">
<xsl:text>&#10;</xsl:text>
      <seg>
                    <xsl:apply-templates/>
                </seg>
<xsl:text>&#10;</xsl:text>

            </tuv> 
        </xsl:otherwise>
    </xsl:choose>
</xsl:template> 

<xsl:template match="body/trans-unit/source/text()|
body/trans-unit/source/mrk[@mtype=&quot;phrase&quot;]/text()">
    <xsl:value-of select="."/>
</xsl:template>

<xsl:template match="body/trans-unit/target/text()|
body/trans-unit/target/mrk[@mtype=&quot;phrase&quot;]/text()">
    <xsl:value-of select="."/>
</xsl:template>

<xsl:template match="
body/trans-unit/source/mrk[@mtype=&quot;phrase&quot;]/bpt |
body/trans-unit/target/mrk[@mtype=&quot;phrase&quot;]/bpt |
body/trans-unit/source/bpt |
body/trans-unit/target/bpt">
    <bpt i="{@id}">
        <xsl:value-of select="."/>
    </bpt>
</xsl:template>

<xsl:template match="
body/trans-unit/source/mrk[@mtype=&quot;phrase&quot;]/ept |
body/trans-unit/target/mrk[@mtype=&quot;phrase&quot;]/ept |
body/trans-unit/source/ept |
body/trans-unit/target/ept">
    <ept i="{@id}">
        <xsl:value-of select="."/>
    </ept>
</xsl:template>

<xsl:template match="
body/trans-unit/source/mrk[@mtype=&quot;phrase&quot;]/it |
body/trans-unit/target/mrk[@mtype=&quot;phrase&quot;]/it |
body/trans-unit/source/it |
body/trans-unit/target/it">
    <it pos="end" x="{@id}">
        <xsl:value-of select="."/>
    </it>
</xsl:template>




<!-- start of supporting M:N  =============================================
  This stuff is essentially a diplicate of the above, except that we also
  allow for the <group> tag to enclose a section of <trans-unit> tags
  
  -->

<xsl:template match="body/group">
<xsl:text>&#10;</xsl:text>
    <tu>
        <!-- 
            The three apply-template tags below are included to ensure that
            the prop elements appear in the correct place. context-group
            elements appear at the end of XLIFF trans-unit elements, but the 
            information they contain has to appear at the start of the tu element.
         -->
        <xsl:apply-templates select="context-group"/>
        <xsl:apply-templates select="trans-unit/source"/>
        <xsl:apply-templates select="trans-unit/target"/>
<xsl:text>&#10;</xsl:text>
    </tu>
</xsl:template>
  
<xsl:template match="body/group/trans-unit/source">
    <!-- This test is to set the correct xml:lang of a source text.
         By default the xml:lang is set in the header of the XLIFF file and
         this value is used for each source segment in the XLIFF file.
         However if a source segment in an XLIFF file specifies its on 
         xml:lang, then the value specified in the source element overrides 
         the default set in the header. -->
    <xsl:choose>
        <xsl:when test="@xml:lang">
<xsl:text>&#10;</xsl:text>
 <tuv xml:lang="{@xml:lang}">
<xsl:text>&#10;</xsl:text>
     <seg>
                    <xsl:apply-templates/>
                </seg>
<xsl:text>&#10;</xsl:text>

            </tuv>
        </xsl:when>
        <xsl:otherwise>
<xsl:text>&#10;</xsl:text>
  <tuv xml:lang="{$GlobalSrc}">
<xsl:text>&#10;</xsl:text>
     <seg>
                    <xsl:apply-templates/>
                </seg>
<xsl:text>&#10;</xsl:text>

            </tuv> 
        </xsl:otherwise>
    </xsl:choose>
</xsl:template>  

<xsl:template match="body/group/trans-unit/target">
    <!-- This test is to set the correct xml:lang of a target text.
         By default the xml:lang is set in the header of the XLIFF file and
         this value is used for each target segment in the XLIFF file.
         However if a target segment in an XLIFF file specifies its on 
         xml:lang, then the value specified in the target element overrides 
         the default set in the header. -->
    <xsl:choose>
    	<xsl:when test=".=' '"></xsl:when>    	
    	<xsl:otherwise>
    		<xsl:choose>
    		<xsl:when test=".=''"></xsl:when>
    		<xsl:otherwise>
    <xsl:choose>
        <xsl:when test="@xml:lang">
<xsl:text>&#10;</xsl:text>
 <tuv xml:lang="{@xml:lang}">
<xsl:text>&#10;</xsl:text>
    <seg>
                    <xsl:apply-templates/>
                </seg>
<xsl:text>&#10;</xsl:text>

            </tuv>
        </xsl:when>
        <xsl:otherwise>
<xsl:text>&#10;</xsl:text>
 <tuv xml:lang="{$GlobalTrg}">
<xsl:text>&#10;</xsl:text>
      <seg>
                    <xsl:apply-templates/>
                </seg>
<xsl:text>&#10;</xsl:text>

            </tuv> 
            	</xsl:otherwise>
            	</xsl:choose>
        </xsl:otherwise>
    </xsl:choose>
    
    </xsl:otherwise>
    </xsl:choose>
</xsl:template> 

<xsl:template match="body/group/trans-unit/source/text()|
body/group/trans-unit/source/mrk[@mtype=&quot;phrase&quot;]/text()
">
    <xsl:value-of select="."/>
</xsl:template>

<xsl:template match="body/group/trans-unit/target/text()|
body/group/trans-unit/target/mrk[@mtype=&quot;phrase&quot;]/text()
">
    <xsl:choose>
    	<xsl:when test=".=''"></xsl:when>
    	<xsl:otherwise>
    		<xsl:value-of select="."/>
    	</xsl:otherwise>
    </xsl:choose>
</xsl:template>

<xsl:template match=" 
body/group/trans-unit/source/mrk[@mtype=&quot;phrase&quot;]/bpt |
body/group/trans-unit/target/mrk[@mtype=&quot;phrase&quot;]/bpt |
body/group/trans-unit/source/bpt |
body/group/trans-unit/target/bpt">
    <bpt i="{@id}">
        <xsl:value-of select="."/>
    </bpt>
</xsl:template>

<xsl:template match="mrk[@mtype=&quot;phrase&quot;]">
  <xsl:apply-templates/>
</xsl:template>



<xsl:template match="
body/group/trans-unit/source/mrk[@mtype=&quot;phrase&quot;]/ept |
body/group/trans-unit/target/mrk[@mtype=&quot;phrase&quot;]/ept |
body/group/trans-unit/source/ept |
body/group/trans-unit/target/ept">
    <ept i="{@id}">
        <xsl:value-of select="."/>
    </ept>
</xsl:template>

<xsl:template match="
body/group/trans-unit/source/mrk[@mtype=&quot;phrase&quot;]/it |
body/group/trans-unit/target/mrk[@mtype=&quot;phrase&quot;]/it |
body/group/trans-unit/source/it | 
body/group/trans-unit/target/it">
    <it pos="end" x="{@id}">
        <xsl:value-of select="."/>
    </it>
</xsl:template>

<!-- end of supportting M:N -->

<!-- Add support for software message leveraging attributes -->
<xsl:template match="context-group[@name='message id']/context[@context-type='record']">
    <prop type="SunTrans::MessageKey"><xsl:value-of select="text()"/></prop>
</xsl:template>

<!-- End of support for software message leveraging attributes -->

<!-- Add support for note handling -->
<!-- Currently this is a simple match, but it can be made more complex to cover
     eventualities -->
<xsl:template match="body/trans-unit/note">
    <note><xsl:value-of select="."/></note>
</xsl:template>

<!-- end note handling -->

</xsl:stylesheet>
