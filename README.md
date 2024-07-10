# query-export-tool

Tool export query in CSV and XLS / XLSX format

[![Keep a Changelog v1.1.0 badge](https://img.shields.io/badge/changelog-Keep%20a%20Changelog%20v1.1.0-%23E05735)](CHANGELOG.md) 
[![Maven Central](https://img.shields.io/maven-central/v/org.fugerit.java/query-export-tool.svg)](https://mvnrepository.com/artifact/org.fugerit.java/query-export-tool)
[![license](https://img.shields.io/badge/License-Apache%20License%202.0-teal.svg)](https://opensource.org/licenses/Apache-2.0)
[![code of conduct](https://img.shields.io/badge/conduct-Contributor%20Covenant-purple.svg)](https://github.com/fugerit-org/fj-universe/blob/main/CODE_OF_CONDUCT.md)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=fugerit-org_query-export-tool&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=fugerit-org_query-export-tool)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=fugerit-org_query-export-tool&metric=coverage)](https://sonarcloud.io/summary/new_code?id=fugerit-org_query-export-tool)

![Java runtime version](https://img.shields.io/badge/run%20on-java%208+-%23113366.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Java build version](https://img.shields.io/badge/build%20on-java%2011+-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Apache Maven](https://img.shields.io/badge/Apache%20Maven-3.9.0+-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)

## 1 Quickstart

### 1.1 Create a sample query catalog 

```
<query-catalog-config>

	<query-catalog id="main-catalog">
		<query id="Q001" sql="SELECT * FROM  test_export" outputFormat="csv" csvSeparator=";" outputFile="target/catalog_test_001.csv"/>
		<query id="Q002" sql="SELECT * FROM  test_export" outputFormat="csv" outputFile="target/catalog_test_002.csv"/>
		<query id="Q003" sql="SELECT * FROM  test_export" outputFormat="html" outputFile="target/catalog_test_003.html" createPath="1"/>
		<query id="Q004" sql="SELECT * FROM  test_export" outputFormat="xls" outputFile="target/catalog_test_004.xls" xlsResize="1"/>
		<query id="Q005" sql="SELECT * FROM  test_export" outputFormat="xlsx" outputFile="target/catalog_test_004.xlsx" xlsTemplate="src/test/resources/template/test_template.xlsx" />
	</query-catalog>

</query-catalog-config>
```

### 1.2 Load and use the catalog

```
		QueryConfigCatalog catalog = QueryConfigCatalog.loadQueryConfigCatalogSafe( "cl://sample/query-catalog-sample.xml" );
		try ( Connection conn = ... ) {
			catalog.handle( conn , "main-catalog", "Q001");
		}
```

## 2 Formats

### 2.1 HTML format

HTML format is handled with core I/O API, no dependency needed.

### 2.2 CSV format

CSV Format needs *OpenCSV* dependency, which is automatically included by default when importing query-export-tool dependency.

If needed it can be added in explicit way : 

```
		<dependency>
			<groupId>com.opencsv</groupId>
			<artifactId>opencsv</artifactId>
			<version>${opencsv-version}</version>
		</dependency>
```

### 2.3 XLS/XLSX formats

XLS/XLSX Formats needs *Apache POI* dependency, which is *NOT* automatically included by default when importing query-export-tool dependency : 

```
		<dependency>
			<groupId>org.apache.poi</groupId>
			<artifactId>poi-ooxml</artifactId>
			<version>${poi-version}</version>
		</dependency>
```

## 3 Usage As SQL Catalog

Create the xml catalog : 

```xml
<query-catalog-config bean-mode="bean-xml-full">

	<query-catalog id="sample-catalog-alt">
		<query id="Q001ALT" outputFormat="csv" csvSeparator=";" outputFile="target/catalog_test_001.csv">
			<sql>SELECT * FROM  test_export</sql>
		</query>
	</query-catalog>

</query-catalog-config>
```

Access the catalog : 

```java
QueryConfigCatalog catalog = QueryConfigCatalog.loadQueryConfigCatalogSafe("cl://sample/query-catalog-sample-alt.xml");
QueryConfig queryConfig = catalog.getListMap( "sample-catalog-alt" ).get( "Q001ALT" );
logger.info( "query config : {}, sql : {}", queryConfig, queryConfig.getSql() );
```

A full example is available in the [JUNIT](src/test/java/test/org/fugerit/java/query/export/tool/TestCatalogAlt.java) .