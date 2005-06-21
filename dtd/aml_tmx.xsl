<?xml version='1.0' ?>

<!-- SunTrans 2 AML To TMX XSLT

     Author: Charles Liu
     Date: 2002/12/06

     Revision History:
     First Draft: 2002/12/05 Charles Liu
-->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output doctype-system="tmx13.dtd" method="xml" indent="no"/>

<xsl:param name="GlobalSrc">
  <xsl:value-of select="/aml/header/@srclang"/>
</xsl:param>
<xsl:param name="GlobalTrg">
  <xsl:value-of select="/aml/header/@trglang"/>
</xsl:param>


<xsl:template match="aml">
  <tmx version="1.3">
    <xsl:apply-templates/>
  </tmx>
</xsl:template>

<xsl:template match="header">
  <header
     datatype="{@datatype}"
     srclang="{@srclang}"
     adminlang="{@adminlang}"
     o-tmf="aml"
     segtype="{@segtype}"
     creationtoolversion="Version 1.0 test"
     creationtool="AmlToTmx Converter"
     >
    <xsl:apply-templates/>
  </header>
</xsl:template>

<xsl:template match="prop">
  <xsl:choose>
    <xsl:when test="starts-with(@type,'SunTrans')">
      <xsl:copy-of select="."/>
    </xsl:when>
  </xsl:choose>
</xsl:template>

<xsl:template match="body">
  <body>
    <xsl:apply-templates/>
  </body>
</xsl:template>

<xsl:template match="body/aligned">
  <tu tuid="{@id}">
    <xsl:apply-templates/>
  </tu>
</xsl:template>

<xsl:template match="body/aligned/src">
  <xsl:choose>
    <xsl:when test="number(@total) > number(1)">
      <tuv xml:lang="{$GlobalSrc}">
        <prop type="SunTrans::MultipleSource"><xsl:value-of select="@index"/></prop>
        <xsl:apply-templates/>
      </tuv>
    </xsl:when>
    <xsl:otherwise>
      <tuv xml:lang="{$GlobalSrc}">
        <xsl:apply-templates/>
      </tuv>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="body/aligned/trg">
  <xsl:choose>
    <xsl:when test="number(@total) > number(1)">
      <tuv xml:lang="{$GlobalTrg}">
        <prop type="SunTrans::MultipleTarget"><xsl:value-of select="@index"/></prop>
        <xsl:apply-templates/>
      </tuv>
    </xsl:when>
    <xsl:otherwise>
      <tuv xml:lang="{$GlobalTrg}">
        <xsl:apply-templates/>
      </tuv>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="body/aligned/src/seg">
  <xsl:copy-of select="."/>
</xsl:template>

<xsl:template match="body/aligned/trg/seg">
  <xsl:copy-of select="."/>
</xsl:template>

</xsl:stylesheet>
