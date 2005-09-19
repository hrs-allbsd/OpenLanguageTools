<?xml version="1.0" encoding="UTF-8" ?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="xml" indent="yes"/>

    <xsl:param name="srcLang"/>
    <xsl:param name="tgtLang"/>
    <xsl:param name="srcLangShort"/>
    <xsl:param name="tgtLangShort"/>
    <xsl:param name="userId"/>    
<!-- For debugging as standalone sheet 
    <xsl:param name="srcLang" select="'en-US'"/>
    <xsl:param name="tgtLang" select="'zh-CN'"/>
    <xsl:param name="srcLangShort" select="'US'"/>
    <xsl:param name="tgtLangShort" select="'ZH'"/>
    <xsl:param name="userId" select="'abc'"/>    
-->    
    <xsl:template match="/">
        <xsl:element name="minitm">
            <xsl:attribute name="name">temp</xsl:attribute>
            <xsl:attribute name="srclang">
                <xsl:value-of select="$srcLangShort"/>
            </xsl:attribute>
            <xsl:attribute name="tgtlang">
                <xsl:value-of select="$tgtLangShort"/>
            </xsl:attribute>
    <!--
            <DEBUG>
                <src><xsl:value-of select="$srcLang"/></src>
                <tgt><xsl:value-of select="$tgtLang"/></tgt>
                <srcshort><xsl:value-of select="$srcLangShort"/></srcshort>
                <tgtshort><xsl:value-of select="$tgtLangShort"/></tgtshort>
                <CONTENTS>
                    <xsl:copy-of select="."/>
                </CONTENTS>
            </DEBUG>
        -->
        
            <xsl:for-each select="/tmx/body/tu">
                <xsl:apply-templates select="."/>
            </xsl:for-each>
        </xsl:element>
    </xsl:template>

    <xsl:template match="tu">
<!-- For debugging purposes
        <DEBUG>
            <x><xsl:value-of select="count(tuv[@xml:lang=$srcLang]/seg)"/></x>
            <x><xsl:value-of select="count(tuv[@xml:lang=$tgtLang]/seg)"/></x>
        </DEBUG>
-->					
        <xsl:if test="count(tuv[@xml:lang=$srcLang]/seg)>0 and count(tuv[@xml:lang=$tgtLang]/seg) > 0">
            <entry>
                <source>
                    <xsl:copy-of select="normalize-space(tuv[@xml:lang=$srcLang]/seg/node())">
                        <xsl:value-of select="normalize-space(.)"/>
                    </xsl:copy-of>
                </source>
                <!--xsl:copy-of select="tuv[@xml:lang=$srcLang]/seg"/-->
                <translation><xsl:value-of select="normalize-space(tuv[@xml:lang=$tgtLang]/seg/node())"/></translation>
                <!-- xsl:copy-of select="tuv[@xml:lang=$tgtLang]/seg/node()"/-->
                <translatorId><xsl:value-of select="$userId"/></translatorId>
            </entry>
        </xsl:if>
    </xsl:template>
</xsl:stylesheet>
