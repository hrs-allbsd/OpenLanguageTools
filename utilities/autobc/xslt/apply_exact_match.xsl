<?xml version="1.0" encoding="UTF-8" ?>

<!--
    Document   : apply_match.xsl
    Created on : August 5, 2004, 11:28 AM
    Author     : jc73554
    Description:
        This transform applies matches in alt-trans segments to the trans-unit.
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
    <xsl:template match="trans-unit[not(target)][alt-trans/@match-quality='100']">
        <xsl:copy>
            <xsl:apply-templates select="attribute::*"/>
            <xsl:text disable-output-escaping="yes">&#10;            </xsl:text>
            <xsl:apply-templates select="source"/>
            <xsl:text disable-output-escaping="yes">&#10;            </xsl:text>
            <xsl:copy-of select="alt-trans[1]/target"/>
            <xsl:text disable-output-escaping="yes">&#10;            </xsl:text>
            <xsl:apply-templates select="child::*[node() != source]"/>
        </xsl:copy>
    </xsl:template> 
</xsl:stylesheet> 
