<xsl:stylesheet version="1.0"
                 xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<!-- Targeting FOP, include the namespace declaration
      xmlns="http://www.w3.org/2000/svg" -->

<!-- Draws an SVG "tree structure" representing the XSLT/XPath
      infoset of an arbitrary input document.

      Distribute and alter freely.
      Please acknowledge your sources.
      (You could use this stylesheet by importing it into
       your own and overriding its variable settings.)

      Includes a named template from Jeni Tennison's and
      Mike Brown's "ASCII Art" Tree Viewing stylesheet.

      By Wendell Piez, Mulberry Technologies, February 2001

-->

<xsl:output method="xml" indent="yes" />

<!-- amend or comment this out if you want to see any whitespace-only text 
nodes -->
<xsl:strip-space elements="*"/>

<xsl:variable name="across-start" select="6"/>

<xsl:variable name="down-start" select="4"/>

<xsl:variable name="across-interval" select="14"/>

<xsl:variable name="text-allowance" select="720"/>
<!-- extra space to the right to allow content of text nodes some room -->

<xsl:variable name="down-interval" select="18"/>

<xsl:variable name="line-thickness" select="1"/>

<xsl:variable name="writing-bump-over" select="6"/>

<xsl:variable name="writing-bump-up" select="-3"/>

<xsl:variable name="background-color" select="'#FFDAB9'"/>

<xsl:variable name="tree-color" select="'#8B4513'"/>

<xsl:variable name="text-font-style" select="'font-size: 12; font-family: 
Courier'"/>

<xsl:variable name="text-color" select="'#8A2BE2'"/>

<xsl:variable name="element-font-style" select="'font-size: 14; 
font-family: serif'"/>

<xsl:variable name="element-color" select="'#006400'"/>

<xsl:variable name="attribute-font-style" select="'font-size: 9; 
font-family: sans-serif'"/>

<xsl:variable name="attribute-color" select="'#4682B4'"/>
<xsl:variable name="attribute-special-color" select="'#FF0000'"/>

<xsl:variable name="code-font-style" select="'font-size: 9; font-family: 
sans-serif'"/>

<xsl:variable name="PI-color" select="'#228B22'"/>

<xsl:variable name="comment-color" select="'#B22222'"/>

<xsl:variable name="element-dot-radius" select="2.5"/>

<xsl:variable name="text-box-width" select="3"/>

<xsl:variable name="text-box-height" select="5"/>

<xsl:variable name="deepest">
   <!-- returns the depth (in ancestors) of the deepest node(s) -->
   <xsl:for-each select="//node()[not(node())]">
     <xsl:sort select="count(ancestor-or-self::*)" order="descending" 
data-type="number"/>
     <xsl:if test="position()=1">
       <xsl:value-of select="count(ancestor-or-self::*)"/>
     </xsl:if>
   </xsl:for-each>
</xsl:variable>

<xsl:variable name="full-width" select="($across-interval * ($deepest +2)) 
+ (2 * $across-start) + $text-allowance"/>

<xsl:variable name="full-height" select="((count(//node()) + 2) * 
$down-interval) + (2 * $down-start)"/>

<xsl:variable name="apos">'</xsl:variable>

<!-- internal functions to calculate position for any node -->

<xsl:template name="get-x-coordinate">
   <xsl:param name="node" select="/*"/>
   <xsl:value-of select="(count($node/ancestor-or-self::node()) * 
$across-interval) + $across-start"/>
</xsl:template>

<xsl:template name="get-y-coordinate">
   <xsl:param name="node" select="/*"/>
   <xsl:value-of 
select="(count($node/preceding::node()|$node/ancestor-or-self::node()) * 
$down-interval) + $down-start"/>
</xsl:template>

<!-- -->

<!-- root template -->

<xsl:template match="/">
   <svg width="{$full-width}" height="{$full-height}" >
     <rect x="0" y="0" width="{$full-width}" height="{$full-height}" 
style="fill:{$background-color}"/>
     <g>
       <xsl:apply-templates select="." mode="label">
         <xsl:with-param name="self-x" select="$across-start + 
$across-interval"/>
         <xsl:with-param name="self-y" select="$down-start + $down-interval"/>
       </xsl:apply-templates>
       <xsl:apply-templates/>
     </g>
   </svg>
</xsl:template>

<!-- -->

<!-- template for any child node -->

<xsl:template match="node()">
   <xsl:variable name="parent-x">
     <xsl:call-template name="get-x-coordinate">
       <xsl:with-param name="node" select=".."/>
     </xsl:call-template>
   </xsl:variable>
   <xsl:variable name="parent-y">
     <xsl:call-template name="get-y-coordinate">
       <xsl:with-param name="node" select=".."/>
     </xsl:call-template>
   </xsl:variable>
   <xsl:variable name="self-x">
     <xsl:call-template name="get-x-coordinate">
       <xsl:with-param name="node" select="."/>
     </xsl:call-template>
   </xsl:variable>
   <xsl:variable name="self-y">
     <xsl:call-template name="get-y-coordinate">
       <xsl:with-param name="node" select="."/>
     </xsl:call-template>
   </xsl:variable>
   <xsl:call-template name="drawpath">
     <xsl:with-param name="fromX" select="$parent-x"/>
     <xsl:with-param name="fromY" select="$parent-y"/>
     <xsl:with-param name="toX" select="$self-x"/>
     <xsl:with-param name="toY" select="$self-y"/>
   </xsl:call-template>
   <!-- place a label on the node (each node type gets a different label) -->
   <xsl:apply-templates select="." mode="label">
     <xsl:with-param name="self-x" select="$self-x"/>
     <xsl:with-param name="self-y" select="$self-y"/>
   </xsl:apply-templates>
   <!-- descend to the next level down -->
   <xsl:apply-templates/>
</xsl:template>

<!-- label for each node type -->

<xsl:template match="*|/" mode="label">
   <xsl:param name="self-x">
     <xsl:call-template name="get-x-coordinate">
       <xsl:with-param name="node" select="."/>
     </xsl:call-template>
   </xsl:param>
   <xsl:param name="self-y">
     <xsl:call-template name="get-y-coordinate">
       <xsl:with-param name="node" select="."/>
     </xsl:call-template>
   </xsl:param>
   <xsl:if test="..">
     <!-- element nodes get a little dot -->
     <circle cx="{$self-x}" cy="{$self-y}" r="{$element-dot-radius}" 
style="stroke:none; fill:{$tree-color}"/>
   </xsl:if>
   <text x="{$self-x + $writing-bump-over}" y="{$self-y - $writing-bump-up}"
         style="{$element-font-style}; stroke:none; fill:{$element-color}">
     <xsl:if test="not(..)">
       <!-- the root node gets labeled with a / -->
       <xsl:text>/</xsl:text>
     </xsl:if>
     <xsl:value-of select="local-name()"/>
     <xsl:if test="@*">
       <!-- any attribute nodes are written out too -->
       <xsl:choose>
         <xsl:when test="@rowid='AAAI2JAANAAABt6AAM'">
           <tspan style="{$attribute-font-style}; stroke:none; 
fill:{$attribute-special-color}">
            <xsl:for-each select="@*">
                <xsl:text>&#xA0;</xsl:text>
                <xsl:value-of select="local-name()"/>
                <xsl:text>="</xsl:text>
                <xsl:value-of select="."/>
                <xsl:text>"</xsl:text>
            </xsl:for-each>
        </tspan>

         </xsl:when>
         <xsl:when test="@rowid='AAAI2DAANAAABsSAAM'">
           <tspan style="{$attribute-font-style}; stroke:none; 
fill:{$attribute-special-color}">
            <xsl:for-each select="@*">
                <xsl:text>&#xA0;</xsl:text>
                <xsl:value-of select="local-name()"/>
                <xsl:text>="</xsl:text>
                <xsl:value-of select="."/>
                <xsl:text>"</xsl:text>
            </xsl:for-each>
        </tspan>

         </xsl:when>
         <xsl:otherwise>
           <tspan style="{$attribute-font-style}; stroke:none; 
fill:{$attribute-color}">
         <xsl:for-each select="@*">
             <xsl:text>&#xA0;</xsl:text>
             <xsl:value-of select="local-name()"/>
             <xsl:text>="</xsl:text>
             <xsl:value-of select="."/>
             <xsl:text>"</xsl:text>
         </xsl:for-each>
       </tspan>
         </xsl:otherwise>
       </xsl:choose>
     </xsl:if>
   </text>
</xsl:template>

<xsl:template match="text()" mode="label">
   <xsl:param name="self-x">
     <xsl:call-template name="get-x-coordinate">
       <xsl:with-param name="node" select="."/>
     </xsl:call-template>
   </xsl:param>
   <xsl:param name="self-y">
     <xsl:call-template name="get-y-coordinate">
       <xsl:with-param name="node" select="."/>
     </xsl:call-template>
   </xsl:param>
   <!-- text nodes are marked with a little box -->
   <rect x="{$self-x - ($text-box-width div 2)}"  y="{$self-y - 
($text-box-height div 2)}"  width="{$text-box-width}" 
height="{$text-box-height}" style="stroke:{$tree-color}; fill:{$tree-color}"/>
   <text x="{$self-x + $writing-bump-over}" y="{$self-y - $writing-bump-up}"
         style="{$text-font-style}; stroke:none; fill:{$text-color}">
     <xsl:text>"</xsl:text>
     <xsl:value-of select="."/>
<!-- replace the value-of with this call
      if you want or need to escape whitespace characters
     <xsl:call-template name="escape-ws">
       <xsl:with-param name="text" select="." />
     </xsl:call-template>
-->
     <xsl:text>"</xsl:text>
   </text>
</xsl:template>

<xsl:template match="processing-instruction()" mode="label">
   <xsl:param name="self-x">
     <xsl:call-template name="get-x-coordinate">
       <xsl:with-param name="node" select="."/>
     </xsl:call-template>
   </xsl:param>
   <xsl:param name="self-y">
     <xsl:call-template name="get-y-coordinate">
       <xsl:with-param name="node" select="."/>
     </xsl:call-template>
   </xsl:param>
   <!-- PI nodes a marked with a little arrow -->
   <polygon points="{$self-x + ($text-box-width div 2)},{$self-y},{$self-x 
- ($text-box-width div 2)},{$self-y - $text-box-width},{$self-x - 
($text-box-width div 2)},{$self-y + $text-box-width}" 
style="fill:{$tree-color}"/>
   <text x="{$self-x + $writing-bump-over}" y="{$self-y - $writing-bump-up}"
         style="{$code-font-style}; stroke:none; fill:{$PI-color}">
     <xsl:text/>&lt;?<xsl:value-of select="concat(local-name(), ' ', 
.)"/>?&gt;<xsl:text/>
   </text>
</xsl:template>

<xsl:template match="comment()" mode="label">
   <xsl:param name="self-x">
     <xsl:call-template name="get-x-coordinate">
       <xsl:with-param name="node" select="."/>
     </xsl:call-template>
   </xsl:param>
   <xsl:param name="self-y">
     <xsl:call-template name="get-y-coordinate">
       <xsl:with-param name="node" select="."/>
     </xsl:call-template>
   </xsl:param>
   <!-- comment nodes are marked with a little triangle -->
   <polygon points="{$self-x - ($text-box-width div 2)},{$self-y},{$self-x 
+ ($text-box-width div 2)},{$self-y - $text-box-width},{$self-x + 
($text-box-width div 2)},{$self-y + $text-box-width}" 
style="fill:{$tree-color}"/>
   <text x="{$self-x + $writing-bump-over}" y="{$self-y - $writing-bump-up}"
         style="{$code-font-style}; stroke:none; fill:{$comment-color}">
     <xsl:text/>&lt;!-- <xsl:value-of select="."/> --&gt;<xsl:text/>
   </text>
</xsl:template>

<!-- template to draw the line from one node to the next ... feel free to 
enhance ... -->

<xsl:template name="drawpath">
   <xsl:param name="fromX" select="10"/>
   <xsl:param name="fromY" select="10"/>
   <xsl:param name="toX" select="20"/>
   <xsl:param name="toY" select="20"/>
   <g style="stroke:{$tree-color}; stroke-width:{$line-thickness}; 
stroke-linecap:round" >
     <line x1="{$fromX}"  y1="{$fromY}" x2="{$fromX}" y2="{$toY}" />
     <line x1="{$fromX}"  y1="{$toY}" x2="{$toX}" y2="{$toY}" />
   </g>
</xsl:template>


<!-- recursive template to escape backslashes, apostrophes, newlines and 
tabs -->
<!-- gratefully duplicated from Jeni Tennison's and Mike Brown's ASCII Art-
      Tree Viewing stylesheet. -->

<xsl:template name="escape-ws">
     <xsl:param name="text" />
     <xsl:choose>
         <xsl:when test="contains($text, '\')">
             <xsl:call-template name="escape-ws">
                 <xsl:with-param name="text" 
select="substring-before($text, '\')" />
             </xsl:call-template>
             <xsl:text>\\</xsl:text>
             <xsl:call-template name="escape-ws">
                 <xsl:with-param name="text" select="substring-after($text, 
'\')" />
             </xsl:call-template>
         </xsl:when>
         <xsl:when test="contains($text, $apos)">
             <xsl:call-template name="escape-ws">
                 <xsl:with-param name="text" 
select="substring-before($text, $apos)" />
             </xsl:call-template>
             <xsl:text>\'</xsl:text>
             <xsl:call-template name="escape-ws">
                 <xsl:with-param name="text" select="substring-after($text, 
$apos)" />
             </xsl:call-template>
         </xsl:when>
         <xsl:when test="contains($text, '&#xA;')">
             <xsl:call-template name="escape-ws">
                 <xsl:with-param name="text" 
select="substring-before($text, '&#xA;')" />
             </xsl:call-template>
             <xsl:text>\n</xsl:text>
             <xsl:call-template name="escape-ws">
                 <xsl:with-param name="text" select="substring-after($text, 
'&#xA;')" />
             </xsl:call-template>
         </xsl:when>
         <xsl:when test="contains($text, '&#x9;')">
             <xsl:value-of select="substring-before($text, '&#x9;')" />
             <xsl:text>\t</xsl:text>
             <xsl:call-template name="escape-ws">
                 <xsl:with-param name="text" select="substring-after($text, 
'&#x9;')" />
             </xsl:call-template>
         </xsl:when>
         <xsl:otherwise><xsl:value-of select="$text" /></xsl:otherwise>
     </xsl:choose>
</xsl:template>

</xsl:stylesheet>

