<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="2.0">
    
<xsl:output method="text" />

<!-- cases converter -->
<xsl:variable name="smallcase" select="'abcdefghijklmnopqrstuvwxyz'" /> <xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'" />

<xsl:param name="term" />

<!-- main template for searching -->
<xsl:template match="/courses">
{"items": [
	<xsl:for-each select="course[contains(translate(id, $smallcase, $uppercase),translate($term, $smallcase, $uppercase))]">
		
	<tr>
		<xsl:choose>
			<xsl:when test="position() != last()">
			{"id": "<xsl:value-of select='id' />","display_name": "<xsl:value-of select='name' />", "info":  "<xsl:value-of select='info' />","time": "<xsl:value-of select='time' />" },
			
			</xsl:when>

			<xsl:otherwise>{"id": "<xsl:value-of select='id' />","display_name": "<xsl:value-of select='name' />", "info":  "<xsl:value-of select='info' />","time": "<xsl:value-of select='time' />" }</xsl:otherwise>

	
	</xsl:choose>
	</tr>
	</xsl:for-each>
	
]}
</xsl:template>

</xsl:stylesheet>
