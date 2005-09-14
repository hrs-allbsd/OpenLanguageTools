<?xml version="1.0" encoding="UTF-8" ?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="xml" indent="yes"/>

    <xsl:param name="srcLang"/>
    <xsl:param name="tgtLang"/>
    <xsl:param name="srcLangShort"/>
    <xsl:param name="tgtLangShort"/>
    <xsl:param name="userId"/>    

 	<xsl:template match="/">
		<xsl:element name="minitm">
			<xsl:attribute name="name">temp</xsl:attribute>
			<xsl:attribute name="srclang">
				<xsl:value-of select="$srcLangShort"/>
			</xsl:attribute>
			<xsl:attribute name="tgtlang">
				<xsl:value-of select="$tgtLangShort"/>
			</xsl:attribute>

			<x>
				<src><xsl:value-of select="$srcLang"/></src>
				<tgt><xsl:value-of select="$tgtLang"/></tgt>
			</x>

            <xsl:for-each select="/tmx/body/tu">          
                <x>
					<x><xsl:value-of select="count(tuv[@xml:lang=$srcLang]/seg/node())"/></x>
					<x><xsl:value-of select="count(tuv[@xml:lang=$tgtLang]/seg/node())"/></x>
				</x>
                <xsl:if test="count(tuv[@xml:lang=$srcLang]/seg/node())>0 and count(tuv[@xml:lang=$tgtLang]/seg/node()) > 0">
                    <entry>
                        <source><xsl:copy-of select="tuv[@xml:lang=$srcLang]/seg/node()"/></source>
                        <translation><xsl:copy-of select="tuv[@xml:lang=$tgtLang]/seg/node()"/></translation>
                        <translatorId><xsl:value-of select="$userId"/></translatorId>
                    </entry>
                </xsl:if>
			</xsl:for-each>
		</xsl:element>
	</xsl:template>
	
</xsl:stylesheet>
