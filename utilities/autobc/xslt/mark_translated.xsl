<?xml version="1.0" encoding="UTF-8" ?>

<!--
    Document   : mark_translated.xsl
    Created on : August 5, 2004, 10:40 AM
    Author     : jc73554
    Description:
        Mark all trans-units as translated in an XLIFF file and use the 
        translations provided, if possible.
-->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" 
        doctype-public="-//XLIFF//DTD XLIFF//EN" 
        doctype-system="http://www.oasis-open.org/committees/xliff/documents/xliff.dtd" />
    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates select="attribute::*"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="attribute::*">
        <xsl:copy/>
    </xsl:template>     
    <xsl:template match="trans-unit/target[not(@state)]">
        <xsl:copy>
            <xsl:attribute name="state">user:translated</xsl:attribute>
            <xsl:apply-templates select="attribute::*"/>
            <xsl:apply-templates/>
        </xsl:copy>    
    </xsl:template>    
</xsl:stylesheet> 
