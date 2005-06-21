<?xml version="1.0" encoding="UTF-8" ?>
<!--
    Document   : add_target.xsl
    Created on : July 13, 2004, 1:24 PM
    Author     : jc73554
    Description:
        This stylesheet adds a target-language attribute to the file element
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" doctype-public="-//XLIFF//DTD XLIFF//EN" doctype-system="http://www.oasis-open.org/committees/xliff/documents/xliff.dtd" />
    <xsl:param name="language">ja-JP</xsl:param>
    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates select="attribute::*"/>
            <xsl:apply-templates>
                <xsl:with-param name="language"><xsl:value-of select="$language"/></xsl:with-param>
            </xsl:apply-templates>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="attribute::*">
        <xsl:copy/>
    </xsl:template>
    <xsl:template match="file[not(@target-language)]">
        <xsl:copy>
            <xsl:apply-templates select="attribute::*"/>
            <xsl:attribute name="target-language"><xsl:value-of select="$language"/></xsl:attribute>
            <xsl:apply-templates />
        </xsl:copy>
    </xsl:template> 
</xsl:stylesheet> 
