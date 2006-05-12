Basic description
----------------

Backconverts xlz file created from StarOffice xliff file. Please check
StarOffice xliff filter implementation.

Reason
------

We cannot use standard backconverter here. We have to create the original
StarOffice xliff file. Segmented translation units needs to be joined and
we need to add backslashes to StarOffice tags for example:

<bookmark>

have to converted to:

\<bookmark\>

How it works
------------

1) it parses content.xlf with dom4j parser

2) the filter use dom4j visitor patern to walk through olt xliff

3) if there is 'x-subformat-ws' context attribute defined the backconverter
   adds next translation unit to actual processed translation unit

4) filter adds backslashes to staroffice tags

5) custom xliff filter writer generates StarOffice xliff file, becase we cannot use
   XliffSegmenterFormatter
