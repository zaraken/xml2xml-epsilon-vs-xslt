# xml2xml-epsilon-vs-xslt
A sample demonstrating difference/similarities in epsilon ETL and xslt in transforming an xml file

This is an Eclipse Java project but you could also compile and run from shell. 
In any case you need to download epsilon-1.4-core.jar from https://eclipse.org/epsilon/download/

The java code is just a boilerplate to load the files and run the transformation.
The logic is in library2index.etl, respectively in library2index.xsl.
The xsl transformation is here for comparison, it cannot be executed via provided project.

This example is way too simple to draw any conclusions but you can see the differences in the style of writing the transformations.

Epsilon ETL script in library2index.etl
```etl
rule Library2Index
	transform s : Source!t_library
	to t : Target!`t_index` {
		Target.root = t;
		t.a_totalpages = s.children.collect(b|b.i_pages).sum();
	}
	
rule Book2Entry
	transform s : Source!t_book
	to t : Target!t_entry {
		t.a_title = s.a_title;
		t.a_id = s.e_id.text;
		s.parentNode.equivalent().appendChild(t);
	}
```

XSLT in library2index.xsl
```xsl
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
```

source in library.xml
```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<library>
  <book title="EMF Eclipse Modeling Framework" pages="744" public="true">
  	<id>EMFBook</id>
    <author>Dave Steinberg</author>
    <author>Frank Budinsky</author>
    <author>Marcelo Paternostro</author>
    <author>Ed Merks</author>
    <published>2009</published>
  </book>
  <book title="Eclipse Modeling Project: A Domain-Specific Language (DSL) Toolkit" 
  	pages="736" public="true">
  	<id>EMPBook</id>
    <author>Richard Gronback</author>
    <published>2009</published>
  </book>
  <book title="Official Eclipse 3.0 FAQs" pages="432" public="false">
  	<id>Eclipse3FAQs</id>
    <author>John Arthorne</author>
    <author>Chris Laffra</author>
    <published>2004</published>
  </book>
  <book title="TestBook1" pages="10" public="false">
  	<id>id1</id>
    <author>Author 1</author>
    <published>2004</published>
  </book>
  <book title="TestBook2" pages="20" public="false">
  	<id>id2</id>
    <author>Author 2</author>
    <published>2004</published>
  </book>
  <book title="TestBook3" pages="30" public="false">
  	<id>id3</id>
    <author>Author 3</author>
    <published>2004</published>
  </book>
</library>
```

result in index.xml
```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<index totalpages="1972">
  <entry id="EMFBook" title="EMF Eclipse Modeling Framework"/>
  <entry id="EMPBook" title="Eclipse Modeling Project: A Domain-Specific Language (DSL) Toolkit"/>
  <entry id="Eclipse3FAQs" title="Official Eclipse 3.0 FAQs"/>
  <entry id="id1" title="TestBook1"/>
  <entry id="id2" title="TestBook2"/>
  <entry id="id3" title="TestBook3"/>
</index>

```
