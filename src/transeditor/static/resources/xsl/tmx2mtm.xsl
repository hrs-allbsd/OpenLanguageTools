<?xml version="1.0" encoding="UTF-8" ?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
    <xsl:output method="xml" indent="yes" encoding="utf-8"/>

    <xsl:param name="srcLang"/>
    <xsl:param name="tgtLang"/>
    <xsl:param name="srcLangShort"/>
    <xsl:param name="tgtLangShort"/>
    <xsl:param name="userId"/>

    <xsl:variable name="srcLangRegex" select="concat('(^',$srcLang,'$)|','(^',$srcLangShort,'$)') " />
    <xsl:variable name="tgtLangRegex" select="concat('(^',$tgtLang,'$)|','(^',$tgtLangShort,'$)') " />
	
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
                <matches><xsl:value-of select="matches('UsA','USA','i')"/></matches>
                <src><xsl:value-of select="$srcLang"/></src>
                <tgt><xsl:value-of select="$tgtLang"/></tgt>
                <srcshort><xsl:value-of select="$srcLangShort"/></srcshort>
                <tgtshort><xsl:value-of select="$tgtLangShort"/></tgtshort>
                <srcreg><xsl:value-of select="$srcLangRegex"/></srcreg>
                <tgtreg><xsl:value-of select="$tgtLangRegex"/></tgtreg>
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
        <DEBUG>
            <x><xsl:value-of select="count(tuv[matches(@xml:lang,$srcLangRegex,'i')]/seg)"/></x>
            <x><xsl:value-of select="count(tuv[matches(@xml:lang,$tgtLangRegex,'i')]/seg)"/></x>
        </DEBUG>
<!-- For debugging purposes >
        <DEBUG>
            <x><xsl:value-of select="count(tuv[@lang=$srcLang]/seg)"/></x>
            <x><xsl:value-of select="count(tuv[@lang=$tgtLang]/seg)"/></x>
        </DEBUG>
< -->

        <xsl:choose>
            <xsl:when test="count(tuv[matches(@xml:lang,$srcLangRegex,'i')]/seg)>0 and count(tuv[matches(@xml:lang,$tgtLangRegex,'i')]/seg) > 0">
                <entry>
                    <source><xsl:value-of  select="tuv[matches(@xml:lang,$srcLangRegex,'i')]/seg"/></source>
                    <translation><xsl:value-of select="tuv[matches(@xml:lang,$tgtLangRegex,'i')]/seg"/></translation>
                    <translatorId><xsl:value-of select="$userId"/></translatorId>
                </entry>
            </xsl:when>
            <xsl:when test="count(tuv[matches(@lang,$srcLangRegex,'i')]/seg)>0 and count(tuv[matches(@lang,$tgtLangRegex,'i')]/seg) > 0">
                <entry>
                    <source><xsl:value-of  select="tuv[matches(@lang,$srcLangRegex,'i')]/seg"/></source>
                    <translation><xsl:value-of select="tuv[matches(@lang,$tgtLangRegex,'i')]/seg"/></translation>
                    <translatorId><xsl:value-of select="$userId"/></translatorId>
                </entry>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
</xsl:stylesheet>
