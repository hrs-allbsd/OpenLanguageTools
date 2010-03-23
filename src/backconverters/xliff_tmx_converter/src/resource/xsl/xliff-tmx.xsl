<?xml version='1.0' ?>

<!-- SunTrans 2 XLIFF To TMX XSLT

     Author: Brian Kidney
     Date: 2002/08/22
     
     Revision History: 
     First Draft: 2002/08/22 Brian Kidney 
-->

<xsl:stylesheet version="1.0" 
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output doctype-system="tmx13.dtd" method="xml" encoding="utf-8" 
indent="no"/>

<xsl:param name="GlobalSrc">
    <xsl:value-of select="/xliff/file[position()=1]/@source-language"/>
</xsl:param>
<xsl:param name="GlobalTrg">
    <xsl:value-of select="/xliff/file[position()=1]/@target-language"/>
</xsl:param>
 
<xsl:template match="text()"/>

<xsl:template match="xliff">
    <tmx version="1.3"> 
      <xsl:apply-templates/>
<xsl:text>&#10;</xsl:text>

    </tmx>
</xsl:template>

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

<xsl:template match="file[position()=1]/body">
<xsl:text>&#10;</xsl:text>

    <body>
        <xsl:apply-templates/>
<xsl:text>&#10;</xsl:text>

    </body>
</xsl:template>

<xsl:template match="file[position()=1]/body/trans-unit">
<xsl:text>&#10;</xsl:text>

    <tu tuid="{@id}">
        <xsl:apply-templates/>
<xsl:text>&#10;</xsl:text>

    </tu>
</xsl:template>
  
<xsl:template match="file[position()=1]/body/trans-unit/source">
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

<xsl:template match="file[position()=1]/body/trans-unit/target">
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

<xsl:template match="file[position()=1]/body/trans-unit/source/text() |
file[position()=1]/body/trans-unit/source/mrk[@mtype=&quot;phrase&quot;]/text()">
    <xsl:value-of select="."/>
</xsl:template>

<xsl:template match="file[position()=1]/body/trans-unit/target/text() |
file[position()=1]/body/trans-unit/target/mrk[@mtype=&quot;phrase&quot;]/text()">
    <xsl:value-of select="."/>
</xsl:template>

<xsl:template match="file[position()=1]/body/trans-unit/source/bpt |
file[position()=1]/body/trans-unit/source/mrk[@mtype=&quot;phrase&quot;]/bpt |
file[position()=1]/body/trans-unit/target/mrk[@mtype=&quot;phrase&quot;]/bpt |
file[position()=1]/body/trans-unit/target/bpt">
    <bpt i="{@id}">
        <xsl:value-of select="."/>
    </bpt>
</xsl:template>

<xsl:template match="mrk[@mtype=&quot;phrase&quot;]">
  <xsl:apply-templates/>
</xsl:template>


<xsl:template match="file[position()=1]/body/trans-unit/source/ept |
file[position()=1]/body/trans-unit/source/mrk[@mtype=&quot;phrase&quot;]/ept |
file[position()=1]/body/trans-unit/target/mrk[@mtype=&quot;phrase&quot;]/ept |
file[position()=1]/body/trans-unit/target/ept">
    <ept i="{@id}">
        <xsl:value-of select="."/>
    </ept>
</xsl:template>

<xsl:template match="file[position()=1]/body/trans-unit/source/it |
file[position()=1]/body/trans-unit/source/mrk[@mtype=&quot;phrase&quot;]/it |
file[position()=1]/body/trans-unit/target/mrk[@mtype=&quot;phrase&quot;]/it |
file[position()=1]/body/trans-unit/target/it">
    <it pos="end" x="{@id}">
        <xsl:value-of select="."/>
    </it>
</xsl:template>

</xsl:stylesheet>
