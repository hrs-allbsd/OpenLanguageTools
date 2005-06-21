Simple and stupid suite for SunTrans BlackBox testing
-----------------------------------------------------

To make XLIFF convert test follow this procedure:
1) upload xliff-fileset.zip to input directory of suntrans2-client-package
2) uncompress the file
3) run 'st2 xliffconvert' command against production and test server
4) compare results: xliff_BLACKbox.pl productionserver_result.zip testserver_result.zip

To make XLIFFBackconversion test use this procedure:
1) upload back-fileset.zip to input directory of suntrans2-client-package
2) uncompress the file
3) run 'st2 xliffbackconv' command against production and test server
4) compare results:  backconv_BLACKbox.pl productionserver_result.zip testserver_result.zip
