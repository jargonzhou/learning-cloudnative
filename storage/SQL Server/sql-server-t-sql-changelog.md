# Comprehensive T‑SQL Language Changes (Functions + Syntax) by SQL Server Release

> **What you’ll get**  
> • A quick‑reference matrix that tells you, at a glance, what major T‑SQL language features appeared in each major release.  
> • Detailed, version‑by‑version breakdowns that cover:
>   - New keywords / statements (e.g., `TRY…CATCH`, `MERGE`, `CREATE OR ALTER`)
>   - New data‑type introductions (e.g., `DATE`, `datetime2`, `JSON`)
>   - Every built‑in **scalar**, **aggregate**, and **table‑valued** function that was added (with a one‑sentence description)
>   - Notable DMVs/DMFs and system‑procedure additions.
>   - Deprecations / breaking‑change notes (including compatibility‑level impact).  
      > • An appendix that lists **all functions** introduced in each release (alphabetical within the version).  
      > • Direct links to the official Microsoft “What’s new in Transact‑SQL” documentation for each version.

---  

## 1️⃣ Quick‑Reference Matrix

| SQL Server version (compatibility level) | Release year | Highlighted language‑construct changes | New built‑in **scalar** functions* | New **aggregate** functions | New **table‑valued** functions | Notable other changes |
|---|---|---|---|---|---|---|
| **2005** (90) | 2005 | `TRY…CATCH`, CTE (`WITH`), `PIVOT`/`UNPIVOT`, `CROSS/OUTER APPLY`, table‑valued parameters | `ROW_NUMBER`, `RANK`, `DENSE_RANK`, `NTILE`, XML methods (`.value()`, `.query()`, `.nodes()`, `.exist()`) | – | – | First “dynamic management views” (DMVs), `sys.all_objects` family |
| **2008** (100) | 2008 | `MERGE` statement, new **datetime** data types (`date`, `time`, `datetime2`, `datetimeoffset`) | `SWITCHOFFSET`, `TODATETIMEOFFSET` (datetimeoffset helpers) | – | – | `sys.dm_db_index_physical_stats` improvements; spatial data type enhancements |
| **2008 R2** (100) | 2008 | Minor release – no new language keywords | – | – | – | Columnstore index enhancements (non‑clustered columnstore) |
| **2012** (110) | 2012 | Sequences (`CREATE SEQUENCE` + `NEXT VALUE FOR`), `OFFSET FETCH` paging, window‑function extensions (`LAG`, `LEAD`, `FIRST_VALUE`, `LAST_VALUE`, `NTH_VALUE`) | `IIF`, `CHOOSE`, `CONCAT`, `CONCAT_WS` (v 2017), `FORMAT`, `PARSE`, `TRY_PARSE`, `TRY_CAST`, `TRY_CONVERT`, `EOMONTH`, `DATEFROMPARTS`, `TIMEFROMPARTS`, `DATETIME2FROMPARTS`, `DATETIMEFROMPARTS`, `DATETIMEOFFSETFROMPARTS` | – | – | New compatibility‑level 110‑specific behavior for `ROW_NUMBER()` offsets, `COLUMNSTORE` index as default for data‑warehouse workloads |
| **2014** (120) | 2014 | No new T‑SQL syntax; continued support for 2012 features. | – | – | – | In‑memory OLTP (Hekaton) introduced – no new scalar functions |
| **2016** (130) | 2016 | `CREATE OR ALTER`, `DROP IF EXISTS`, **JSON** support, **Temporal tables**, **Row‑Level Security**, **Dynamic Data Masking**, **Query Store**, **AT TIME ZONE** | `STRING_SPLIT`, `ISJSON`, `JSON_VALUE`, `JSON_QUERY`, `JSON_MODIFY`, `OPENJSON`, `AT TIME ZONE`, `SESSION_CONTEXT` | – | – | `sys.fn_listextendedproperty` improvements, new DMVs for Query Store |
| **2017** (140) | 2017 | Graph extensions (`CREATE TABLE … AS NODE/EDGE`, `MATCH`), **Accelerated Database Recovery**, **Resumable Index Create**, **Automatic Plan Correction** | `STRING_AGG`, `CONCAT_WS`, `TRIM`, `TRANSLATE` | – | – | **Scalar‑UDF inlining** (performance feature) |
| **2019** (150) | 2019 | **Scalar‑UDF inlining** (general availability), **Batch‑mode on rowstore**, **Intelligent query processing**, **Approximate analytics** | `APPROX_COUNT_DISTINCT`, `APPROX_PERCENTILE` (approximate aggregates) | `APPROX_COUNT_DISTINCT`, `APPROX_PERCENTILE` (count as aggregates) | – | New system‑function `sys.fn_dbfilestats`, `sys.dm_exec_query_profile` |
| **2022** (160) | 2022 | **Ledger** (`LEDGER = ON`), **Azure‑linked server** improvements, **Hybrid buffer pool**, **STRING_SPLIT** now supports an **ordinal** column, **Enhanced `DROP IF EXISTS`** for all object types, **graph** and **temporal** table enhancements | `STRING_SPLIT` (ordinal support) | – | – | `sys.ledger_hash` (function to view the hash chain of a ledger table) – **new built‑in for ledger** |

\*Columns marked “New scalar functions” list only the **first appearance** of a particular scalar function (or method).  If the same function was enhanced in a later release (e.g., `STRING_SPLIT` gaining the ordinal column in 2022), the original appearance is still listed in the row for its first introduction.

---

## 2️⃣ Detailed Release‑by‑Release Breakdown

Below each version, the sections are ordered the same way (keywords → data types → scalar functions → aggregate functions → table‑valued functions → DMVs/DMFs → deprecations).  **Bolded items** are the most common things people look for when upgrading.

> **Tip:** Most of the items only become visible when a database’s **compatibility level** is set to the version’s level (or higher).  If you observe a feature “missing” after an upgrade, check `ALTER DATABASE <db> SET COMPATIBILITY_LEVEL = <level>`.

---  

### 2.1 SQL Server 2005 (Compatibility 90)

| Category | Change |
|---|---|
| **Keywords / Statements** | `TRY…CATCH` (structured error handling) <br> `WITH` common table expressions (CTEs) <br> `PIVOT` / `UNPIVOT` <br> `CROSS APPLY` / `OUTER APPLY` (table‑valued function laterial join) |
| **Data Types** | Introduction of **user‑defined CLR types** (via `CREATE ASSEMBLY`) – not a built‑in type but a language expansion. |
| **Scalar functions** | **Ranking / analytic functions** (all use `OVER` clause): <br> `ROW_NUMBER()`, `RANK()`, `DENSE_RANK()`, `NTILE()` |
| **XML methods** (treated as scalar functions) | `xml.value()`, `xml.query()`, `xml.nodes()`, `xml.exist()` |
| **Table‑valued functions** | TVPs – ability to pass a table‑valued parameter to a stored procedure (`CREATE TYPE … AS TABLE`). |
| **DMVs / DMFs** | First set of Dynamic Management Views (e.g., `sys.dm_exec_requests`, `sys.dm_os_wait_stats`). |
| **Break‑compatibility notes** | `ROW_NUMBER()` is **not deterministic** if the `ORDER BY` clause is omitted – behaviour changed in later versions when `ROW_NUMBER()` is used in a `SELECT DISTINCT`. |
| **Reference** | <https://learn.microsoft.com/sql/t-sql/what-s-new-in-transact-sql-2005> |

---  

### 2.2 SQL Server 2008 (Compatibility 100)

| Category | Change |
|---|---|
| **Keywords / Statements** | `MERGE` (UPSERT) <br> `TABLESAMPLE` (seeded sampling) <br> `WITH (FORCESEEK)` hints (new options). |
| **Data Types** | New **date**‑only types: `date`, `time`, `datetime2`, `datetimeoffset`. |
| **Scalar functions** | `SWITCHOFFSET (datetimeoffset, tz)` – converts a `datetimeoffset` to a different time‑zone offset. <br> `TODATETIMEOFFSET (datetime, tz)` – creates a `datetimeoffset` from a `datetime` and an offset. |
| **System functions** | `SYSDATETIME()`, `SYSUTCDATETIME()`, `SYSUTCDATETIME()` (already existed but gain higher‑precision). |
| **DMVs / DMFs** | `sys.dm_db_index_physical_stats`, `sys.dm_db_index_operational_stats`, `sys.dm_db_missing_index_details`. |
| **Deprecated / Behaviour changes** | The **old** `MERGE` syntax was extended in later versions with `OUTPUT` and `WHEN NOT MATCHED BY SOURCE`. |
| **Reference** | <https://learn.microsoft.com/sql/t-sql/what-s-new-in-transact-sql-2008> |

---  

### 2.3 SQL Server 2008 R2 (Compatibility 100)

| Category | Change |
|---|---|
| **Keywords / Statements** | No new T‑SQL keywords; primary focus was **performance** (e.g., non‑clustered columnstore indexes). |
| **Data Types** | No new types. |
| **Functions** | **None** that were added as new scalar or table‑valued functions. Existing functions received minor performance tweaks. |
| **DMVs / DMFs** | Additional DMVs for columnstore (`sys.dm_db_column_store_row_group_physical_stats`). |
| **Reference** | <https://learn.microsoft.com/sql/t-sql/what-s-new-in-transact-sql-2008-r2> |

---  

### 2.4 SQL Server 2012 (Compatibility 110)

| Category | Change |
|---|---|
| **Keywords / Statements** | `SEQUENCE` objects (`CREATE SEQUENCE`), `OFFSET FETCH` clause for paging (`ORDER BY … OFFSET n ROWS FETCH NEXT m ROWS ONLY`). |
| **Data Types** | None new besides those from 2008. |
| **Scalar Functions** | **Logical / conversion**: <br> `IIF (boolean_expression, true_value, false_value)` <br> `CHOOSE (index, value1, …)` (returns the *index‑th* value) <br> `CONCAT (string1, …)` – concatenates with implicit `NULL` handling <br> `FORMAT (value, format_string [, culture])` – .NET‑style formatting <br> `PARSE (string, type [, culture])` – culture‑aware conversion <br> `TRY_PARSE (string, type [, culture])` – returns `NULL` on conversion failure <br> `TRY_CAST (expression AS data_type)` <br> `TRY_CONVERT (data_type, expression [, style])` |
| **Date/Time Functions** | `EOMONTH (date, [month_to_add])` – last day of month <br> `DATEFROMPARTS (year, month, day)` <br> `TIMEFROMPARTS (hour, minute, seconds, fractions, precision)` <br> `DATETIME2FROMPARTS` <br> `DATETIMEFROMPARTS` <br> `DATETIMEOFFSETFROMPARTS` |
| **Window Functions (new)** | `LAG (expression [, offset [, default]]) OVER (… )` <br> `LEAD (expression [, offset [, default]]) OVER (… )` <br> `FIRST_VALUE (expression) OVER (… )` <br> `LAST_VALUE (expression) OVER (… )` <br> `NTH_VALUE (expression, n) OVER (… )` |
| **Sequence Functions** | `NEXT VALUE FOR <sequence_name>` (returns the next integer from a sequence). |
| **Table‑valued Functions** | None introduced as “new”, although the **inline TVF** syntax (`RETURNS TABLE AS RETURN …`) became a best‑practice, and TVPs remained. |
| **DMVs / DMFs** | `sys.dm_exec_scalar_uDFS` for scalar UDF stats, `sys.dm_db_file_space_usage` for space management. |
| **Behaviour / Compatibility** | Compatibility 110 adds the `STRING_SPLIT` *placeholder* (actually 2016) – any references to it before 2016 raise an error; `ROW_NUMBER()` now throws an error if the `ORDER BY` clause is omitted under the new level. |
| **Reference** | <https://learn.microsoft.com/sql/t-sql/what-s-new-in-transact-sql-2012> |

---  

### 2.5 SQL Server 2014 (Compatibility 120)

| Category | Change |
|---|---|
| **Keywords / Statements** | No new T‑SQL keywords. Introduced **In‑Memory OLTP** (Hekaton) – syntax `CREATE MEMORY‑OPTIMIZED TABLE`. |
| **Scalar Functions** | **None** freshly added. Existing functions (e.g., `FORMAT`) now support **CLR** improvements. |
| **Aggregate Functions** | No new aggregates. |
| **Table‑valued Functions** | No new TVFs. |
| **DMVs / DMFs** | New DMVs for In‑Memory tables: `sys.dm_db_xtp_memory_consumers`, `sys.dm_db_xtp_index_stats`. |
| **Reference** | <https://learn.microsoft.com/sql/t-sql/what-s-new-in-transact-sql-2014> |

---  

### 2.6 SQL Server 2016 (Compatibility 130)

| Category | Change |
|---|---|
| **Keywords / Statements** | **`CREATE OR ALTER`** (for procedures, functions, triggers, views, and more) <br> **`DROP IF EXISTS`** (applies to most objects) |
| **Data Types** | **`JSON`** is **not** a separate data type, but a set of functions and operators for storing JSON in `NVARCHAR` columns. |
| **Scalar Functions** | `STRING_SPLIT (string, separator)` – table‑valued function that returns rows of substrings (single column `value`). <br> **JSON functions** (all return `NVARCHAR` or bit): <br> `ISJSON (expression)` – returns 1 if valid JSON. <br> `JSON_VALUE (json_expression, path)` – scalar extractor. <br> `JSON_QUERY (json_expression, path)` – returns a JSON fragment. <br> `JSON_MODIFY (json_expression, path, new_value)` – returns modified JSON. <br> `OPENJSON (jsonExpression [, path] [, WITH (col specs)])` – parses JSON into a table. |
| **Date/Time Functions** | **`AT TIME ZONE`** – converts datetime to a specific time‑zone (returns `datetimeoffset`). |
| **System / Session Functions** | `SESSION_CONTEXT (key [, default])` – retrieve a value set via `sp_set_session_context`. |
| **Temporal Tables (system‑versioned)** | Syntax `PERIOD FOR SYSTEM_TIME` and `SYSTEM_VERSIONING = ON`. (Not a function, but a **new DDL clause**.) |
| **Security** | **Row‑Level Security** – `CREATE SECURITY POLICY`, `FILTER PREDICATE`, `BLOCK PREDICATE`. <br> **Dynamic Data Masking** – `ALTER TABLE … ALTER COLUMN … ADD MASKED WITH (FUNCTION = …)`. |
| **DMVs / DMFs** | Query Store related DMVs: `sys.query_store_plan`, `sys.query_store_runtime_stats`. |
| **Behaviour / Compatibility** | Compatibility 130 disables “old‑style” `INSERT…EXEC` when `SET ANSI_WARNINGS OFF`. |
| **Reference** | <https://learn.microsoft.com/sql/t-sql/what-s-new-in-transact-sql-2016> |

---  

### 2.7 SQL Server 2017 (Compatibility 140)

| Category | Change |
|---|---|
| **Keywords / Statements** | **Graph extensions** – `CREATE TABLE … AS NODE`, `CREATE TABLE … AS EDGE`, `MATCH` clause for pattern matching. |
| **Scalar Functions** | **String aggregation**: <br> `STRING_AGG (expression, separator) [WITHIN GROUP (ORDER BY …)]` – concatenates values across rows. <br> `CONCAT_WS (separator, value1, …)` – concatenates with a separator (nulls ignored). <br> `TRIM ( [ characters FROM ] string )` – removes leading/trailing characters (default whitespace). <br> `TRANSLATE (inputString, characters, translations)` – replaces each character in `characters` with the matching char from `translations`. |
| **Aggregate Functions** | `STRING_AGG` (see above). |
| **Table‑valued Functions** | `OPENJSON` adds **`WITH (schema)`** column definition support (was introduced earlier, but 2017 made it full‑featured). |
| **Graph Functions** | `sys.fn_isgraph` (returns 1 if a table is a graph node or edge). |
| **Security / Auditing** | **Transparent Data Encryption (TDE)** improvements; **Accelerated Database Recovery** (new internal algorithmic changes, not a T‑SQL function). |
| **DMVs / DMFs** | `sys.dm_db_graph_properties` (exposes graph metadata). |
| **Behaviour / Compatibility** | Compatibility 140 adds **`TRUNCATE TABLE … WITH (DROP_EXISTING = ON)`** for partitioned tables. |
| **Reference** | <https://learn.microsoft.com/sql/t-sql/what-s-new-in-transact-sql-2017> |

---  

### 2.8 SQL Server 2019 (Compatibility 150)

| Category | Change |
|---|---|
| **Keywords / Statements** | **Scalar‑UDF inlining** (automatic for many scalar functions) – not a syntax change but a power‑user optimisation visible via `sys.dm_exec_function_stats`. |
| **Scalar Functions** | **Approximate aggregate functions** (greatly faster for very large data sets): <br> `APPROX_COUNT_DISTINCT (expression)` – returns an approximate distinct count. <br> `APPROX_PERCENTILE (expression, percentile [, accuracy])` – estimates a percentile. |
| **Aggregate Functions** | Same two approximate functions (they are **aggregate** in nature). |
| **Table‑valued Functions** | `STRING_SPLIT` **ordinal** column (third argument) still **not** available until 2022 – not applicable yet. |
| **Intelligent Query Processing** | Features like **Batch‑Mode on Rowstore**, **Table Variable Deferred Compilation**, **Approximate Query Processing**, **Scalar UDF Inlining** – all affect query plans, not syntax. |
| **DMVs / DMFs** | New DMVs for Intelligent Query Processing: `sys.dm_exec_query_memory_grants`, `sys.dm_db_xtp_memory_consumers` (improved). |
| **Reference** | <https://learn.microsoft.com/sql/t-sql/what-s-new-in-transact-sql-2019> |

---  

### 2.9 SQL Server 2022 (Compatibility 160)

| Category | Change |
|---|---|
| **Keywords / Statements** | **Ledger** database feature (`ALTER DATABASE <db> SET LEDGER = ON`) – adds an immutable hash‑chain to every transaction. <br> **`DROP IF EXISTS`** expands to all object types (including graph objects). |
| **Scalar Functions** | **`STRING_SPLIT` – ordinal support**: `STRING_SPLIT (input, separator, <ordinal_flag>)` returns a second column `ordinal` (1‑based position). <br> **`sys.ledger_hash`** – returns the hash value for a ledger transaction (exposed as a built‑in scalar function). |
| **Other New Built‑in Functions** | **`STRING_REVERSE`** – not new (exists earlier). <br> **`JSON_VALID`** – still not in core (future). |
| **DMVs / DMFs** | `sys.dm_db_ledger_historical_transactions` – view ledger transaction history (not a function but a DMV). |
| **Behaviour / Compatibility** | Compatibility 160 introduces **`TRIM`** with **multiple characters** (e.g., `TRIM('ab' FROM 'abacaba')`). <br> **`ROW_NUMBER()`** now supports a new `ORDER BY (SELECT NULL)` syntax for deterministic but unsorted enumeration. |
| **Reference** | <https://learn.microsoft.com/sql/t-sql/what-s-new-in-transact-sql-2022> |

---

## 3️⃣ Appendix – Functions **First Appearing** in Each Release

Below is a **complete** alphabetical list of each built‑in function that was **added** in a given version.  Functions that were **enhanced** (e.g., `STRING_SPLIT` gaining an `ordinal` column) are listed under the version where the **enhancement** was introduced, with a note.

> **Notation** – “*TVF*” = table‑valued function, “*scalar*” = scalar‑valued, “*aggregate*” = aggregate‑type.

### 3.1 SQL Server 2005

| Function | Category | Short description |
|---|---|---|
| **`ROW_NUMBER`** | scalar (window) | Returns sequential integer starting at 1 for each row in the window. |
| **`RANK`** | scalar (window) | Gives rank with gaps for ties. |
| **`DENSE_RANK`** | scalar (window) | Gives rank without gaps for ties. |
| **`NTILE`** | scalar (window) | Divides rows into *n* groups. |
| **XML methods** (`.value`, `.query`, `.nodes`, `.exist`) | scalar (XML) | Extract data from an `xml` value using XQuery. |

*(All XML type methods were exposed as functions on the XML datatype.)*

---  

### 3.2 SQL Server 2008

| Function | Category | Description |
|---|---|---|
| **`SWITCHOFFSET`** | scalar (datetimeoffset) | Changes the time‑zone offset of a `datetimeoffset` value while preserving the UTC moment. |
| **`TODATETIMEOFFSET`** | scalar (datetimeoffset) | Creates a `datetimeoffset` from a `datetime` and a timezone offset. |

---  

### 3.3 SQL Server 2008 R2

> No new built‑in functions.

---  

### 3.4 SQL Server 2012

| Function | Category | Description |
|---|---|---|
| **`IIF`** | scalar | Shorthand for `CASE WHEN <cond> THEN <true> ELSE <false> END`. |
| **`CHOOSE`** | scalar | Returns the *n*‑th value from a list of expressions (1‑based). |
| **`CONCAT`** | scalar (string) | Concatenates strings, treating `NULL` as empty string. |
| **`FORMAT`** | scalar (string) | Formats a value using a .NET format string and optional culture. |
| **`PARSE`** | scalar (conversion) | Converts a string to a date/time or numeric value using a .NET format and culture (throws if fails). |
| **`TRY_PARSE`** | scalar (conversion) | Same as `PARSE` but returns `NULL` on failure. |
| **`TRY_CAST`** | scalar (conversion) | Like `CAST`, but returns `NULL` on conversion error. |
| **`TRY_CONVERT`** | scalar (conversion) | Like `CONVERT`, but returns `NULL` on conversion error. |
| **`EOMONTH`** | scalar (date) | Returns the last day of the month that contains the specified date. |
| **`DATEFROMPARTS`** | scalar (date) | Constructs a `date` value from year, month, day integers. |
| **`TIMEFROMPARTS`** | scalar (time) | Constructs a `time` value from hour, minute, second, fractions, and precision. |
| **`DATETIME2FROMPARTS`** | scalar (datetime2) | Constructs a `datetime2` value from its constituent parts. |
| **`DATETIMEFROMPARTS`** | scalar (datetime) | Constructs a `datetime` value from its constituent parts. |
| **`DATETIMEOFFSETFROMPARTS`** | scalar (datetimeoffset) | Constructs a `datetimeoffset` value from its parts, including offset. |
| **`LAG`** | scalar (window) | Returns the value from a preceding row within the window. |
| **`LEAD`** | scalar (window) | Returns the value from a following row within the window. |
| **`FIRST_VALUE`** | scalar (window) | Returns the first value in the window. |
| **`LAST_VALUE`** | scalar (window) | Returns the last value in the window. |
| **`NTH_VALUE`** | scalar (window) | Returns the *n*‑th value in the window (ordinal). |
| **`NEXT VALUE FOR`** | scalar (sequence) | Returns the next value from a sequence object. |
| **`SEQUENCE`** | object (not a function) | Enables creation of numeric sequences (`CREATE SEQUENCE`). |
| **`OFFSET FETCH`** | clause (paging) | Enables `ORDER BY … OFFSET x ROWS FETCH NEXT y ROWS ONLY` (not a function, but a new language clause). |

---  

### 3.5 SQL Server 2014

> No new built‑in functions were added.

---  

### 3.6 SQL Server 2016

| Function | Category | Description |
|---|---|---|
| **`STRING_SPLIT`** | TVF | Splits a delimited string into rows (single column `value`). |
| **`ISJSON`** | scalar (json) | Returns `1` if the argument is valid JSON, otherwise `0`. |
| **`JSON_VALUE`** | scalar (json) | Extracts a scalar value from a JSON string using a JSON path. |
| **`JSON_QUERY`** | scalar (json) | Returns a JSON fragment (object/array) from a JSON string. |
| **`JSON_MODIFY`** | scalar (json) | Returns a new JSON string after inserting, updating, or deleting a value. |
| **`OPENJSON`** | TVF | Parses JSON text and returns a rowset (key/value pairs). |
| **`AT TIME ZONE`** | scalar (datetimeoffset) | Converts a `datetime`/`datetimeoffset` to a target time‑zone, returning a `datetimeoffset`. |
| **`SESSION_CONTEXT`** | scalar (session) | Retrieves a value set via `sp_set_session_context`. |
| **`TRY_CONVERT`**, **`TRY_CAST`**, **`TRY_PARSE`** (already introduced in 2012) – no new functions but fully supported for JSON & datetimeoffset. |
| **`CREATE OR ALTER`** (statement) – not a function. |
| **`DROP IF EXISTS`** (statement) – not a function. |

---  

### 3.7 SQL Server 2017

| Function | Category | Description |
|---|---|---|
| **`STRING_AGG`** | aggregate (string) | Concatenates string values from multiple rows using a separator; optional ordering via `WITHIN GROUP`. |
| **`CONCAT_WS`** | scalar (string) | Concatenates strings with a separator; `NULL` values are ignored. |
| **`TRIM`** | scalar (string) | Removes leading and trailing spaces (or other characters) from a string. |
| **`TRANSLATE`** | scalar (string) | Replaces each character in the first argument with the corresponding character in the second argument. |
| **`STRING_ESCAPE`** | scalar (string) | Escapes a string for JSON or XML (added in 2016, but many docs list it as 2017 for full support). |
| **Graph TVF** `sys.fn_isgraph` | scalar (graph) | Returns `1` if the supplied table is a node or edge table. |
| **`OPENJSON`** – **`WITH`** clause (enhanced) – ability to define a schema for the output (makes it a TVF with column definitions). |

---  

### 3.8 SQL Server 2019

| Function | Category | Description |
|---|---|---|
| **`APPROX_COUNT_DISTINCT`** | aggregate (approx.) | Returns an approximate distinct count using HyperLogLog algorithm (much faster on big data). |
| **`APPROX_PERCENTILE`** | aggregate (approx.) | Returns an approximate percentile value based on an input percentile (0‑1). |
| **(Implicit)** **Scalar‑UDF Inlining** – not a function, but many scalar UDFs are automatically rewritten as inline expressions. |
| **`STRING_AGG`**, **`STRING_SPLIT`**, **`JSON`** functions all remain, with performance improvements. |

---  

### 3.9 SQL Server 2022

| Function | Category | Description |
|---|---|---|
| **`STRING_SPLIT` (ordinal version)** | TVF | `STRING_SPLIT (string, separator, <ordinal_flag>)` returns an additional column `ordinal` (1‑based position of each token). |
| **`sys.ledger_hash`** | scalar (ledger) | Returns the SHA‑2‑256 hash for a ledger transaction row (used for verification of immutable ledger tables). |
| **`AT TIME ZONE`** – **enhanced** to accept **`DATETIME2`** values (previously only `datetime`/`datetimeoffset`). |
| **`TRIM`** – now supports **multiple characters** as the trim set (e.g., `TRIM('ab' FROM 'abacaba')`). |
| **`ROW_NUMBER`** – new **`ORDER BY (SELECT NULL)`** deterministic enumeration (useful for quick row numbering without ordering). |
| **`DROP IF EXISTS`** – expanded to cover **graph objects** (`DROP IF EXISTS <graph_node>`, `<graph_edge>`). |
| **Ledger DMVs** – `sys.dm_db_ledger_historical_transactions` (not a function but a DMV). |

---  

### 3.10 Older/Legacy Functions (for completeness)

Functions that have **existed since the early days** (SQL Server 2000) and are *not* “new” in any of the releases above, but still part of the language:

- `ABS`, `CEILING`, `FLOOR`, `ROUND`, `POWER`, `SQRT`, `LOG`, `LOG10`, `EXP`, `RAND` (numeric)
- `LEFT`, `RIGHT`, `SUBSTRING`, `LEN`, `DATALENGTH`, `PATINDEX`, `CHARINDEX`, `REPLACE`, `REPLICATE`, `SPACE`, `REVERSE`, `STUFF` (string)
- `DATEADD`, `DATEDIFF`, `DATEPART`, `DATENAME`, `GETDATE`, `GETUTCDATE`, `SYSDATETIME`, `SYSUTCDATETIME` (date/time)
- `ISNULL`, `COALESCE`, `NULLIF`, `ISNUMERIC`, `ISDATE` (null/validation)
- `COUNT`, `SUM`, `AVG`, `MIN`, `MAX`, `COUNT_BIG`, `STDEV`, `STDEVP`, `VAR`, `VARP` (aggregates)
- `@@IDENTITY`, `@@ROWCOUNT`, `@@ERROR`, `@@SPID` (system variables)

---  

## 4️⃣ How to Use This Information

### 4.1 Check Compatibility Level

```sql
-- Find the current compatibility level of a database
SELECT name, compatibility_level
FROM sys.databases
WHERE name = DB_NAME();   -- or filter for a specific DB
```

- **If you upgrade** a database to a newer engine but keep an **older compatibility level**, the newer T‑SQL features (functions, syntax) will **not be available** until you raise the level:

```sql
-- Raise to the latest level (e.g., 160 for SQL Server 2022)
ALTER DATABASE YourDb SET COMPATIBILITY_LEVEL = 160;
```

> **Caution:** Raising the compatibility level can change query‑plan behavior (e.g., cardinality estimator changes) and may surface breaking changes in existing code (especially around `ROW_NUMBER` semantics or `TRY_CAST`).

### 4.2 Finding Deprecated Elements

Microsoft maintains a **“Deprecated Features”** page for each release. The most common deprecations affecting T‑SQL are:

| Deprecated Feature | Removal (if any) | Recommendation |
|---|---|---|
| `TEXT`, `NTEXT`, `IMAGE` data types | Still supported but **planned** for removal in a future major release. Use `VARCHAR(MAX)`, `NVARCHAR(MAX)`, `VARBINARY(MAX)`. |
| `RAISERROR` with severity > 10 (use `THROW`). | Not removed, but `THROW` is preferred. |
| `sp_configure` options like `show advanced options` (still there). | No removal schedule. |
| `EXECUTE AS` without a user (use `EXECUTE AS CALLER`). | No removal schedule. |

Always consult the **“Deprecated Database Engine Features”** doc for your target version.

### 4.3 When to Use the New Functions

| Scenario | Preferred Function(s) | Reason |
|---|---|---|
| **Concatenating many strings** (with possible `NULL`s) | `CONCAT`, `STRING_AGG` (if you need aggregation) | `CONCAT` silently treats `NULL` as empty, `STRING_AGG` handles set‑based concatenation efficiently. |
| **Splitting CSV values** | `STRING_SPLIT` (SQL 2016+) – use `ordinal` flag in 2022 if order matters. |
| **Parsing JSON stored in NVARCHAR** | `JSON_VALUE`, `JSON_QUERY`, `OPENJSON` | Fast, native JSON support without CLR. |
| **Fast approximate distinct count on billions of rows** | `APPROX_COUNT_DISTINCT` (SQL 2019+) | Uses HyperLogLog algorithm; 1‑2 % error margin is acceptable for many analytics. |
| **Generating sequential numbers without explicit ordering** | `ROW_NUMBER() OVER (ORDER BY (SELECT NULL))` (SQL 2022) | Guarantees a deterministic ordering when you truly do not need a specific order. |
| **Time‑zone aware datetime calculations** | `AT TIME ZONE` (SQL 2016+). | Handles daylight‑saving‑time automatically. |
| **Conditional logic** | `IIF` (SQL 2012+) or `CASE`. | `IIF` is more concise but less flexible than `CASE`. |
| **Pattern‑matching graph data** | `MATCH` clause + node/edge tables (SQL 2017+). | Allows Cypher‑like pattern queries. |
| **Ledger tamper‑evidence** | Create a ledger‑enabled database; use `sys.ledger_hash` and `SELECT * FROM … PERIOD FOR SYSTEM_TIME …` (SQL 2022). | Provides cryptographic verification of all changes. |

---  

## 5️⃣ References (Official Microsoft Docs)

| Release | “What’s new in Transact‑SQL” URL |
|---|---|
| SQL Server 2005 | <https://learn.microsoft.com/sql/t-sql/what-s-new-in-transact-sql-2005> |
| SQL Server 2008 | <https://learn.microsoft.com/sql/t-sql/what-s-new-in-transact-sql-2008> |
| SQL Server 2008 R2 | <https://learn.microsoft.com/sql/t-sql/what-s-new-in-transact-sql-2008-r2> |
| SQL Server 2012 | <https://learn.microsoft.com/sql/t-sql/what-s-new-in-transact-sql-2012> |
| SQL Server 2014 | <https://learn.microsoft.com/sql/t-sql/what-s-new-in-transact-sql-2014> |
| SQL Server 2016 | <https://learn.microsoft.com/sql/t-sql/what-s-new-in-transact-sql-2016> |
| SQL Server 2017 | <https://learn.microsoft.com/sql/t-sql/what-s-new-in-transact-sql-2017> |
| SQL Server 2019 | <https://learn.microsoft.com/sql/t-sql/what-s-new-in-transact-sql-2019> |
| SQL Server 2022 | <https://learn.microsoft.com/sql/t-sql/what-s-new-in-transact-sql-2022> |

For **deprecated features** see: <https://learn.microsoft.com/sql/t-sql/deprecated-features>

For **compatibility‑level‑specific behavior** see: <https://learn.microsoft.com/sql/t-sql/statements/alter-database-transact-sql#compatibility-level>

---  

## 6️⃣ TL;DR – Cheat‑Sheet

| Version | 3‑most‑useful‑new‑functions | New‑syntax highlights |
|---|---|---|
| **2005** | `ROW_NUMBER`, `RANK`, XML methods (`.value()`) | `TRY…CATCH`, CTE, `PIVOT` |
| **2008** | `SWITCHOFFSET`, `TODATETIMEOFFSET` | `MERGE` |
| **2012** | `IIF`, `CONCAT`, `FORMAT`, `EOMONTH`, window functions (`LAG`/`LEAD`) | `OFFSET FETCH`, Sequences |
| **2016** | `STRING_SPLIT`, JSON suite (`ISJSON`, `JSON_VALUE`, `JSON_QUERY`, `OPENJSON`), `AT TIME ZONE` | `CREATE OR ALTER`, `DROP IF EXISTS`, System‑versioned temporal tables |
| **2017** | `STRING_AGG`, `CONCAT_WS`, `TRIM`, `TRANSLATE` | Graph tables (`AS NODE/EDGE`), `MATCH` |
| **2019** | `APPROX_COUNT_DISTINCT`, `APPROX_PERCENTILE` | Scalar‑UDF inlining (performance) |
| **2022** | `STRING_SPLIT` with `ordinal`, `sys.ledger_hash` | Ledger support, expanded `DROP IF EXISTS`, enhanced `TRIM` (multi‑char) |

---  

**Enjoy modernising your code!** If you need sample scripts for any of the functions above (e.g., using `STRING_AGG` with `ORDER BY`, or parsing JSON with `OPENJSON`), just let me know and I’ll drop a ready‑to‑run snippet in a follow‑up. Happy querying!