<?xml version="1.0" encoding="UTF-8" ?>
<!--
    Document   : fix-formatting.xsl
    Created on : July 13, 2004, 3:16 PM
    Author     : jc73554
    Description:
        This stylesheet adds the type attribute to formatting elements that only 
        have a single whitespace in them.
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" 
        doctype-public="-//SUN-TRANSTECH//DTD XLIFF SKELETON//EN" 
        doctype-system="http://www.ireland/~timf/tt-xliff-skl.dtd" />
    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates select="attribute::*"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="attribute::*">
        <xsl:copy/>
    </xsl:template>
    <xsl:template match="formatting[text()=' '][not(@type)]">
        <xsl:copy>
            <xsl:attribute name="type">whitespace</xsl:attribute>
            <xsl:apply-templates select="attribute::*"/>
            <xsl:apply-templates/>
        </xsl:copy>    
    </xsl:template>    
</xsl:stylesheet> 
