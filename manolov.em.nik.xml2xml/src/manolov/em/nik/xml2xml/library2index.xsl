<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="xml" indent="yes"/>

  <xsl:template match="/library">
    <index>
        <xsl:attribute name="totalpages">
            <sum>
                <xsl:value-of select="sum(book/@pages)"/>
            </sum>
        </xsl:attribute>
      <xsl:apply-templates select="book"/>
    </index>
  </xsl:template>

  <xsl:template match="book">
    <entry title="{@title}">
        <xsl:attribute name="id">
            <xsl:value-of select="id/text()" />
        </xsl:attribute>
    </entry>
  </xsl:template>

</xsl:stylesheet>
