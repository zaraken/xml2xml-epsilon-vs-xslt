<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions" exclude-result-prefixes="#all">
	<xsl:output method="xml" encoding="UTF-8" byte-order-mark="no" indent="yes"/>
	<xsl:template match="/">
		<genres>
			<xsl:attribute name="xsi:noNamespaceSchemaLocation" namespace="http://www.w3.org/2001/XMLSchema-instance" select="'./genres.xsd'"/>
			<xsl:for-each-group select="/catalog/book" group-by="genre">
				<xsl:sort select="current-grouping-key()"/>
				
				<genre 
					name="{current-grouping-key()}" 
					average-price = "{format-number(avg(current-group()/price), '#.00')}">
					
					<xsl:apply-templates select="current-group()">
						<xsl:sort select="title" collation="http://www.w3.org/2005/xpath-functions/collation/codepoint"/>
					</xsl:apply-templates>
				</genre>
			</xsl:for-each-group>
		</genres>
	</xsl:template>
	
	<xsl:template match="book">
		<title> <xsl:value-of select="title"/> </title>
	</xsl:template>
</xsl:stylesheet>