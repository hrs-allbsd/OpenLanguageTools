Currently we have to synchronize all methods, because IndexReader and IndexWriter
are not automatically synchronized in Lucene engine.

The problem is that fuzzy search is invoked when user is moving from segment to
segment. I think that fuzzy search should be done only when the file is opened
or when user invoke the fuzzy search manually.

TODO
-----

1) TMX Imports

2) MiniTM Merge

3) Improove performance
   a) use stop/snowball analyzer
   b) tune index store parameters